package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import java.sql.Timestamp;
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
import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceDocumentType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowAlert;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestDocumentTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.DocumentTypeConfigRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PaymentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestRuleDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateSRonEventCommand extends BaseActionCommand<ServiceRequestDto, ServiceResponseDto> {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    @Autowired
    private ServiceRequestDocumentTypeMasterCache serviceRequestDocumentTypeMasterCache;

    @Autowired
    private DocumentTypeConfigRepository documentTypeConfigRepository;

    @Autowired
    private IServiceRequestService serviceRequestService;
    
	@Value("${ADVICE_AMOUNT}")
	private String adviceAmount;

	@Value("${ADVICE_CHARGE_ID}")
	private String adviceChargeId;
	
	@Autowired
	private OasysProxy oasysProxy;

    private final Logger logger = Logger.getLogger(UpdateSRonEventCommand.class);

    @Override
    protected CommandResult<ServiceResponseDto> canExecuteCommand(CommandInput<ServiceRequestDto> commandInput) throws BusinessServiceException {
        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        ServiceResponseDto response = new ServiceResponseDto();
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        commandResult.setData(response);

        try {
            if (commandInput.getData().getWorkflowData() == null) {
                ServiceRequestDto requestDto = serviceRequestHelper.convertObject(commandInput.getServiceRequestEntity(), ServiceRequestDto.class);
                commandInput.getData().setWorkflowData(requestDto.getWorkflowData());
            }
            if (commandInput.getCommandEventCode() != null) {
                switch (commandInput.getCommandEventCode()) {
                	case SPARE_PART_NOT_AVAILABLE:
                		if("SN".equals(commandInput.getData().getWorkflowStageStatus())){
                			throw new InputValidationException("Spare Parts request already raised.");
                		}
                	break;
                    case COMPLETE_CLAIM_SETTLEMENT:
                        inputValidator.validateRequiredField(Constants.WORKFLOWDATA, commandInput.getData().getWorkflowData(), errorInfoList);
                        inputValidator.validateRequiredField(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName(), commandInput.getData().getWorkflowData().getClaimSettlement(), errorInfoList);
                        inputValidator.validateRequiredField("claimAmount", commandInput.getData().getWorkflowData().getClaimSettlement().getClaimAmount(), errorInfoList);
                        inputValidator.validateDoubleFormat("claimAmount", commandInput.getData().getWorkflowData().getClaimSettlement().getClaimAmount(), errorInfoList);
                        break;
                    case TECHNICIAN_AT_LOCATION_AND_BEGINS_SERVICE:
                        // inputValidator.validateRequiredField("otp", commandInput.getData().getOtp(), errorInfoList);
                        break;
                    case REPAIR_SUCCESSFUL: {
                        // inputValidator.validateRequiredField("otp", commandInput.getData().getOtp(), errorInfoList);
                        inputValidator.validateRequiredField(Constants.WORKFLOWDATA, commandInput.getData().getWorkflowData(), errorInfoList);

                        if (commandInput.getData().getWorkflowData().getRepairAssessment() != null
                                && CollectionUtils.isNotEmpty(commandInput.getData().getWorkflowData().getRepairAssessment().getSparePartsRequired())) {

                            Map<String, DocTypeMstEntity> documentTypeEntitiesMap = serviceRequestDocumentTypeMasterCache.getAll();
                            List<DocTypeMstEntity> documentTypeEntitiesList = new ArrayList<>(documentTypeEntitiesMap.values());
                            DocTypeMstEntity damagedSparePartsEntity = null;

                            for (DocTypeMstEntity docType : documentTypeEntitiesList) {
                                if (docType.getDocName().equalsIgnoreCase(ServiceDocumentType.DAMAGED_SPARE_PART.getServiceDocumentType())) {
                                    damagedSparePartsEntity = docType;
                                    break;
                                }
                            }

                            List<SpareParts> sparePartsList = commandInput.getData().getWorkflowData().getRepairAssessment().getSparePartsRequired();
                            List<ServiceDocumentEntity> sparePartDocumentsUploaded = serviceDocumentRepository.findByServiceRequestIdAndDocumentTypeIdAndStatus(commandInput.getData().getServiceRequestId(),
                                    damagedSparePartsEntity.getDocTypeId(), Constants.ACTIVE);

                            Map<String, ServiceDocumentEntity> sparePartDocumentsUploadedMap = new HashMap<>();
                            for (ServiceDocumentEntity document : sparePartDocumentsUploaded) {
                                sparePartDocumentsUploadedMap.put(document.getDocumentId(), document);
                            }
                            for (SpareParts sparePart : sparePartsList) {
                                if (!sparePartDocumentsUploadedMap.containsKey(sparePart.getDocumentId())) {
                                	throw new InputValidationException("Spare Parts does not have mandatory documents uploaded for Damaged Spare Part");
                                  
                                }
                            }
                        }
                        break;
                    }

                    case COMPLETE_DOCUMENT_UPLOAD:
                        List<Long> requiredDocuments = new ArrayList<>();
                        List<ServiceDocumentEntity> uploadedDocumentBySR = serviceDocumentRepository.findByServiceRequestId(commandInput.getData().getServiceRequestId());
                        List<DocTypeConfigDetailEntity> docTypeConfigEntitylist = documentTypeConfigRepository.findByServiceRequestTypeId(commandInput.getData().getServiceRequestType());
                        for (DocTypeConfigDetailEntity docTypeConfig : docTypeConfigEntitylist) {
                            if (docTypeConfig.getIsMandatory().equalsIgnoreCase(Constants.YES_FLAG)) {
                                int docPresentFlag = 0;
                                Long documentTypeId = docTypeConfig.getDocTypeId();
                                for (ServiceDocumentEntity document : uploadedDocumentBySR) {
                                    if (document.getDocumentTypeId().longValue() == documentTypeId.longValue()) {
                                        docPresentFlag = 1;
                                        break;
                                    }
                                }
                                if (docPresentFlag == 0) {
                                    requiredDocuments.add(documentTypeId);
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(requiredDocuments)) {
                            throw new BusinessServiceException("Following documents Required:" + requiredDocuments.toString());
                        }
                        break;
                    default:
                        logger.info("Event code validation is not requied.");
                }

                ServiceRequestType requestType = ServiceRequestType.getServiceRequestType(commandInput.getData().getServiceRequestType());
                switch (requestType) {
                    case HA_AD:
                    case HA_BD:
                    case HA_EW:

                        switch (commandInput.getCommandEventCode()) {
                            case TECHNICIAN_AT_LOCATION_AND_BEGINS_SERVICE: {
                                /*
                                 * ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
                                 * serviceRequestHelper.validateAuthorizationCode(commandInput.getData().getOtp(), 1, serviceRequestEntity);
                                 * commandInput.getData().setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                                 */
                                break;
                            }
                            case REPAIR_SUCCESSFUL: {
                                /*
                                 * ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(commandInput.getData().getServiceRequestId());
                                 * serviceRequestHelper.validateAuthorizationCode(commandInput.getData().getOtp(), 2, serviceRequestEntity);
                                 * commandInput.getData().setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                                 */
                                break;
                            }
                            default:
                                logger.info("Event code validation is not requied.");
                        }
                    default:
                        logger.info("No Valid Event codes needed for this RequestType");
                }
                commandResult.setCanExecute(true);
            } else {
                throw new BusinessServiceException("Invalid Event Code");
            }

            if (!errorInfoList.isEmpty()) {
                throw new InputValidationException();
            }
        } catch (InputValidationException ex) {

            commandResult.setCanExecute(false);
            if (errorInfoList.isEmpty()) {
                ErrorInfoDto errorInfoDto = new ErrorInfoDto(16, ex.getMessage(), null);
                errorInfoList.add(errorInfoDto);
            }
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
            case COMPLETE_CLAIM_SETTLEMENT: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowData(ServiceRequestStatus.CLOSED.getStatus(), serviceRequestUpdateDto.getWorkflowJsonString(),
                        currentTimestamp, serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;

            case SPARE_PART_NOT_AVAILABLE:
            case TRANSPORTATION_REQUIRED: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, updateEventCode);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;
            case CUSTOMER_CAN_WAIT_FURTHER: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                ServiceRequestRuleDto serviceRequestRuleDto = getSLA(serviceRequestUpdateDto);

                
                isUpdated = serviceRequestRepository.updateServiceRequestScheduleAndCMSStatus(serviceRequestUpdateDto.getWorkflowStage(), serviceRequestUpdateDto.getWorkflowStageStatus(), new Date(),
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId(),
                        DateUtils.addDays(serviceRequestUpdateDto.getDueDateTime(), serviceRequestRuleDto.getServiceRequestSLAExtentionDays()), serviceRequestUpdateDto.getWorkflowJsonString());
            }
                break;
            case IC_APPROVAL_WAITING_WITH_SPARE_NEEDED: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_SPARE_NEEDED, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;
            case IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.IC_APPROVAL_WAITING, serviceRequestEntity);
                populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.TRANSPORTATION_REQUIRED);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;
            case SPARE_PART_AVAILABLE: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowAlert(null);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;
            case COMPLETED_AFTER_REPAIR_ASSESSMENT:
            case IC_DECISION_REJECTED:
            case APPLIANCE_PICKED_FOR_BER:
            case REPAIR_SUCCESSFUL:
            case APPLIANCE_PICKED_FOR_REFUND:
            case HA_BD_ACCIDENTAL_DAMAGE: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestUpdateDto.setWorkflowAlert(null);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusWorkflowAlertAndActualEndDate(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                        currentTimestamp, serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;
            case IC_DECISION_APPROVED: {
                ServiceRequestEntity oldServiceRequestEntity = new ServiceRequestEntity();
                oldServiceRequestEntity.setWorkflowData(serviceRequestUpdateDto.getWorkflowJsonString());
                ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(oldServiceRequestEntity, ServiceRequestDto.class);

                if (null != serviceRequestDto.getWorkflowData().getRepairAssessment().getIsSpareRequestRaisedWithIC()
                        && serviceRequestDto.getWorkflowData().getRepairAssessment().getIsSpareRequestRaisedWithIC().equalsIgnoreCase(Constants.YES_FLAG)) {
                    serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                    serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE, serviceRequestEntity);
                    populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.SPARE_PART_NOT_AVAILABLE);
                    isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                            serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                            serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                } else if (null != serviceRequestDto.getWorkflowData().getRepairAssessment().getIsTransportationRaisedWithIC()
                        && serviceRequestDto.getWorkflowData().getRepairAssessment().getIsTransportationRaisedWithIC().equalsIgnoreCase(Constants.YES_FLAG)) {
                    serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                    serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.TRANSPORTATION_REQUIRED, serviceRequestEntity);
                    populateWorkflowAlert(serviceRequestUpdateDto, ServiceRequestEventCode.TRANSPORTATION_REQUIRED);
                    isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                            serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), serviceRequestUpdateDto.getWorkflowAlert(), currentTimestamp,
                            serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                } else {
                    serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                    serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                    isUpdated = serviceRequestRepository.updateServiceRequestOnEvent(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                            serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                            serviceRequestUpdateDto.getServiceRequestId());
                }
            }
                break;
            case TECHNICIAN_AT_LOCATION_AND_BEGINS_SERVICE: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusWorkflowAlertAndActualStartDate(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), null, currentTimestamp, currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
            }
                break;

            case INSPECTION_STARTED:
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestStatusWorkflowAlertAndActualStartDate(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), null, currentTimestamp, currentTimestamp,
                        serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());
                break;

            case COMPLETE_DOCUMENT_UPLOAD:
                if (StringUtils.isNotBlank(serviceRequestUpdateDto.getRequestDescription())) {
                    serviceRequestEntity.setRequestDescription(serviceRequestUpdateDto.getRequestDescription());
                }
                if (serviceRequestUpdateDto.getDateOfIncident() != null) {
                    WorkflowData workflowdata = serviceRequestHelper.getWorkflowDataByServiceRequest(serviceRequestEntity);
                    workflowdata.getDocumentUpload().setDateOfIncident(serviceRequestUpdateDto.getDateOfIncident().toString());
                }
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
				
				serviceRequestEntity.setStatus(serviceRequestUpdateDto.getStatus());
				serviceRequestEntity.setWorkflowStage(serviceRequestUpdateDto.getWorkflowStage());
				serviceRequestEntity.setWorkflowStageStatus(serviceRequestUpdateDto.getWorkflowStageStatus());
				serviceRequestEntity.setWorkflowData(serviceRequestUpdateDto.getWorkflowJsonString());
				serviceRequestEntity.setModifiedBy(serviceRequestUpdateDto.getModifiedBy());
				
				  try {
	                    serviceRequestRepository.save(serviceRequestEntity);
	                    isUpdated = 1;
	                } catch (Exception e) {
	                    throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
	                }

                break;

		case VERIFICATION_SUCCESSFUL:
			if (serviceRequestUpdateDto.getAdviceId() == null && serviceRequestUpdateDto.getServiceRequestType() != null
					&& serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_AD.getRequestType())) {

				PaymentDto adviceDto = new PaymentDto();
				adviceDto.setChargeId(Integer.parseInt(adviceChargeId));
				adviceDto.setMembershipId(serviceRequestUpdateDto.getReferenceNo());
				adviceDto.setAdviceAmount(Double.parseDouble(adviceAmount));
				String adviceId = oasysProxy.createAdvice(adviceDto);
				System.out.println("Advice id:: " + adviceId);
				logger.error(">>> In ServiceRequestServiceImpl>> AdviceId for serviceRequestId "
						+ serviceRequestUpdateDto.getServiceRequestId() + ", " + adviceId);

				if (adviceId != null) {
					serviceRequestEntity.setAdviceId(adviceId);
					//serviceRequestRepository.updateAdviceId(adviceId, serviceRequestEntity.getServiceRequestId());
				} else {
					throw new BusinessServiceException(
							"Unable to create advice. SR:- " + serviceRequestUpdateDto.getServiceRequestId());
				}
			}
            case VERIFICATION_UNSUCCESSFUL:
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                ObjectMapper objectMapper = new ObjectMapper();
                String pendencyString = objectMapper.writeValueAsString(serviceRequestUpdateDto.getPendency());
                serviceRequestEntity.setPendency(pendencyString);
                serviceRequestEntity.setWorkflowData(serviceRequestUpdateDto.getWorkflowJsonString());
                serviceRequestEntity.setStatus(serviceRequestUpdateDto.getStatus());
                serviceRequestEntity.setWorkflowStage(serviceRequestUpdateDto.getWorkflowStage());
                serviceRequestEntity.setWorkflowStageStatus(serviceRequestUpdateDto.getWorkflowStageStatus());
                serviceRequestEntity.setModifiedOn(currentTimestamp);
                serviceRequestEntity.setModifiedBy(serviceRequestUpdateDto.getModifiedBy());
                try {
                    serviceRequestRepository.save(serviceRequestEntity);
                    isUpdated = 1;
                } catch (Exception e) {
                    throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode());
                }
                break;

            default: {
                serviceRequestHelper.populateJsonWithEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, updateEventCode, serviceRequestEntity);
                isUpdated = serviceRequestRepository.updateServiceRequestOnEvent(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                        serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), currentTimestamp, serviceRequestUpdateDto.getModifiedBy(),
                        serviceRequestUpdateDto.getServiceRequestId());
            }
        }

        if (isUpdated == 1) {
            CommunicationGatewayEventCode commEventCode = this.getCommunicationTemplate(updateEventCode, serviceRequestUpdateDto.getServiceRequestType());

            if (commEventCode != null) {
                HashMap<String, Object> additionalAttributes = null;
                communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestUpdateDto, commEventCode, additionalAttributes);
            }

            srDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT, serviceRequestUpdateDto.getServiceRequestId());

            commandResult.setData(null);
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

    private CommunicationGatewayEventCode getCommunicationTemplate(ServiceRequestEventCode updateEventCode, String serviceRequestType) {
        CommunicationGatewayEventCode commEventCode = null;

        switch (updateEventCode) {
            case CUSTOMER_NOT_AVAILABLE:
            case CUSTOMER_NOT_AVAILABLE_RA_STAGE: {
                commEventCode = CommunicationGatewayEventCode.SP_CUSTOMER_NOT_AVAILABLE;
            }
                break;
            case CANNOT_PERFORM_SERVICE_FOR_SOME_REASON:
            case CANNOT_PERFORM_SERVICE_FOR_SOME_REASON_RA_STAGE: {
                if (serviceRequestType != null && serviceRequestType.equals(ServiceRequestType.HA_BD.getRequestType())) {
                    commEventCode = CommunicationGatewayEventCode.SP_BD_TECH_UNABLE_TO_SERVICE;
                } else {
                    commEventCode = CommunicationGatewayEventCode.SP_TECH_UNABLE_TO_SERVICE;
                }
            }
                break;

            case CUSTOMER_CAN_WAIT_FURTHER:
                commEventCode = CommunicationGatewayEventCode.SP_CUSTOMER_CAN_WAIT_FURTHER;
                break;

            case TECHNICIAN_CANCEL_INSPECTION_FOR_SOME_REASON:
                commEventCode = CommunicationGatewayEventCode.WHC_TECHNICIAN_CANCELLED_FOR_INSPECTION;
                break;

            default:
                logger.info("No communication needs to be in for default event code.");
        }

        return commEventCode;
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
                    RuleEngine.execute(RuleName.PRODUCTCODE_SLA_RULE, serviceRequestRuleDto);
                    break;
                case Constants.PARTNER_PRODUCT:
                    serviceRequestRuleDto.setServicePartnerCode(serviceRequestUpdateDto.getServicePartnerCode());
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
