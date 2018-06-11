package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
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
public class UpdateWorkFlowDataCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    private final Logger logger = Logger.getLogger(UpdateWorkFlowDataCommand.class);

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        try {

            if (commandInput.getData().getWorkflowStage() == null) {
                inputValidator.populateMandatoryFieldError("workFlowStage", errorInfoList);
            }

            if (commandInput.getData().getWorkflowData() == null) {
                inputValidator.populateMandatoryFieldError("workFlowData", errorInfoList);
            }

            WorkflowStage workflowStage = WorkflowStage.getWorkflowStage(commandInput.getData().getWorkflowStage());
            if (workflowStage != null) {
                switch (workflowStage) {
                    case VISIT:
                        inputValidator.validateRequiredField(WorkflowStage.VISIT.getWorkflowStageName().toLowerCase(), commandInput.getData().getWorkflowData().getVisit(), errorInfoList);
                        break;
                    case DOCUMENT_UPLOAD:
                        inputValidator.validateRequiredField(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName(), commandInput.getData().getWorkflowData().getDocumentUpload(), errorInfoList);
                        break;
                    case VERIFICATION:
                        inputValidator
                                .validateRequiredField(WorkflowStage.VERIFICATION.getWorkflowStageName().toLowerCase(), commandInput.getData().getWorkflowData().getVerification(), errorInfoList);
                        break;
                    case REPAIR_ASSESSMENT:
                        inputValidator.validateRequiredField(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName(), commandInput.getData().getWorkflowData().getRepairAssessment(), errorInfoList);
                        break;
                    case SOFT_APPROVAL:
                        inputValidator.validateRequiredField("softApproval", commandInput.getData().getWorkflowData().getSoftApproval(), errorInfoList);
                        break;
                    case REPAIR:
                        inputValidator.validateRequiredField(WorkflowStage.REPAIR.getWorkflowStageName(), commandInput.getData().getWorkflowData().getRepair(), errorInfoList);
                        break;
                    case CLAIM_SETTLEMENT:
                        inputValidator.validateRequiredField(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName(), commandInput.getData().getWorkflowData().getClaimSettlement(), errorInfoList);
                        break;
                    case COMPLETED:
                        inputValidator.validateRequiredField(WorkflowStage.COMPLETED.getWorkflowStageName(), commandInput.getData().getWorkflowData().getCompleted(), errorInfoList);
                        break;
                    case INSPECTION_ASSESSMENT:
                        inputValidator.validateRequiredField(WorkflowStage.INSPECTION_ASSESSMENT.getWorkflowStageName(), commandInput.getData().getWorkflowData().getInspectionAssessment(),
                                errorInfoList);
                        break;
                    default:
                        inputValidator.populateMandatoryFieldError("workflowStage", errorInfoList);
                        logger.error("No valid workflowstage set to update workflow data.");
                }
            } else {
                inputValidator.populateMandatoryFieldError("workflowStage", errorInfoList);
            }

            if (Strings.isNullOrEmpty(commandInput.getData().getModifiedBy())) {
                inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoList);
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
        ServiceResponseDto serviceResponseDto = new ServiceResponseDto();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
        if (serviceRequestEntity == null) {
            serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
        }
        String updatedJsonData = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
        int isUpdated = serviceRequestRepository.updateServiceRequestWorkflowData(updatedJsonData, currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                serviceRequestUpdateDto.getServiceRequestId());

        if (isUpdated != 1) {
            throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
        }

        commandResult.setData(serviceResponseDto);

        return commandResult;
    }
}
