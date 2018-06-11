package com.oneassist.serviceplatform.commons.proxies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.domain.CommunicationDetailDTO;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.communicationgateway.provider.CommunicationGatewayClient;
import com.oneassist.serviceplatform.commons.cache.PartnerBUCache;
import com.oneassist.serviceplatform.commons.constants.CommunicationTemplatesConfig;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.proxies.base.BaseProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.EmailDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.SmsDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;
import com.oneassist.serviceplatform.externalcontracts.EmailVO;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;
import com.oneassist.serviceplatform.externalcontracts.SmsVO;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class CommunicationGatewayProxy extends BaseProxy {

    private final Logger logger = Logger.getLogger(CommunicationGatewayProxy.class);

    // @Value("${OASYSAdminUrl}")
    // private String oasysAdminUrl;

    // @Value("${oasysAdminUsername}")
    // private String oasysAdminUsername;
    //
    // @Value("${oasysAdminPassword}")
    // private String oasysAdminPassword;

    @Autowired
    private CommunicationGatewayClient communicationGatewayClient;

    @Autowired
    private ServiceAddressRepository serviceAddressRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final int MAX_RETRY_LIMIT = 5;
    private final int RETRY_FREQUENCY_IN_MINS = 5;
    private final int INITIAL_RETRY_RELOAD_IN_MINS = 5;

    @Autowired
    private OasysProxy oasysProxy;

    @Autowired
    private PartnerBUCache partnerBUCache;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Override
    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.set("userName", oasysAdminUsername);
        // headers.set("passWord", oasysAdminPassword);

        return headers;
    }

    @Override
    protected String getBaseUrl() {
        return "";
    }

    public boolean sendCommunication(Recipient recipient, ServiceRequestDto serviceRequestDto, final CommunicationGatewayEventCode eventCode, Map<String, Object> additionalAttributes) {

        logger.error("Communcation Request Received for :" + recipient.getRecipientType() + " for Event Code : " + eventCode);

        final CommunicationDetailDTO[] communicationDetails = new CommunicationDetailDTO[1];
        boolean communicationSent = false;
        try {
            communicationDetails[0] = populateCommunicationDetail(serviceRequestDto, additionalAttributes, recipient);

            if (communicationDetails.length > 0 && communicationDetails[0] != null) {

                logger.error("Sending communiction for eventCode:" + eventCode + " with communicationDetails:" + communicationDetails);
                try {
                    communicationSent = communicationGatewayClient.sendCommunication(eventCode, communicationDetails[0]);

                    if (!communicationSent) {
                        retryCommunication(eventCode, communicationDetails[0]);
                    }
                } catch (Exception e) {
                    logger.error("Exception invoking Communication Gateway ", e);
                    retryCommunication(eventCode, communicationDetails[0]);

                }
            } else {
                logger.error("Communication details is empty, hence not sending communication.");
            }
        } catch (Exception e) {
            logger.error("Exception while sending communication ", e);
        }
        return communicationSent;
    }

    private void retryCommunication(final CommunicationGatewayEventCode eventCode, final CommunicationDetailDTO communicationDetails) {

        logger.error("Retry Process Started to Send Communication for the Mobile Number: " + communicationDetails.getMobileNumber() + " And Email ID: " + communicationDetails.getEmailId());

        final ScheduledExecutorService execService = Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(new Runnable() {

            int retryCount = 0;
            boolean communicationSent = false;

            @Override
            public void run() {
                ++retryCount;
                try {
                    communicationSent = communicationGatewayClient.sendCommunication(eventCode, communicationDetails);

                } catch (Exception e) {
                    logger.error("Error Occurred during Sending Communication:: " + e);
                }

                if (communicationSent) {
                    logger.info("Communication Sent successfully:: Shutting down Retry process");
                    if (!execService.isShutdown())
                        execService.shutdown();
                    return;
                }

                if (retryCount >= MAX_RETRY_LIMIT) {
                    logger.error("Max Retry limit has been reached for Sending Communication:: ");
                    if (!execService.isShutdown())
                        execService.shutdown();
                }
            }
        }, INITIAL_RETRY_RELOAD_IN_MINS, RETRY_FREQUENCY_IN_MINS, TimeUnit.MINUTES);
    }

    private CommunicationDetailDTO populateCommunicationDetail(ServiceRequestDto serviceRequestDto, Map<String, Object> additionalAttributes, Recipient recipient) throws Exception {

        CommunicationDetailDTO communicationDetails = null;
        ServiceRequestDto serviceRequestEntityInfo = null;

        ObjectMapper mapper = new ObjectMapper();

        switch (recipient) {

            case CUSTOMER: {
                serviceRequestEntityInfo = fetchServiceRequestInfo(serviceRequestDto);
                ServiceAddressDetailDto serviceAddressDetailDto = getAddress(serviceRequestEntityInfo);
                if (serviceAddressDetailDto != null) {
                    communicationDetails = new CommunicationDetailDTO();
                    communicationDetails.setEmailId(serviceAddressDetailDto.getEmail());
                    communicationDetails.setMobileNumber(String.valueOf(serviceAddressDetailDto.getMobileNo()));

                    HashMap<String, Object> modelAttributes = mapper.readValue(mapper.writeValueAsString(serviceRequestEntityInfo), HashMap.class);
                    if (modelAttributes != null && !CollectionUtils.isEmpty(additionalAttributes)) {
                        modelAttributes.putAll(additionalAttributes);
                    }

                    modelAttributes.putAll(mapper.readValue(mapper.writeValueAsString(serviceAddressDetailDto), HashMap.class));
                    communicationDetails.setModelAttributes(modelAttributes);
                } else {
                    throw new Exception("Address details not found for address :" + serviceRequestDto.getRefPrimaryTrackingNo());
                }

                break;
            }

            case SERVICEPARTNER: {

                Long partnerBuCode = null;

                if (additionalAttributes.containsKey(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue())) {
                    partnerBuCode = (Long) (additionalAttributes.get(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue()));
                } else if (serviceRequestDto.getServicePartnerBuCode() != null) {
                    partnerBuCode = serviceRequestDto.getServicePartnerBuCode();
                } else {
                    serviceRequestEntityInfo = fetchServiceRequestInfo(serviceRequestDto);
                    partnerBuCode = serviceRequestEntityInfo.getServicePartnerBuCode();
                }

                if (partnerBuCode != null) {
                    PartnerBusinessUnit partnerBusinessUnit = partnerBUCache.get(String.valueOf(partnerBuCode));

                    if (partnerBusinessUnit == null) {
                        throw new Exception("Invalid Partner BU for SR# :" + serviceRequestDto.getServiceRequestId());
                    }

                    if (Strings.isNullOrEmpty(partnerBusinessUnit.getEmailId()) && partnerBusinessUnit.getMobile() == 0) {
                        throw new Exception("Communication Details not found for the Partner for Reference# :" + serviceRequestDto.getRefPrimaryTrackingNo());
                    }

                    HashMap<String, Object> modelAttributes = mapper.readValue(mapper.writeValueAsString(serviceRequestDto), HashMap.class);
                    if (modelAttributes != null && !CollectionUtils.isEmpty(additionalAttributes)) {
                        modelAttributes.putAll(additionalAttributes);
                    }

                    communicationDetails = new CommunicationDetailDTO();
                    communicationDetails.setEmailId(partnerBusinessUnit.getEmailId());
                    communicationDetails.setMobileNumber(String.valueOf(partnerBusinessUnit.getMobile()));
                    communicationDetails.setModelAttributes(modelAttributes);

                } else {
                    throw new Exception("No BU found for Reference# :" + serviceRequestDto.getRefPrimaryTrackingNo());
                }

                break;
            }

            case TECHNICIAN: {

                Long technicianId = null;
                UserProfileData userProfileData = null;

                if (additionalAttributes.containsKey(CommunicationConstants.COMM_TECHNICIAN_ASSIGNEE_ID.getValue())) {
                    technicianId = (Long) (additionalAttributes.get(CommunicationConstants.COMM_TECHNICIAN_ASSIGNEE_ID.getValue()));
                }

                else if (serviceRequestDto.getAssignee() != null) {
                    technicianId = serviceRequestDto.getAssignee();

                } else {
                    serviceRequestEntityInfo = fetchServiceRequestInfo(serviceRequestDto);
                    technicianId = serviceRequestEntityInfo.getAssignee();
                }

                if (technicianId == null) {
                    throw new Exception("No Assignee found for SR# :" + serviceRequestDto.getServiceRequestId());
                }

                if (additionalAttributes.containsKey(CommunicationConstants.COMM_TECHNICIAN_PROFILE_DATA.getValue())) {
                    userProfileData = (UserProfileData) additionalAttributes.get(CommunicationConstants.COMM_TECHNICIAN_PROFILE_DATA.getValue());
                }

                else {
                    userProfileData = oasysAdminProxy.getTechnicianUserProfile(String.valueOf(technicianId));
                }

                if (userProfileData == null) {
                    throw new Exception("Technician Details Not found for Reference# :" + serviceRequestDto.getRefPrimaryTrackingNo());
                }

                if (Strings.isNullOrEmpty(userProfileData.getEmailId()) && Strings.isNullOrEmpty(userProfileData.getMobileNumber())) {
                    throw new Exception("Communication Details not found for the Technician for Reference# :" + serviceRequestDto.getRefPrimaryTrackingNo());
                }

                HashMap<String, Object> modelAttributes = mapper.readValue(mapper.writeValueAsString(serviceRequestDto), HashMap.class);
                if (modelAttributes != null && !CollectionUtils.isEmpty(additionalAttributes)) {
                    modelAttributes.putAll(additionalAttributes);
                }

                communicationDetails = new CommunicationDetailDTO();
                communicationDetails.setEmailId(userProfileData.getEmailId());
                communicationDetails.setMobileNumber(userProfileData.getMobileNumber());
                communicationDetails.setModelAttributes(modelAttributes);

                break;
            }

            default:
                logger.info("Invalid Recipient to send Communication");
                break;
        }

        return communicationDetails;
    }

    private ServiceAddressDetailDto getAddress(ServiceRequestDto serviceRequestDto) {

        String serviceAddressId = null;
        ServiceAddressDetailDto serviceAddressDetailDto = null;

        if (null != serviceRequestDto.getWorkflowData() && null != serviceRequestDto.getWorkflowData().getVisit() && null != serviceRequestDto.getWorkflowData().getVisit().getServiceAddress()) {

            serviceAddressId = serviceRequestDto.getWorkflowData().getVisit().getServiceAddress();
            ServiceAddressEntity serviceAddressEntity = serviceAddressRepository.findByServiceAddressId(Long.valueOf(serviceAddressId));
            serviceAddressDetailDto = modelMapper.map(serviceAddressEntity, ServiceAddressDetailDto.class);
        }

        return serviceAddressDetailDto;
    }

    public void sendCommunication(ShipmentRequestDto shipmentRequestDto, String partnerName, String templateId) {

        try {
            if (templateId != null) {
                String serviceRequestType = shipmentRequestDto.getServiceRequestDetails().getServiceRequestType();

                if (serviceRequestType.equalsIgnoreCase(ServiceRequestType.PICKUP.getRequestType())) {
                    templateId = CommunicationTemplatesConfig.DEVICE_PICKUP_SMS_EMAIL_TEMPLATE;
                } else if (serviceRequestType.equalsIgnoreCase(ServiceRequestType.DELIVERY.getRequestType())) {
                    templateId = CommunicationTemplatesConfig.DEVICE_DELIVERY_SMS_EMAIL_TEMPLATE;
                } else if (serviceRequestType.equalsIgnoreCase(ServiceRequestType.COURTESYDELIVERY.getRequestType())) {
                    templateId = CommunicationTemplatesConfig.COURTESY_DELIVERY_SMS_EMAIL_TEMPLATE;
                }
            }

            if (templateId != null) {
                String toEmail = null;

                if (shipmentRequestDto.getSender() != null && shipmentRequestDto.getSender().equalsIgnoreCase("CUSTOMER")) {
                    if (shipmentRequestDto.getOriginAddressDetails() != null && shipmentRequestDto.getOriginAddressDetails().getEmail() != null) {
                        toEmail = shipmentRequestDto.getOriginAddressDetails().getEmail();
                    } else {
                        logger.info("shipmentRequestDto.getOriginAddressDetails() : toemail is empty");
                    }
                } else {
                    if (shipmentRequestDto.getDestinationAddressDetails() != null && shipmentRequestDto.getDestinationAddressDetails().getEmail() != null) {
                        toEmail = shipmentRequestDto.getDestinationAddressDetails().getEmail();
                    } else {
                        logger.info("shipmentRequestDto.getDestinationAddressDetails() : toemail is empty");
                    }
                }

                try {
                    EmailVO emailVO = new EmailVO();
                    EmailDto emailDto = new EmailDto();
                    emailVO.setTemplateId(Integer.valueOf(templateId));
                    emailVO.setInitiatingSystem(InitiatingSystem.SERVICE_PLATFORM.getInitiatingSystem());
                    emailVO.setCreatedBy(InitiatingSystem.SERVICE_PLATFORM.toString());
                    emailVO.setSendImmediate(true);
                    emailVO.setToAddress(toEmail);

                    Map<String, Object> modelEmail = new HashMap<String, Object>();
                    modelEmail.put("shipementDetailsDto", shipmentRequestDto);
                    modelEmail.put("shipmentStage", shipmentRequestDto);
                    modelEmail.put("SRNO", shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo());
                    modelEmail.put("vendor", partnerName);

                    emailDto.setEmailVo(emailVO);
                    emailDto.setModelEmail(modelEmail);
                    logger.info("emailDto is :" + emailDto.toString());
                    oasysProxy.sendEmail(emailDto);
                } catch (Exception exception) {
                    logger.error("While exception sending an email:", exception);
                }

                Long mobileNo = null;

                if (shipmentRequestDto.getSender() != null && shipmentRequestDto.getSender().equalsIgnoreCase("CUSTOMER")) {
                    if (shipmentRequestDto.getOriginAddressDetails() != null && shipmentRequestDto.getOriginAddressDetails().getMobileNo() != null) {
                        mobileNo = shipmentRequestDto.getOriginAddressDetails().getMobileNo();
                    } else {
                        logger.info("shipmentRequestDto.getDestinationAddressDetails() : mobileno is empty");
                    }
                } else {
                    if (shipmentRequestDto.getDestinationAddressDetails() != null && shipmentRequestDto.getDestinationAddressDetails().getMobileNo() != null) {
                        mobileNo = shipmentRequestDto.getDestinationAddressDetails().getMobileNo();
                    } else {
                        logger.info("shipmentRequestDto.getDestinationAddressDetails() : mobileno is empty");
                    }
                }

                try {
                    SmsVO smsVo = new SmsVO();
                    smsVo.setImmediate(true);
                    smsVo.setToMobileNo(String.valueOf(mobileNo));
                    smsVo.setTemplateId(Integer.valueOf(templateId));
                    smsVo.setCreatedBy(InitiatingSystem.SERVICE_PLATFORM.toString());
                    logger.info("shipmentRequestDto.getLogisticPartnerRefTrackingNumber()..srno" + shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo());

                    Map<String, Object> modelsms = new HashMap<String, Object>();
                    modelsms.put("claimSrNo", shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo());
                    modelsms.put("AWBNO", shipmentRequestDto.getLogisticPartnerRefTrackingNumber());
                    modelsms.put("content", partnerName);
                    modelsms.put("vendor", partnerName);

                    SmsDto smsDto = new SmsDto();
                    smsDto.setSmsVo(smsVo);
                    smsDto.setModelSms(modelsms);

                    oasysProxy.sendSMS(smsDto);

                    logger.info("smsdto is :" + smsDto.toString());
                } catch (Exception exception) {
                    logger.error("While exception sending an sms:", exception);
                }
            }
        } catch (Exception exception) {
            logger.error("Exception occurred while sending communication :", exception);
        }
    }

    private ServiceRequestDto fetchServiceRequestInfo(ServiceRequestDto serviceRequestDto) throws Exception {

        List<ServiceRequestEntity> serviceRequesteInfo = new ArrayList<>();
        Long servicerequestId = serviceRequestDto.getServiceRequestId();
        String refPrimaryTrackingNo = serviceRequestDto.getRefPrimaryTrackingNo();
        if (null != servicerequestId && !Strings.isNullOrEmpty(refPrimaryTrackingNo)) {
            serviceRequesteInfo = serviceRequestRepository.findServiceRequestEntityByServiceRequestIdAndRefPrimaryTrackingNo(Long.valueOf(servicerequestId), refPrimaryTrackingNo);
        } else if (null != servicerequestId) {
            ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findServiceRequestEntityByServiceRequestId(Long.valueOf(servicerequestId));
            if (serviceRequestEntity != null)
                serviceRequesteInfo.add(serviceRequestEntity);
        } else if (!Strings.isNullOrEmpty(refPrimaryTrackingNo)) {
            serviceRequesteInfo = serviceRequestRepository.findByRefPrimaryTrackingNo(refPrimaryTrackingNo);
        }

        if (CollectionUtils.isEmpty(serviceRequesteInfo)) {
            throw new Exception("Service request entity not found for :" + serviceRequestDto.getRefPrimaryTrackingNo());
        }

        ServiceRequestDto serviceResponseDt = serviceRequestHelper.convertObject(serviceRequesteInfo.get(0), ServiceRequestDto.class);
        logger.error("Workflow data :" + serviceResponseDt.getWorkflowData());

        return serviceResponseDt;
    }
}
