package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class CloseCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    protected MessageSource messageSource;

    private final Logger logger = Logger.getLogger(CloseCommand.class);

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private InputValidator inputValidator;

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        try {

            if (commandInput.getData().getModifiedBy() == null) {
                inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoList);
            }
            if (commandInput.getData().getServiceCancelReason() == null) {
                inputValidator.populateMandatoryFieldError("serviceCancelReason", errorInfoList);
            }
            if (commandInput.getData().getRemarks() == null) {
                inputValidator.populateMandatoryFieldError("remarks", errorInfoList);
            }
            if (ServiceRequestStatus.COMPLETED.getStatus().equals(commandInput.getData().getStatus()) || ServiceRequestStatus.CLOSED.getStatus().equals(commandInput.getData().getStatus())) {
                inputValidator.populateInvalidData("Service Request Id", errorInfoList);
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
        ServiceResponseDto response = new ServiceResponseDto();
        commandResult.setData(response);

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        ServiceRequestEventCode updateEventCode = ServiceRequestEventCode.HA_CLAIMS_CLOSE_SERVICE_REQUEST;
        commandInput.setCommandEventCode(updateEventCode);
        ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
        if (serviceRequestEntity == null) {
            serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
        }
        serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
        serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);

        int isUpdated = serviceRequestRepository.updateServiceRequestStatusWorkflowAlertAndActualEndDate(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), null, currentTimestamp, currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                serviceRequestUpdateDto.getServiceRequestId());

        if (isUpdated == 1) {
            communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, CommunicationGatewayEventCode.SP_CLOSE_SR, null);

            srDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, ServiceRequestUpdateAction.CLOSE_SERVICE_REQUEST, serviceRequestUpdateDto.getServiceRequestId());

            commandResult.setData(null);
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
        }

        return commandResult;
    }
}
