package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class AssignCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    private final Logger logger = Logger.getLogger(AssignCommand.class);

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    private static String TECHNICIAN_NAME_PARAM = "TechnicianName";

    private static String TECHNICIAN_MOBILE_PARAM = "TechnicianMobile";

    private static String OTP_PARAM = "OTP";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        try {

            if (commandInput.getData().getAssignee() == null) {
                inputValidator.populateMandatoryFieldError("assignee", errorInfoList);
            }
            if (commandInput.getData().getServicePartnerCode() == null) {
                inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_NON_SERVICEABLE.getErrorCode(), errorInfoList);
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
        ServiceRequestDto serviceRequestUpdateDto = commandInput.getData();

        UserProfileData userProfileData = oasysAdminProxy.getTechnicianUserProfile(String.valueOf(serviceRequestUpdateDto.getAssignee()));

        if (userProfileData != null) {
            boolean technicianChanged = false;

            ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
            if (serviceRequestEntity == null) {
                serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
            }

            ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
            String startOtp = null;

            if (null != serviceRequestDto.getWorkflowData().getVisit().getServiceStartCode()) {
                startOtp = serviceRequestDto.getWorkflowData().getVisit().getServiceStartCode();
            }

            serviceRequestUpdateDto.setServiceRequestType(serviceRequestHelper.getServiceReqeustType(serviceRequestEntity.getServiceRequestTypeId()));
            serviceRequestUpdateDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
            serviceRequestUpdateDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());

            ServiceRequestEventCode updateEventCode = serviceRequestHelper.populateEventCodeBasedOnActionAndRequestType(ServiceRequestUpdateAction.ASSIGN, commandInput.getData());

            if (serviceRequestEntity.getAssignee() != null && serviceRequestEntity.getAssignee().longValue() != commandInput.getData().getAssignee().longValue()) {

                logger.debug("There is a change in Technician Assignment");
                technicianChanged = true;
            }

            int isUpdated = 0;
            CommunicationGatewayEventCode commEventCode = null;
            CommunicationGatewayEventCode commTechEventCode = null;

            if (updateEventCode != null) {
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);

                isUpdated = serviceRequestRepository.updateAssigneeAndServiceRequestOnEvent(serviceRequestUpdateDto.getAssignee(), serviceRequestUpdateDto.getStatus(),
                        serviceRequestUpdateDto.getWorkflowStage(), serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), new Date(),
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());

            } else {
                isUpdated = serviceRequestRepository.updateServiceRequestAssignee(serviceRequestUpdateDto.getAssignee(), new Date(), serviceRequestUpdateDto.getModifiedBy(),
                        serviceRequestUpdateDto.getServiceRequestId());

                commEventCode = CommunicationGatewayEventCode.SP_TECH_ASSIGNED;
                commTechEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_NOTIFICATION_REPAIR_ASSIGN;
            }

            if (isUpdated == 1) {
                HashMap<String, Object> additionalAttributes = null;
                if (!org.springframework.util.StringUtils.isEmpty(updateEventCode)) {
                    switch (updateEventCode) {
                        case ALLOCATE_TECH_INSPECT:
                            if (technicianChanged) {
                                commEventCode = CommunicationGatewayEventCode.WHC_OTHER_TECHNICIAN_ASSIGNED_FOR_INSPECTION;
                            } else {
                                commEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_ASSIGNED_FOR_INSPECTION;
                            }
                            commTechEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_NOTIFICATION_INSPECTION_ASSIGN;
                            break;

                        default:
                            logger.info("No communication needs to be sent");
                    }
                }

                if (commEventCode != null) {
                    String fullName = StringUtils.concatenate(new String[] { userProfileData.getFirstName(), userProfileData.getMiddleName(), userProfileData.getLastName() }, " ");

                    additionalAttributes = new HashMap<String, Object>();
                    additionalAttributes.put(TECHNICIAN_NAME_PARAM, fullName);
                    additionalAttributes.put(TECHNICIAN_MOBILE_PARAM, userProfileData.getMobileNumber());
                    additionalAttributes.put(OTP_PARAM, startOtp);
                    additionalAttributes.put(CommunicationConstants.COMM_TECHNICIAN_PROFILE_DATA.getValue(), userProfileData);
                    communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode, additionalAttributes);
                    communicationGatewayProxy.sendCommunication(Recipient.TECHNICIAN, serviceRequestUpdateDto, commTechEventCode, additionalAttributes);

                }

                try {
                    serviceRequestDto.setAssignee(serviceRequestUpdateDto.getAssignee());
                    serviceRequestDto.setStatus(serviceRequestUpdateDto.getStatus());
                    serviceRequestDto.setWorkflowStage(serviceRequestUpdateDto.getWorkflowStage());
                    serviceRequestDto.setWorkflowStageStatus(serviceRequestUpdateDto.getWorkflowStageStatus());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestUpdateDto.getWorkflowJsonString());

                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                    serviceRequestDto.setWorkflowData(objectMapper.readValue(serviceRequestUpdateDto.getWorkflowJsonString(), WorkflowData.class));
                    serviceRequestDto.setServiceRequestType(serviceRequestHelper.getServiceReqeustType(serviceRequestEntity.getServiceRequestTypeId()));

                    srDataNotificationManager.notify(DataNotificationEventType.UPDATED, serviceRequestDto, ServiceRequestUpdateAction.ASSIGN);
                } catch (Exception e) {
                    logger.error("Exception while publishing data to notification queue", e);
                }

                commandResult.setData(null);
            } else {
                throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
            }
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.INVALID_ASSIGNEE.getErrorCode());
        }

        return commandResult;
    }
}
