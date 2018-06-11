package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.AuthCodeGenerator;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SoftApproval;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;

// This provides the default functionality for CRUD operations for service request.
@Component
public abstract class BaseWHCServiceTypeHandler extends BaseServiceTypeHandler {

	private final Logger logger = Logger.getLogger(BaseWHCServiceTypeHandler.class);

	protected static final String SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT = "SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT";
	private static final String INCIDENT_DESCRIPTION_MAX_LENGTH = "INCIDENT_DESCRIPTION_MAX_LENGTH";

	@SuppressWarnings("rawtypes")
	@Autowired
	private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

	@Autowired
	protected ServiceRequestRepository serviceRequestRepository;

	@Autowired
	protected IServiceRequestService serviceRequestService;

	public BaseWHCServiceTypeHandler(ServiceRequestType serviceRequestType) {
		super(serviceRequestType);
	}

	@Override
	public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
		super.validateOnServiceRequestCreate(serviceRequestDto);
		if (serviceRequestDto.getInitiatingSystem().longValue() == InitiatingSystem.CRM.getInitiatingSystem()
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

		doValidateServiceAddress(serviceRequestDto);

		if (serviceRequestDto.getDateOfIncident() == null) {
			inputValidator.populateMandatoryFieldError("dateOfIncident", errorInfoDtoList);
		}
		inputValidator.validateFutureDate("dateOfIncident", serviceRequestDto.getDateOfIncident(), errorInfoDtoList);

		/*
		 * if (Strings.isNullOrEmpty(serviceRequestDto.getPlaceOfIncident()) &&
		 * serviceRequestDto.getInitiatingSystem() ==
		 * InitiatingSystem.CRM.getInitiatingSystem()) {
		 * inputValidator.populateMandatoryFieldError("placeOfIncident",
		 * errorInfoDtoList); }
		 */

		if (!Strings.isNullOrEmpty(serviceRequestDto.getIncidentDescription())) {
			String incidentDescriptionMaxLengthStr = messageSource.getMessage(INCIDENT_DESCRIPTION_MAX_LENGTH,
					new Object[] { "" }, null);
			int incidentDescriptionMaxLength = incidentDescriptionMaxLengthStr == null ? 250
					: Integer.parseInt(incidentDescriptionMaxLengthStr);
			try {
				inputValidator.validateFieldMaxLength("requestDescription", serviceRequestDto.getRequestDescription(),
						incidentDescriptionMaxLength, errorInfoDtoList);
			} catch (InputValidationException ex) {
				throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(),
						errorInfoDtoList, ex);
			}
		}
	}

	@Override
	public CommandResult<ServiceResponseDto> updateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction,
			ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
			ServiceRequestEntity serviceRequestEntity) throws Exception {

		// Based on ServiceRequestUpdateAction it will call different action
		// command
		BaseActionCommand<ServiceRequestDto, ServiceResponseDto> a = serviceRequestActionCommandFactory
				.getServiceRequestActionCommand(serviceRequestUpdateAction);

		CommandInput<ServiceRequestDto> ci = new CommandInput<>();
		ci.setData(serviceRequestDto);
		ci.setCommandEventCode(eventCode);
		ci.setServiceRequestEntity(serviceRequestEntity);
		return a.execute(ci);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void validateDuplicateSRForMembershipAndAsset(List<ServiceRequestEntity> srList)
			throws BusinessServiceException {
		for (ServiceRequestEntity srEntity : srList) {
			if (serviceRequestService.validateDuplicateSR(srEntity)) {
				throw new BusinessServiceException(
						ServiceRequestResponseCodes.CREATE_REPAIR_REQUEST_SR_NOTCLOSED_MEMBERSHIPID_AND_PRODUCT
								.getErrorCode(),
						new Object[] { srEntity.getReferenceNo() }, new InputValidationException());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void validatePrimaryRefNumAlreadyExists(String primaryTrackingNum) throws BusinessServiceException {
		List serviceRequestTypeList = new ArrayList<Long>();
		Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();
		serviceRequestTypeList.add(
				serviceRequestTypeMasterList.get(ServiceRequestType.HA_AD.getRequestType()).getServiceRequestTypeId());
		serviceRequestTypeList.add(
				serviceRequestTypeMasterList.get(ServiceRequestType.HA_BD.getRequestType()).getServiceRequestTypeId());
		serviceRequestTypeList.add(
				serviceRequestTypeMasterList.get(ServiceRequestType.HA_BR.getRequestType()).getServiceRequestTypeId());
		serviceRequestTypeList.add(
				serviceRequestTypeMasterList.get(ServiceRequestType.HA_FR.getRequestType()).getServiceRequestTypeId());
		serviceRequestTypeList.add(
				serviceRequestTypeMasterList.get(ServiceRequestType.HA_EW.getRequestType()).getServiceRequestTypeId());

		List<ServiceRequestEntity> srsForPrimaryNum = serviceRequestRepository
				.findByRefPrimaryTrackingNoAndServiceRequestTypeIdIn(primaryTrackingNum, serviceRequestTypeList);

		if (srsForPrimaryNum != null && !srsForPrimaryNum.isEmpty()) {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.CREATE_REPAIR_REQUEST_SR_ALREADY_EXISTS.getErrorCode(),
					new InputValidationException());
		}
	}

	protected void populateWorkFlowData(ServiceAddressEntity addressEntity, ServiceRequestEntity serviceRequestEntity,
			ServiceRequestDto serviceRequestDto, String isPincodeServicable) throws BusinessServiceException {
		Visit visit = new Visit();
		visit.setServiceAddress(String.valueOf(addressEntity.getServiceAddressId()));
		visit.setStatus(Constants.ACTIVE);
		visit.setIsSelfService(isPincodeServicable.equals(Constants.YES_FLAG) ? Constants.NO_FLAG : Constants.YES_FLAG);

		visit.setIssueReportedByCustomer(serviceRequestDto.getIssueReportedByCustomer());
		visit.setDateOfIncident(com.oneassist.serviceplatform.commons.utils.DateUtils
				.toLongFormattedString(serviceRequestDto.getDateOfIncident()));
		visit.setPlaceOfIncident(serviceRequestDto.getPlaceOfIncident());

		Repair repair = new Repair();
		WorkflowData workFlowData = new WorkflowData();
		ClaimSettlement claimSettlement = new ClaimSettlement();
		DocumentUpload documentUpload = new DocumentUpload();
		RepairAssessment repairAssessment = new RepairAssessment();
		SoftApproval softApproval = new SoftApproval();
		Verification verification = new Verification();
		ICDoc icDoc = new ICDoc();
		InsuranceDecision insuranceDecision = new InsuranceDecision();

		/* Set description in document upload workflow */
		switch (this.serviceRequestType) {

		case HA_AD:
			documentUpload.setDescription(messageSource.getMessage("HA_AD_DU_DUP_CREATE_SR_MSG", null, null));
			break;

		case HA_BR:
			documentUpload
					.setDescription(messageSource.getMessage(String.valueOf("HA_BR_DU_DUP_CREATE_SR_MSG"), null, null));
			break;

		case HA_EW:
			documentUpload
					.setDescription(messageSource.getMessage(String.valueOf("HA_EW_DU_DUP_CREATE_SR_MSG"), null, null));
			break;

		case HA_FR:
			documentUpload
					.setDescription(messageSource.getMessage(String.valueOf("HA_FR_DU_DUP_CREATE_SR_MSG"), null, null));
			break;

		default:
			/* What we have to do in default case ????? */
			break;

		}

		if (serviceRequestDto.getInitiatingSystem() != null && serviceRequestDto.getInitiatingSystem() == 3) {

			documentUpload.setDescription(
					messageSource.getMessage(String.valueOf("HA_CRM_DU_DUP_CREATE_SR_MSG"), null, null));
		}

		ServiceRequestEventCode eventCode = null;
		if (isPincodeServicable.equalsIgnoreCase(Constants.YES_FLAG)) {
			if (this.serviceRequestType.equals(ServiceRequestType.HA_BD)
					|| this.serviceRequestType.equals(ServiceRequestType.HA_AD)) {
				visit.setServiceStartCode(AuthCodeGenerator.generateFourDigitAuthPin());
				repair.setServiceEndCode(AuthCodeGenerator.generateFourDigitAuthPin());
			}
			if (this.serviceRequestType.equals(ServiceRequestType.HA_BD)) {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VISIT;
			} else if (this.serviceRequestType.equals(ServiceRequestType.HA_FR)
					&& serviceRequestDto.getWorkFlowValue() != null
					&& serviceRequestDto.getWorkFlowValue() == Constants.WORKFLOW_DOC_UPLOADED_STAGE) {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VERIFICATION;
			} else {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_DOCUPLOAD;
			}
		} else {
			if (this.serviceRequestType.equals(ServiceRequestType.HA_BD)) {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VISIT_SELFREPAIR;
			} else if (this.serviceRequestType.equals(ServiceRequestType.HA_FR)
					&& serviceRequestDto.getWorkFlowValue() != null
					&& serviceRequestDto.getWorkFlowValue() == Constants.WORKFLOW_DOC_UPLOADED_STAGE) {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VERIFICATION_SELFREPAIR;
			} else {
				eventCode = ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_DOCUPLOAD_SELFREPAIR;
			}

		}

		workFlowData.setVisit(visit);
		workFlowData.setRepair(repair);
		workFlowData.setClaimSettlement(claimSettlement);
		workFlowData.setDocumentUpload(documentUpload);
		workFlowData.setRepairAssessment(repairAssessment);
		workFlowData.setSoftApproval(softApproval);
		workFlowData.setVerification(verification);
		workFlowData.setIcDoc(icDoc);
		workFlowData.setInsuranceDecision(insuranceDecision);
		String workflowStageStatus = null;
		final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache
				.get(this.serviceRequestType.getRequestType());
		if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {
			for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {
				if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
					String workflowStage = serviceRequestTransitionConfigEntity.getTransitionToStage();
					workflowStageStatus = StringUtils
							.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());

					final List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusEntities = serviceRequestStageStatusMstCache
							.get(this.serviceRequestType.getRequestType());
					if (serviceRequestStageStatusEntities != null && !serviceRequestStageStatusEntities.isEmpty()) {
						for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusMstEntity : serviceRequestStageStatusEntities) {
							if (workflowStage.equals(serviceRequestStageStatusMstEntity.getStageCode())
									&& workflowStageStatus.equals(StringUtils
											.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusCode()))) {
								String partnerStage = serviceRequestStageStatusMstEntity.getServiceRequestStatus();

								serviceRequestEntity.setWorkflowStage(workflowStage);
								serviceRequestEntity.setWorkflowStageStatus(workflowStageStatus);
								serviceRequestEntity.setStatus(partnerStage);
								break;
							}
						}
					} else {
						throw new BusinessServiceException(
								ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(),
								new InputValidationException());
					}
					break;
				}
			}
			if (this.serviceRequestType.equals(ServiceRequestType.HA_BD)) {
				visit.setStatusCode(workflowStageStatus);
			} else if (this.serviceRequestType.equals(ServiceRequestType.HA_FR)
					&& serviceRequestDto.getWorkFlowValue() != null
					&& serviceRequestDto.getWorkFlowValue() == Constants.WORKFLOW_DOC_UPLOADED_STAGE) {
				documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
				verification.setStatusCode(workflowStageStatus);
			} else {
				documentUpload.setStatusCode(workflowStageStatus);
			}
		} else {
			throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(),
					new InputValidationException());
		}

		try {
			String workFlowJson = new ObjectMapper().writeValueAsString(workFlowData);
			serviceRequestEntity.setWorkflowData(workFlowJson);
			logger.debug("Workflow JSON " + workFlowJson);
		} catch (JsonProcessingException e) {
			logger.error("Error converting to Work Flow Json String", e);
			throw new BusinessServiceException("Technical Error! Unable to parse in Json", e);
		}
	}

	@Override
	public void validateOnCostToCustomerCalculate(Long serviceRequestId,
			AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws BusinessServiceException {
		super.validateOnCostToCustomerCalculate(serviceRequestId, assigneeRepairCostRequestDto);
	}

	protected void validateBERCaseForAsset(List<ServiceRequestEntity> srList,
			List<ServiceRequestAssetEntity> serviceRequestAssetsList) throws BusinessServiceException {
		List<String> invalidAssets = null;
		if (CollectionUtils.isNotEmpty(serviceRequestAssetsList)) {

			Map<Long, ServiceRequestAssetEntity> assetMap = new HashMap<>();
			for (ServiceRequestAssetEntity asset : serviceRequestAssetsList) {
				assetMap.put(asset.getServiceRequestId(), asset);
			}

			invalidAssets = new ArrayList<>();
			for (ServiceRequestEntity entity : srList) {

				if (!serviceRequestService.validateBERCaseforSR(entity)) {
					invalidAssets.add(assetMap.get(entity.getServiceRequestId()).getAssetReferenceNo());
				}
			}

			if (invalidAssets.size() > 0) {
				throw new BusinessServiceException("BER Approved for assets:" + invalidAssets);
			}
		}

	}

	protected void validateServiceRequestEligibility(ServiceRequestDto serviceRequestDto) {

		List<ServiceRequestAssetEntity> serviceRequestAssetsList = serviceRequestAssetRepository
				.findByAssetReferenceNoIn(StringUtils.getListFromString(serviceRequestDto.getRefSecondaryTrackingNo()));
		if (CollectionUtils.isNotEmpty(serviceRequestAssetsList)) {

			List<Long> srIdList = new ArrayList<>();

			for (ServiceRequestAssetEntity asset : serviceRequestAssetsList) {
				srIdList.add(asset.getServiceRequestId());
			}

			List<ServiceRequestEntity> serviceRequestsRaisedByMembershipId = serviceRequestRepository
					.findByServiceRequestIdInAndReferenceNo(srIdList, serviceRequestDto.getReferenceNo());

			validateBERCaseForAsset(serviceRequestsRaisedByMembershipId, serviceRequestAssetsList);
			validateDuplicateSRForMembershipAndAsset(serviceRequestsRaisedByMembershipId);

		}
	}

	public void validateOnServiceRequestUpdates(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

		if (serviceRequestDto.getModifiedBy().equalsIgnoreCase(CRM)) {
			inputValidator.populateInvalidData("modifiedBy", errorInfoDtoList);
		}

		if (serviceRequestDto.getServiceRequestId() == null) {
			inputValidator.populateMandatoryFieldError("serviceRequestId", errorInfoDtoList);

		}
		if (!Strings.isNullOrEmpty(serviceRequestDto.getRefPrimaryTrackingNo())) {
			inputValidator.populateInvalidData("refPrimaryTrackingNo", errorInfoDtoList);
		}

		if (Strings.isNullOrEmpty(serviceRequestDto.getRefSecondaryTrackingNo())) {
			inputValidator.populateMandatoryFieldError("refSecondaryTrackingNo", errorInfoDtoList);
		}

		if (Strings.isNullOrEmpty(serviceRequestDto.getReferenceNo())) {
			inputValidator.populateMandatoryFieldError("referenceNo", errorInfoDtoList);
		}

		if (Strings.isNullOrEmpty(serviceRequestDto.getModifiedBy())) {
			inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoDtoList);
		}

		doValidateServiceAddress(serviceRequestDto);

		if (serviceRequestDto.getDateOfIncident() == null) {
			inputValidator.populateMandatoryFieldError("dateOfIncident", errorInfoDtoList);
		}
		inputValidator.validateFutureDate("dateOfIncident", serviceRequestDto.getDateOfIncident(), errorInfoDtoList);

		/*
		 * if (Strings.isNullOrEmpty(serviceRequestDto.getPlaceOfIncident())) {
		 * inputValidator.populateMandatoryFieldError("placeOfIncident",
		 * errorInfoDtoList); }
		 */

		if (Strings.isNullOrEmpty(serviceRequestDto.getRequestDescription())) {
			inputValidator.populateMandatoryFieldError("requestDescription", errorInfoDtoList);
		}

	}

}
