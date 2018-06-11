package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

@Service
public class SubmitFeedbackCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    protected MessageSource messageSource;
    
    @Autowired
    ModelMapper modelMapper;

    private final Logger logger = Logger.getLogger(SubmitFeedbackCommand.class);

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        
        try {

            if (commandInput.getData().getServiceRequestId() == null) {
                inputValidator.populateMandatoryFieldError("serviceRequestId", errorInfoList);
            }
            else {
                ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());

                if (serviceRequestEntity == null) {
                    logger.error("Invalid Service Request Id");
                    inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode(), errorInfoList);
                } else {
                    commandInput.getData().setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    commandInput.getData().setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                }

            }

            if(commandInput.getData().getServiceRequestFeedback() == null){
                inputValidator.populateMandatoryFieldError("serviceRequestFeedback", errorInfoList);
            }
            
            if(Strings.isNullOrEmpty(commandInput.getData().getServiceRequestFeedback().getFeedbackRating())){
                inputValidator.populateMandatoryFieldError("FeedbackRating", errorInfoList);
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
    protected CommandResult<ServiceResponseDto> executeCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {
        
        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        ServiceRequestDto serviceRequestUpdateDto = commandInput.getData();
       // ServiceResponseDto response = new ServiceResponseDto();
        commandResult.setData(modelMapper.map(serviceRequestUpdateDto,ServiceResponseDto.class));
        String feedBackCodeString = null;
        
        if(serviceRequestUpdateDto.getServiceRequestFeedback()!=null && org.apache.commons.lang3.StringUtils.isNotEmpty(serviceRequestUpdateDto.getServiceRequestFeedback().getFeedbackCode())){
        	feedBackCodeString = serviceRequestUpdateDto.getServiceRequestFeedback().getFeedbackCode();
        }
        
        int isUpdated = serviceRequestRepository.updateServiceRequestFeedbackDetails(
										        		serviceRequestUpdateDto.getServiceRequestId(), 
										        		serviceRequestUpdateDto.getServiceRequestFeedback().getFeedbackRating(), 
										        		feedBackCodeString, 
										        		serviceRequestUpdateDto.getServiceRequestFeedback().getFeedbackComments(),
										                new Date(), 
										                serviceRequestUpdateDto.getModifiedBy());
        
        if (isUpdated != 1) {
        	throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
        }
        
        return commandResult;
    }
}
