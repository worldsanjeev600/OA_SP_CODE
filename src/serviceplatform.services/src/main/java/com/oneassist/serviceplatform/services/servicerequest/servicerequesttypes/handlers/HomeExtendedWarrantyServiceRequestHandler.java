package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.DocTypeConfigDetailCache;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.utils.AuthCodeGenerator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetDocument;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Completed;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Diagnosis;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Issues;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.LabourCost;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SoftApproval;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Transport;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;

@Component
public class HomeExtendedWarrantyServiceRequestHandler extends BaseServiceTypeHandler {

	private final Logger logger = Logger.getLogger(HomeExtendedWarrantyServiceRequestHandler.class);
	private final String HA_EW_DOC_KEY = "INVOICE_IMAGE";
	private final String HA = "HA";

	@Autowired
	private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

	@Autowired
	private ServiceDocumentRepository serviceDocumentRepository;

	@Autowired
	private OasysProxy oasysProxy;

	@Autowired
	private DocTypeConfigDetailCache docTypeConfigDetailCache;

	private static final int WORKFLOW_DOC_VERIFIED_STAGE = 3;
	private static final String SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT = "SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT";
	public static final String COST_TO_COMPANY_ESTIMATE_LIMIT = "CTCEstimateLimit";
	public static final String COST_TO_COMPANY_INVOICE_PERCENTAGE_LIMIT = "CTCInvoicePercentLimit";

	public HomeExtendedWarrantyServiceRequestHandler() {

		super(ServiceRequestType.HA_EW);
	}

	@Override
	public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

		super.validateOnServiceRequestCreate(serviceRequestDto);

		if (serviceRequestDto.getInitiatingSystem() != null
				&& serviceRequestDto.getInitiatingSystem() == InitiatingSystem.CRM.getInitiatingSystem()
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

		/*
		 * if (serviceRequestDto.getWorkFlowValue() == null) {
		 * inputValidator.populateMandatoryFieldError("workFlowValue",
		 * errorInfoDtoList); }
		 */

		if (serviceRequestDto.getWorkFlowValue() != null
				&& serviceRequestDto.getWorkFlowValue() == WORKFLOW_DOC_VERIFIED_STAGE) {
			if (serviceRequestDto.getScheduleSlotStartDateTime() == null) {
				inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoDtoList);
			}

			if (serviceRequestDto.getScheduleSlotEndDateTime() == null) {
				inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoDtoList);
			}

