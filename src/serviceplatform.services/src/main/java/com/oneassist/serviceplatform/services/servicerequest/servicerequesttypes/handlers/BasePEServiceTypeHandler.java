package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.datanotification.SRDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTransitionConfigCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.OasysAdminProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.utils.CopyProperties;
import com.oneassist.serviceplatform.commons.utils.CopyPropertiesUtils;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.ServiceRequestEventDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Completed;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DeliveryDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Pendency;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PickupDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SoftApproval;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;

public abstract class BasePEServiceTypeHandler extends BaseServiceTypeHandler {

    protected ServiceRequestType serviceRequestType;

    private static final String INCIDENT_DESCRIPTION_MAX_LENGTH_PE = "INCIDENT_DESCRIPTION_MAX_LENGTH_PE";

    @Autowired
    protected ServiceRequestTransitionConfigCache serviceRequestTransitionConfigCache;

    @Autowired
    private GenericKeySetCache genericKeySetCache;

    @Autowired
    private SRDataNotificationManager srDataNotificationManager;

    @Autowired
    private OasysAdminProxy oasysAdminProxy;
    
    @Autowired
    private ServiceAddressRepository serviceAddressRepository;
    
   
    
    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;
    
   

    private final Logger logger = Logger.getLogger(BasePEServiceTypeHandler.class);

    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
   

    public BasePEServiceTypeHandler(ServiceRequestType serviceRequestType) {
        super(serviceRequestType);
        this.serviceRequestType = serviceRequestType;
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

        super.validateOnServiceRequestCreate(serviceRequestDto);

        if (serviceRequestDto.getInitiatingSystem() != null && serviceRequestDto.getInitiatingSystem() == InitiatingSystem.CRM.getInitiatingSystem()
                && Strings.isNullOrEmpty(serviceRequestDto.getRefPrimaryTrackingNo())) {

            inputValidator.populateMandatoryFieldError("refPrimaryTrackingNo", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getRefSecondaryTrackingNo())) {
            inputValidator.populateMandatoryFieldError("refSecondaryTrackingNo", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getReferenceNo())) {
            inputValidator.populateMandatoryFieldError("referenceNo", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getCreatedBy())) {
            inputValidator.populateMandatoryFieldError("createdBy", errorInfoDtoList);
        }

        if (serviceRequestDto.getCustomerId() == null || serviceRequestDto.getCustomerId() == 0l) {
            inputValidator.populateMandatoryFieldError("customerId", errorInfoDtoList);
        }

        doValidateServiceAddress(serviceRequestDto);

        if (serviceRequestDto.getDateOfIncident() == null) {
            inputValidator.populateMandatoryFieldError("dateOfIncident", errorInfoDtoList);
        }

        inputValidator.validateFutureDate("dateOfIncident", serviceRequestDto.getDateOfIncident(), errorInfoDtoList);

        if (Strings.isNullOrEmpty(serviceRequestDto.getPlaceOfIncident())) {
            inputValidator.populateMandatoryFieldError("placeOfIncident", errorInfoDtoList);
        }
        
		if (serviceRequestDto.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()
				&& Strings.isNullOrEmpty(serviceRequestDto.getRequestDescription())) {
			inputValidator.populateMandatoryFieldError("requestDescription", errorInfoDtoList);
		}

        if (!Strings.isNullOrEmpty(serviceRequestDto.getRequestDescription())) {
            String incidentDescriptionMaxLengthStr = messageSource.getMessage(INCIDENT_DESCRIPTION_MAX_LENGTH_PE, new Object[] { "" }, null);
            int incidentDescriptionMaxLength = incidentDescriptionMaxLengthStr == null ? 400 : Integer.parseInt(incidentDescriptionMaxLengthStr);
            try {
                inputValidator.validateFieldMaxLength("requestDescription", serviceRequestDto.getRequestDescription(), incidentDescriptionMaxLength, errorInfoDtoList);
            } catch (InputValidationException ex) {
                throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, ex);
            }
        }

        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();

