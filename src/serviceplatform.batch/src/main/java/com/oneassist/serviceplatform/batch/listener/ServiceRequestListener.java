package com.oneassist.serviceplatform.batch.listener;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oneassist.serviceplatform.batch.enums.ServiceRequestListenerEvent;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.utils.DateFormatDeserializer;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.commons.utils.ServiceReqeustDtoToCreateClaimDtoMapper;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestDtoToEditClaimDetailDtoMapper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CreateClaimDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.EditClaimDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.logisticpartner.services.ILogisticPartnerService;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;
import com.oneassist.serviceplatform.services.shipment.IShipmentService;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author priya.prakash
 *         <p>
 *         Service platform listener for service request handler
 *         </p>
 */
@Component
public class ServiceRequestListener implements Runnable {

    private static final Logger logger = Logger.getLogger(ServiceRequestListener.class);

    @Autowired
    protected AmazonSQSClient sqsClient;

    private ObjectMapper objectMapper;

    @Value("${AWS_SR_DATA_QUEUE_NAME}")
    private String queueName;

    private String queueUrl;
    @Autowired
    private ThreadPoolTaskExecutor listenerThreadPoolTaskExecutor;

    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private IServiceRequestService serviceRequestService;

    @Autowired
    @Qualifier("logisticProviders")
    private HashMap<String, ILogisticPartnerService> logisticProviders;

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;

