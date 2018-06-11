package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.RuleEngine;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.enums.RuleName;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceFulfilmentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestValidator;
import com.oneassist.serviceplatform.commons.workflowmanager.IWorkflowManager;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestAddressDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestRuleDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ScheduleCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    private static final String SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT = "SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT";

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private ServiceRequestValidator serviceRequestValidator;

    @Autowired
    protected MessageSource messageSource;

    private final Logger logger = Logger.getLogger(ScheduleCommand.class);

    @Autowired
    protected IWorkflowManager workflowManager;

    @Autowired
    private PincodeServiceFulfilmentRepository fulfilmentRepository;

    @Autowired
    private ServiceAddressRepository serviceAddressRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    private static String INSPECTION_TIME_PARAM = "inspectionTime";
    private static String INSPECTION_DATE_PARAM = "inspectionDate";
    private static String OTP_PARAM = "OTP";

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        try {

            if (commandInput.getData().getScheduleSlotStartDateTime() == null) {
                inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoList);
            }

            if (commandInput.getData().getScheduleSlotEndDateTime() == null) {
                inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoList);
            }

            String serviceRequestRaiseHrLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);

            serviceRequestValidator.validateServiceRequestDateAfterXHours(Constants.SERVICESCHEDULESTARTTIME, commandInput.getData().getScheduleSlotStartDateTime(),
                    Double.valueOf(serviceRequestRaiseHrLimit), errorInfoList);

            if (commandInput.getData().getServiceRequestAddressDetails() != null) {
                serviceRequestValidator.validateInspectionAddress(commandInput.getData().getServiceRequestAddressDetails(), errorInfoList);
            }

            ServiceRequestType serviceRequestType = ServiceRequestType.getServiceRequestType(commandInput.getData().getServiceRequestType());

            switch (serviceRequestType) {
                case WHC_INSPECTION:

                    if (!(ServiceRequestStatus.PENDING.getStatus()).equalsIgnoreCase(commandInput.getData().getStatus())) {
                        inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_STATUS_FOR_RESCHEDULE.getErrorCode(), errorInfoList);
                    }

                    break;
                case HA_EW:
                case HA_AD:
                case HA_BD:
                    if (!commandInput.getData().getWorkflowStage().equals(WorkflowStage.VISIT.getWorkflowStageCode())
                            && !commandInput.getData().getWorkflowStage().equals(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageCode()) && !commandInput.getData().getWorkflowStage().equals(WorkflowStage.VERIFICATION.getWorkflowStageCode())) {
                        inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.INVALID_STAGE_FOR_RESCHEDULE_SERVICE.getErrorCode(), errorInfoList);
                    }
                    String isSelfService = commandInput.getData().getServicePartnerCode() == null ? Constants.YES_FLAG : Constants.NO_FLAG;
                    if (null != isSelfService && Constants.NO_FLAG.equals(isSelfService)) {
                        // if SR is waiting for Spare, it cannt be rescheduled..
                        if (commandInput.getData().getWorkflowStageStatus() != null && commandInput.getData().getWorkflowStageStatus().equals("SN")) {
                            logger.error("Spare Part request is raised for the Service Request. Cannot reschedule.");
                            inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE_SPARE_REQ_RAISED.getErrorCode(), errorInfoList);
                        }
                    } else {
                        logger.error("Service Request is non-serviceable.");
                        inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_NON_SERVICEABLE.getErrorCode(), errorInfoList);
                    }

                    break;
                default:
                    logger.info("This action may not require an Event code for Request type");
            }
            if (!errorInfoList.isEmpty()) {
                throw new InputValidationException();
            }

            commandResult.setCanExecute(true);
        } catch (InputValidationException ex) {

            commandResult.setCanExecute(false);
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, ex);
        }

        return commandResult;
    }

    @Override
    protected CommandResult<ServiceResponseDto> executeCommand(CommandInput<ServiceRequestDto> commandInput) throws Exception {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        ServiceResponseDto response = new ServiceResponseDto();
        ServiceRequestDto serviceRequestUpdateDto = commandInput.getData();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        commandResult.setData(response);

        int isUpdated = 0;
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        CommunicationGatewayEventCode commEventCode = null;
        CommunicationGatewayEventCode commTechEventCode = null;
        CommunicationGatewayEventCode commPartnerEventCode = null;

        try {

            ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
            if (serviceRequestEntity == null) {
                serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
            }
            // ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
            ObjectMapper mapper = new ObjectMapper();
            WorkflowData workflowData = mapper.readValue(commandInput.getData().getWorkflowJsonString(), WorkflowData.class);

            ServiceRequestEventCode updateEventCode = serviceRequestHelper.populateEventCodeBasedOnActionAndRequestType(ServiceRequestUpdateAction.RESCHEDULE_SERVICE, serviceRequestUpdateDto);

            Map<String, Object> additionalAttributes = new HashMap<>();
            additionalAttributes.put(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue(), serviceRequestUpdateDto.getServicePartnerBuCode());
            additionalAttributes.put(CommunicationConstants.COMM_TECHNICIAN_ASSIGNEE_ID.getValue(), serviceRequestUpdateDto.getAssignee());

            if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.WHC_INSPECTION.getRequestType())) {
                if (serviceRequestUpdateDto.getServiceRequestAddressDetails() != null) {
                    processUpdateAddressRequest(serviceRequestUpdateDto);
                }

                if (updateEventCode != null) {
                    serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                    isUpdated = serviceRequestRepository.updateInspectionRequestScheduleAndStatus(serviceRequestUpdateDto.getScheduleSlotStartDateTime(),
                            serviceRequestUpdateDto.getScheduleSlotEndDateTime(), serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                            serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                            serviceRequestUpdateDto.getServiceRequestId());
                }
            } else if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_EW.getRequestType())
                    || serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())
                    || serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())) {

                populateUpdatedServiceRequestDueDate(serviceRequestUpdateDto, updateEventCode);
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestScheduleAndStatus(serviceRequestUpdateDto.getScheduleSlotStartDateTime(),
                        serviceRequestUpdateDto.getScheduleSlotEndDateTime(), serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), new Date(), serviceRequestUpdateDto.getModifiedBy(),
                        serviceRequestUpdateDto.getServiceRequestId(), serviceRequestUpdateDto.getDueDateTime());

                communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, CommunicationGatewayEventCode.SP_RESCHEDULED_POST_TECH_ALLOCATION, null);

                if (serviceRequestUpdateDto.getWorkflowStage().equals(WorkflowStage.VERIFICATION.getWorkflowStageCode())) {
                    if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
                        communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestUpdateDto, CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_REPAIR_ASSIGN,
                                additionalAttributes);
                    }
                }

                if (serviceRequestUpdateDto.getWorkflowStage().equals(WorkflowStage.VISIT.getWorkflowStageCode())
                        || serviceRequestUpdateDto.getWorkflowStage().equals(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageCode())) {
                    communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestUpdateDto, CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_SR_RESCHEDULE,
                            additionalAttributes);
                    communicationGatewayProxy.sendCommunication(Recipient.TECHNICIAN, serviceRequestUpdateDto, CommunicationGatewayEventCode.WHC_TECHNICIAN_NOTIFICATION_SR_RESCHEDULE,
                            additionalAttributes);

                }

                SimpleDateFormat formatter = new SimpleDateFormat(Constants.SERVICE_SCHEDULE_DATETIME_FORMAT);

                workflowManager.completeActivitiTask(Long.valueOf(serviceRequestUpdateDto.getWorkflowProcessId()),
                        "Rescheduled to " + formatter.format(serviceRequestUpdateDto.getScheduleSlotStartDateTime()), serviceRequestUpdateDto.getModifiedBy(), null);
            } else {
                isUpdated = serviceRequestRepository.updateServiceRequestSchedule(serviceRequestUpdateDto.getScheduleSlotStartDateTime(), serviceRequestUpdateDto.getScheduleSlotEndDateTime(),
                        currentTimestamp, serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }

            if (isUpdated == 1) {

                if (!StringUtils.isEmpty(updateEventCode)) {
                    switch (updateEventCode) {
                        case INSPECTION_SR_RESCHEDULE:
                            commEventCode = CommunicationGatewayEventCode.WHC_INSPECTION_RESCHEDULE;
                            commTechEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_NOTIFICATION_SR_RESCHEDULE;
                            commPartnerEventCode = CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_SR_RESCHEDULE;

                            additionalAttributes.put(INSPECTION_TIME_PARAM, serviceRequestUpdateDto.getScheduleSlotStartDateTime());
                            additionalAttributes.put(INSPECTION_DATE_PARAM, serviceRequestUpdateDto.getScheduleSlotStartDateTime());
                            additionalAttributes.put(OTP_PARAM, workflowData.getVisit().getServiceStartCode());
                            break;

                        default:
                            logger.info("No communication needs to be sent");
                    }
                }

                if (commEventCode != null) {
                    communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode, additionalAttributes);
                    communicationGatewayProxy.sendCommunication(Recipient.TECHNICIAN, serviceRequestUpdateDto, commTechEventCode, additionalAttributes);
                    communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestUpdateDto, commPartnerEventCode, additionalAttributes);
                }

                srDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, ServiceRequestUpdateAction.RESCHEDULE_SERVICE, serviceRequestUpdateDto.getServiceRequestId());

                commandResult.setData(null);
            } else {
                throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
            }

            commandResult.setCanExecute(false);

        } catch (BusinessServiceException ex) {

            commandResult.setCanExecute(false);
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, ex);
        }

        return commandResult;
    }

    private void processUpdateAddressRequest(ServiceRequestDto serviceRequestUpdateDto) throws BusinessServiceException {

        try {
            ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestUpdateDto.getServiceRequestId());

            if (serviceRequestEntity != null) {

                List<PincodeServiceFulfilmentEntity> pincodeFulfilments = fulfilmentRepository.findServiceCentreByPincodeAndServiceRequestTypeIdAndStatus(serviceRequestUpdateDto
                        .getServiceRequestAddressDetails().getPincode(), serviceRequestEntity.getServiceRequestTypeId(), Constants.ACTIVE);

                if (pincodeFulfilments == null || pincodeFulfilments.isEmpty()) {
                    throw new BusinessServiceException(messageSource.getMessage((String.valueOf(ServiceRequestResponseCodes.REQUEST_CANNOT_BE_PROCESSED_FOR_PINCODE.getErrorCode())),
                            new Object[] { "" }, null));
                }

                PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
                long partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null) ? Long.valueOf(pincodeServiceFulfilmentEntity.getPartnerBUCode()) : null;
                serviceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
                serviceRequestEntity.setServicePartnerBuCode(partnerBuCode);
                serviceRequestEntity.setAssignee(null);
                serviceRequestEntity.setModifiedOn(new Date());
                serviceRequestEntity.setModifiedBy(serviceRequestUpdateDto.getModifiedBy());

                ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
                WorkflowData workflowData = serviceRequestDto.getWorkflowData();
                ServiceRequestAddressDetailsDto serviceAddressRequest = serviceRequestUpdateDto.getServiceRequestAddressDetails();

                if (workflowData != null && workflowData.getVisit() != null) {
                    Visit visit = workflowData.getVisit();
                    String serviceAddressId = visit.getServiceAddress();
                    ServiceAddressEntity serviceAddressEntity = serviceAddressRepository.findOne(Long.valueOf(serviceAddressId));
                    serviceAddressEntity.setAddressLine1(serviceAddressRequest.getAddressLine1());
                    serviceAddressEntity.setAddressLine2(serviceAddressRequest.getAddressLine2());
                    serviceAddressEntity.setAddresseeFullName(serviceAddressRequest.getAddresseeFullName());
                    serviceAddressEntity.setCountryCode(serviceAddressRequest.getCountryCode());
                    serviceAddressEntity.setDistrict(serviceAddressRequest.getDistrict());
                    serviceAddressEntity.setEmail(serviceAddressRequest.getEmail());
                    serviceAddressEntity.setLandmark(serviceAddressRequest.getLandmark());
                    serviceAddressEntity.setMobileNo(BigDecimal.valueOf(serviceAddressRequest.getMobileNo()));
                    serviceAddressEntity.setPincode(serviceAddressRequest.getPincode());
                    serviceAddressEntity.setModifiedOn(new Date());
                    serviceAddressEntity.setModifiedBy(serviceRequestUpdateDto.getModifiedBy());

                    serviceAddressRepository.save(serviceAddressEntity);
                    serviceRequestRepository.save(serviceRequestEntity);
                }
            }
        } catch (Exception e) {
            logger.error("Error Updating Inspection Address Details", e);
            throw new BusinessServiceException("Error Updating Inspection Address Details", e);
        }
    }

    @SuppressWarnings("deprecation")
    private void populateUpdatedServiceRequestDueDate(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode eventCode) {

        if (serviceRequestUpdateDto.getWorkflowStage().equals("VE") || serviceRequestUpdateDto.getWorkflowStage().equals("VR")) {
            ServiceRequestRuleDto serviceRequestRuleDto = getSLA(serviceRequestUpdateDto);

            if (logger.isDebugEnabled())
                logger.debug("SLA Days:: " + serviceRequestRuleDto.getServiceRequestSLAInDays() + ", Extension Days:: " + serviceRequestRuleDto.getServiceRequestSLAExtentionDays());

            Date serviceScheduleStartTime = serviceRequestUpdateDto.getScheduleSlotStartDateTime();
            Date serviceDueDateTime = DateUtils.addDays(serviceScheduleStartTime, serviceRequestRuleDto.getServiceRequestSLAInDays());
            serviceDueDateTime = DateUtils.setHours(serviceDueDateTime, 21);
            serviceDueDateTime = DateUtils.setMinutes(serviceDueDateTime, 0);
            serviceDueDateTime = DateUtils.setSeconds(serviceDueDateTime, 0);
            serviceDueDateTime = DateUtils.setMilliseconds(serviceDueDateTime, 0);
            serviceRequestUpdateDto.setDueDateTime(serviceDueDateTime);
        } else if (serviceRequestUpdateDto.getWorkflowStage().equals("RA")) {
            ServiceRequestEntity oldServiceRequestEntity = new ServiceRequestEntity();
            oldServiceRequestEntity.setWorkflowData(serviceRequestUpdateDto.getWorkflowJsonString());
            ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(oldServiceRequestEntity, ServiceRequestDto.class);

            if (serviceRequestDto.getWorkflowData().getPartnerStageStatus().getStatus() != null && serviceRequestDto.getWorkflowData().getPartnerStageStatus().getStatus().equalsIgnoreCase("AR")) {
                String isICApprovalTimeToIncludeInDueTime = workflowManager.getVariable(serviceRequestUpdateDto.getWorkflowProcessId(), Constants.IS_LAST_STAGE_IC_DECISION);
                if (isICApprovalTimeToIncludeInDueTime.equals(Constants.YES_FLAG)) {
                    List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(serviceRequestUpdateDto.getWorkflowProcessId().toString())
                            .orderByHistoricTaskInstanceStartTime().finished().desc().list();
                    HistoricTaskInstance lastCompletedTask = historicTaskInstanceList.get(0);
                    // Date icDecisionStartTime = lastCompletedTask.getStartTime();
                    Date icDecisionStartTime = lastCompletedTask.getEndTime();
                    int days = Days.daysBetween(new LocalDate(icDecisionStartTime), new LocalDate(serviceRequestUpdateDto.getScheduleSlotStartDateTime())).getDays();
                    Date serviceDueDateTime = serviceRequestUpdateDto.getDueDateTime();
                    serviceDueDateTime = DateUtils.addDays(serviceDueDateTime, days);
                    if (logger.isDebugEnabled())
                        logger.debug("Due date extended after IC decision by noOfDays: " + days);
                    serviceRequestUpdateDto.setDueDateTime(serviceDueDateTime);
                    workflowManager.setVariable(serviceRequestUpdateDto.getWorkflowProcessId(), Constants.IS_LAST_STAGE_IC_DECISION, Constants.NO_FLAG);
                } else {
                    // RESCHEDULING AFTER SPAREPART_AVAILABLE LOGIC WILL GO HERE....
                    String sparePartMadeAvailableDateStr = workflowManager.getVariable(serviceRequestUpdateDto.getWorkflowProcessId(), Constants.SPARE_AVAILABLE_DATETIME);
                    SimpleDateFormat formatter = new SimpleDateFormat(Constants.SERVICE_SCHEDULE_DATETIME_FORMAT);
                    Date sparePartMadeAvailableDate = null;
                    try {
                        if (sparePartMadeAvailableDateStr != null)
                            sparePartMadeAvailableDate = formatter.parse(sparePartMadeAvailableDateStr);
                    } catch (java.text.ParseException e) {
                        logger.error("Exception while parsing the date: ", e);
                    }

                    if (sparePartMadeAvailableDate != null) {
                        final int days = Days.daysBetween(new LocalDate(sparePartMadeAvailableDate), new LocalDate(serviceRequestUpdateDto.getScheduleSlotStartDateTime())).getDays();
                        Date serviceDueDateTime = serviceRequestUpdateDto.getDueDateTime();
                        serviceDueDateTime = DateUtils.addDays(serviceDueDateTime, days);

                        if (logger.isDebugEnabled())
                            logger.debug("Due date extended after Spare Available by noOfDays: " + days);
                        serviceRequestUpdateDto.setDueDateTime(serviceDueDateTime);
                    }
                }
            } else {
                // Second time rescheduling taking place WHEREAS once re-scheduling is done after IC approved OR Spare Part available..
                ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findServiceRequestEntityByServiceRequestId(serviceRequestUpdateDto.getServiceRequestId());
                final int days = Days.daysBetween(new LocalDate(serviceRequestEntity.getScheduleSlotStartDateTime()), new LocalDate(serviceRequestUpdateDto.getScheduleSlotStartDateTime())).getDays();
                Date serviceDueDateTime = serviceRequestUpdateDto.getDueDateTime();
                serviceDueDateTime = DateUtils.addDays(serviceDueDateTime, days);

                if (logger.isDebugEnabled())
                    logger.debug("Due date extended while in RA stage by noOfDays: " + days);

                serviceRequestUpdateDto.setDueDateTime(serviceDueDateTime);
            }
        }
    }

    private ServiceRequestRuleDto getSLA(ServiceRequestDto serviceRequestUpdateDto) {

        ServiceRequestRuleDto serviceRequestRuleDto = new ServiceRequestRuleDto();
        serviceRequestRuleDto.setServicePartnerCode(serviceRequestUpdateDto.getServicePartnerCode());
        serviceRequestRuleDto.setIsRuleValid(0);
        RuleEngine.execute(RuleName.CRITERIA_RULE, serviceRequestRuleDto);

        if (serviceRequestRuleDto.getIsRuleValid() < 1) {
            serviceRequestRuleDto.setCriteria("DEFAULT");// if criteria is not set in rule for ServicePartner..
            serviceRequestRuleDto.setServicePartnerCode(0L);
            serviceRequestRuleDto.setProductCode(null);
            RuleEngine.execute(RuleName.PARTNER_SLA_RULE, serviceRequestRuleDto);
        } else {
            serviceRequestRuleDto.setIsRuleValid(0);
            switch (serviceRequestRuleDto.getCriteria()) {
                case Constants.PARTNER:
                    serviceRequestRuleDto.setServicePartnerCode(serviceRequestUpdateDto.getServicePartnerCode());
                    serviceRequestRuleDto.setProductCode(null);
                    RuleEngine.execute(RuleName.PARTNER_SLA_RULE, serviceRequestRuleDto);
                    break;
                case Constants.PRODUCT:
                    serviceRequestRuleDto.setServicePartnerCode(0L);
                    serviceRequestRuleDto.setProductCode(null);
                    RuleEngine.execute(RuleName.PRODUCTCODE_SLA_RULE, serviceRequestRuleDto);
                    break;
                case Constants.PARTNER_PRODUCT:
                    serviceRequestRuleDto.setServicePartnerCode(serviceRequestUpdateDto.getServicePartnerCode());
                    serviceRequestRuleDto.setProductCode(null);
                    RuleEngine.execute(RuleName.PARTNER_PRODUCTCODE_SLA_RULE, serviceRequestRuleDto);
                    break;
                default:
                    serviceRequestRuleDto.setServicePartnerCode(0L);
                    serviceRequestRuleDto.setProductCode(null);
                    RuleEngine.execute(RuleName.PARTNER_SLA_RULE, serviceRequestRuleDto);
            }
        }

        if (serviceRequestRuleDto.getIsRuleValid() < 1) {
            serviceRequestRuleDto.setServicePartnerCode(0L);
            serviceRequestRuleDto.setProductCode(null);
            RuleEngine.execute(RuleName.PARTNER_SLA_RULE, serviceRequestRuleDto);
        }

        return serviceRequestRuleDto;
    }
}
