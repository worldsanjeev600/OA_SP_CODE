package com.oneassist.serviceplatform.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.cache.SystemConfigMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.CacheConstants;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.ShipmentType;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageStatusMstCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTransitionConfigCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceTaskRepository;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.AssetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Completed;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InspectionAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSMembershipDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PartnerStageStatus;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SoftApproval;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;

@Component
@SuppressWarnings("unchecked")
public class ServiceRequestHelper {

	private final Logger logger = Logger.getLogger(ServiceRequestHelper.class);

	@Autowired
	private ModelMapper modelMapper;

	@Value("${getCustomerMembershipDetail}")
	private String getCustomerMembershipDetail;

	@Autowired
	private CacheFactory cacheFactory;

	@Autowired
	private OasysProxy oasysProxy;

	@Autowired
	private SystemConfigMasterCache systemConfigMasterCache;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private ServiceTaskRepository serviceTaskRepository;

	@Autowired
	private MessageProducer messageProducer;

	@Autowired
	private CustomerPropogationMessageProducer customerPropogationMessageProducer;

	@Autowired
	private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

	@Value("${ServiceDocumentByteArrayDownload}")
	private String ServiceDocumentByteArrayDownload;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected ServiceRequestTransitionConfigCache serviceRequestTransitionConfigCache;

	@Autowired
	protected ServiceRequestStageStatusMstCache serviceRequestStageStatusMstCache;

	@Autowired
	protected ServiceRequestStageMasterCache serviceRequestStageMasterCache;

	private final String MAX_AUTH_FAIL_COUNT_LIMIT = "MAX_AUTH_FAIL_COUNT_LIMIT";

	@Autowired
	private PinCodeMasterCache pinCodeMasterCache;

	@Autowired
	private LogisticShipmentRepository shipmentRepository;

	@Autowired
	private PincodeServiceRepository pincodeServiceRepository;

	private final String PINCODE_CATEGORY_D = "D";

	public void publishMessageInSPSRProcessingQueue(Object messageBody) throws Exception {
		messageProducer.sendMessages(messageBody);
	}

	public void publishMessageInCustomerPropagationQueue(Object messageBody) throws Exception {
		customerPropogationMessageProducer.sendMessages(messageBody);
	}

	public List<SystemConfigDto> getShipmentStage(String shipmentPartnerCode, int shipmentTypeCode) {

		List<SystemConfigDto> filteredShipmentStages = null;
		List<SystemConfigDto> shipmentStages = systemConfigMasterCache.get(CacheConstants.SHIPMENT_STAGE_PARAMCODE);

		if (!StringUtils.isEmpty(shipmentPartnerCode) && shipmentTypeCode != 0) {
			HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory
					.get(Constants.PARTNER_MASTER_CACHE).getAll();
			PartnerMasterDto logistictPartner = partnerMasterCache.get(shipmentPartnerCode);

			if (logistictPartner != null && logistictPartner.getPartnerName() != null) {
				String shipmentPartner = logistictPartner.getPartnerName();

				if (!StringUtils.isEmpty(shipmentPartner) && shipmentTypeCode != 0
						&& !CollectionUtils.isEmpty(shipmentStages)) {

					filteredShipmentStages = new ArrayList<SystemConfigDto>();
					String inputParamType = shipmentPartner + "_"
							+ ShipmentType.getShipmentType(shipmentTypeCode).toString();

					for (SystemConfigDto shipmentStage : shipmentStages) {
						if (!StringUtils.isEmpty(shipmentStage.getParamType())
								&& !"NA".equalsIgnoreCase(shipmentStage.getParamType())
								&& inputParamType.equalsIgnoreCase(shipmentStage.getParamType())) {

							filteredShipmentStages.add(shipmentStage);
						}
					}

				}
			}
		}
		if (CollectionUtils.isEmpty(filteredShipmentStages)) {
			filteredShipmentStages = getUniqueStages(shipmentStages);
		}
		return filteredShipmentStages;
	}

	private List<SystemConfigDto> getUniqueStages(List<SystemConfigDto> shipmentStages) {

		List<SystemConfigDto> filteredShipmentStages = new ArrayList<SystemConfigDto>();
		if (shipmentStages != null) {
			List<String> uniqueStages = new ArrayList<String>();
			for (SystemConfigDto stage : shipmentStages) {
				if (!uniqueStages.contains(stage.getParamName())) {
					filteredShipmentStages.add(stage);
				}
				uniqueStages.add(stage.getParamName());
			}
		}
		return filteredShipmentStages;
	}

	public List<SystemConfigDto> getShipmentStatus(String shipmentPartnerCode) {

		return systemConfigMasterCache.get(CacheConstants.SHIPMENT_STATUS_PARAMCODE);
	}

	public String populateJsonWithRequestParameters(ServiceRequestDto serviceRequestUpdateDto,
			ServiceRequestEntity oldServiceRequestEntity) throws Exception {

		logger.info(
				">>> In ServiceRequestServiceImpl: populateJsonWithRequestParameters(serviceRequestUpdateDto,serviceRequestEntity)"
						+ serviceRequestUpdateDto);
		ServiceRequestDto serviceRequestDto = null;
		ObjectMapper objMapper = new ObjectMapper();

		if (serviceRequestUpdateDto.getWorkflowJsonString() == null) {
			if (oldServiceRequestEntity == null) {
				oldServiceRequestEntity = serviceRequestRepository
						.findServiceRequestEntityByServiceRequestId(serviceRequestUpdateDto.getServiceRequestId());
			}
			serviceRequestDto = modelMapper.map(oldServiceRequestEntity, new TypeToken<ServiceRequestDto>() {
			}.getType());
		} else {
			oldServiceRequestEntity = new ServiceRequestEntity();
			oldServiceRequestEntity.setWorkflowData(serviceRequestUpdateDto.getWorkflowJsonString());
			serviceRequestDto = convertObject(oldServiceRequestEntity, ServiceRequestDto.class);
		}

		serviceRequestDto
				.setWorkflowData(objMapper.readValue(oldServiceRequestEntity.getWorkflowData(), WorkflowData.class));
		copyBeanProperties(serviceRequestUpdateDto, serviceRequestDto);
		logger.info("After copy serviceRequestUpdateDto -" + serviceRequestUpdateDto);

		ObjectMapper objectMapper = new ObjectMapper();
		String workflowJsonString = objectMapper.writeValueAsString(serviceRequestUpdateDto.getWorkflowData());
		return workflowJsonString;
	}

