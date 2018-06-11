package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.utils.DecimalFormatter;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Diagnosis;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.LabourCost;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SpareParts;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Transport;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.externalcontracts.CustMemView;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;

@Component
public class HomeAccidentalDamageServiceRequestHandler extends BaseWHCServiceTypeHandler {

	private final Logger logger = Logger.getLogger(HomeAccidentalDamageServiceRequestHandler.class);

	public static final String COSTTOCOMPANY_PERCENTLIMIT_ON_INSURANCE_FOR_ACCIDENTAL_DAMAGE = "CTCPercentageLimitOnInsuranceForAccidentalDamage";

	@SuppressWarnings("rawtypes")
	@Autowired
	private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

	@Autowired
	private OasysProxy oasysProxy;

	public HomeAccidentalDamageServiceRequestHandler() {
		super(ServiceRequestType.HA_AD);
	}

	@Override
	public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
		super.validateOnServiceRequestCreate(serviceRequestDto);

		if (!errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}
		if (StringUtils.isNotBlank(serviceRequestDto.getRefPrimaryTrackingNo())) {
			validatePrimaryRefNumAlreadyExists(serviceRequestDto.getRefPrimaryTrackingNo());
		}
		validateServiceRequestEligibility(serviceRequestDto);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto,
			ServiceRequestEntity serviceRequestEntity, String serviceRequestType)
			throws BusinessServiceException, JsonProcessingException {

		String workflowProcessId = null;
		String isPincodeServicable = Constants.NO_FLAG;
		ObjectMapper mapper = new ObjectMapper();

		List<PincodeServiceFulfilmentEntity> pincodeFulfilments = validatePincodeServicibility(
				serviceRequestDto.getServiceRequestAddressDetails().getPincode(),
				serviceRequestEntity.getServiceRequestTypeId());

		// Service Centre found for the given Pincode
		if (pincodeFulfilments != null && !pincodeFulfilments.isEmpty()) {
			PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
			serviceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
			long partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null)
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

			/*
			 * Date serviceRequestDueDate =
			 * DateUtils.addDays(serviceRequestDto.getScheduleSlotStartDateTime(
			 * ), 10); serviceRequestEntity.setDueDateTime(setEndWorkingHour(
			 * serviceRequestDueDate));
			 */
		} else {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED_DUE_TO_NONSERVICEABLE_PINCODE
							.getErrorCode(),
					new Object[] { serviceRequestEntity.getServiceRequestId() });
		}

		// Populate Service Address Details
		ServiceAddressEntity addressEntity = new ServiceAddressEntity();
		populateServiceAddressEntity(addressEntity, serviceRequestDto);

		serviceAddressRepository.save(addressEntity);

		populateWorkFlowData(addressEntity, serviceRequestEntity, serviceRequestDto, isPincodeServicable);
		Map<String, Object> activitiVariablesMap = getActivitiVariables();
		Map<String, String> responseMap = workflowManager
				.startActivitiProcess(this.serviceRequestType.getRequestTypeActivitiKey(), activitiVariablesMap);
		workflowProcessId = responseMap.get(Constants.WORKFLOW_PROCESS_ID);

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

		communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto,
				CommunicationGatewayEventCode.SP_FR_BR_CREATE_SR, null);

		return serviceRequestDto;
	}

	@Override
	public void validateOnCostToCustomerCalculate(Long serviceRequestId,
			AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws BusinessServiceException {
		super.validateOnCostToCustomerCalculate(serviceRequestId, assigneeRepairCostRequestDto);

		if (!errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		double sumOfTaskCost = 0;

		RepairAssessment repairAssessment = serviceRequestDto.getWorkflowData().getRepairAssessment();
		if (repairAssessment != null) {
			List<Diagnosis> diagnosisList = repairAssessment.getDiagonosisReportedbyAssignee();
			List<SpareParts> sparesList = repairAssessment.getSparePartsRequired();

			if (diagnosisList != null && !diagnosisList.isEmpty()) {
				for (Diagnosis diagnosis : diagnosisList) {
					ServiceTaskDto serviceTaskDto = serviceTaskMasterCache.get(diagnosis.getDiagnosisId());
					sumOfTaskCost = sumOfTaskCost + serviceTaskDto.getCost();
				}
			}

			if (sparesList != null && !sparesList.isEmpty()) {
				for (SpareParts sparepart : sparesList) {
					ServiceTaskDto serviceTaskDto = serviceTaskMasterCache.get(sparepart.getSparePartId());
					sumOfTaskCost = sumOfTaskCost + serviceTaskDto.getCost();
				}
			}

			LabourCost labourCost = repairAssessment.getLabourCost();
			Transport transport = repairAssessment.getTransport();

			if (labourCost != null && !Strings.isNullOrEmpty(labourCost.getCost())) {
				sumOfTaskCost = sumOfTaskCost + Double.valueOf(labourCost.getCost());
			}

			if (transport != null && !Strings.isNullOrEmpty(transport.getCost())) {
				sumOfTaskCost = sumOfTaskCost + Double.valueOf(transport.getCost());
			}

		}

		costToServiceDto.setCostToCompany(sumOfTaskCost);
		costToServiceDto.setCostToCustomer(0);
		boolean estimateRequestApprovedStatus = true;

		List<String> workflowStageStatusList = new ArrayList<String>();
		workflowStageStatusList.add(WorkflowStageStatus.SUCCESSFUL.getWorkflowStageStatus());
		workflowStageStatusList.add(WorkflowStageStatus.BER.getWorkflowStageStatus());
		workflowStageStatusList.add(WorkflowStageStatus.REFUND.getWorkflowStageStatus());

		Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache
				.getAll();
		List serviceRequestTypeList = new ArrayList<Long>();
		serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_BD.getRequestType())
				.getServiceRequestTypeId());
		serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_AD.getRequestType())
				.getServiceRequestTypeId());
		serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_BR.getRequestType())
				.getServiceRequestTypeId());
		serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_FR.getRequestType())
				.getServiceRequestTypeId());

		List<ServiceRequestEntity> serviceRequestEntityList = serviceRequestRepository
				.findByReferenceNoAndWorkflowStageAndWorkflowStageStatusInAndServiceRequestTypeIdIn(
						serviceRequestEntity.getReferenceNo(), ServiceRequestStatus.COMPLETED.getStatus(),
						workflowStageStatusList, serviceRequestTypeList);

		double sumOfCostToCompanyForAllSRRaised = 0;
		double remainingCoverAmountForMemberShip = 0;
		double totalCoverAmountForMemberShip = 0;

		ServiceRequestDto serviceResDto = new ServiceRequestDto();
		for (ServiceRequestEntity serviceReqEntity : serviceRequestEntityList) {
			serviceResDto = serviceRequestHelper.convertObject(serviceReqEntity, ServiceRequestDto.class);
			if (serviceResDto.getWorkflowData() != null && serviceResDto.getWorkflowData().getRepairAssessment() != null
					&& serviceResDto.getWorkflowData().getRepairAssessment().getCostToCompany() != null) {
				sumOfCostToCompanyForAllSRRaised += Double
						.valueOf(serviceResDto.getWorkflowData().getRepairAssessment().getCostToCompany());
			}
		}

		CustMemView custMemView = oasysProxy.getCustomerMembershipDetail(serviceRequestEntity.getReferenceNo());
		if (custMemView != null)
			totalCoverAmountForMemberShip = custMemView.getMaxInsuranceValue();

		remainingCoverAmountForMemberShip = totalCoverAmountForMemberShip - sumOfCostToCompanyForAllSRRaised;
		String ctcPercentLimitForBreakdown = messageSource
				.getMessage(COSTTOCOMPANY_PERCENTLIMIT_ON_INSURANCE_FOR_ACCIDENTAL_DAMAGE, new Object[] { "" }, null);
		double balanceCostToCusotmer = 0.0;

		if (Strings.isNullOrEmpty(existingCostToCompany)
				|| (Double.valueOf(existingCostToCompany).doubleValue() != costToServiceDto.getCostToCompany())) {
			if (costToServiceDto.getCostToCompany() <= remainingCoverAmountForMemberShip
					&& (costToServiceDto.getCostToCompany() <= ((totalCoverAmountForMemberShip
							* Double.valueOf(ctcPercentLimitForBreakdown)) / 100))) {
				estimateRequestApprovedStatus = true; // Not go for IC decision
														// & IC already approved
			} else if (costToServiceDto.getCostToCompany() <= remainingCoverAmountForMemberShip && (costToServiceDto
					.getCostToCompany() > ((totalCoverAmountForMemberShip * Double.valueOf(ctcPercentLimitForBreakdown))
							/ 100))) {
				estimateRequestApprovedStatus = false; // IC approval needed
			} else if (costToServiceDto.getCostToCompany() > remainingCoverAmountForMemberShip) {
				balanceCostToCusotmer = costToServiceDto.getCostToCompany() - remainingCoverAmountForMemberShip;
				costToServiceDto.setCostToCustomer(costToServiceDto.getCostToCustomer() + balanceCostToCusotmer);
				costToServiceDto.setCostToCompany(remainingCoverAmountForMemberShip);

				existingCostToCompany = DecimalFormatter.getEmptyDecimalIfNull(existingCostToCompany);
				if ((Double.valueOf(existingCostToCompany).doubleValue() != costToServiceDto.getCostToCompany())
						&& costToServiceDto.getCostToCompany() > ((totalCoverAmountForMemberShip
								* Double.valueOf(ctcPercentLimitForBreakdown)) / 100)) {
					estimateRequestApprovedStatus = false; // IC approval needed
				}
			}
		}
		costToServiceDto.setEstimateRequestApprovedStatus(estimateRequestApprovedStatus);
		return costToServiceDto;
	}

	@Override
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

		super.validateOnServiceRequestUpdates(serviceRequestDto);

		if (errorInfoDtoList != null && !errorInfoDtoList.isEmpty()) {
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList,
					new InputValidationException());
		}

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

			if (pincodeFulfilments != null && !pincodeFulfilments.isEmpty()) {
				PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
				newServiceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
				long partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null)
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

		populateWorkFlowData(addressEntity, newServiceRequestEntity, serviceRequestDto, isPincodeServicable);
		WorkflowData oldWorkFlowData = serviceRequestHelper
				.getWorkflowDataByServiceRequest(existingServiceRequestEntity);
		WorkflowData newWorkFlowData = serviceRequestHelper.getWorkflowDataByServiceRequest(newServiceRequestEntity);
		newWorkFlowData.getVisit().setServiceStartCode(oldWorkFlowData.getVisit().getServiceStartCode());
		newWorkFlowData.getRepair().setServiceEndCode(oldWorkFlowData.getRepair().getServiceEndCode());
		newWorkFlowData.getVisit().setPlaceOfIncident(oldWorkFlowData.getVisit().getPlaceOfIncident());

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
