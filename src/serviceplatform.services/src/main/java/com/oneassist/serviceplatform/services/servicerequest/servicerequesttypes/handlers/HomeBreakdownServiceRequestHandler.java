package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.enums.WorkflowStageStatus;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.utils.DecimalFormatter;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Diagnosis;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Issues;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HomeBreakdownServiceRequestHandler extends BaseWHCServiceTypeHandler {

    private final Logger logger = Logger.getLogger(HomeBreakdownServiceRequestHandler.class);

    public static final String COSTTOCOMPANY_PERCENTLIMIT_ON_INSURANCE_FOR_BREAKDOWN = "CTCPercentageLimitOnInsuranceForBreakdown";

    @Autowired
    private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

    @Autowired
    private OasysProxy oasysProxy;

    public HomeBreakdownServiceRequestHandler() {

        super(ServiceRequestType.HA_BD);
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        super.validateOnServiceRequestCreate(serviceRequestDto);

        if (serviceRequestDto.getScheduleSlotStartDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoDtoList);
        }

        if (serviceRequestDto.getScheduleSlotEndDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoDtoList);
        }

        String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);
        serviceRequestValidator.validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime", serviceRequestDto.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit),
                errorInfoDtoList);

        if (serviceRequestDto.getIssueReportedByCustomer() == null || serviceRequestDto.getIssueReportedByCustomer().isEmpty()) {

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

        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(serviceRequestDto.getRefPrimaryTrackingNo())) {
            validatePrimaryRefNumAlreadyExists(serviceRequestDto.getRefPrimaryTrackingNo());
        }
        validateServiceRequestEligibility(serviceRequestDto);
    }
    
    @Override
    public void validateOnServiceRequestUpdates(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

    	 super.validateOnServiceRequestUpdates(serviceRequestDto);
    	 
        if (serviceRequestDto.getScheduleSlotStartDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoDtoList);
        }

        if (serviceRequestDto.getScheduleSlotEndDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoDtoList);
        }

        String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);
        serviceRequestValidator.validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime", serviceRequestDto.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit),
                errorInfoDtoList);

        if (serviceRequestDto.getIssueReportedByCustomer() == null || serviceRequestDto.getIssueReportedByCustomer().isEmpty()) {

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

        if (errorInfoDtoList!=null && !errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }

    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String serviceRequestType) throws BusinessServiceException,
            JsonProcessingException {

        String workflowProcessId = null;
        String isPincodeServicable = Constants.NO_FLAG;
        ObjectMapper mapper = new ObjectMapper();

        List<PincodeServiceFulfilmentEntity> pincodeFulfilments = validatePincodeServicibility(serviceRequestDto.getServiceRequestAddressDetails().getPincode(),
                serviceRequestEntity.getServiceRequestTypeId());
        long partnerBuCode = 0;
        // Service Centre found for the given Pincode
        if (CollectionUtils.isNotEmpty(pincodeFulfilments)) {
            PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
            serviceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
            partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null) ? Long.valueOf(pincodeServiceFulfilmentEntity.getPartnerBUCode()) : null;
            serviceRequestEntity.setServicePartnerBuCode(partnerBuCode);
            isPincodeServicable = Constants.YES_FLAG;

            Date serviceRequestDueDate = DateUtils.addDays(serviceRequestDto.getScheduleSlotStartDateTime(), 10);
            serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED_DUE_TO_NONSERVICEABLE_PINCODE.getErrorCode(),
                    new Object[] { serviceRequestEntity.getServiceRequestId() });
        }

        // Populate Service Address Details
        ServiceAddressEntity addressEntity = new ServiceAddressEntity();
        populateServiceAddressEntity(addressEntity, serviceRequestDto);

        serviceAddressRepository.save(addressEntity);

        populateWorkFlowData(addressEntity, serviceRequestEntity, serviceRequestDto, isPincodeServicable);
        Map<String, Object> activitiVariablesMap = getActivitiVariables();
        Map<String, String> responseMap = workflowManager.startActivitiProcess(this.serviceRequestType.getRequestTypeActivitiKey(), activitiVariablesMap);
        workflowProcessId = responseMap.get(Constants.WORKFLOW_PROCESS_ID);

        serviceRequestEntity.setCreatedOn(new Date());
        serviceRequestEntity.setWorkflowProcessId(workflowProcessId);
        serviceRequestEntity.setWorkflowProcDefKey(responseMap.get(Constants.WORKFLOW_PROC_DEF_KEY));
        serviceRequestEntity.setThirdPartyProperties(mapper.writeValueAsString(serviceRequestDto.getThirdPartyProperties()));

        serviceRequestRepository.save(serviceRequestEntity);
        saveAssetsforSR(serviceRequestDto.getRefSecondaryTrackingNo(), serviceRequestEntity.getServiceRequestId());

        serviceRequestDto.setWorkflowStage(serviceRequestEntity.getWorkflowStage());
		serviceRequestDto.setWorkflowStageStatus(serviceRequestEntity.getWorkflowStageStatus());
        serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
        serviceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());
        workflowManager.setVariable(workflowProcessId, "IS_PIN_CODE_SERVICEABLE", isPincodeServicable);
        workflowManager.setVariable(workflowProcessId, Constants.SERVICE_REQUEST_ID_LOWER, String.valueOf(serviceRequestEntity.getServiceRequestId()));

        communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto, CommunicationGatewayEventCode.SP_BD_CREATE_SR_FOR_VISIT, null);

        Map<String, Object> additionalAttributes = new HashMap<>();
        additionalAttributes.put(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue(), partnerBuCode);
        communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestDto, CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_REPAIR_ASSIGN, additionalAttributes);

        return serviceRequestDto;
    }

    @Override
    public void validateOnCostToCustomerCalculate(Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws BusinessServiceException {
        super.validateOnCostToCustomerCalculate(serviceRequestId, assigneeRepairCostRequestDto);
        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected CostToServiceDto calcualteCostToCustomer(Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto, ServiceRequestEntity serviceRequestEntity)
            throws BusinessServiceException {
        // TODO Auto-generated method stub
        CostToServiceDto costToServiceDto = new CostToServiceDto();
        ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
        // CTC calculated -> IC Appoved -> Calculate CTC - Needed old Cost to Company to auto approve if both are same
        String existingCostToCompany = null;
        if (serviceRequestDto.getWorkflowData() != null && serviceRequestDto.getWorkflowData().getRepairAssessment() != null) {
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
        if (repairAssessment.getAccidentalDamage() != null && repairAssessment.getAccidentalDamage().trim().equalsIgnoreCase(Constants.YES_FLAG)) {
            costToServiceDto.setCostToCustomer(sumOfInsuredTaskCost + sumOfNonInsuredTaskCost);
        } else if (repairAssessment.getAccidentalDamage() != null && repairAssessment.getAccidentalDamage().trim().equalsIgnoreCase(Constants.NO_FLAG)) {
            costToServiceDto.setCostToCustomer(sumOfNonInsuredTaskCost);
            costToServiceDto.setCostToCompany(sumOfInsuredTaskCost);
        }

        List<String> workflowStageStatusList = new ArrayList<String>();
        workflowStageStatusList.add(WorkflowStageStatus.SUCCESSFUL.getWorkflowStageStatus());
        workflowStageStatusList.add(WorkflowStageStatus.BER.getWorkflowStageStatus());
        workflowStageStatusList.add(WorkflowStageStatus.REFUND.getWorkflowStageStatus());

        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();
        List serviceRequestTypeList = new ArrayList<Long>();
        serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_BD.getRequestType()).getServiceRequestTypeId());
        serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_AD.getRequestType()).getServiceRequestTypeId());
        serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_BR.getRequestType()).getServiceRequestTypeId());
        serviceRequestTypeList.add(serviceRequestTypeMstEntityMap.get(ServiceRequestType.HA_FR.getRequestType()).getServiceRequestTypeId());

        List<ServiceRequestEntity> serviceRequestEntityList = serviceRequestRepository.findByReferenceNoAndWorkflowStageAndWorkflowStageStatusInAndServiceRequestTypeIdIn(
                serviceRequestEntity.getReferenceNo(), ServiceRequestStatus.COMPLETED.getStatus(), workflowStageStatusList, serviceRequestTypeList);

        double sumOfCostToCompanyForAllSRRaised = 0;
        double remainingCoverAmountForMemberShip = 0;
        double totalCoverAmountForMemberShip = 0;

        ServiceRequestDto serviceResDto = new ServiceRequestDto();
        for (ServiceRequestEntity serviceReqEntity : serviceRequestEntityList) {
            serviceResDto = serviceRequestHelper.convertObject(serviceReqEntity, ServiceRequestDto.class);
            if (serviceResDto.getWorkflowData() != null && serviceResDto.getWorkflowData().getRepairAssessment() != null
                    && serviceResDto.getWorkflowData().getRepairAssessment().getCostToCompany() != null) {
                sumOfCostToCompanyForAllSRRaised += Double.valueOf(serviceResDto.getWorkflowData().getRepairAssessment().getCostToCompany());
            }
        }

        CustMemView custMemView = oasysProxy.getCustomerMembershipDetail(serviceRequestEntity.getReferenceNo());
        if (custMemView != null)
            totalCoverAmountForMemberShip = custMemView.getMaxInsuranceValue();

        remainingCoverAmountForMemberShip = totalCoverAmountForMemberShip - sumOfCostToCompanyForAllSRRaised;
        String ctcPercentLimitForBreakdown = messageSource.getMessage(COSTTOCOMPANY_PERCENTLIMIT_ON_INSURANCE_FOR_BREAKDOWN, new Object[] { "" }, null);
        double balanceCostToCusotmer = 0.0;

        if (Strings.isNullOrEmpty(existingCostToCompany) || (Double.valueOf(existingCostToCompany).doubleValue() != costToServiceDto.getCostToCompany())) {
            if (costToServiceDto.getCostToCompany() <= remainingCoverAmountForMemberShip
                    && (costToServiceDto.getCostToCompany() <= ((totalCoverAmountForMemberShip * Double.valueOf(ctcPercentLimitForBreakdown)) / 100))) {
                estimateRequestApprovedStatus = true; // Not go for IC decision & IC already approved
            } else if (costToServiceDto.getCostToCompany() <= remainingCoverAmountForMemberShip
                    && (costToServiceDto.getCostToCompany() > ((totalCoverAmountForMemberShip * Double.valueOf(ctcPercentLimitForBreakdown)) / 100))) {
                estimateRequestApprovedStatus = false; // IC approval needed
            } else if (costToServiceDto.getCostToCompany() > remainingCoverAmountForMemberShip) {
                balanceCostToCusotmer = costToServiceDto.getCostToCompany() - remainingCoverAmountForMemberShip;
                costToServiceDto.setCostToCustomer(costToServiceDto.getCostToCustomer() + balanceCostToCusotmer);
                costToServiceDto.setCostToCompany(remainingCoverAmountForMemberShip);
                existingCostToCompany = DecimalFormatter.getEmptyDecimalIfNull(existingCostToCompany);
                if ((Double.valueOf(existingCostToCompany).doubleValue() != costToServiceDto.getCostToCompany())
                        && costToServiceDto.getCostToCompany() > ((totalCoverAmountForMemberShip * Double.valueOf(ctcPercentLimitForBreakdown)) / 100)) {
                    estimateRequestApprovedStatus = false; // IC approval needed
                }
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
			validatePrimaryRefNumAlreadyExists(serviceRequestDto.getRefPrimaryTrackingNo());
			existingServiceRequestEntity.setRefPrimaryTrackingNo(serviceRequestDto.getRefPrimaryTrackingNo());
			existingServiceRequestEntity.setModifiedBy(serviceRequestDto.getModifiedBy());
			serviceRequestRepository.save(existingServiceRequestEntity);
			return serviceRequestDto;
		}

		validateOnServiceRequestUpdates(serviceRequestDto);

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
			// Service Centre found for the given Pincode
			if (CollectionUtils.isNotEmpty(pincodeFulfilments)) {
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

		}else{
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
