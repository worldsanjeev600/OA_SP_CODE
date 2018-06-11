package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class CancelCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    protected MessageSource messageSource;

    private final Logger logger = Logger.getLogger(CancelCommand.class);

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        commandResult.setCanExecute(true);
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

        ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
        if (serviceRequestEntity == null) {
            serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
        }
        serviceRequestUpdateDto.setServiceRequestType(ServiceRequestType.WHC_INSPECTION.getRequestType());
        serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, commandInput.getCommandEventCode(), serviceRequestEntity);
        serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, commandInput.getCommandEventCode(), serviceRequestEntity);

        int isUpdated = serviceRequestRepository.updateServiceRequestOnEvent(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                serviceRequestUpdateDto.getServiceRequestId());

        if (isUpdated == 1) {
            CommunicationGatewayEventCode commEventCode = null;
            HashMap<String, Object> additionalAttributes = null;

            switch (commandInput.getCommandEventCode()) {
                case TECHNICIAN_CANCEL_INSPECTION_FOR_SOME_REASON:
                    commEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_CANCELLED_FOR_INSPECTION;

                    break;
                default:
                    logger.info("No communication needs to be sent");
            }

            if (commEventCode != null) {
                communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode, additionalAttributes);
            }

            srDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, ServiceRequestUpdateAction.CANCEL_SERVICE, serviceRequestUpdateDto.getServiceRequestId());

            commandResult.setData(null);
        } else {

            throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
        }

        return commandResult;
    }
}
