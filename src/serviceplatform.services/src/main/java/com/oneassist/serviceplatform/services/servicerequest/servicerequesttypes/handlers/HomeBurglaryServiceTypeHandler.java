package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.ArrayList;
import java.util.Date;
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
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HomeBurglaryServiceTypeHandler extends BaseWHCServiceTypeHandler {

    private final Logger logger = Logger.getLogger(HomeBurglaryServiceTypeHandler.class);

    public HomeBurglaryServiceTypeHandler() {
        super(ServiceRequestType.HA_BR);
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        super.validateOnServiceRequestCreate(serviceRequestDto);
        
        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }
        
        if (org.apache.commons.lang3.StringUtils.isNotBlank(serviceRequestDto.getRefPrimaryTrackingNo())) {
            validatePrimaryRefNumAlreadyExists(serviceRequestDto.getRefPrimaryTrackingNo());
        }
        
        if (Strings.isNullOrEmpty(serviceRequestDto.getRequestDescription())
				&& serviceRequestDto.getInitiatingSystem() == null
				&& serviceRequestDto.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()) {
			inputValidator.populateMandatoryFieldError("requestDescription", errorInfoDtoList);
		}
        
        validateServiceRequestEligibility(serviceRequestDto);
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String serviceRequestType) throws BusinessServiceException,
            JsonProcessingException {
        String isPincodeServicable = Constants.NO_FLAG;

        ObjectMapper mapper = new ObjectMapper();

        List<PincodeServiceFulfilmentEntity> pincodeFulfilments = validatePincodeServicibility(serviceRequestDto.getServiceRequestAddressDetails().getPincode(),
                serviceRequestEntity.getServiceRequestTypeId());

        // Service Centre found for the given Pincode
        if (CollectionUtils.isNotEmpty(pincodeFulfilments)) {
            isPincodeServicable = Constants.YES_FLAG;
            if(serviceRequestDto.getScheduleSlotStartDateTime()!=null){
	            Date serviceRequestDueDate = DateUtils.addDays(serviceRequestDto.getScheduleSlotStartDateTime(), 10);
	            serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
            }
            else{
            	Date serviceRequestDueDate = DateUtils.addDays(new Date(), 10);
            	serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
            }
            serviceRequestEntity.setServicePartnerCode(-1L);
            serviceRequestEntity.setServicePartnerBuCode(-1L);
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
        Map<String, String> responseMap = workflowManager.startActivitiProcess(ServiceRequestType.HA_BR.getRequestTypeActivitiKey(), activitiVariablesMap);
        String workflowProcessId = responseMap.get(Constants.WORKFLOW_PROCESS_ID);

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

        communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto, CommunicationGatewayEventCode.SP_FR_BR_CREATE_SR, null);

        return serviceRequestDto;
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

			if (CollectionUtils.isNotEmpty(pincodeFulfilments)) {
				isPincodeServicable = Constants.YES_FLAG;
				newServiceRequestEntity.setServicePartnerCode(-1L);
				newServiceRequestEntity.setServicePartnerBuCode(-1L);
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
