package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowAlert;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Diagnosis;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class UpdateWorkflowDataOnEventCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private IServiceRequestService serviceRequestService;

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    private final Logger logger = Logger.getLogger(UpdateWorkflowDataOnEventCommand.class);

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {
        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        ServiceResponseDto response = new ServiceResponseDto();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        commandResult.setData(response);

        try {
            inputValidator.validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, commandInput.getData().getServiceRequestId(), errorInfoList);
            if (Strings.isNullOrEmpty(commandInput.getData().getModifiedBy())) {
                inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoList);
            }
            if (commandInput.getData().getWorkflowData() == null) {
                inputValidator.populateMandatoryFieldError("workflowData", errorInfoList);
            }
            if (commandInput.getData().getWorkflowData().getRepairAssessment() == null) {
                inputValidator.populateMandatoryFieldError("repairAssessment", errorInfoList);
            }

            if (!errorInfoList.isEmpty()) {
                throw new InputValidationException();
            }

            if (!commandInput.getData().getStatus().equals(ServiceRequestStatus.INPROGRESS.getStatus())) {
                logger.error("Invalid Service Request Status to update.");
                inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_STATUS.getErrorCode(), errorInfoList);
            }
            if (commandInput.getData().getServicePartnerCode() == null) {
                logger.error("Service Request is of type Self-service. Invalid to update.");
                inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_NON_SERVICEABLE.getErrorCode(), errorInfoList);
            }
            commandInput.setCommandEventCode(getValidEventCode(commandInput.getData()));

            commandResult.setCanExecute(true);

        } catch (InputValidationException ex) {
            commandResult.setCanExecute(false);
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, ex);
        }

        return commandResult;
    }

    private ServiceRequestEventCode getValidEventCode(ServiceRequestDto serviceRequestDto) throws NoSuchMessageException, BusinessServiceException {
        ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestDto.getServiceRequestId());
        AssigneeRepairCostRequestDto assigneeRepairCostRequestDto = new AssigneeRepairCostRequestDto();
        List<String> repairAssessmentIds = new ArrayList<>();
        List<SpareParts> spareList = null;
        if (serviceRequestDto != null) {
            spareList = serviceRequestDto.getWorkflowData().getRepairAssessment().getSparePartsRequired();
            if (spareList != null) {
                for (SpareParts sparePart : spareList) {
                    repairAssessmentIds.add(sparePart.getSparePartId());
                }
            }
            List<Diagnosis> diagnosisList = serviceRequestDto.getWorkflowData().getRepairAssessment().getDiagonosisReportedbyAssignee();
            if (diagnosisList != null) {
                for (Diagnosis diagnosis : diagnosisList) {
                    repairAssessmentIds.add(diagnosis.getDiagnosisId());
                }
            }
            if (serviceRequestDto.getWorkflowData().getRepairAssessment().getTransport() != null) {
                repairAssessmentIds.add(serviceRequestDto.getWorkflowData().getRepairAssessment().getTransport().getTransportTaskId());
            }
            if (serviceRequestDto.getWorkflowData().getRepairAssessment().getLabourCost() != null) {
                repairAssessmentIds.add(serviceRequestDto.getWorkflowData().getRepairAssessment().getLabourCost().getLabourChargeId());
            }
        }
        assigneeRepairCostRequestDto.setInvoiceValue(serviceRequestDto.getWorkflowData().getRepairAssessment().getInvoiceValue() == null ? 0 : Double.parseDouble(serviceRequestDto.getWorkflowData()
                .getRepairAssessment().getInvoiceValue()));
        CostToServiceDto costToServiceDto = serviceRequestService.calculateCostToCustomer(serviceRequestEntity.getServiceRequestId(), assigneeRepairCostRequestDto);

        if (costToServiceDto != null) {
            ServiceRequestDto existingServiceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
            if (!costToServiceDto.isEstimateRequestApprovedStatus()) {
                if (serviceRequestDto.getWorkflowData().getRepairAssessment().getTransport() != null && existingServiceRequestDto.getWorkflowData().getRepairAssessment().getTransport() == null) {
                    return ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED;
                } else if (spareList != null) {
                    for (SpareParts sparePart : spareList) {
                        if (sparePart.getSparePartAvailable().equals(Constants.NO_FLAG)) {
                            return ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_SPARE_NEEDED;
                        }
                    }
                }
                return ServiceRequestEventCode.IC_APPROVAL_WAITING;
            } else if (serviceRequestDto.getWorkflowData().getRepairAssessment().getTransport() != null && existingServiceRequestDto.getWorkflowData().getRepairAssessment().getTransport() == null) {
                return ServiceRequestEventCode.TRANSPORTATION_REQUIRED;
            } else if (spareList != null) {
                for (SpareParts sparePart : spareList) {
                    if (sparePart.getSparePartAvailable().equals(Constants.NO_FLAG)) {
                        return ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE;
                    }
                }
            }
        }
        return ServiceRequestEventCode.REPAIR_ASSESSMENT_COMPLETED;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected CommandResult<ServiceResponseDto> executeCommand(CommandInput<ServiceRequestDto> commandInput) throws Exception {

        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        ServiceRequestDto serviceRequestUpdateDto = commandInput.getData();
        ServiceResponseDto response = new ServiceResponseDto();
        List<String> additionalAttributes = new ArrayList<>();

        int isUpdated = 0;
        ServiceRequestEventCode updateEventCode = commandInput.getCommandEventCode();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        ServiceRequestEntity serviceRequestEntity = commandInput.getServiceRequestEntity();
        if (serviceRequestEntity == null) {
            serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
        }

        switch (updateEventCode) {

            case SPARE_PART_NOT_AVAILABLE:
            case TRANSPORTATION_REQUIRED: {
                String workflowJsonString = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowJsonString(workflowJsonString);
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, updateEventCode);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                additionalAttributes.add(updateEventCode.equals(ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE) ? ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE.getServiceRequestEvent()
                        : ServiceRequestEventCode.TRANSPORTATION_REQUIRED.getServiceRequestEvent());
            }
                break;
            case IC_APPROVAL_WAITING_WITH_SPARE_NEEDED: {
                String workflowJsonString = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowJsonString(workflowJsonString);
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_SPARE_NEEDED, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                additionalAttributes.add(ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_SPARE_NEEDED.getServiceRequestEvent());
            }
                break;
            case IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED: {
                String workflowJsonString = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowJsonString(workflowJsonString);
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.TRANSPORTATION_REQUIRED);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                additionalAttributes.add(ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED.getServiceRequestEvent());
            }
                break;
            case IC_APPROVAL_WAITING: {
                String workflowJsonString = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowJsonString(workflowJsonString);
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestOnEvent(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                        serviceRequestUpdateDto.getServiceRequestId());
                additionalAttributes.add(ServiceRequestEventCode.IC_APPROVAL_WAITING.getServiceRequestEvent());
            }
                break;
            default:
                additionalAttributes.add(ServiceRequestEventCode.REPAIR_ASSESSMENT_COMPLETED.getServiceRequestEvent());
                isUpdated = 1;
                CommunicationGatewayEventCode commEventCode = null;
                if (ServiceRequestType.HA_BD.getRequestType().equals(serviceRequestUpdateDto.getServiceRequestType())) {
                    commEventCode = CommunicationGatewayEventCode.SP_BD_RAPAIR_ASSESSMENT_COMPLETED;
                } else {
                    commEventCode = CommunicationGatewayEventCode.SP_RAPAIR_ASSESSMENT_COMPLETED;
                }

                if (commEventCode != null) {
                    Map additionalAttr = new HashMap();
                    additionalAttr.put("balanceCostToCusotmer", serviceRequestUpdateDto.getWorkflowData().getRepairAssessment().getCostToCustomer());
                    communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode, additionalAttr);
                }
        }

        if (isUpdated == 1) {
            srDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT, serviceRequestUpdateDto.getServiceRequestId());
            response.setAdditionalAttributes(additionalAttributes);
            commandResult.setData(response);
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
        }
        return commandResult;
    }

    private void populateWorkflowAlert(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode eventCode) {
        String alertCode = null;
        if (ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE.equals(eventCode)) {
            alertCode = WorkflowAlert.PROCURE_SPARE.getWorkflowAlertCode();
        } else if (ServiceRequestEventCode.TRANSPORTATION_REQUIRED.equals(eventCode)) {
            alertCode = WorkflowAlert.RETURN_MACHINE_TO_CUSTOMER.getWorkflowAlertCode();
        }
        serviceRequestUpdateDto.setWorkflowAlert(alertCode);
    }
}