	public void copyBeanProperties(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestDto serviceResponseDto) {

		logger.info(
				">>> In ServiceRequestServiceImpl: copyBeanProperties(serviceRequestUpdateDto, ServiceResponseDto)");
		WorkflowData existingWorkflowData = null;
		WorkflowStage workflowStage = WorkflowStage.getWorkflowStage(serviceRequestUpdateDto.getWorkflowStage());

		switch (workflowStage) {

		case VISIT: {
			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			Visit existingData = existingWorkflowData.getVisit() == null ? new Visit()
					: existingWorkflowData.getVisit();
			Visit newRequest = serviceRequestUpdateDto.getWorkflowData().getVisit() == null ? new Visit()
					: serviceRequestUpdateDto.getWorkflowData().getVisit();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setVisit(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;
		case DOCUMENT_UPLOAD: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			DocumentUpload existingData = existingWorkflowData.getDocumentUpload() == null ? new DocumentUpload()
					: existingWorkflowData.getDocumentUpload();
			DocumentUpload newRequest = serviceRequestUpdateDto.getWorkflowData().getDocumentUpload() == null
					? new DocumentUpload() : serviceRequestUpdateDto.getWorkflowData().getDocumentUpload();
			CopyProperties.copyNonNullProperties(newRequest, existingData);

			Verification verificationExistData = existingWorkflowData.getVerification() == null ? new Verification()
					: existingWorkflowData.getVerification();
			Verification verificationNewRequest = serviceRequestUpdateDto.getWorkflowData().getVerification() == null
					? new Verification() : serviceRequestUpdateDto.getWorkflowData().getVerification();
			CopyProperties.copyNonNullProperties(verificationNewRequest, verificationExistData);

			existingWorkflowData.setDocumentUpload(existingData);
			existingWorkflowData.setVerification(verificationExistData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;
		case VERIFICATION: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			Verification existingData = existingWorkflowData.getVerification() == null ? new Verification()
					: existingWorkflowData.getVerification();
			Verification newRequest = serviceRequestUpdateDto.getWorkflowData().getVerification() == null
					? new Verification() : serviceRequestUpdateDto.getWorkflowData().getVerification();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setVerification(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;
		case REPAIR_ASSESSMENT: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			RepairAssessment existingData = existingWorkflowData.getRepairAssessment() == null ? new RepairAssessment()
					: existingWorkflowData.getRepairAssessment();
			serviceRequestUpdateDto.setWorkflowData(serviceRequestUpdateDto.getWorkflowData() == null
					? new WorkflowData() : serviceRequestUpdateDto.getWorkflowData());
			RepairAssessment newRequest = serviceRequestUpdateDto.getWorkflowData().getRepairAssessment() == null
					? new RepairAssessment() : serviceRequestUpdateDto.getWorkflowData().getRepairAssessment();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setRepairAssessment(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;
		case IC_DECISION: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			InsuranceDecision existingData = existingWorkflowData.getInsuranceDecision() == null
					? new InsuranceDecision() : existingWorkflowData.getInsuranceDecision();
			InsuranceDecision newRequest = serviceRequestUpdateDto.getWorkflowData().getInsuranceDecision() == null
					? new InsuranceDecision() : serviceRequestUpdateDto.getWorkflowData().getInsuranceDecision();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setInsuranceDecision(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case SOFT_APPROVAL: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			SoftApproval existingData = existingWorkflowData.getSoftApproval() == null ? new SoftApproval()
					: existingWorkflowData.getSoftApproval();
			SoftApproval newRequest = serviceRequestUpdateDto.getWorkflowData().getSoftApproval() == null
					? new SoftApproval() : serviceRequestUpdateDto.getWorkflowData().getSoftApproval();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setSoftApproval(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case REPAIR: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			Repair existingData = existingWorkflowData.getRepair() == null ? new Repair()
					: existingWorkflowData.getRepair();
			Repair newRequest = serviceRequestUpdateDto.getWorkflowData().getRepair();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setRepair(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case CLAIM_SETTLEMENT: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			ClaimSettlement existingData = existingWorkflowData.getClaimSettlement() == null ? new ClaimSettlement()
					: existingWorkflowData.getClaimSettlement();
			ClaimSettlement newRequest = serviceRequestUpdateDto.getWorkflowData().getClaimSettlement() == null
					? new ClaimSettlement() : serviceRequestUpdateDto.getWorkflowData().getClaimSettlement();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setClaimSettlement(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case COMPLETED: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			Completed existingData = existingWorkflowData.getCompleted() == null ? new Completed()
					: existingWorkflowData.getCompleted();
			Completed newRequest = serviceRequestUpdateDto.getWorkflowData().getCompleted() == null ? new Completed()
					: serviceRequestUpdateDto.getWorkflowData().getCompleted();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setCompleted(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case PARTNER_STAGE_STATUS: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			PartnerStageStatus existingData = existingWorkflowData.getPartnerStageStatus() == null
					? new PartnerStageStatus() : existingWorkflowData.getPartnerStageStatus();
			PartnerStageStatus newRequest = serviceRequestUpdateDto.getWorkflowData().getPartnerStageStatus() == null
					? new PartnerStageStatus() : serviceRequestUpdateDto.getWorkflowData().getPartnerStageStatus();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setPartnerStageStatus(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case IC_DOC: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			ICDoc existingData = existingWorkflowData.getIcDoc() == null ? new ICDoc()
					: existingWorkflowData.getIcDoc();
			ICDoc newRequest = serviceRequestUpdateDto.getWorkflowData().getIcDoc() == null ? new ICDoc()
					: serviceRequestUpdateDto.getWorkflowData().getIcDoc();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setIcDoc(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case INSPECTION_ASSESSMENT: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			InspectionAssessment existingData = existingWorkflowData.getInspectionAssessment() == null
					? new InspectionAssessment() : existingWorkflowData.getInspectionAssessment();
			InspectionAssessment newRequest = serviceRequestUpdateDto.getWorkflowData()
					.getInspectionAssessment() == null ? new InspectionAssessment()
							: serviceRequestUpdateDto.getWorkflowData().getInspectionAssessment();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setInspectionAssessment(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case INSPECTION_STARTED: {

			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			InspectionAssessment existingData = existingWorkflowData.getInspectionAssessment() == null
					? new InspectionAssessment() : existingWorkflowData.getInspectionAssessment();
			InspectionAssessment newRequest = serviceRequestUpdateDto.getWorkflowData()
					.getInspectionAssessment() == null ? new InspectionAssessment()
							: serviceRequestUpdateDto.getWorkflowData().getInspectionAssessment();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setInspectionAssessment(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;

		case CANCEL_INSPECTION: {
			existingWorkflowData = serviceResponseDto.getWorkflowData() == null ? new WorkflowData()
					: serviceResponseDto.getWorkflowData();
			Visit existingData = existingWorkflowData.getVisit() == null ? new Visit()
					: existingWorkflowData.getVisit();
			Visit newRequest = serviceRequestUpdateDto.getWorkflowData().getVisit() == null ? new Visit()
					: serviceRequestUpdateDto.getWorkflowData().getVisit();
			CopyProperties.copyNonNullProperties(newRequest, existingData);
			existingWorkflowData.setVisit(existingData);
			serviceRequestUpdateDto.setWorkflowData(existingWorkflowData);
		}
			break;
		default:
			logger.info("No valid workflow stage set to copyProperties()");
		}
	}

	public void populateJsonWithEventCode(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode eventCode,
			ServiceRequestEntity serviceRequestEntity) throws Exception {

		logger.error("Inside populateJsonWithEventCode::" + eventCode);
		// Get the workflowStageStatus to set into the workflow_stage.status
		// attribute..
		String workflowStageStatus = null;
		final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache
				.get(serviceRequestUpdateDto.getServiceRequestType());
		if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {
			for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {
				if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
					workflowStageStatus = com.oneassist.serviceplatform.commons.utils.StringUtils
							.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());
				}
			}
		}
		switch (eventCode) {
		case TECHNICIAN_AT_LOCATION_AND_BEGINS_SERVICE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			visit.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_AD_VE_CO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_BD_VE_CO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_EW_VE_CO_MSG"), null, null));
			}

			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case COMPLETED_AFTER_REPAIR_ASSESSMENT: {// its when technician
			// completed repair
			// assessment, & then
			// customer denying service
			// serviceRequestUpdateRequestDto.setWorkflowStage(Constants.WFSTAGE_REPAIRASSESSMENT);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				completed.setDescription(messageSource.getMessage(String.valueOf("HA_AD_CO_CDP_MSG"), null, null));

				RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
						: workflowData.getRepairAssessment();

				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_CO_MSG"), null, null));
				workflowData.setRepairAssessment(repairAssessment);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				completed.setDescription(messageSource.getMessage(String.valueOf("HA_EW_CO_CDP_MSG"), null, null));

				RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
						: workflowData.getRepairAssessment();

				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_CO_MSG"), null, null));
				workflowData.setRepairAssessment(repairAssessment);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_BD_CO_CDP_MSG"), null, null));

				RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
						: workflowData.getRepairAssessment();

				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_CO_MSG"), null, null));
				workflowData.setRepairAssessment(repairAssessment);
			}

			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ClaimSettlement claimSettlement = workflowData.getClaimSettlement() == null ? new ClaimSettlement()
					: workflowData.getClaimSettlement();
			claimSettlement.setStatusCode(workflowStageStatus);
			workflowData.setClaimSettlement(claimSettlement);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case IC_APPROVAL_WAITING_WITH_SPARE_NEEDED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setIsSpareRequestRaisedWithIC(Constants.YES_FLAG);
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatusCode(workflowStageStatus);
			workflowData.setInsuranceDecision(insuranceDecision);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case IC_APPROVAL_WAITING_WITH_TRANSPORTATION_NEEDED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setIsTransportationRaisedWithIC(Constants.YES_FLAG);
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatusCode(workflowStageStatus);
			workflowData.setInsuranceDecision(insuranceDecision);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case IC_APPROVAL_WAITING: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatusCode(workflowStageStatus);
			workflowData.setInsuranceDecision(insuranceDecision);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {

				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_ID_AA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {

				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_ID_AA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {

				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_ID_AA_MSG"), null, null));
			}
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case IC_DECISION_APPROVED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			insuranceDecision.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setInsuranceDecision(insuranceDecision);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_ID_AP_MSG"), null, null));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_ID_AP_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_ID_AP_MSG"), null, null));
			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())
					|| serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())
					|| serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_EW.getRequestType())) {
				serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
						: workflowData.getRepairAssessment();
				repairAssessment.setIsSpareRequestRaisedWithIC(Constants.NO_FLAG);
				repairAssessment.setIsTransportationRaisedWithIC(Constants.NO_FLAG);
				repairAssessment.setStatusCode(workflowStageStatus);
				workflowData.setRepairAssessment(repairAssessment);
				serviceRequestUpdateDto.setWorkflowData(workflowData);

				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
			} else if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_FR.getRequestType())
					|| serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_BR.getRequestType())) {
				serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();
				completed.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
				workflowData.setCompleted(completed);
				serviceRequestUpdateDto.setWorkflowData(workflowData);
				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

				serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				ClaimSettlement claimSettlement = workflowData.getClaimSettlement() == null ? new ClaimSettlement()
						: workflowData.getClaimSettlement();
				claimSettlement.setStatusCode(workflowStageStatus);
				workflowData.setClaimSettlement(claimSettlement);
				serviceRequestUpdateDto.setWorkflowData(workflowData);
				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
			}

		}
			break;
		case IC_DECISION_REJECTED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
			insuranceDecision.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setInsuranceDecision(insuranceDecision);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_ID_RJ_MSG"), null, null));

				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_AD_CO_RF_MSG"), null, null));
				workflowData.setCompleted(completed);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_ID_RJ_MSG"), null, null));

				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_BD_CO_RF_MSG"), null, null));
				workflowData.setCompleted(completed);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_ID_RJ_MSG"), null, null));

				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_EW_CO_RF_MSG"), null, null));
				workflowData.setCompleted(completed);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BR.getRequestType())) {

				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_BR_CO_RF_MSG"), null, null));
				workflowData.setCompleted(completed);
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_FR.getRequestType())) {

				Completed completed = workflowData.getCompleted() == null ? new Completed()
						: workflowData.getCompleted();

				completed.setDescription(messageSource.getMessage(String.valueOf("HA_FR_CO_RF_MSG"), null, null));
				workflowData.setCompleted(completed);
			}
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);

			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			completed.setStatusCode(workflowStageStatus);
			// completed.setRemarks(serviceRequestUpdateDto.getRemarks());

			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case IC_DECISION_REJECTED_SELF_REPAIR: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
			insuranceDecision.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setInsuranceDecision(insuranceDecision);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

		}
			break;
		case IC_DECISION_BER_APPROVED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus());
			insuranceDecision.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setInsuranceDecision(insuranceDecision);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_ID_BER_CO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_ID_BER_CO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getInsuranceDecision()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_ID_BER_CO_MSG"), null, null));
			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case IC_DECISION_BER_APPROVED_SELF_REPAIR: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.BER_APPROVED.getWorkflowStageStatus());
			insuranceDecision.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setInsuranceDecision(insuranceDecision);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case CUSTOMER_NOT_AVAILABLE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setStatusCode(workflowStageStatus);
			workflowData.setVisit(visit);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getVisit()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_VE_TCCA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getVisit()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_VE_TCCA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getVisit()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_VE_TCCA_MSG"), null, null));
			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case SPARE_PART_NOT_AVAILABLE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);
			workflowData.setRepairAssessment(repairAssessment);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_SN_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_SN_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_SN_MSG"), null, null));
			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case SPARE_PART_AVAILABLE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);
			workflowData.setRepairAssessment(repairAssessment);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_SPA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_SPA_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				workflowData.getRepairAssessment()
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_SPA_MSG"), null, null));
			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case TRANSPORTATION_REQUIRED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_TR_MSG"), null, null));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_TR_MSG"), null, null));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_TR_MSG"), null, null));

			}

			

			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

		}
			break;

		case CUSTOMER_NOT_AVAILABLE_RA_STAGE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_TCCA_MSG"), null, null));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_TCCA_MSG"), null, null));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_TCCA_MSG"), null, null));

			}

			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

		}
			break;
		case CANNOT_PERFORM_SERVICE_FOR_SOME_REASON_RA_STAGE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(workflowStageStatus);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_TCO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_TCO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_TCO_MSG"), null, null));
			}

			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case REPAIR_SUCCESSFUL: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			completed.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {

				String completeDescription = messageSource.getMessage(String.valueOf("HA_AD_CO_SU_MSG"), null, null);
				if (completeDescription != null) {
					completed.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(
									completeDescription, "$DATE", DateUtils.toDayMonthFormattedString(new Date())));
					workflowData.setCompleted(completed);
				}

				workflowData.setCompleted(completed);

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BR.getRequestType())) {
				RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
						: workflowData.getRepairAssessment();

				List<String> referenceNos = new ArrayList<String>();
				referenceNos.add(serviceRequestEntity.getReferenceNo());
				String repareAssesDescription = messageSource.getMessage(String.valueOf("HA_BR_CO_SU_MSG"), null, null);
				if (repareAssesDescription != null) {
					repairAssessment.setDescription(repareAssesDescription);
					workflowData.setRepairAssessment(repairAssessment);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_FR.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_FR_CO_SU_MSG"), null, null);
				if (description != null) {
					completed.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(description,
									"$DATE", DateUtils.toDayMonthFormattedString(new Date())));
					workflowData.setCompleted(completed);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_EW_CO_SU_MSG"), null, null);
				if (description != null) {
					completed.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(description,
									"$DATE", DateUtils.toDayMonthFormattedString(new Date())));
					workflowData.setCompleted(completed);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_BD_CO_SU_MSG"), null, null);
				if (description != null) {
					completed.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(description,
									"$DATE", DateUtils.toDayMonthFormattedString(new Date())));
					workflowData.setCompleted(completed);
				}
			}

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ClaimSettlement claimSettlement = workflowData.getClaimSettlement() == null ? new ClaimSettlement()
					: workflowData.getClaimSettlement();
			claimSettlement.setStatusCode(workflowStageStatus);
			workflowData.setClaimSettlement(claimSettlement);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case CUSTOMER_ASKS_REFUND: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			completed.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ClaimSettlement claimSettlement = workflowData.getClaimSettlement() == null ? new ClaimSettlement()
					: workflowData.getClaimSettlement();
			claimSettlement.setStatusCode(workflowStageStatus);
			workflowData.setClaimSettlement(claimSettlement);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_AD_RA_RFPP_MSG"), null, null);
				if (description != null) {
					repairAssessment.setDescription(description);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_BD_RA_RFPP_MSG"), null, null);
				if (description != null) {
					repairAssessment.setDescription(description);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_EW_RA_RFPP_MSG"), null, null);
				if (description != null) {
					repairAssessment.setDescription(description);
				}
			}

			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case HA_BD_ACCIDENTAL_DAMAGE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			completed.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setCompleted(completed);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_BD_CO_AD_MSG"), null, null);
				if (description != null) {
					completed.setDescription(description);
					workflowData.setCompleted(completed);
				}

			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();
			repairAssessment.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case ESTIMATED_INVOICE_UPLOAD: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			visit.setEstimatedInvoiceUploadStaus(WorkflowStageStatus.DOC_UPLOADED.getWorkflowStageStatus());
			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case ESTIMATED_INVOICE_VERIFICATION_SUCCESS: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DOC.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ICDoc iCDoc = workflowData.getIcDoc() == null ? new ICDoc() : workflowData.getIcDoc();
			iCDoc.setEstimatedInvoiceVerificationStatus(WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			workflowData.setIcDoc(iCDoc);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case ESTIMATED_INVOICE_VERIFICATION_FAIL: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DOC.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ICDoc iCDoc = workflowData.getIcDoc() == null ? new ICDoc() : workflowData.getIcDoc();
			iCDoc.setEstimatedInvoiceVerificationStatus(WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
			workflowData.setIcDoc(iCDoc);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case IC_DECISION_APPROVED_SELF_REPAIR: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CANCEL_INSPECTION.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null ? new InsuranceDecision()
					: workflowData.getInsuranceDecision();
			insuranceDecision.setStatus(WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			workflowData.setInsuranceDecision(insuranceDecision);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case COMPLETE_CLAIM_SETTLEMENT: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			ClaimSettlement claimSettlement = workflowData.getClaimSettlement() == null ? new ClaimSettlement()
					: workflowData.getClaimSettlement();
			claimSettlement.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			claimSettlement.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			workflowData.setClaimSettlement(claimSettlement);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case COMPLETE_DOCUMENT_UPLOAD: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			DocumentUpload documentUpload = workflowData.getDocumentUpload() == null ? new DocumentUpload()
					: workflowData.getDocumentUpload();
			documentUpload.setStatus(WorkflowStageStatus.DOC_UPLOADED.getWorkflowStageStatus());
			documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setDocumentUpload(documentUpload);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_AD_DU_CO_MSG"), null, null);
				if (description != null)
					documentUpload.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(description,
									"$DATE", DateUtils.toDayMonthFormattedString(new Date())));
				workflowData.setDocumentUpload(documentUpload);

				Verification verification = workflowData.getVerification() == null ? new Verification()
						: workflowData.getVerification();
				verification.setDescription(messageSource.getMessage(String.valueOf("HA_AD_VR_VP_MSG"), null, null));
				workflowData.setVerification(verification);

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BR.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_BR_DU_CO_MSG"), null, null);
				if (description != null) {
					documentUpload.setDescription(description);
					workflowData.setDocumentUpload(documentUpload);
				}

				Verification verification = workflowData.getVerification() == null ? new Verification()
						: workflowData.getVerification();
				verification.setDescription(messageSource.getMessage(String.valueOf("HA_BR_VR_VP_MSG"), null, null));
				workflowData.setVerification(verification);

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_EW_DU_CO_MSG"), null, null);
				if (description != null) {
					documentUpload.setDescription(description);
					workflowData.setDocumentUpload(documentUpload);
				}

				Verification verification = workflowData.getVerification() == null ? new Verification()
						: workflowData.getVerification();
				verification.setDescription(messageSource.getMessage(String.valueOf("HA_EW_VR_VP_MSG"), null, null));
				workflowData.setVerification(verification);

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_FR.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_FR_DU_CO_MSG"), null, null);
				if (description != null) {
					documentUpload.setDescription(description);
					workflowData.setDocumentUpload(documentUpload);
				}

				Verification verification = workflowData.getVerification() == null ? new Verification()
						: workflowData.getVerification();
				verification.setDescription(messageSource.getMessage(String.valueOf("HA_FR_VR_VP_MSG"), null, null));
				workflowData.setVerification(verification);

			}

			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatusCode(workflowStageStatus);
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case COMPLETE_DOCUMENT_UPLOAD_SELFREPAIR: {

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			DocumentUpload documentUpload = workflowData.getDocumentUpload() == null ? new DocumentUpload()
					: workflowData.getDocumentUpload();
			documentUpload.setStatus(WorkflowStageStatus.DOC_UPLOADED.getWorkflowStageStatus());
			documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setDocumentUpload(documentUpload);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatusCode(workflowStageStatus);
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case VERIFICATION_SUCCESSFUL:
		case INVOICE_VERIFICATION_SUCCESS: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatus(WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			verification.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setVerification(verification);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_FR.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_FR_VR_CO_MSG"), null, null);
				if (description != null) {
					verification.setDescription(description);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_AD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_AD_VR_CPP_MSG"), null, null);
				if (description != null) {
					verification.setDescription(description);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BR.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_BR_VR_CO_MSG"), null, null);
				if (description != null) {
					verification.setDescription(
							com.oneassist.serviceplatform.commons.utils.StringUtils.getAfterReplaceByString(description,
									"$DATE", DateUtils.toDayMonthFormattedString(new Date())));
				}

			} else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {

				String description = messageSource.getMessage(String.valueOf("HA_EW_VR_CO_MSG"), null, null);
				if (description != null) {
					verification.setDescription(description);
				}

			}

			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_BD.getRequestType())
					// ||
					// serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())
					|| serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_EW.getRequestType())) {
				serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
				visit.setStatusCode(workflowStageStatus);
				workflowData.setVisit(visit);
				serviceRequestUpdateDto.setWorkflowData(workflowData);
				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
			} else if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_BR.getRequestType())
					|| serviceRequestUpdateDto.getServiceRequestType()
							.equals(ServiceRequestType.HA_FR.getRequestType())) {

				serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.IC_DECISION.getWorkflowStageName());
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				InsuranceDecision insuranceDecision = workflowData.getInsuranceDecision() == null
						? new InsuranceDecision() : workflowData.getInsuranceDecision();
				insuranceDecision.setStatusCode(workflowStageStatus);
				workflowData.setInsuranceDecision(insuranceDecision);
				serviceRequestUpdateDto.setWorkflowData(workflowData);
				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
			} else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
						: serviceRequestUpdateDto.getWorkflowData();
				verification = workflowData.getVerification() == null ? new Verification()
						: workflowData.getVerification();
				verification.setStatusCode(workflowStageStatus);
				workflowData.setVerification(verification);
				serviceRequestUpdateDto.setWorkflowData(workflowData);
				updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
						serviceRequestEntity);
				serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
			}
		}
			break;
		case DEDUCTIBLE_PAYMENT_DONE: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatusCode(workflowStageStatus);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {

				verification.setDescription(messageSource.getMessage(String.valueOf("HA_AD_VR_CPC_MSG"), null, null));
			}
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case CUSTOMER_RESCHEDULE_VISIT_POST_DEDUCTIBLE_PAYMENT: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setStatusCode(workflowStageStatus);
			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case INVOICE_VERIFICATION_SUCCESS_SELFREPAIR: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatus(WorkflowStageStatus.APPROVED.getWorkflowStageStatus());
			verification.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setStatusCode(workflowStageStatus);
			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case INVOICE_VERIFICATION_FAIL:
		case INVOICE_VERIFICATION_FAIL_SELFREPAIR: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatus(WorkflowStageStatus.REJECTED.getWorkflowStageStatus());
			verification.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
			workflowData.setVerification(verification);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName());

			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			DocumentUpload documentUpload = workflowData.getDocumentUpload() == null ? new DocumentUpload()
					: workflowData.getDocumentUpload();
			documentUpload.setStatusCode(workflowStageStatus);
			workflowData.setDocumentUpload(documentUpload);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case VERIFICATION_UNSUCCESSFUL: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VERIFICATION.getWorkflowStageName());

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Verification verification = workflowData.getVerification() == null ? new Verification()
					: workflowData.getVerification();
			verification.setStatus(WorkflowStageStatus.VERIFICATION_UNSUCCESSFUL.getWorkflowStageStatus());
			verification.setStatusCode(null);
			workflowData.setVerification(verification);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName());

			workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			DocumentUpload documentUpload = workflowData.getDocumentUpload() == null ? new DocumentUpload()
					: workflowData.getDocumentUpload();
			documentUpload.setStatusCode(workflowStageStatus);
			workflowData.setDocumentUpload(documentUpload);

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				workflowData.getDocumentUpload()
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_DU_VU_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				workflowData.getDocumentUpload()
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_DU_VU_MSG"), null, null));

			}
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto, serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case INSPECTION_STARTED: {
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			InspectionAssessment inspectionAssessment = workflowData.getInspectionAssessment() == null
					? new InspectionAssessment() : workflowData.getInspectionAssessment();
			List<ServiceTaskEntity> inspectionTaskEntities = serviceTaskRepository
					.findByTaskTypeAndStatus(Constants.SERVICE_TASK_INSPECTION_CHARGE, Constants.ACTIVE);
			if (inspectionTaskEntities != null && !inspectionTaskEntities.isEmpty()) {
				ServiceTaskEntity serviceTaskEntity = inspectionTaskEntities.get(0);
				logger.info("Inspection Charges Configured in the System >>> " + serviceTaskEntity.getCost());
				inspectionAssessment.setInspectionCharge(serviceTaskEntity.getCost());
			}
			workflowData.setInspectionAssessment(inspectionAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.INSPECTION_STARTED.getWorkflowStageName());
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case INSPECTION_COMPLETED: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.INSPECTION_ASSESSMENT.getWorkflowStageName());
			
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			
			if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.WHC_INSPECTION.getRequestType())) {
				completed.setDescription(messageSource.getMessage(String.valueOf("WHC_INSPECTION_INCS_MSG"), null, null));

			}
			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case TECHNICIAN_CANCEL_INSPECTION_FOR_SOME_REASON:

		{
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setServiceCancelReason(serviceRequestUpdateDto.getServiceCancelReason());
			
			if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.WHC_INSPECTION.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("WHC_INSPECTION_CTNA_MSG"), null, null));

			}

			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CANCEL_INSPECTION.getWorkflowStageName());
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
			
		case CUSTOMER_CANCELS_INSPECTION: {
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();
			visit.setServiceCancelReason(serviceRequestUpdateDto.getServiceCancelReason());

			if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.WHC_INSPECTION.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("WHC_INSPECTION_CACC_MSG"), null, null));

			}

			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CANCEL_INSPECTION.getWorkflowStageName());
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;
		case HA_CLAIMS_CLOSE_SERVICE_REQUEST: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatus(Constants.DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE);
			completed.setStatusCode(workflowStageStatus);
			completed.setRemarks(serviceRequestUpdateDto.getRemarks());
			completed.setServiceCancelReason(serviceRequestUpdateDto.getServiceCancelReason());

			String reason = completed.getServiceCancelReason();

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				String desc = messageSource.getMessage(String.valueOf("HA_AD_CO_UNRESOLVED_MSG"), null, null);

				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(desc, "$REASON", reason));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				String desc = messageSource.getMessage(String.valueOf("HA_BD_CO_UNRESOLVED_MSG"), null, null);

				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(desc, "$REASON", reason));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				String desc = messageSource.getMessage(String.valueOf("HA_EW_CO_UNRESOLVED_MSG"), null, null);

				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(desc, "$REASON", reason));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_FR.getRequestType())) {
				String desc = messageSource.getMessage(String.valueOf("HA_FR_CO_UNRESOLVED_MSG"), null, null);

				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(desc, "$REASON", reason));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BR.getRequestType())) {
				String desc = messageSource.getMessage(String.valueOf("HA_BR_CO_UNRESOLVED_MSG"), null, null);

				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(desc, "$REASON", reason));
			}

			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowData(workflowData);

			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case APPLIANCE_PICKED_FOR_REFUND: {

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();

			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatusCode(workflowStageStatus);

			String refundAmount = completed.getRefundAmount();
			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_AD_CO_RF_MSG"), null, null);
				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(description, "$AMOUNT", refundAmount));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_BD_CO_RF_MSG"), null, null);
				if (description != null) {
					completed.setDescription(description);
				}
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_EW_CO_RF_MSG"), null, null);
				if (description != null) {
					completed.setDescription(description);
				}
			}

			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case APPLIANCE_PICKED_FOR_BER: {

			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();

			Completed completed = workflowData.getCompleted() == null ? new Completed() : workflowData.getCompleted();
			completed.setStatusCode(workflowStageStatus);

			String refundAmount = completed.getRefundAmount();
			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_AD_CO_BER_MSG"), null, null);
				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(description, "$AMOUNT", refundAmount));

			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_BD_CO_BER_MSG"), null, null);
				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(description, "$AMOUNT", refundAmount));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				String description = messageSource.getMessage(String.valueOf("HA_EW_CO_BER_MSG"), null, null);
				completed.setDescription(com.oneassist.serviceplatform.commons.utils.StringUtils
						.getAfterReplaceByString(description, "$AMOUNT", refundAmount));
			}

			workflowData.setCompleted(completed);
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.COMPLETED.getWorkflowStageName());
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);
		}
			break;

		case CUSTOMER_CAN_WAIT_FURTHER: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();

			RepairAssessment repairAssessment = workflowData.getRepairAssessment() == null ? new RepairAssessment()
					: workflowData.getRepairAssessment();

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_AD_RA_SE_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_BD_RA_SE_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				repairAssessment
						.setDescription(messageSource.getMessage(String.valueOf("HA_EW_RA_SE_MSG"), null, null));
			}

			workflowData.setRepairAssessment(repairAssessment);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			// What will be stage and status ??
			// serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName());

		}
			break;

		case CANNOT_PERFORM_SERVICE_FOR_SOME_REASON: {
			serviceRequestUpdateDto.setWorkflowStage(WorkflowStage.VISIT.getWorkflowStageName());
			WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData()
					: serviceRequestUpdateDto.getWorkflowData();
			Visit visit = workflowData.getVisit() == null ? new Visit() : workflowData.getVisit();

			if (serviceRequestUpdateDto.getServiceRequestType().equals(ServiceRequestType.HA_AD.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_AD_VE_TCO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_BD.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_BD_VE_TCO_MSG"), null, null));
			}

			else if (serviceRequestUpdateDto.getServiceRequestType()
					.equals(ServiceRequestType.HA_EW.getRequestType())) {
				visit.setDescription(messageSource.getMessage(String.valueOf("HA_EW_VE_TCO_MSG"), null, null));
			}

			workflowData.setVisit(visit);
			serviceRequestUpdateDto.setWorkflowData(workflowData);
			String updatedJsonWorkflowData = populateJsonWithRequestParameters(serviceRequestUpdateDto,
					serviceRequestEntity);
			serviceRequestUpdateDto.setWorkflowJsonString(updatedJsonWorkflowData);

			/* What is here stage and status?? */
		}
			break;
		default:
			logger.info("No valid code found to set json.");
		}

	}

	public ServiceRequestEventCode populateEventCodeBasedOnActionAndRequestType(
			ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestUpdateDto) {
		ServiceRequestEventCode eventCode = null;
		ServiceRequestType serviceRequestType = ServiceRequestType
				.getServiceRequestType(serviceRequestUpdateDto.getServiceRequestType());

		switch (serviceRequestUpdateAction) {
		case ASSIGN:
			switch (serviceRequestType) {
			case WHC_INSPECTION:
				eventCode = ServiceRequestEventCode.ALLOCATE_TECH_INSPECT;
				break;

			default:
				logger.info("This action may not require an Event code for Request type");
			}
			break;

		case RESCHEDULE_SERVICE:
			switch (serviceRequestType) {
			case WHC_INSPECTION:
				eventCode = ServiceRequestEventCode.INSPECTION_SR_RESCHEDULE;
				break;
			case HA_EW:
			case HA_BD:
			case HA_AD:
				switch (serviceRequestUpdateDto.getWorkflowStage()) {
				case "VR": {
					if (serviceRequestType.equals(ServiceRequestType.HA_AD)) {
						eventCode = ServiceRequestEventCode.CUSTOMER_RESCHEDULE_VISIT_POST_DEDUCTIBLE_PAYMENT;
					} else {
						throw new BusinessServiceException(
								ServiceRequestResponseCodes.INVALID_STAGE_FOR_RESCHEDULE_SERVICE.getErrorCode(),
								new InputValidationException());
					}
				}
					break;
				case "VE":
					if (serviceRequestUpdateDto.getAssignee() != null) {
						eventCode = ServiceRequestEventCode.CUSTOMER_RESCHEDULE_VISIT_POST_TECHNICIAN_ALLOCATION;
					} else {
						eventCode = ServiceRequestEventCode.CUSTOMER_RESCHEDULE_VISIT;
					}
					break;
				case "RA":
					eventCode = ServiceRequestEventCode.REPAIR_SERVICE_RESCHEDULED_SPARE_AVAILABLE;
					break;
				default:
					throw new BusinessServiceException(
							ServiceRequestResponseCodes.INVALID_STAGE_FOR_RESCHEDULE_SERVICE.getErrorCode(),
							new InputValidationException());
				}

				break;
			default:
				logger.info("This action may not require an Event code for Request type");
			}
			break;

		default:
			logger.info("This action may not require an Event code");
		}

		return eventCode;
	}

	public List<SystemConfigDto> getShipmentStage(String shipmentPartnerCode, Integer shipmentTypeCode) {
		List<SystemConfigDto> filteredShipmentStages = null;
		List<SystemConfigDto> shipmentStages = systemConfigMasterCache.get(CacheConstants.SHIPMENT_STAGE_PARAMCODE);

		if (!StringUtils.isEmpty(shipmentPartnerCode) && shipmentTypeCode != 0) {
			HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory
					.get(Constants.PARTNER_MASTER_CACHE).getAll();
			PartnerMasterDto logistictPartner = partnerMasterCache.get(shipmentPartnerCode);

			if (logistictPartner != null && logistictPartner.getPartnerName() != null) {
				String shipmentPartner = logistictPartner.getPartnerName();

				if (!StringUtils.isEmpty(shipmentPartner) && shipmentTypeCode != 0
						&& !CollectionUtils.isEmpty(shipmentStages)) {
					filteredShipmentStages = new ArrayList<SystemConfigDto>();
					String inputParamType = shipmentPartner + "_"
							+ ShipmentType.getShipmentType(shipmentTypeCode).toString();

					for (SystemConfigDto shipmentStage : shipmentStages) {
						if (!StringUtils.isEmpty(shipmentStage.getParamType())
								&& !"NA".equalsIgnoreCase(shipmentStage.getParamType())
								&& inputParamType.equalsIgnoreCase(shipmentStage.getParamType())) {
							filteredShipmentStages.add(shipmentStage);
						}
					}

				}
			}
		}

		if (CollectionUtils.isEmpty(filteredShipmentStages)) {
			filteredShipmentStages = getUniqueStages(shipmentStages);
		}

		return filteredShipmentStages;
	}

	public List<SystemConfigDto> getPartnerStatus(String shipmentPartnerCode, Integer shipmentTypeCode) {
		List<SystemConfigDto> filteredShipmentStages = null;
		List<SystemConfigDto> partnerStatus = systemConfigMasterCache.get(CacheConstants.PARTNER_STATUS);

		if (!StringUtils.isEmpty(shipmentPartnerCode) && shipmentTypeCode != 0) {
			HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory
					.get(Constants.PARTNER_MASTER_CACHE).getAll();
			PartnerMasterDto logistictPartner = partnerMasterCache.get(shipmentPartnerCode);

			if (logistictPartner != null && logistictPartner.getPartnerName() != null) {
				String shipmentPartner = logistictPartner.getPartnerName();

				if (!StringUtils.isEmpty(shipmentPartner) && shipmentTypeCode != 0
						&& !CollectionUtils.isEmpty(partnerStatus)) {
					filteredShipmentStages = new ArrayList<SystemConfigDto>();
					String inputParamType = shipmentPartner + "_"
							+ ShipmentType.getShipmentType(shipmentTypeCode).toString();

					for (SystemConfigDto status : partnerStatus) {
						if (!StringUtils.isEmpty(status.getParamType()) && !"NA".equalsIgnoreCase(status.getParamType())
								&& inputParamType.equalsIgnoreCase(status.getParamType())) {
							filteredShipmentStages.add(status);
						}
					}

				}
			}
		}
		return filteredShipmentStages;
	}

	public String getServiceReqeustType(Long serviceRequestTypeId) {

		Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
				.getAll();
		ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
		String requestType = null;
		for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
			serviceRequestTypeMstEntity = entry.getValue();
			if (serviceRequestTypeMstEntity.getServiceRequestTypeId() == serviceRequestTypeId.longValue()) {
				requestType = entry.getKey();
				break;
			}
		}
		return requestType;
	}

	public <T> T convertObject(Object inputObject, Class<T> classType) {
		T convertedObject = null;
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		List<SimpleModule> modules = new ArrayList<SimpleModule>();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		module.addDeserializer(Date.class, new DateFormatDeserializer());
		modules.add(module);
		SimpleModule workflowModule = new SimpleModule();
		workflowModule.addDeserializer(WorkflowData.class, new WorkflowDataFormatDeserializer());

		SimpleModule thirdPartyModule = new SimpleModule();
		thirdPartyModule.addDeserializer(Map.class, new ThirdPartyDataDeserializer());

		SimpleModule pendencyModule = new SimpleModule();
		pendencyModule.addDeserializer(Map.class, new PendencyDeserializer());

		mapper.registerModule(workflowModule);

		mapper.registerModule(module);
		mapper.registerModule(thirdPartyModule);
		mapper.registerModule(pendencyModule);
		convertedObject = mapper.convertValue(inputObject, classType);
		return convertedObject;
	}

	public void validateAuthorizationCode(String authorizationCode, int authorizationCodeSequence,
			ServiceRequestEntity serviceRequestEntity) throws BusinessServiceException, InputValidationException {
		ServiceRequestDto serviceRequestDto = new ServiceRequestDto();
		serviceRequestDto = convertObject(serviceRequestEntity, ServiceRequestDto.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		if (null != serviceRequestDto.getWorkflowData()) {

			String maxAuthFailCountLimit = messageSource.getMessage(MAX_AUTH_FAIL_COUNT_LIMIT, new Object[] { "" },
					null);
			if (authorizationCodeSequence == 1) {

				Visit visit = serviceRequestDto.getWorkflowData().getVisit();

				if (visit == null) {
					throw new InputValidationException("Start OTP not Configured for the given SR ID : "
							+ serviceRequestEntity.getServiceRequestId());
				}

				if (visit.getMaxIncorrectAttempts() >= Integer.valueOf(maxAuthFailCountLimit)) {
					throw new InputValidationException(
							"Max Incorrect Attempts has been Reached. Please contact Administrator ");
				}

				if (!(authorizationCode).equals(visit.getServiceStartCode())) {
					serviceRequestDto.getWorkflowData().getVisit()
							.setMaxIncorrectAttempts(visit.getMaxIncorrectAttempts() + 1);
					try {
						serviceRequestEntity
								.setWorkflowData(objectMapper.writeValueAsString(serviceRequestDto.getWorkflowData()));
					} catch (JsonProcessingException e) {
						throw new BusinessServiceException("Not able to parse service data");
					}

					serviceRequestRepository.save(serviceRequestEntity);

					throw new InputValidationException("Invalid Start OTP Code");

				} else {
					serviceRequestDto.getWorkflowData().getVisit().setMaxIncorrectAttempts(0);

					try {
						serviceRequestEntity
								.setWorkflowData(objectMapper.writeValueAsString(serviceRequestDto.getWorkflowData()));
					} catch (JsonProcessingException e) {
						throw new BusinessServiceException("Not able to parse service data");
					}

					serviceRequestRepository.save(serviceRequestEntity);
				}

			} else if (authorizationCodeSequence == 2) {

				Repair repair = serviceRequestDto.getWorkflowData().getRepair();

				if (repair == null) {
					throw new InputValidationException("End OTP not Configured for the given SR ID : "
							+ serviceRequestEntity.getServiceRequestId());
				}

				if (!(authorizationCode).equals(repair.getServiceEndCode())) {
					throw new InputValidationException("Invalid End OTP Code");

				}
			} else {
				throw new InputValidationException("Invalid Authorization Code Sequence: " + authorizationCodeSequence);
			}

		} else {
			throw new InputValidationException("Invalid SR type : " + serviceRequestEntity.getServiceRequestId());
		}
	}

	public void populateAllStatusesBasedOnEventCode(ServiceRequestDto serviceRequestUpdateDto,
			ServiceRequestEventCode eventCode, ServiceRequestEntity serviceRequestEntity) throws Exception {

		logger.info("   BaseActionCommand >>> populateAllStatusesBasedOnEventCode() >>> " + eventCode);
		final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache
				.get(serviceRequestUpdateDto.getServiceRequestType());
		if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {
			for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {
				if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
					String workflowStage = serviceRequestTransitionConfigEntity.getTransitionToStage();
					String workflowStageStatus = com.oneassist.serviceplatform.commons.utils.StringUtils
							.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());

					final List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusEntities = serviceRequestStageStatusMstCache
							.get(serviceRequestUpdateDto.getServiceRequestType());
					if (serviceRequestStageStatusEntities != null && !serviceRequestStageStatusEntities.isEmpty()) {
						for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusMstEntity : serviceRequestStageStatusEntities) {
							if (workflowStage.equals(serviceRequestStageStatusMstEntity.getStageCode())
									&& workflowStageStatus.equals(
											com.oneassist.serviceplatform.commons.utils.StringUtils.getEmptyIfNull(
													serviceRequestStageStatusMstEntity.getStageStatusCode()))) {
								String partnerStage = serviceRequestStageStatusMstEntity.getServiceRequestStatus();
								String partnerStageStatus = com.oneassist.serviceplatform.commons.utils.StringUtils
										.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusDisplayName());

								WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null
										? new WorkflowData() : serviceRequestUpdateDto.getWorkflowData();
								PartnerStageStatus partnerStageStatusObj = workflowData.getPartnerStageStatus() == null
										? new PartnerStageStatus() : workflowData.getPartnerStageStatus();
								partnerStageStatusObj.setStatus(partnerStageStatus);

								workflowData.setPartnerStageStatus(partnerStageStatusObj);

								ServiceRequestDto serviceRequestUpdateRequestDtoForStatus = new ServiceRequestDto();
								serviceRequestUpdateRequestDtoForStatus.setWorkflowData(workflowData);
								serviceRequestUpdateRequestDtoForStatus
										.setWorkflowStage(WorkflowStage.PARTNER_STAGE_STATUS.getWorkflowStageName());
								serviceRequestUpdateRequestDtoForStatus
										.setWorkflowJsonString(serviceRequestUpdateDto.getWorkflowJsonString());
								serviceRequestUpdateRequestDtoForStatus
										.setServiceRequestId(serviceRequestUpdateDto.getServiceRequestId());

								final String updatedWithPartnerStatus = populateJsonWithRequestParameters(
										serviceRequestUpdateRequestDtoForStatus, serviceRequestEntity);
								serviceRequestUpdateDto.setWorkflowJsonString(updatedWithPartnerStatus);

								serviceRequestUpdateDto.setWorkflowStage(workflowStage);
								serviceRequestUpdateDto.setWorkflowStageStatus(workflowStageStatus);
								serviceRequestUpdateDto.setStatus(partnerStage);
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
		} else {
			throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(),
					new InputValidationException());
		}
	}

	public List<String> filterPincodes(List<String> pincodeList, List<String> stateList, List<String> cityList) {
		List<String> pincodes = null;
		if (CollectionUtils.isEmpty(cityList) && CollectionUtils.isEmpty(stateList)) {
			pincodes = pincodeList;
		} else {
			Collection<PincodeMasterDto> pincodeMsts = pinCodeMasterCache.getAll().values();
			if (!CollectionUtils.isEmpty(pincodeMsts)) {
				pincodes = new ArrayList<String>();
				for (PincodeMasterDto pincodeMst : pincodeMsts) {
					if (!CollectionUtils.isEmpty(cityList) && !CollectionUtils.isEmpty(stateList)) {
						if (cityList.contains(pincodeMst.getCityCode())
								&& stateList.contains(pincodeMst.getStateCode())) {
							pincodes.add(pincodeMst.getPinCode());
						}
					} else if (!CollectionUtils.isEmpty(cityList)) {
						if (cityList.contains(pincodeMst.getCityCode())) {
							pincodes.add(pincodeMst.getPinCode());
						}
					} else if (!CollectionUtils.isEmpty(stateList)) {
						if (stateList.contains(pincodeMst.getStateCode())) {
							pincodes.add(pincodeMst.getPinCode());
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(pincodeList) && !CollectionUtils.isEmpty(pincodes)) {
				pincodes = (List<String>) org.apache.commons.collections.CollectionUtils.intersection(pincodeList,
						pincodes);
			}
			if (pincodes == null) {
				pincodes = new ArrayList<String>();
			}
		}
		return pincodes;
	}

	private String getAllAssetNamesByMembershipId(String referenceNo, String refSecondaryTrackingNo) {

		String assetName = null;
		OASYSCustMemDetails custMembershipDetails = oasysProxy.getMembershipAssetDetails(referenceNo,
				Long.parseLong(refSecondaryTrackingNo), "HA", false);
		if (custMembershipDetails != null) {

			List<OASYSMembershipDetails> memberships = custMembershipDetails.getMemberships();

			if (memberships != null && memberships.size() > 0) {

				assetName = "";
				for (OASYSMembershipDetails oasysMembershipDetails : memberships) {

					List<AssetDetailDto> assets = oasysMembershipDetails.getAssets();

					if (assets != null && assets.size() > 0) {

						for (AssetDetailDto assetDetailDto : assets) {

							if (assetDetailDto.getName() != null) {
								assetName = assetName + assetDetailDto.getName() + ",";
							}
						}
					}
				}
			}

		}
		return assetName;

	}

	public void updateShipmentDescription(ShipmentEntity shipmentEntity) {
		List<ServiceRequestEntity> serviceRequestEntity = null;
		Long PE_ADLD_ID = serviceRequestTypeMasterCache.get(ServiceRequestType.PE_ADLD.getRequestType())
				.getServiceRequestTypeId();
		Long PE_THEFT_ID = serviceRequestTypeMasterCache.get(ServiceRequestType.PE_THEFT.getRequestType())
				.getServiceRequestTypeId();
		Long PE_EW_ID = serviceRequestTypeMasterCache.get(ServiceRequestType.PE_EW.getRequestType())
				.getServiceRequestTypeId();
		List<Long> serviceRequestTypeIDList = new ArrayList<>();
		serviceRequestTypeIDList.add(PE_ADLD_ID.longValue());
		serviceRequestTypeIDList.add(PE_THEFT_ID.longValue());
		serviceRequestTypeIDList.add(PE_EW_ID.longValue());
		serviceRequestEntity = serviceRequestRepository.findByRefPrimaryTrackingNoAndServiceRequestTypeIdIn(
				shipmentEntity.getServiceRequestDetails().getRefPrimaryTrackingNo(), serviceRequestTypeIDList);
		if (serviceRequestEntity.size() != 1) {
			logger.error("Error updating Service Request Dscription for Shipment Id:" + shipmentEntity.getShipmentId());
			return;
		}

		WorkflowData workflowData = getWorkflowDataByServiceRequest(serviceRequestEntity.get(0));
		if (shipmentEntity.getShipmentType().longValue() == serviceRequestTypeMasterCache
				.get(ServiceRequestType.DELIVERY.getRequestType()).getServiceRequestTypeId()) {
			if (serviceRequestEntity.get(0).getWorkflowStage().equals(WorkflowStage.DELIVERY.getWorkflowStageCode())) {
				workflowData.getDelivery()
						.setDescription(messageSource.getMessage(String.valueOf("PE_CUNR_DE_IP_MSG"), null, null));
			}
		}

		else if (shipmentEntity.getShipmentType().longValue() == serviceRequestTypeMasterCache
				.get(ServiceRequestType.PICKUP.getRequestType()).getServiceRequestTypeId()) {

			if (serviceRequestEntity.get(0).getWorkflowStage().equals(WorkflowStage.PICKUP.getWorkflowStageCode())) {
				if (serviceRequestEntity.get(0).getWorkflowStageStatus()
						.equals(WorkflowStageStatus.IN_PROGRESS.getWorkflowStageStatusCode())) {
					workflowData.getVerification()
							.setDescription(messageSource.getMessage(String.valueOf("PE_VR_CO_MSG"), null, null));

					PincodeServiceEntity pincodeServiceEntity = null;
					if (shipmentEntity.getOriginAddressDetails() != null) {
						if (shipmentEntity.getOriginAddressDetails().getPincode() != null) {
							pincodeServiceEntity = pincodeServiceRepository.findPincodeServiceByPincodeAndStatus(
									shipmentEntity.getOriginAddressDetails().getPincode(), Constants.ACTIVE);

						}
					}

					if (pincodeServiceEntity != null
							&& !pincodeServiceEntity.getPincodeCategory().equals(PINCODE_CATEGORY_D)) {
						workflowData.getPickup()
								.setDescription(messageSource.getMessage(String.valueOf("PE_PU_IP_MSG"), null, null));
						workflowData.getPickup().setPincodeCategory(pincodeServiceEntity.getPincodeCategory());

					} else if (pincodeServiceEntity != null
							&& pincodeServiceEntity.getPincodeCategory().equals(PINCODE_CATEGORY_D)) {
						workflowData.getPickup().setDescription(
								messageSource.getMessage(String.valueOf("PE_PU_IP_ALT_MSG"), null, null));
						workflowData.getPickup().setPincodeCategory(pincodeServiceEntity.getPincodeCategory());
					}
				}
			}
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serviceRequestEntity.get(0).setWorkflowData(objectMapper.writeValueAsString(workflowData));
		} catch (Exception ex) {
			logger.error("Error updating PE shipment description: " + shipmentEntity.getShipmentId());
		}
		serviceRequestRepository.save(serviceRequestEntity);
	}

	public ServiceRequestTypeMstEntity getServiceRequestTypeById(Long id) {

		List<ServiceRequestTypeMstEntity> serviceRequestTypeList = new ArrayList<>(
				serviceRequestTypeMasterCache.getAll().values());
		for (ServiceRequestTypeMstEntity mstEntity : serviceRequestTypeList) {
			if (mstEntity.getServiceRequestTypeId().longValue() == id.longValue()) {
				return mstEntity;
			}
		}

		return null;
	}

	public WorkflowData getWorkflowDataByServiceRequest(ServiceRequestEntity serviceRequestEntity)
			throws BusinessServiceException {
		try {
			ServiceRequestDto serviceRequestDto = convertObject(serviceRequestEntity, ServiceRequestDto.class);
			return serviceRequestDto.getWorkflowData();
		} catch (Exception e) {
			throw new BusinessServiceException("Error in parsing workflowdata");
		}
	}

	public void updateShipmentDescriptionForAWB(Long shipmentId) {
		ShipmentEntity shipmentEntity = shipmentRepository.findOne(shipmentId);
		ServiceRequestType serviceRequestType = ServiceRequestType.getServiceRequestType(
				getServiceRequestTypeById(shipmentEntity.getServiceRequestDetails().getServiceRequestTypeId())
						.getServiceRequestType());
		if (serviceRequestType.equals(ServiceRequestType.PICKUP)
				|| serviceRequestType.equals(ServiceRequestType.DELIVERY)) {
			updateShipmentDescription(shipmentEntity);

		}
	}
}
