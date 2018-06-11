package com.oneassist.serviceplatform.batch.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oneassist.serviceplatform.batch.enums.ServiceRequestListenerEvent;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestDocumentTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.utils.CopyProperties;
import com.oneassist.serviceplatform.commons.utils.DateFormatDeserializer;
import com.oneassist.serviceplatform.commons.utils.UpdateClaimDetailDtoToServiceRequestDtoMapper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CMSDocumentDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentRejection;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSMembershipDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Pendency;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RejectionStatus;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.UpdateClaimDetailDto;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Create new listener class for PE
 * 
 * @author sanjeev.gupta
 *
 */
@Component
public class ServiceRequestPEListener implements Runnable {

    private static String EVENT_NAME_PARAM_NAME = "eventName";
    private static String SERVICE_REQUEST_PAYLOAD_PARAM_NAME = "request";
    private static final Object CLAIM_DETAIL_PAYLOAD_PARAM_NAME = "claimdetailDto";

    private static final Logger logger = Logger.getLogger(ServiceRequestPEListener.class);

    @Autowired
    protected AmazonSQSClient sqsClient;

    private ObjectMapper objectMapper;

    @Value("${AWS_PE_SR_DATA_QUEUE_NAME}")
    private String queueName;

    private String queueUrl;
    @Autowired
    private ThreadPoolTaskExecutor listenerThreadPoolTaskExecutor;

    @Autowired
    private IServiceRequestService serviceRequestService;

    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private ServiceRequestDocumentTypeMasterCache serviceRequestDocumentTypeMasterCache;

    @Autowired
    private OasysProxy oasysProxy;