        checkSREligibility(serviceRequestDto, serviceRequestTypeMasterList.get(this.serviceRequestType.getRequestType()).getServiceRequestTypeId(),errorInfoDtoList);
        
        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }

       
    }

    @Override
    public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String serviceRequestType) {
        ServiceAddressEntity addressEntity = new ServiceAddressEntity();
        populateServiceAddressEntity(addressEntity, serviceRequestDto);
        serviceAddressRepository.save(addressEntity);

        populateWorkFlowData(addressEntity, serviceRequestEntity, serviceRequestDto);
        serviceRequestEntity.setCreatedOn(new Date());
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {

            serviceRequestEntity.setStatus(ServiceRequestStatus.PENDING.getStatus());
            serviceRequestEntity.setCreatedBy(serviceRequestDto.getCreatedBy());
            serviceRequestEntity.setWorkflowStage(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageCode());
            serviceRequestEntity.setRequestDescription(serviceRequestDto.getRequestDescription());
            serviceRequestEntity.setInitiatingSystem(serviceRequestDto.getInitiatingSystem());
            List<ServiceRequestTransitionConfigEntity> serviceReqeustTransitionConfigEventList = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());
            if (serviceReqeustTransitionConfigEventList != null && !serviceReqeustTransitionConfigEventList.isEmpty()) {
                for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceReqeustTransitionConfigEventList) {
                    if (serviceRequestTransitionConfigEntity.getTransitionFromStage() == null) {
                        serviceRequestEntity.setWorkflowStageStatus(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());
                        break;
                    }
                }
            }
        } else {
            serviceRequestEntity.setStatus(getSRStatusByCMSStatus(serviceRequestDto.getStatus()));
            serviceRequestEntity.setWorkflowStage(getSRStageByCMSStage(serviceRequestDto.getWorkflowStage()));
            try {
                if (serviceRequestDto.getThirdPartyProperties() != null) {
                    serviceRequestEntity.setThirdPartyProperties(objectMapper.writeValueAsString(serviceRequestDto.getThirdPartyProperties()));
                }
                if (serviceRequestDto.getPendency() != null) {
                    serviceRequestEntity.setPendency(objectMapper.writeValueAsString(serviceRequestDto.getPendency()));
                    setPendencyStatus(serviceRequestEntity);
                }
            } catch (Exception e) {
                logger.error("Exception while setting service request entity", e);
            }
        }
        serviceRequestEntity = serviceRequestRepository.save(serviceRequestEntity);
        saveAssetsforSR(serviceRequestDto.getRefSecondaryTrackingNo(), serviceRequestEntity.getServiceRequestId());
        serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
        serviceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            publishMessagesToQueue(serviceRequestDto, ClaimLifecycleEvent.CREATE_EXTERNAL_SERVICE_REQUEST);
        }
        return serviceRequestDto;
    }

    @Override
    protected CommandResult<ServiceResponseDto> updateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
            ServiceRequestEntity serviceRequestEntity) throws Exception {
        CommandResult<ServiceResponseDto> commandResult = new CommandResult<>();
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            ServiceRequestDto serviceRequestDetail = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
            CopyProperties.copyNonNullProperties(serviceRequestDto, serviceRequestDetail);
            serviceRequestDto = serviceRequestDetail;
        }
        String currentStage = serviceRequestEntity.getWorkflowStage();
        String newStage = serviceRequestDto.getWorkflowStage();
        if (Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            serviceRequestDto.setStatus(getSRStatusByCMSStatus(serviceRequestDto.getStatus()));
            setStageDescription(serviceRequestDto,serviceRequestEntity);
            newStage = getSRStageByCMSStage(newStage);
            serviceRequestDto.setWorkflowStage(newStage);
            setDescriptionMessage(serviceRequestEntity);
        }
        if (!currentStage.equalsIgnoreCase(newStage)) {
            String transistionStageStatus = null;
            boolean validTransition = false;
            if (!serviceRequestEntity.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus()) && !ServiceRequestStatus.PENDING.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())
                    && !ServiceRequestStatus.CLOSEDRESOLVED.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())) {
                validTransition = true;
                transistionStageStatus = WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode();
            } else {
                List<ServiceRequestTransitionConfigEntity> transitionEntities = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());
                if (!CollectionUtils.isEmpty(transitionEntities)) {
                    for (ServiceRequestTransitionConfigEntity transition : transitionEntities) {
                        if ((Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource()) || currentStage.equalsIgnoreCase(transition.getTransitionFromStage()))
                                && newStage.equalsIgnoreCase(transition.getTransitionToStage())) {
                            validTransition = true;
                            transistionStageStatus = transition.getTransitionToStageStatus();
                            break;
                        }
                    }
                }
            }

            if (!validTransition) {
                throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(), new InputValidationException());
            } else {
                serviceRequestDto.setWorkflowStage(newStage);
                serviceRequestDto.setWorkflowStageStatus(transistionStageStatus);
                setNewStageStatus(newStage, serviceRequestDto, transistionStageStatus);
                updateCurrentStageAsCompleted(currentStage, serviceRequestDto, serviceRequestEntity.getStatus());
            }
        } else if (!serviceRequestEntity.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus()) && !ServiceRequestStatus.PENDING.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())
                && !ServiceRequestStatus.CLOSEDRESOLVED.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())) {
            serviceRequestDto.setWorkflowStageStatus(WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode());
            setNewStageStatus(newStage, serviceRequestDto, WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode());
        }

        populateICDecisionStatus(newStage, serviceRequestDto);

        serviceRequestEntity = mergeServiceRequest(serviceRequestEntity, serviceRequestDto);
        serviceRequestEntity = serviceRequestRepository.save(serviceRequestEntity);
        publishMessagesToQueue(serviceRequestDto, ClaimLifecycleEvent.UPDATE_EXTERNAL_SERVICE_REQUEST);

        srDataNotificationManager.notify(DataNotificationEventType.UPDATED, serviceRequestDto);
        commandResult.setData(null);
        return commandResult;
    }

    private void publishMessagesToQueue(ServiceRequestDto serviceRequestDto, ClaimLifecycleEvent event) {
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            try {
                ServiceRequestEventDto serviceRequestEventDto = new ServiceRequestEventDto();
                serviceRequestEventDto.setEventName(event);
                serviceRequestEventDto.setRequest(serviceRequestDto);
                serviceRequestHelper.publishMessageInSPSRProcessingQueue(serviceRequestEventDto);
            } catch (Exception e) {
                logger.error("Exception while publishing PE create service request to queue" + serviceRequestDto, e);
            }
        }
    }

    private void populateICDecisionStatus(String newStage, ServiceRequestDto serviceRequestDto) {
        if (WorkflowStage.IC_DECISION.getWorkflowStageCode().equalsIgnoreCase(newStage)) {
            if (serviceRequestDto.getWorkflowData().getInsuranceDecision().getIcExcessAmt() != null && serviceRequestDto.getWorkflowData().getInsuranceDecision().getIcExcessAmt().doubleValue() > 0) {
                if (serviceRequestDto.getWorkflowData().getInsuranceDecision().getExcessAmtReceived() != null
                        && Constants.YES_FLAG.equalsIgnoreCase(serviceRequestDto.getWorkflowData().getInsuranceDecision().getExcessAmtReceived())) {
                    serviceRequestDto.setWorkflowStageStatus(WorkflowStageStatus.PAYMENT_PENDING.getWorkflowStageStatusCode());
                } else {
                    WorkflowStageStatus workflowStageStats = WorkflowStageStatus.getWorkflowStageStatus(serviceRequestDto.getWorkflowData().getInsuranceDecision().getIcDecision());
                    if (workflowStageStats != null) {
                        serviceRequestDto.setWorkflowStageStatus(workflowStageStats.getWorkflowStageStatusCode());
                        serviceRequestDto.getWorkflowData().getInsuranceDecision().setStatusCode(workflowStageStats.getWorkflowStageStatusCode());
                        serviceRequestDto.getWorkflowData().getInsuranceDecision().setStatus(workflowStageStats.getWorkflowStageStatus());
                    }
                }
            }
        }
    }

    private void setNewStageStatus(String newStage, ServiceRequestDto serviceRequestDto, String transitionToStageStatus) {
        WorkflowStage currentWorkflowStage = WorkflowStage.getWorkflowStageByCode(newStage);
        WorkflowStageStatus workflowStageStatus = WorkflowStageStatus.getWorkflowStageStatusCode(transitionToStageStatus);
        switch (currentWorkflowStage) {
            case CLAIM_SETTLEMENT:
                if (serviceRequestDto.getWorkflowData().getClaimSettlement() == null) {
                    serviceRequestDto.getWorkflowData().setClaimSettlement(new ClaimSettlement());
                }
                serviceRequestDto.getWorkflowData().getClaimSettlement().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getClaimSettlement().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case COMPLETED:
                if (serviceRequestDto.getWorkflowData().getCompleted() == null) {
                    serviceRequestDto.getWorkflowData().setCompleted(new Completed());
                }
                serviceRequestDto.getWorkflowData().getCompleted().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getCompleted().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case DELIVERY:
                serviceRequestDto.getWorkflowData().getDelivery().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getDelivery().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case DOCUMENT_UPLOAD:
                serviceRequestDto.getWorkflowData().getDocumentUpload().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getDocumentUpload().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case IC_DOC:
                serviceRequestDto.getWorkflowData().getIcDoc().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getIcDoc().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case PICKUP:
                serviceRequestDto.getWorkflowData().getPickup().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getPickup().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case REPAIR:
                serviceRequestDto.getWorkflowData().getRepair().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getRepair().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case REPAIR_ASSESSMENT:
                serviceRequestDto.getWorkflowData().getRepairAssessment().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getRepairAssessment().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            case VERIFICATION:
                serviceRequestDto.getWorkflowData().getVerification().setStatusCode(workflowStageStatus.getWorkflowStageStatusCode());
                serviceRequestDto.getWorkflowData().getVerification().setStatus(workflowStageStatus.getWorkflowStageStatus());
                break;
            default:
                break;

        }
    }

	private void updateCurrentStageAsCompleted(String currentStage, ServiceRequestDto serviceRequestDto,
			String currentSRStatus) {
		WorkflowStage currentWorkflowStage = WorkflowStage.getWorkflowStageByCode(currentStage);
		String currentStatus = ServiceRequestStatus.COMPLETED.getValue();
		String currentStatusCode = ServiceRequestStatus.COMPLETED.getStatus();
		if (!currentSRStatus.equalsIgnoreCase(serviceRequestDto.getStatus())
				&& !ServiceRequestStatus.PENDING.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())
				&& !ServiceRequestStatus.CLOSEDRESOLVED.getStatus().equalsIgnoreCase(serviceRequestDto.getStatus())) {
			currentStatus = WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatus();
			currentStatusCode = WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode();
		}
		switch (currentWorkflowStage) {
		case CLAIM_SETTLEMENT:
			serviceRequestDto.getWorkflowData().getClaimSettlement().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getClaimSettlement().setStatus(currentStatus);
			break;
		case COMPLETED: {
			serviceRequestDto.getWorkflowData().getCompleted().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getCompleted().setStatus(currentStatus);
			serviceRequestDto.getWorkflowData().getCompleted()
					.setDescription(messageSource.getMessage(String.valueOf("PE_SR_CO_MSG"), null, null));
		}
			break;
		case DELIVERY: {
			serviceRequestDto.getWorkflowData().getDelivery().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getDelivery().setStatus(currentStatus);
			if (WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode()
					.equals(serviceRequestDto.getWorkflowData().getRepairAssessment().getStatusCode())) {
				serviceRequestDto.getWorkflowData().getDelivery()
						.setDescription(messageSource.getMessage(String.valueOf("PE_CUNR_DE_CO_MSG"), null, null));
			} else {
				serviceRequestDto.getWorkflowData().getDelivery()
						.setDescription(messageSource.getMessage(String.valueOf("PE_DE_CO_MSG"), null, null));
			}
		}
			break;
		case DOCUMENT_UPLOAD: {
			serviceRequestDto.getWorkflowData().getDocumentUpload().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getDocumentUpload().setStatus(currentStatus);
			serviceRequestDto.getWorkflowData().getVerification()
					.setDescription(messageSource.getMessage(String.valueOf("PE_VR_VP_MSG"), null, null));
		}
			break;
		case IC_DECISION:
			serviceRequestDto.getWorkflowData().getInsuranceDecision().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getInsuranceDecision().setStatus(currentStatus);
			break;
		case IC_DOC:
			serviceRequestDto.getWorkflowData().getIcDoc().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getIcDoc().setStatus(currentStatus);
			break;
		case PICKUP: {
			serviceRequestDto.getWorkflowData().getPickup().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getPickup().setStatus(currentStatus);
			serviceRequestDto.getWorkflowData().getPickup()
					.setDescription(messageSource.getMessage(String.valueOf("PE_PU_CO_MSG"), null, null));
		}
			break;
		case REPAIR: {
			serviceRequestDto.getWorkflowData().getRepair().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getRepair().setStatus(currentStatus);
			serviceRequestDto.getWorkflowData().getRepair()
					.setDescription(messageSource.getMessage(String.valueOf("PE_RE_CO_MSG"), null, null));
		}
			break;
		case REPAIR_ASSESSMENT: {
			serviceRequestDto.getWorkflowData().getRepairAssessment().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getRepairAssessment().setStatus(currentStatus);
			if (currentStatusCode.equals(WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode())) {
				serviceRequestDto.getWorkflowData().getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("PE_RA_SC_MSG"), null, null));
			} else {
				serviceRequestDto.getWorkflowData().getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("PE_RA_CO_MSG"), null, null));
			}
		}
			break;
		case VERIFICATION: {
			serviceRequestDto.getWorkflowData().getVerification().setStatusCode(currentStatusCode);
			serviceRequestDto.getWorkflowData().getVerification().setStatus(currentStatus);
			if (currentStatusCode.equals(WorkflowStageStatus.UNRESOLVED.getWorkflowStageStatusCode())) {
				serviceRequestDto.getWorkflowData().getVerification()
						.setDescription(messageSource.getMessage(String.valueOf("PE_VR_URES_MSG"), null, null));
			} else {
				serviceRequestDto.getWorkflowData().getVerification()
						.setDescription(messageSource.getMessage(String.valueOf("PE_VR_CO_MSG"), null, null));
			}
		}
			break;
		default:
			break;

		}
	}

    private String getSRStatusByCMSStatus(String status) {
        if (!StringUtils.isEmpty(status)) {
            GenericKeySetEntity genericKeySet = genericKeySetCache.get(Constants.SP_AND_CMS_STATUS_MAPPING);
            if (genericKeySet != null && !CollectionUtils.isEmpty(genericKeySet.getGenericKeySetValueDetails())) {
                for (GenericKeySetValueEntity keysetValue : genericKeySet.getGenericKeySetValueDetails()) {
                    if (keysetValue.getValue().equalsIgnoreCase(status)) {
                        status = keysetValue.getKey();
                        break;
                    }
                }
            }
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private ServiceRequestEntity mergeServiceRequest(ServiceRequestEntity serviceRequestEntity, ServiceRequestDto serviceRequestDto) throws JsonParseException, JsonMappingException, IOException {
        ServiceRequestEntity modifiedEntity = modelMapper.map(serviceRequestDto, new TypeToken<ServiceRequestEntity>() {
        }.getType());
        Map<String, Object> newThirdPartyProperties = serviceRequestDto.getThirdPartyProperties() != null ? serviceRequestDto.getThirdPartyProperties() : null;
        WorkflowData newWorkflowdata = serviceRequestDto.getWorkflowData() != null ? serviceRequestDto.getWorkflowData() : null;
        Map<String, Pendency> newPendancy = serviceRequestDto.getPendency() != null ? serviceRequestDto.getPendency() : null;

        modifiedEntity.setWorkflowData(objectMapper.writeValueAsString(newWorkflowdata));
        modifiedEntity.setThirdPartyProperties(objectMapper.writeValueAsString(newThirdPartyProperties));
        modifiedEntity.setPendency(objectMapper.writeValueAsString(newPendancy));

        WorkflowData existingWorkflowdata = serviceRequestEntity.getWorkflowData() != null ? objectMapper.readValue(serviceRequestEntity.getWorkflowData(), WorkflowData.class) : new WorkflowData();
        Map<String, Object> existingThirdPartyProperties = serviceRequestEntity.getThirdPartyProperties() != null ? objectMapper.readValue(serviceRequestEntity.getThirdPartyProperties(),
                HashMap.class) : new HashMap<String, Object>();
        CopyProperties.copyNonNullProperties(modifiedEntity, serviceRequestEntity);
        if (newWorkflowdata != null) {
            existingWorkflowdata = CopyPropertiesUtils.copyNotNullWorkflowData(newWorkflowdata, existingWorkflowdata);
        }
        serviceRequestEntity.setWorkflowData(objectMapper.writeValueAsString(existingWorkflowdata));

        if (newThirdPartyProperties != null) {
            existingThirdPartyProperties = CopyPropertiesUtils.copyNotNullThirdPartyProperties(newThirdPartyProperties, existingThirdPartyProperties);
        }
        serviceRequestEntity.setThirdPartyProperties(objectMapper.writeValueAsString(existingThirdPartyProperties));

        if (newPendancy != null) {
            serviceRequestEntity.setPendency(objectMapper.writeValueAsString(newPendancy));
            setPendencyStatus(serviceRequestEntity);
        } else {
            serviceRequestEntity.setPendency(null);
        }

        return serviceRequestEntity;
    }

    private String getSRStageByCMSStage(String newStage) {
        if (!StringUtils.isEmpty(newStage)) {
            GenericKeySetEntity genericKeySet = genericKeySetCache.get(Constants.SP_AND_CMS_STAGE_MAPPING);
            if (genericKeySet != null && !CollectionUtils.isEmpty(genericKeySet.getGenericKeySetValueDetails())) {
                for (GenericKeySetValueEntity keysetValue : genericKeySet.getGenericKeySetValueDetails()) {
                    if (keysetValue.getValue().equalsIgnoreCase(newStage)) {
                        newStage = keysetValue.getKey();
                        break;
                    }
                }
            }
        }
        return newStage;
    }

    private void populateWorkFlowData(ServiceAddressEntity addressEntity, ServiceRequestEntity serviceRequestEntity, ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        Visit visit = new Visit();
        visit.setServiceAddress(String.valueOf(addressEntity.getServiceAddressId()));
        visit.setStatus(Constants.ACTIVE);

        WorkflowData workFlowData = serviceRequestDto.getWorkflowData();
        if (workFlowData == null) {
            workFlowData = new WorkflowData();
        }
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            workFlowData.setRepair(new Repair());
            workFlowData.setClaimSettlement(new ClaimSettlement());
            DocumentUpload documentUpload = new DocumentUpload();
            documentUpload.setDateOfIncident(DateUtils.toLongFormattedString(serviceRequestDto.getDateOfIncident()));
            documentUpload.setPlaceOfIncident(serviceRequestDto.getPlaceOfIncident());
            documentUpload.setStatus(ServiceRequestStatus.PENDING.getValue());
            documentUpload.setStatusCode(ServiceRequestStatus.PENDING.getStatus());
            switch (serviceRequestType) {
                case PE_ADLD:
                    documentUpload.setMobileDamageDetails(serviceRequestDto.getIncidentDetail());
                    break;
                case PE_EW:
                    documentUpload.setDeviceBreakdownDetail(serviceRequestDto.getIncidentDetail());
                    break;
                case PE_THEFT:
                    documentUpload.setMobileLossDetails(serviceRequestDto.getIncidentDetail());
                    break;
                default:
                    break;
            }
            workFlowData.setDocumentUpload(documentUpload);
            workFlowData.setRepairAssessment(new RepairAssessment());
            workFlowData.setSoftApproval(new SoftApproval());
            workFlowData.setVerification(new Verification());
            workFlowData.setIcDoc(new ICDoc());
            workFlowData.setInsuranceDecision(new InsuranceDecision());
            workFlowData.setPickup(new PickupDetail());
            workFlowData.setDelivery(new DeliveryDetail());
        }

        workFlowData.setVisit(visit);

        try {
            String workFlowJson = objectMapper.writeValueAsString(workFlowData);
            serviceRequestEntity.setWorkflowData(workFlowJson);
            serviceRequestDto.setWorkflowData(workFlowData);
            logger.debug("Workflow JSON " + workFlowJson);
        } catch (JsonProcessingException e) {
            logger.error("Error converting to Work Flow Json String", e);
            throw new BusinessServiceException("Technical Error! Unable to parse in Json", e);
        }
    }

    private void checkSREligibility(ServiceRequestDto serviceRequestDto, Long serviceRequestTypeId,List<ErrorInfoDto> errorInfoDtoList) {
        List<ServiceRequestEntity> srsForPrimaryNum = serviceRequestRepository.findByRefSecondaryTrackingNoAndReferenceNoAndServiceRequestTypeIdAndStatus(
                serviceRequestDto.getRefSecondaryTrackingNo(), serviceRequestDto.getReferenceNo(), serviceRequestTypeId, ServiceRequestStatus.PENDING.getStatus());

        if (srsForPrimaryNum != null && !srsForPrimaryNum.isEmpty()) {
            throw new BusinessServiceException(ServiceRequestResponseCodes.RAISE_CLAIM_ELIGIBILITY_FAILED.getErrorCode(), new InputValidationException());
        }
        if (!Constants.SOURCE_QUEUE.equalsIgnoreCase(serviceRequestDto.getSource())) {
            String responseJson = oasysAdminProxy.checkClaimEligibility(serviceRequestDto.getReferenceNo(), serviceRequestDto.getRefSecondaryTrackingNo());
            boolean eligibilty = false;
            if (responseJson != null) {
                JSONObject jsonObject = new JSONObject(responseJson);
                eligibilty = (Boolean) jsonObject.get("eligibility");
            }
            if (!eligibilty) {
            	ErrorInfoDto errorInfoDto = new ErrorInfoDto(30088, "Raise claim eligibility check failed", null);
            	errorInfoDtoList.add(errorInfoDto);
            }
        }
    }

    @Override
    public ServiceRequestDto updateServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity newServiceRequestEntity, ServiceRequestEntity existingServiceRequestEntity)
            throws Exception {
        if (existingServiceRequestEntity.getInitiatingSystem() != null && InitiatingSystem.CRM.getInitiatingSystem() == existingServiceRequestEntity.getInitiatingSystem().intValue()) {
            CopyProperties.copyNonNullProperties(newServiceRequestEntity, existingServiceRequestEntity);
            if (serviceRequestDto.getIncidentDescription() != null) {
                existingServiceRequestEntity.setRequestDescription(serviceRequestDto.getRequestDescription());
            }
            newServiceRequestEntity = serviceRequestRepository.save(existingServiceRequestEntity);
            serviceRequestDto = serviceRequestHelper.convertObject(existingServiceRequestEntity, ServiceRequestDto.class);
            publishMessagesToQueue(serviceRequestDto, ClaimLifecycleEvent.UPDATE_EXTERNAL_SERVICE_REQUEST);
            srDataNotificationManager.notify(DataNotificationEventType.UPDATED, serviceRequestDto);
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.EDIT_CLAIM_RESTRICTED.getErrorCode(), new InputValidationException());
        }
        return serviceRequestDto;
    }
    
	private void setDescriptionMessage(ServiceRequestEntity serviceRequestEntity) {
		if (serviceRequestEntity != null) {
			String workFlowStage = serviceRequestEntity.getWorkflowStage();
			String workFlowStageStatus = serviceRequestEntity.getWorkflowStageStatus();
			WorkflowData workflowData = serviceRequestHelper.getWorkflowDataByServiceRequest(serviceRequestEntity);
			if (workFlowStage != null) {
				if (workFlowStage.equals(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageCode())) {
					logger.error("RAStatuscode:" + workFlowStageStatus);
					logger.error("RAStatuscodeEnum:" + WorkflowStageStatus.PENDING.getWorkflowStageStatusCode());
					if (workFlowStageStatus.equals(WorkflowStageStatus.PENDING.getWorkflowStageStatusCode())) {
						workflowData.getRepairAssessment()
								.setDescription(messageSource.getMessage(String.valueOf("PE_RA_P_MSG"), null, null));
					}
				}

				else if (workFlowStage.equals(WorkflowStage.IC_DECISION.getWorkflowStageCode())) {
					if (workFlowStageStatus
							.equals(WorkflowStageStatus.AWAITING_APPROVAL.getWorkflowStageStatusCode())) {
						workflowData.getInsuranceDecision()
								.setDescription(messageSource.getMessage(String.valueOf("PE_ID_AA_MSG"), null, null));
					}
					if (Constants.YES_FLAG.equals(workflowData.getInsuranceDecision().getDeliveryICBerApproved())) {
						workflowData.getInsuranceDecision().setDescription(
								messageSource.getMessage(String.valueOf("PE_ID_AP_BER_MSG"), null, null));
					} else if (workFlowStageStatus.equals(WorkflowStageStatus.PAYMENT_PENDING.getWorkflowStageStatusCode())) {
						workflowData.getInsuranceDecision()
								.setDescription(messageSource.getMessage(String.valueOf("PE_ID_CPP_MSG"), null, null));
						workflowData.getRepair()
								.setDescription(messageSource.getMessage(String.valueOf("PE_RE_IP_MSG"), null, null));
					} else if (workFlowStageStatus.equals(WorkflowStageStatus.REJECTED.getWorkflowStageStatusCode())) {
						workflowData.getInsuranceDecision()
								.setDescription(messageSource.getMessage(String.valueOf("PE_ID_RJ_MSG"), null, null));
					}
				}

				else if (workFlowStage.equals(WorkflowStage.REPAIR.getWorkflowStageCode())) {
					if (workFlowStageStatus.equals(WorkflowStageStatus.IN_PROGRESS.getWorkflowStageStatusCode())) {
						workflowData.getInsuranceDecision()
								.setDescription(messageSource.getMessage(String.valueOf("PE_ID_AP_MSG"), null, null));
						workflowData.getRepair()
								.setDescription(messageSource.getMessage(String.valueOf("PE_RE_IP_MSG"), null, null));
					}
				}

				else if (workFlowStage.equals(WorkflowStage.DELIVERY.getWorkflowStageCode())) {
					if (workFlowStageStatus.equals(WorkflowStageStatus.IN_PROGRESS.getWorkflowStageStatusCode())) {
						workflowData.getDelivery()
								.setDescription(messageSource.getMessage(String.valueOf("PE_DE_IP_MSG"), null, null));
					}
				}
				else if (workFlowStage.equals(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageCode())) {
					if (workFlowStageStatus
							.equals(WorkflowStageStatus.VERIFICATION_UNSUCCESSFUL.getWorkflowStageStatusCode())
							&& serviceRequestEntity.getPendency() != null) {
						workflowData.getVerification()
								.setDescription(messageSource.getMessage(String.valueOf("PE_DU_VU_MSG"), null, null));
					}
				}

				try {
					ObjectMapper objectMapper = new ObjectMapper();
					serviceRequestEntity.setWorkflowData(objectMapper.writeValueAsString(workflowData));
				} catch (Exception e) {
					logger.error("Could not set description for PE: " + serviceRequestEntity.getServiceRequestId());
				}
			}
		}
	}
    
    private void setStageDescription(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity){
		WorkflowData workflowData = serviceRequestHelper.getWorkflowDataByServiceRequest(serviceRequestEntity);
		WorkflowData dto = serviceRequestDto.getWorkflowData();
		dto.getRepair().setDescription(workflowData.getRepair().getDescription());
		dto.getPickup().setDescription(workflowData.getPickup().getDescription());
		dto.getDocumentUpload().setDescription(workflowData.getDocumentUpload().getDescription());
		dto.getInsuranceDecision().setDescription(workflowData.getInsuranceDecision().getDescription());
		dto.getRepairAssessment().setDescription(workflowData.getRepairAssessment().getDescription());
		dto.getDelivery().setDescription(workflowData.getDelivery().getDescription());
		dto.getVerification().setDescription(workflowData.getVerification().getDescription());
	}
    

    private void setPendencyStatus(ServiceRequestEntity serviceRequestEntity) throws JsonParseException, JsonMappingException, IOException {
        WorkflowData workflowData = objectMapper.readValue(serviceRequestEntity.getWorkflowData(), WorkflowData.class);
        if (WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageCode().equalsIgnoreCase(serviceRequestEntity.getWorkflowStage())) {
            serviceRequestEntity.setWorkflowStageStatus(WorkflowStageStatus.VERIFICATION_UNSUCCESSFU.getWorkflowStageStatusCode());
        }
        workflowData.getDocumentUpload().setStatus(WorkflowStageStatus.VERIFICATION_UNSUCCESSFU.getWorkflowStageStatus());
        workflowData.getDocumentUpload().setStatusCode(WorkflowStageStatus.VERIFICATION_UNSUCCESSFU.getWorkflowStageStatusCode());
        workflowData.getVerification().setStatus(WorkflowStageStatus.PENDING.getWorkflowStageStatus());
        workflowData.getVerification().setStatusCode(WorkflowStageStatus.PENDING.getWorkflowStageStatusCode());
    }

}