    @PostConstruct
    public void startListening() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateFormatDeserializer());
        objectMapper.registerModule(module);
        GetQueueUrlRequest urlRequest = new GetQueueUrlRequest();
        urlRequest.setQueueName(queueName);
        queueUrl = sqsClient.getQueueUrl(urlRequest).getQueueUrl();
        ServiceRequestListener listener = this;
        listenerThreadPoolTaskExecutor.execute(listener);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        while (true) {
            try {
                ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest(queueUrl);
                ReceiveMessageResult receiveMsgResult = sqsClient.receiveMessage(receiveRequest);
                if (receiveMsgResult != null && !CollectionUtils.isEmpty(receiveMsgResult.getMessages())) {
                    for (Message message : receiveMsgResult.getMessages()) {
                        try {
                            HashMap eventMsg = objectMapper.readValue(message.getBody(), HashMap.class);
                            String eventName = (String) eventMsg.get(EVENT_NAME_PARAM_NAME);
                            if (!StringUtils.isEmpty(eventName)) {
                                processEvent(eventMsg, eventName);
                            } else {
                                logger.error("Event name is empty in message ::" + message.getBody());
                            }
                            sqsClient.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error("Inside ServicePlatformListener onMessage", ex);
                            throw new RuntimeException(ex);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Exceptin in ServicePlatformListener", e);
            }

        }
    }

    @SuppressWarnings({ "rawtypes" })
    private void processEvent(HashMap eventMsg, String eventName) throws NumberFormatException, Exception {
        ServiceRequestListenerEvent event = ServiceRequestListenerEvent.valueOf(eventName);
        logger.error("Processing Message ::" + objectMapper.writeValueAsString(eventMsg));
        if (event != null) {
            switch (event) {
                case CLOSE_CLAIM:
                    List<ShipmentUpdateRequestDto> updateRequestDtos = objectMapper.readValue(objectMapper.writeValueAsString(eventMsg.get(UPDATE_SHIPMENT_REQUEST_PARAM_NAME)),
                            new TypeReference<List<ShipmentUpdateRequestDto>>() {
                            });
                    if (!CollectionUtils.isEmpty(updateRequestDtos)) {
                        for (ShipmentUpdateRequestDto updateRequestDto : updateRequestDtos) {
                            shipmentService.updateShipment(Long.parseLong(updateRequestDto.getShipmentId()), SHIPMENT_STATUS_UPDATE_FIELD_NAME, updateRequestDto);
                        }
                    }
                    break;
                case CREATE_SERVICE_REQUEST:
                case RAISE_SHIPMENT:
                    String serviceName = (String) eventMsg.get(SERVICE_NAME_PARAM_NAME);
                    ServiceRequestType requestType = ServiceRequestType.getServiceRequestType(serviceName);
                    if (ServiceRequestType.PICKUP.equals(requestType) || ServiceRequestType.DELIVERY.equals(requestType) || ServiceRequestType.ASCDELIVERY.equals(requestType)
                            || ServiceRequestType.ASCPICKUP.equals(requestType) || ServiceRequestType.COURTESYDELIVERY.equals(requestType) || ServiceRequestType.COURTESYPICKUP.equals(requestType)) {
                        ShipmentRequestDto shipmentRequestDto = getObject((LinkedHashMap) eventMsg.get(SHIPMENT_REQUEST_PAYLOAD_PARAM_NAME), ShipmentRequestDto.class);
                        ResponseDto<ShipmentRequestDto> response = shipmentService.createShipment(shipmentRequestDto);
                        if (response != null && ResponseConstant.SUCCESS.equalsIgnoreCase(response.getStatus())) {
                            logger.info("Shipment created successfully");
                        } else {
                            logger.error("Shipment creation is failed with error ::" + (response != null ? "null response " : response));
                            throw new Exception("shipment creation failed");
                        }
                    } else {
                        ServiceRequestDto serviceRequestDto = getObject((LinkedHashMap) eventMsg.get(SERVICE_REQUEST_PAYLOAD_PARAM_NAME), ServiceRequestDto.class);
                        ServiceRequestDto serviceResponseDto = serviceRequestService.createServiceRequest(serviceRequestDto);
                        logger.error("Create inspection response ::" + serviceResponseDto);
                    }
                    break;
                case ECOM_PINCODE:
                case FEDEX_PROCESS_SHIPMENT:
                case LOGINEXT_SHIPMENT:
                case DHL_SHIPMENT:
                    ShipmentRequestDto shipmentRequest = null;
                    try {
                        Long shipmentId = Long.parseLong(eventMsg.get("shipmentId").toString());
                        if (eventMsg.get(SHIPMENT_REQUEST_DETAILS_PAYLOAD_PARAM_NAME) != null) {
                            shipmentRequest = getObject((LinkedHashMap) eventMsg.get(SHIPMENT_REQUEST_DETAILS_PAYLOAD_PARAM_NAME), ShipmentRequestDto.class);
                            shipmentRequest.setShipmentId(shipmentId);
                        } else {
                            shipmentRequest = shipmentService.getByShipmentId(shipmentId);
                        }
                        logisticProviders.get(event.getEventType()).createShipment(shipmentRequest);
                    } catch (Exception e) {
                        logger.error("Exception while creating shipment with logistic partner::" + eventMsg, e);
                        commonService.updateShipmentFailReason(shipmentRequest.getShipmentId(), SHIPMENT_CREATION_FAILED_ERROR, e, shipmentRequest.getCreatedBy());
                        throw e;
                    }
                    break;
                case UPDATE_EXTERNAL_SERVICE_REQUEST:
                case CREATE_EXTERNAL_SERVICE_REQUEST:
                    createOrUpdateExternalServiceRequest(event, eventMsg);
                    break;
                default:
                    logger.error("No event found with name ::" + eventName);
                    break;

            }
        } else {
            logger.error("Invalid event " + eventName + "is received");
        }
    }

    @SuppressWarnings("rawtypes")
    private void createOrUpdateExternalServiceRequest(ServiceRequestListenerEvent event, HashMap eventMsg) throws Exception {

        ServiceRequestDto serviceRequestDto = getObject((LinkedHashMap) eventMsg.get(SERVICE_REQUEST_PAYLOAD_PARAM_NAME), ServiceRequestDto.class);
        ModelMapper modelMapper = new ModelMapper();
        if (ServiceRequestListenerEvent.CREATE_EXTERNAL_SERVICE_REQUEST.equals(event)) {
            modelMapper.addMappings(new ServiceReqeustDtoToCreateClaimDtoMapper());
            CreateClaimDto createClaimDto = modelMapper.map(serviceRequestDto, CreateClaimDto.class);
            createClaimDto.setClaimType(createClaimDto.getClaimType().replace("PE_", ""));
            String dateOfIncident = createClaimDto.getDateOfIncident();
            createClaimDto.setDateOfIncident(dateOfIncident);
            String response = oasysAdminProxy.raiseClaim(createClaimDto);
            JSONObject jsonObject = new JSONObject(response);
            String status = (String) jsonObject.get("status");
            if (Constants.SUCCESS.equalsIgnoreCase(status)) {
                String externalServiceId = (String) jsonObject.get("claimPK");
                serviceRequestService.updateExternalSRId(externalServiceId, new Date(), Constants.MODIFIED_BY_BATCH, serviceRequestDto.getServiceRequestId());
            } else {
                throw new BusinessServiceException("Exception while creating claim in external system:" + jsonObject);
            }
        } else {
            modelMapper.addMappings(new ServiceRequestDtoToEditClaimDetailDtoMapper());
            EditClaimDetailDto editClaimDetailDto = modelMapper.map(serviceRequestDto, EditClaimDetailDto.class);
            if (editClaimDetailDto.getOcdCreatedDate() == null) {
                editClaimDetailDto.setOcdCreatedDate(DateUtils.toShortFormattedString(serviceRequestDto.getCreatedOn()));
            }
            if (editClaimDetailDto.getOcdDamageLossDateTime() == null) {
                editClaimDetailDto.setOcdDamageLossDateTime(serviceRequestDto.getWorkflowData().getDocumentUpload().getDateOfIncident());
            }
            if (editClaimDetailDto.getOcdIncidentDescription() == null) {
                editClaimDetailDto.setOcdIncidentDescription(serviceRequestDto.getRequestDescription());
            }
            String status = oasysAdminProxy.updateClaim(editClaimDetailDto);
            if (!Constants.SUCCESS.equalsIgnoreCase(status)) {
                throw new BusinessServiceException("Exception while updating claim in external system:" + status);
            }
        }

    }

    @SuppressWarnings("rawtypes")
    private <T> T getObject(LinkedHashMap requestPayload, Class<T> classType) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(requestPayload), classType);
    }

    private static String EVENT_NAME_PARAM_NAME = "eventName";
    private static String UPDATE_SHIPMENT_REQUEST_PARAM_NAME = "shipmentUpdateRequestDto";
    private static String SHIPMENT_STATUS_UPDATE_FIELD_NAME = "status";
    private static String SERVICE_NAME_PARAM_NAME = "serviceName";
    private static String SERVICE_REQUEST_PAYLOAD_PARAM_NAME = "request";
    private static String SHIPMENT_REQUEST_PAYLOAD_PARAM_NAME = "shipmentRequestDto";
    private static String SHIPMENT_REQUEST_DETAILS_PAYLOAD_PARAM_NAME = "shipmentDetails";
    private static final String SHIPMENT_CREATION_FAILED_ERROR = "Failed to place shipment Request";
}