    @PostConstruct
    public void startListening() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateFormatDeserializer());
        objectMapper.registerModule(module);
        GetQueueUrlRequest urlRequest = new GetQueueUrlRequest();
        urlRequest.setQueueName(queueName);
        sqsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        queueUrl = sqsClient.getQueueUrl(urlRequest).getQueueUrl();
        ServiceRequestPEListener listener = this;
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
                                System.out.println(" Queue:: " + eventName);
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
                case CREATE_SERVICE_REQUEST:
                    ServiceRequestDto serviceRequestDto = null;
                    UpdateClaimDetailDto updateClaimDto = getObject((LinkedHashMap) eventMsg.get(SERVICE_REQUEST_PAYLOAD_PARAM_NAME), UpdateClaimDetailDto.class);
                    ModelMapper modelMapper = new ModelMapper();
                    modelMapper.addMappings(new UpdateClaimDetailDtoToServiceRequestDtoMapper());
                    serviceRequestDto = modelMapper.map(updateClaimDto, ServiceRequestDto.class);
                    serviceRequestDto.setSource(Constants.SOURCE_QUEUE);
                    createCRMServiceRequest(serviceRequestDto, updateClaimDto);
                    break;
                case SERVICE_REQUEST_UPDATE:
                    updateClaimDto = getObject((LinkedHashMap) eventMsg.get(CLAIM_DETAIL_PAYLOAD_PARAM_NAME), UpdateClaimDetailDto.class);

                    modelMapper = new ModelMapper();
                    modelMapper.addMappings(new UpdateClaimDetailDtoToServiceRequestDtoMapper());
                    serviceRequestDto = null;
                    try {
                        serviceRequestDto = serviceRequestService.getServiceRequestByExternalSRId(updateClaimDto.getOcdClaimIdTmp().toString());
                    } catch (BusinessServiceException e) {
                        logger.error("Exception while getting service request details,", e);
                    }
                    ServiceRequestDto tempServiceRequestDto = modelMapper.map(updateClaimDto, ServiceRequestDto.class);
                    tempServiceRequestDto.setSource(Constants.SOURCE_QUEUE);
                    setPendencyDetails(updateClaimDto, tempServiceRequestDto);
                    if (serviceRequestDto != null) {
                        CopyProperties.copyNonNullProperties(tempServiceRequestDto, serviceRequestDto);
                        serviceRequestService.performServiceAction(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT, serviceRequestDto, ServiceRequestEventCode.SERVICE_REQUEST_UPDATE);
                    } else {
                        createCRMServiceRequest(tempServiceRequestDto, updateClaimDto);
                    }
                    break;
                default:
                    logger.error("No event found with name ::" + eventName);
                    break;

            }
        } else {
            logger.error("Invalid event " + eventName + "is received");
        }
    }

    private void createCRMServiceRequest(ServiceRequestDto tempServiceRequestDto, UpdateClaimDetailDto updateClaimDto) throws BusinessServiceException, JsonProcessingException {
        tempServiceRequestDto.setServiceRequestType("PE_" + updateClaimDto.getClaimType());
        tempServiceRequestDto.setInitiatingSystem(new Long(InitiatingSystem.CRM.getInitiatingSystem()));
        OASYSCustMemDetails custMemDetails = oasysProxy.getMembershipAssetDetails(tempServiceRequestDto.getReferenceNo(), null, null, true);
        if (custMemDetails != null && !CollectionUtils.isEmpty(custMemDetails.getMemberships())) {
            boolean assetFound = false;
            for (OASYSMembershipDetails custMembership : custMemDetails.getMemberships()) {
                if (!CollectionUtils.isEmpty(custMembership.getAssets())) {
                    for (AssetDetailDto asset : custMembership.getAssets()) {
                        if (asset.getCustId().longValue() == tempServiceRequestDto.getCustomerId().longValue()) {
                            tempServiceRequestDto.setRefSecondaryTrackingNo(asset.getAssetId().toString());
                            assetFound = true;
                            break;
                        }
                    }
                    if (assetFound) {
                        ServiceRequestDto response = serviceRequestService.createServiceRequest(tempServiceRequestDto);
                        if (response == null || response.getServiceRequestId() == null || response.getServiceRequestId() == 0) {
                            throw new BusinessServiceException("Failed to created service request");
                        }
                        break;
                    }
                }
            }
        } else {
            throw new BusinessServiceException("Customer asset details not found");
        }
    }

    private void setPendencyDetails(UpdateClaimDetailDto updateClaimDto, ServiceRequestDto serviceRequestDto) {
        String serviceRequestType = "PE_" + updateClaimDto.getClaimType();
        if (ServiceRequestType.PE_ADLD.getRequestType().equals(serviceRequestType) || ServiceRequestType.PE_EW.getRequestType().equals(serviceRequestType)
                || ServiceRequestType.PE_THEFT.getRequestType().equals(serviceRequestType)) {
            Pendency pendency = new Pendency();
            boolean pendencyCase = false;
            if (!StringUtils.isEmpty(updateClaimDto.getOcdIincidentDescVerifyStatus()) && Constants.COMPLETE.equalsIgnoreCase(updateClaimDto.getOcdIincidentDescVerifyStatus())) {
                pendency.setIncidenceDescription(null);
            } else {
                RejectionStatus descRejectionStatus = new RejectionStatus();
                descRejectionStatus.setStatus(updateClaimDto.getOcdIincidentDescVerifyStatus());
                descRejectionStatus.setRemarks(updateClaimDto.getOcdIincidentDescVerRemarks());
                pendency.setIncidenceDescription(descRejectionStatus);
                pendencyCase = true;
            }
            if (!StringUtils.isEmpty(updateClaimDto.getOcdDamageLossDateTimeVerStat()) && Constants.COMPLETE.equalsIgnoreCase(updateClaimDto.getOcdDamageLossDateTimeVerStat())) {
                pendency.setIncidenceDate(null);
            } else {
                RejectionStatus descRejectionStatus = new RejectionStatus();
                descRejectionStatus.setStatus(updateClaimDto.getOcdDamageLossDateTimeVerStat());
                descRejectionStatus.setRemarks(updateClaimDto.getOcdDamageLossDateTimeVerRemarks());
                pendency.setIncidenceDate(descRejectionStatus);
                pendencyCase = true;
            }
            if (!CollectionUtils.isEmpty(updateClaimDto.getClaimDocumentDetails())) {
                List<DocumentRejection> documentRejections = new ArrayList<DocumentRejection>();
                for (CMSDocumentDetail cmsDocument : updateClaimDto.getClaimDocumentDetails()) {
                    if (Constants.YES_FLAG.equalsIgnoreCase(cmsDocument.getDocMandatory()) && !StringUtils.isEmpty(cmsDocument.getOcddDocMongoId())
                            && !StringUtils.isEmpty(cmsDocument.getOcddDocStatus()) && Constants.COMPLETE.equalsIgnoreCase(cmsDocument.getOcddDocStatus())) {
                        ServiceDocumentEntity documentEntity = serviceDocumentRepository.findByStorageRefId(cmsDocument.getOcddDocMongoId());
                        if (documentEntity != null) {
                            DocumentRejection documentRejection = new DocumentRejection();
                            documentRejection.setDocumentId(documentEntity.getDocumentId());
                            documentRejection.setDocumentTypeId(documentEntity.getDocumentTypeId().toString());
                            Map<String, DocTypeMstEntity> documentTypeEntitiesMap = serviceRequestDocumentTypeMasterCache.getAll();
                            List<DocTypeMstEntity> documentTypeEntitiesList = new ArrayList<>(documentTypeEntitiesMap.values());
                            for (DocTypeMstEntity docType : documentTypeEntitiesList) {
                                if (docType.getDocTypeId().longValue() == documentEntity.getDocumentTypeId().longValue()) {
                                    documentRejection.setDocumentName(docType.getDocName());
                                    break;
                                }
                            }

                            RejectionStatus descRejectionStatus = new RejectionStatus();
                            descRejectionStatus.setStatus(cmsDocument.getOcddDocStatus());
                            descRejectionStatus.setRemarks(cmsDocument.getOcddDocRemarks());
                            documentRejection.setDocumentStatus(descRejectionStatus);
                            documentRejections.add(documentRejection);
                            pendencyCase = true;
                        }
                    }
                }
                pendency.setDocuments(documentRejections);
            }

            if (pendencyCase) {
                HashMap<String, Pendency> pendencyMap = new HashMap<String, Pendency>();
                pendencyMap.put(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowTaskName(), pendency);
                serviceRequestDto.setPendency(pendencyMap);
            } else {
                serviceRequestDto.setPendency(null);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private <T> T getObject(LinkedHashMap requestPayload, Class<T> classType) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(requestPayload), classType);
    }
}