			String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT,
					new Object[] { "" }, null);
			serviceRequestValidator.validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime",
					serviceRequestDto.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit),
					errorInfoDtoList);
		}

		if (serviceRequestDto.getIssueReportedByCustomer() == null
				|| serviceRequestDto.getIssueReportedByCustomer().isEmpty()) {
			inputValidator.populateMandatoryFieldError("issueReportedByCustomer", errorInfoDtoList);

		} else {
			for (Issues issue : serviceRequestDto.getIssueReportedByCustomer()) {
				if (Strings.isNullOrEmpty(issue.getIssueId())) {
					ErrorInfoDto errorInfoDto = new ErrorInfoDto(16, " issueId cannot be Empty");
					errorInfoDtoList.add(errorInfoDto);

				} else if (serviceTaskMasterCache.get(issue.getIssueId()) == null) {
					ErrorInfoDto errorInfoDto = new ErrorInfoDto(16, "Invalid issueId:" + issue.getIssueId());
					errorInfoDtoList.add(errorInfoDto);
				}
			}
		}

		doValidateServiceAddress(serviceRequestDto);

		if (serviceRequestDto.getDateOfIncident() == null) {
			inputValidator.populateMandatoryFieldError("dateOfIncident", errorInfoDtoList);
		}

		inputValidator.validateFutureDate("dateOfIncident", serviceRequestDto.getDateOfIncident(), errorInfoDtoList);

		if (Strings.isNullOrEmpty(serviceRequestDto.getPlaceOfIncident())
				&& serviceRequestDto.getInitiatingSystem() == InitiatingSystem.CRM.getInitiatingSystem()) {
			inputValidator.populateMandatoryFieldError("placeOfIncident", errorInfoDtoList);
		}

		if (!errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}

		Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();
		if (org.apache.commons.lang3.StringUtils.isNotBlank(serviceRequestDto.getRefPrimaryTrackingNo())) {
			validatePrimaryRefNumAlreadyExists(serviceRequestDto.getRefPrimaryTrackingNo(), serviceRequestTypeMasterList
					.get(this.serviceRequestType.getRequestType()).getServiceRequestTypeId());

		}
		validateExisitingSRForMembershipIdAndAssetId(serviceRequestDto.getReferenceNo(),
				serviceRequestTypeMasterList.get(serviceRequestDto.getServiceRequestType()).getServiceRequestTypeId(),
				serviceRequestDto.getRefSecondaryTrackingNo());

	}

	public void validateOnServiceRequestUpdate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

		if (Strings.isNullOrEmpty(serviceRequestDto.getModifiedBy())) {
			inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoDtoList);
		}

		if (serviceRequestDto.getModifiedBy().equalsIgnoreCase(CRM)) {
			inputValidator.populateInvalidData("modifiedBy", errorInfoDtoList);
		}
		if (Strings.isNullOrEmpty(serviceRequestDto.getRefSecondaryTrackingNo())) {
			inputValidator.populateMandatoryFieldError("refSecondaryTrackingNo", errorInfoDtoList);
		}

		if (Strings.isNullOrEmpty(serviceRequestDto.getReferenceNo())) {
			inputValidator.populateMandatoryFieldError("referenceNo", errorInfoDtoList);
		}

		if (serviceRequestDto.getWorkFlowValue() == null) {
			inputValidator.populateMandatoryFieldError("workFlowValue", errorInfoDtoList);
		}

		if (serviceRequestDto.getWorkFlowValue() != null
				&& serviceRequestDto.getWorkFlowValue() == WORKFLOW_DOC_VERIFIED_STAGE) {
			if (serviceRequestDto.getScheduleSlotStartDateTime() == null) {
				inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoDtoList);
			}

			if (serviceRequestDto.getScheduleSlotEndDateTime() == null) {
				inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoDtoList);
			}

			String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT,
					new Object[] { "" }, null);
			serviceRequestValidator.validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime",
					serviceRequestDto.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit),
					errorInfoDtoList);
		}

		if (serviceRequestDto.getIssueReportedByCustomer() == null
				|| serviceRequestDto.getIssueReportedByCustomer().isEmpty()) {
			inputValidator.populateMandatoryFieldError("issueReportedByCustomer", errorInfoDtoList);
		} else {
			for (Issues issue : serviceRequestDto.getIssueReportedByCustomer()) {
				if (Strings.isNullOrEmpty(issue.getIssueId())) {
					ErrorInfoDto errorInfoDto = new ErrorInfoDto(16, " issueId cannot be Empty");
					errorInfoDtoList.add(errorInfoDto);
				} else if (serviceTaskMasterCache.get(issue.getIssueId()) == null) {
					ErrorInfoDto errorInfoDto = new ErrorInfoDto(16, "Invalid issueId:" + issue.getIssueId());
					errorInfoDtoList.add(errorInfoDto);
				}
			}
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

		if (errorInfoDtoList != null && !errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
	public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto,
			ServiceRequestEntity serviceRequestEntity, String serviceRequestType)
			throws BusinessServiceException, JsonProcessingException {

		String workflowProcessId = null;
		String isPincodeServicable = Constants.NO_FLAG;
		ObjectMapper mapper = new ObjectMapper();

		List<PincodeServiceFulfilmentEntity> pincodeFulfilments = validatePincodeServicibility(
				serviceRequestDto.getServiceRequestAddressDetails().getPincode(),
				serviceRequestEntity.getServiceRequestTypeId());

		long partnerBuCode = 0;
		// Service Centre found for the given Pincode
		if (pincodeFulfilments != null && !pincodeFulfilments.isEmpty()) {

			PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
			serviceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
			partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null)
					? Long.valueOf(pincodeServiceFulfilmentEntity.getPartnerBUCode()) : null;
			serviceRequestEntity.setServicePartnerBuCode(partnerBuCode);
			isPincodeServicable = Constants.YES_FLAG;

			if (serviceRequestDto.getScheduleSlotStartDateTime() != null) {
				Date serviceRequestDueDate = DateUtils.addDays(serviceRequestDto.getScheduleSlotStartDateTime(), 10);
				serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
			} else {
				Date serviceRequestDueDate = DateUtils.addDays(new Date(), 10);
				serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
			}
		}

		// Populate Service Address Details
		ServiceAddressEntity addressEntity = new ServiceAddressEntity();
		populateServiceAddressEntity(addressEntity, serviceRequestDto);
		serviceAddressRepository.save(addressEntity);

		// Populate WorkFlow Json Data
		populateWorkFlowData(serviceRequestDto.getWorkFlowValue(), addressEntity, serviceRequestEntity,
				serviceRequestDto, isPincodeServicable);

		Map<String, Object> activitiVariablesMap = getActivitiVariables();
		Map<String, String> responseMap = workflowManager
				.startActivitiProcess(this.serviceRequestType.getRequestTypeActivitiKey(), activitiVariablesMap);

		workflowProcessId = responseMap.get(Constants.WORKFLOW_PROCESS_ID);

		if (serviceRequestDto.getWorkFlowValue() == Constants.WORKFLOW_DOC_UPLOADED_STAGE) {
			workflowManager.completeActivitiTask(Long.parseLong(workflowProcessId),
					Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, "Auto-complete",
					WorkflowStage.DOCUMENT_UPLOAD);
		} else if (serviceRequestDto.getWorkFlowValue() == WORKFLOW_DOC_VERIFIED_STAGE) {
			workflowManager.completeActivitiTask(Long.parseLong(workflowProcessId),
					Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, "Auto-complete",
					WorkflowStage.DOCUMENT_UPLOAD);
			workflowManager.setVariable(workflowProcessId, Constants.WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR,
					WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			workflowManager.completeActivitiTask(Long.parseLong(workflowProcessId),
					Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE, "Auto-complete", WorkflowStage.VERIFICATION);
		}

		serviceRequestEntity.setCreatedOn(new Date());
		serviceRequestEntity.setWorkflowProcessId(workflowProcessId);
		serviceRequestEntity.setWorkflowProcDefKey(responseMap.get(Constants.WORKFLOW_PROC_DEF_KEY));
		serviceRequestEntity
				.setThirdPartyProperties(mapper.writeValueAsString(serviceRequestDto.getThirdPartyProperties()));

		serviceRequestRepository.save(serviceRequestEntity);
		saveAssetsforSR(serviceRequestDto.getRefSecondaryTrackingNo(), serviceRequestEntity.getServiceRequestId());

		serviceRequestDto.setWorkflowStage(serviceRequestEntity.getWorkflowStage());
		serviceRequestDto.setWorkflowStageStatus(serviceRequestEntity.getWorkflowStageStatus());
		serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
		serviceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());
		workflowManager.setVariable(workflowProcessId, "IS_PIN_CODE_SERVICEABLE", isPincodeServicable);
		workflowManager.setVariable(workflowProcessId, Constants.SERVICE_REQUEST_ID_LOWER,
				String.valueOf(serviceRequestEntity.getServiceRequestId()));

		/* Save Asset document for HA_EW */
		OASYSCustMemDetails custMembershipDetails = oasysProxy.getMembershipAssetDetails(null,
				Long.parseLong(serviceRequestEntity.getRefSecondaryTrackingNo()), HA, true);

		logger.info("Response of get asstDocument details : " + custMembershipDetails);

		if (custMembershipDetails != null && custMembershipDetails.getMemberships() != null
				&& custMembershipDetails.getMemberships().get(0) != null
				&& custMembershipDetails.getMemberships().get(0).getAssets() != null
				&& custMembershipDetails.getMemberships().get(0).getAssets().get(0) != null
				&& custMembershipDetails.getMemberships().get(0).getAssets().get(0).getAssetDocuments() != null) {
			AssetDocument assetDocument = custMembershipDetails.getMemberships().get(0).getAssets().get(0)
					.getAssetDocuments().get(0);

			Long documentTypeId = null;
			if (assetDocument != null) {

				List<DocTypeConfigDetailEntity> docTypeConfigDetailEntities = docTypeConfigDetailCache
						.get(ServiceRequestType.HA_EW.getRequestType());

				for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : docTypeConfigDetailEntities) {
					if (docTypeConfigDetailEntity.getDocTypeMstEntity() != null
							&& docTypeConfigDetailEntity.getDocTypeMstEntity().getDocName().equals(HA_EW_DOC_KEY)) {
						documentTypeId = docTypeConfigDetailEntity.getDocTypeId();
					}
				}

				if (documentTypeId != null) {
					ServiceDocumentEntity serviceDocumentEntity = populateServiceDocumentDtl(null, documentTypeId,
							assetDocument.getFileName(), serviceRequestEntity.getServiceRequestId(),
							assetDocument.getDocReferenceId());
					/* Save mongodb reference Id in table */
					serviceDocumentEntity = serviceDocumentRepository.save(serviceDocumentEntity);
				}

				else {
					logger.info("document type ID is null");

				}
			}

			else {
				logger.info("Asset Document is null: " + assetDocument);

			}
		}

		if (Constants.YES_FLAG.equalsIgnoreCase(isPincodeServicable)) {
			if (serviceRequestDto.getWorkFlowValue() == WORKFLOW_DOC_VERIFIED_STAGE) {
				communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
						CommunicationGatewayEventCode.SP_CREATE_SR_FOR_VISIT, null);

				Map<String, Object> additionalAttributes = new HashMap<>();
				additionalAttributes.put(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue(), partnerBuCode);
				communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestDto,
						CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_REPAIR_ASSIGN, additionalAttributes);

			} else if (serviceRequestDto.getWorkFlowValue() == Constants.WORKFLOW_DOC_UPLOADED_STAGE) {
				communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
						CommunicationGatewayEventCode.SP_CREATE_SR_FOR_VERIFICATION, null);
			} else {
				communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
						CommunicationGatewayEventCode.SP_CREATE_SR, null);
			}
			communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
					CommunicationGatewayEventCode.SP_SR_OTP, null);
		} else {
			communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
					CommunicationGatewayEventCode.SP_CREATE_SR_SELF_SERVICE, null);
		}

		return serviceRequestDto;
	}

	private ServiceDocumentEntity populateServiceDocumentDtl(String documentId, Long docTypeId, String fileName,
			Long serviceRequestId, String storageRefId) {
		ServiceDocumentEntity serviceRequestDocumentEntity = new ServiceDocumentEntity();

		serviceRequestDocumentEntity.setDocumentId(
				StringUtils.isEmpty(documentId) ? UUID.randomUUID().toString().replaceAll("-", "") : documentId);
		serviceRequestDocumentEntity.setDocumentTypeId(docTypeId);
		serviceRequestDocumentEntity.setDocumentName(fileName);
		serviceRequestDocumentEntity.setServiceRequestId(serviceRequestId);
		serviceRequestDocumentEntity.setStorageRefId(storageRefId);
		serviceRequestDocumentEntity.setStatus(Constants.ACTIVE);
		serviceRequestDocumentEntity.setCreatedBy(Constants.USER_ADMIN);
		serviceRequestDocumentEntity.setCreatedOn(new Date());
		serviceRequestDocumentEntity.setModifiedBy(Constants.USER_ADMIN);
		serviceRequestDocumentEntity.setModifiedOn(new Date());

		return serviceRequestDocumentEntity;
	}

	@Override
	protected CommandResult<ServiceResponseDto> updateServiceRequest(
			ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto,
			ServiceRequestEventCode eventCode, ServiceRequestEntity serviceRequestEntity) throws Exception {

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

	private void validateExisitingSRForMembershipIdAndAssetId(String membershipId, Long serviceRequestTypeId,
			String assetId) throws BusinessServiceException {

		List<String> statusList = new ArrayList<>();
		statusList.add(ServiceRequestStatus.COMPLETED.getStatus());
		statusList.add(ServiceRequestStatus.CLOSED.getStatus());

		List<ServiceRequestEntity> inCompleteSrsForMemId = serviceRequestRepository
				.findByReferenceNoAndRefSecondaryTrackingNoAndServiceRequestTypeIdAndStatusNotIn(membershipId, assetId,
						serviceRequestTypeId, statusList);

		if (inCompleteSrsForMemId != null && !inCompleteSrsForMemId.isEmpty()) {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.CREATE_REPAIR_REQUEST_SR_NOTCLOSED_MEMBERSHIPID.getErrorCode(),
					new Object[] { membershipId }, new InputValidationException());
		}
	}

	private void validatePrimaryRefNumAlreadyExists(String primaryTrackingNum, Long serviceRequestTypeId)
			throws BusinessServiceException {

		List<ServiceRequestEntity> srsForPrimaryNum = serviceRequestRepository
				.findByRefPrimaryTrackingNoAndServiceRequestTypeId(primaryTrackingNum, serviceRequestTypeId);

		if (srsForPrimaryNum != null && !srsForPrimaryNum.isEmpty()) {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.CREATE_REPAIR_REQUEST_SR_ALREADY_EXISTS.getErrorCode(),
					new InputValidationException());
		}
	}

	private void populateWorkFlowData(int workFlowValue, ServiceAddressEntity addressEntity,
			ServiceRequestEntity serviceRequestEntity, ServiceRequestDto serviceRequestDto, String isPincodeServicable)
			throws BusinessServiceException {
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
		if (isPincodeServicable.equalsIgnoreCase(Constants.YES_FLAG)) {
			visit.setServiceStartCode(AuthCodeGenerator.generateFourDigitAuthPin());
			repair.setServiceEndCode(AuthCodeGenerator.generateFourDigitAuthPin());
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
		workFlowData.setCompleted(new Completed());

		ServiceRequestEventCode eventCode = null;
		switch (workFlowValue) {
		case Constants.WORKFLOW_DOC_UPLOADED_STAGE:
			documentUpload.setStatus(WorkflowStageStatus.DOC_UPLOADED.getWorkflowStageStatus());
			eventCode = isPincodeServicable.equalsIgnoreCase(Constants.NO_FLAG)
					? ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VERIFICATION_SELFREPAIR
					: ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VERIFICATION;
			break;

		case WORKFLOW_DOC_VERIFIED_STAGE:
			eventCode = isPincodeServicable.equalsIgnoreCase(Constants.NO_FLAG)
					? ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VISIT_SELFREPAIR
					: ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_VISIT;
			documentUpload.setStatus(WorkflowStageStatus.DOC_UPLOADED.getWorkflowStageStatus());
			verification.setStatus(WorkflowStageStatus.DOC_VERIFIED.getWorkflowStageStatus());
			break;

		default:
			eventCode = isPincodeServicable.equalsIgnoreCase(Constants.NO_FLAG)
					? ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_DOCUPLOAD_SELFREPAIR
					: ServiceRequestEventCode.CREATE_SERVICE_SERVICEABLE_PINCODE_DOCUPLOAD;

			if (serviceRequestDto.getInitiatingSystem() != null && serviceRequestDto.getInitiatingSystem() == 3) {

				documentUpload.setDescription(
						messageSource.getMessage(String.valueOf("HA_CRM_DU_DUP_CREATE_SR_MSG"), null, null));
			}

			else
				documentUpload.setDescription(messageSource.getMessage("HA_EW_DU_DUP_CREATE_SR_MSG", null, null));
		}
		String workflowStageStatus = null;
		final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache
				.get(this.serviceRequestType.getRequestType());
		if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {
			for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {
				if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
					String workflowStage = serviceRequestTransitionConfigEntity.getTransitionToStage();
					workflowStageStatus = com.oneassist.serviceplatform.commons.utils.StringUtils
							.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());

					final List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusEntities = serviceRequestStageStatusMstCache
							.get(this.serviceRequestType.getRequestType());
					if (serviceRequestStageStatusEntities != null && !serviceRequestStageStatusEntities.isEmpty()) {
						for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusMstEntity : serviceRequestStageStatusEntities) {
							if (workflowStage.equals(serviceRequestStageStatusMstEntity.getStageCode())
									&& workflowStageStatus.equals(
											com.oneassist.serviceplatform.commons.utils.StringUtils.getEmptyIfNull(
													serviceRequestStageStatusMstEntity.getStageStatusCode()))) {
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
			switch (workFlowValue) {
			case Constants.WORKFLOW_DOC_UPLOADED_STAGE:
				documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
				verification.setStatusCode(workflowStageStatus);
				break;
			case WORKFLOW_DOC_VERIFIED_STAGE:
				documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
				verification.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
				visit.setStatusCode(workflowStageStatus);
				break;
			default:
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

		if (assigneeRepairCostRequestDto.getInvoiceValue() == null) {
			inputValidator.populateMandatoryFieldError("invoiceValue", errorInfoDtoList);
		}
		if (!errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}
	}

	@Override
	protected CostToServiceDto calcualteCostToCustomer(Long serviceRequestId,
			AssigneeRepairCostRequestDto assigneeRepairCostRequestDto, ServiceRequestEntity serviceRequestEntity)
			throws BusinessServiceException {
		// TODO Auto-generated method stub

		CostToServiceDto costToServiceDto = new CostToServiceDto();
		ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity,
				ServiceRequestDto.class);
		// CTC calculated -> IC Appoved -> Calculate CTC - Needed old Cost to
		// Company to auto approve if both are same
		String existingCostToCompany = null;
		if (serviceRequestDto.getWorkflowData() != null
				&& serviceRequestDto.getWorkflowData().getRepairAssessment() != null) {
			existingCostToCompany = serviceRequestDto.getWorkflowData().getRepairAssessment().getCostToCompany();
		}
		logger.error("Existing Cost to Company for SR# " + serviceRequestId + " is > " + existingCostToCompany);
		List<ServiceTaskDto> serviceTasks = new ArrayList<>();

		RepairAssessment repairAssessment = serviceRequestDto.getWorkflowData().getRepairAssessment();
		List<String> repairAssessmentIds = new ArrayList<>();

		if (repairAssessment != null) {
			List<Diagnosis> diagnosisList = repairAssessment.getDiagonosisReportedbyAssignee();
			List<SpareParts> sparesList = repairAssessment.getSparePartsRequired();

			if (diagnosisList != null && !diagnosisList.isEmpty()) {
				for (Diagnosis diagnosis : diagnosisList) {
					repairAssessmentIds.add(diagnosis.getDiagnosisId());
				}
			}

			if (sparesList != null && !sparesList.isEmpty()) {
				for (SpareParts sparepart : sparesList) {
					repairAssessmentIds.add(sparepart.getSparePartId());
				}
			}

			LabourCost labourCost = repairAssessment.getLabourCost();
			Transport transport = repairAssessment.getTransport();

			if (labourCost != null) {
				repairAssessmentIds.add(labourCost.getLabourChargeId());
			}

			if (transport != null) {
				repairAssessmentIds.add(transport.getTransportTaskId());
			}
		}

		for (String assessmentId : repairAssessmentIds) {
			serviceTasks.add(serviceTaskMasterCache.get(assessmentId));
		}

		double sumOfInsuredTaskCost = 0;
		double sumOfNonInsuredTaskCost = 0;
		boolean estimateRequestApprovedStatus = true;

		for (ServiceTaskDto serviceTaskDto : serviceTasks) {
			if (serviceTaskDto != null && !Strings.isNullOrEmpty(serviceTaskDto.getIsInsuranceCovered())) {
				if (serviceTaskDto.getIsInsuranceCovered().trim().equalsIgnoreCase(Constants.YES_FLAG)) {
					sumOfInsuredTaskCost = sumOfInsuredTaskCost + serviceTaskDto.getCost();
				} else if (serviceTaskDto.getIsInsuranceCovered().trim().equalsIgnoreCase(Constants.NO_FLAG)) {
					sumOfNonInsuredTaskCost = sumOfNonInsuredTaskCost + serviceTaskDto.getCost();
				}
			}
		}
		if (repairAssessment.getAccidentalDamage() != null
				&& repairAssessment.getAccidentalDamage().trim().equalsIgnoreCase(Constants.YES_FLAG)) {
			costToServiceDto.setCostToCustomer(sumOfInsuredTaskCost + sumOfNonInsuredTaskCost);
		} else if (repairAssessment.getAccidentalDamage() != null
				&& repairAssessment.getAccidentalDamage().trim().equalsIgnoreCase(Constants.NO_FLAG)) {
			costToServiceDto.setCostToCustomer(sumOfNonInsuredTaskCost);
			costToServiceDto.setCostToCompany(sumOfInsuredTaskCost);
		}
		String ctcPercentLimit = messageSource.getMessage(COST_TO_COMPANY_INVOICE_PERCENTAGE_LIMIT, new Object[] { "" },
				null);
		String ctcEstimateAmountLimit = messageSource.getMessage(COST_TO_COMPANY_ESTIMATE_LIMIT, new Object[] { "" },
				null);
		if (ctcPercentLimit == null || ctcEstimateAmountLimit == null) {
			throw new BusinessServiceException(
					"System Error ! Either Cost To Company Invoice Percentage Limit (or) Cost to Company Estimate Limit is not configured",
					new InputValidationException());
		}
		if (Strings.isNullOrEmpty(existingCostToCompany)) {
			if (costToServiceDto.getCostToCompany() > Double.valueOf(ctcEstimateAmountLimit)
					|| (costToServiceDto.getCostToCompany() > (Double.valueOf(ctcPercentLimit) / 100)
							* assigneeRepairCostRequestDto.getInvoiceValue())) {
				estimateRequestApprovedStatus = false;
			}
		} else {
			if ((Double.valueOf(existingCostToCompany) != costToServiceDto.getCostToCompany())
					&& (costToServiceDto.getCostToCompany() > Double.valueOf(ctcEstimateAmountLimit)
							|| (costToServiceDto.getCostToCompany() > (Double.valueOf(ctcPercentLimit) / 100)
									* assigneeRepairCostRequestDto.getInvoiceValue()))) {
				estimateRequestApprovedStatus = false;
			}
		}
		costToServiceDto.setEstimateRequestApprovedStatus(estimateRequestApprovedStatus);
		return costToServiceDto;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public ServiceRequestDto updateServiceRequest(ServiceRequestDto serviceRequestDto,
			ServiceRequestEntity newServiceRequestEntity, ServiceRequestEntity existingServiceRequestEntity)
			throws Exception {

		if (CRM.equalsIgnoreCase(serviceRequestDto.getModifiedBy())) {

			if (serviceRequestDto.getRefPrimaryTrackingNo() == null) {
				inputValidator.populateMandatoryFieldError("refPrimaryTrackingNo", errorInfoDtoList);
			}
			existingServiceRequestEntity.setRefPrimaryTrackingNo(serviceRequestDto.getRefPrimaryTrackingNo());
			existingServiceRequestEntity.setModifiedBy(serviceRequestDto.getModifiedBy());
			serviceRequestRepository.save(existingServiceRequestEntity);
			return serviceRequestDto;
		}

		validateOnServiceRequestUpdate(serviceRequestDto);

		if (!existingServiceRequestEntity.getReferenceNo().equalsIgnoreCase(serviceRequestDto.getReferenceNo())) {
			throw new BusinessServiceException("Membership Cannot be changed");
		}

		String isPincodeServicable = Constants.NO_FLAG;

		ServiceAddressEntity addressEntity = serviceAddressRepository
				.findByServiceAddressId(Long.parseLong(serviceRequestHelper
						.getWorkflowDataByServiceRequest(existingServiceRequestEntity).getVisit().getServiceAddress()));

		if (!addressEntity.getPincode().equals(serviceRequestDto.getServiceRequestAddressDetails().getPincode())) {

			List<PincodeServiceFulfilmentEntity> pincodeFulfilments = validatePincodeServicibility(
					serviceRequestDto.getServiceRequestAddressDetails().getPincode(),
					newServiceRequestEntity.getServiceRequestTypeId());

			long partnerBuCode = 0;
			if (pincodeFulfilments != null && !pincodeFulfilments.isEmpty()) {

				PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
				newServiceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
				partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null)
						? Long.valueOf(pincodeServiceFulfilmentEntity.getPartnerBUCode()) : null;
				newServiceRequestEntity.setServicePartnerBuCode(partnerBuCode);
				isPincodeServicable = Constants.YES_FLAG;

			} else {
				throw new BusinessServiceException(ServiceRequestResponseCodes.PINCODE_NOT_SERVICABLE.getErrorCode(),
						new Object[] { newServiceRequestEntity.getServiceRequestId() });
			}

		} else {
			newServiceRequestEntity.setServicePartnerCode(existingServiceRequestEntity.getServicePartnerCode());
			newServiceRequestEntity.setServicePartnerBuCode(existingServiceRequestEntity.getServicePartnerBuCode());
			isPincodeServicable = Constants.YES_FLAG;
		}

		populateServiceAddressEntity(addressEntity, serviceRequestDto);
		serviceAddressRepository.save(addressEntity);

		populateWorkFlowData(serviceRequestDto.getWorkFlowValue(), addressEntity, newServiceRequestEntity,
				serviceRequestDto, isPincodeServicable);
		WorkflowData oldWorkFlowData = serviceRequestHelper
				.getWorkflowDataByServiceRequest(existingServiceRequestEntity);
		WorkflowData newWorkFlowData = serviceRequestHelper.getWorkflowDataByServiceRequest(newServiceRequestEntity);
		newWorkFlowData.getVisit().setServiceStartCode(oldWorkFlowData.getVisit().getServiceStartCode());
		newWorkFlowData.getRepair().setServiceEndCode(oldWorkFlowData.getRepair().getServiceEndCode());
		newWorkFlowData.getVisit().setPlaceOfIncident(oldWorkFlowData.getVisit().getPlaceOfIncident());

		if (com.oneassist.serviceplatform.commons.utils.StringUtils
				.getListFromString(newServiceRequestEntity.getRefSecondaryTrackingNo()).size() == 1) {

			Set<String> existingAssetRefIds = new HashSet<>();
			for (ServiceRequestAssetEntity existingAssetEntity : existingServiceRequestEntity
					.getServiceRequestAssetEntity()) {
				existingAssetRefIds.add(existingAssetEntity.getAssetReferenceNo());
			}

			Set<String> newAssetRefIds = new HashSet<>(com.oneassist.serviceplatform.commons.utils.StringUtils
					.getListFromString(newServiceRequestEntity.getRefSecondaryTrackingNo()));

			for (String newAssetRefId : newAssetRefIds) {
				if (!existingAssetRefIds.contains(newAssetRefId)) {
					saveAssetsforSR(newAssetRefId, newServiceRequestEntity.getServiceRequestId());
				}
			}

			for (ServiceRequestAssetEntity asset : existingServiceRequestEntity.getServiceRequestAssetEntity()) {
				if (!newAssetRefIds.contains(asset.getAssetReferenceNo())) {
					serviceRequestAssetRepository.delete(asset);
				}
			}

		} else {
			throw new BusinessServiceException("Assets cannot be greater than 1");
		}

		copyUnModifiableDataInUpdateServiceRequest(existingServiceRequestEntity, newServiceRequestEntity);
		serviceRequestRepository.save(newServiceRequestEntity);
		
        serviceRequestDto.setWorkflowStage(newServiceRequestEntity.getWorkflowStage());
		serviceRequestDto.setWorkflowStageStatus(newServiceRequestEntity.getWorkflowStageStatus());

		return serviceRequestDto;
	}

	private void copyUnModifiableDataInUpdateServiceRequest(ServiceRequestEntity oldSr, ServiceRequestEntity newSr) {
		newSr.setServiceRequestTypeId(oldSr.getServiceRequestTypeId());
		newSr.setInitiatingSystem(oldSr.getInitiatingSystem());
		newSr.setRefPrimaryTrackingNo(oldSr.getRefPrimaryTrackingNo());
		newSr.setDueDateTime(oldSr.getDueDateTime());
		newSr.setCreatedOn(oldSr.getCreatedOn());
		newSr.setWorkflowProcessId(oldSr.getWorkflowProcessId());
		newSr.setWorkflowProcDefKey(oldSr.getWorkflowProcDefKey());
		newSr.setThirdPartyProperties(oldSr.getThirdPartyProperties());
	}
}
