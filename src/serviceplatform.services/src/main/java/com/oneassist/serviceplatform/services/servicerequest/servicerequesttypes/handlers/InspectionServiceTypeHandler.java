package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.oneassist.communicationgateway.enums.CommunicationGatewayEventCode;
import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commands.dtos.CommandInput;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.CommunicationConstants;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.Recipient;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.utils.AuthCodeGenerator;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InspectionAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PartnerStageStatus;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.SoftApproval;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.actioncommands.ServiceRequestActionCommandFactory;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectionServiceTypeHandler extends BaseServiceTypeHandler {

    private final Logger logger = Logger.getLogger(InspectionServiceTypeHandler.class);

    @Autowired
    private ServiceRequestActionCommandFactory serviceRequestActionCommandFactory;

    public InspectionServiceTypeHandler() {

        super(ServiceRequestType.WHC_INSPECTION);
    }

    @Override
    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {

        super.validateOnServiceRequestCreate(serviceRequestDto);

        if (Strings.isNullOrEmpty(serviceRequestDto.getRefPrimaryTrackingNo())) {
            inputValidator.populateMandatoryFieldError("refPrimaryTrackingNo", errorInfoDtoList);
        }

        if (serviceRequestDto.getScheduleSlotStartDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotStartDateTime", errorInfoDtoList);
        }

        if (serviceRequestDto.getScheduleSlotEndDateTime() == null) {
            inputValidator.populateMandatoryFieldError("scheduleSlotEndDateTime", errorInfoDtoList);
        }

        doValidateServiceAddress(serviceRequestDto);
        if (!errorInfoDtoList.isEmpty()) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList);
        }

        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();

        validateDuplicateOrderId(serviceRequestDto.getRefPrimaryTrackingNo(), serviceRequestTypeMasterList.get(serviceRequestDto.getServiceRequestType()).getServiceRequestTypeId());
    }

    @Override
    public ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String requestType) throws BusinessServiceException,
            JsonProcessingException {

        List<PincodeServiceFulfilmentEntity> pincodeFulfilments = fulfilmentRepository.findServiceCentreByPincodeAndServiceRequestTypeIdAndStatus(serviceRequestDto.getServiceRequestAddressDetails()
                .getPincode(), serviceRequestEntity.getServiceRequestTypeId(), Constants.ACTIVE);

        String isPincodeServicable = Constants.NO_FLAG;
        ObjectMapper mapper = new ObjectMapper();
        long partnerBuCode = 0;
        // Service centre found for the given pincode
        if (pincodeFulfilments != null && !pincodeFulfilments.isEmpty()) {
            PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = pincodeFulfilments.get(0);
            serviceRequestEntity.setServicePartnerCode(pincodeServiceFulfilmentEntity.getPartnerCode());
            partnerBuCode = (pincodeServiceFulfilmentEntity.getPartnerBUCode() != null) ? Long.valueOf(pincodeServiceFulfilmentEntity.getPartnerBUCode()) : null;
            serviceRequestEntity.setServicePartnerBuCode(partnerBuCode);
            isPincodeServicable = Constants.YES_FLAG;

            Date serviceRequestDueDate = DateUtils.addDays(new Date(), 30);
            serviceRequestEntity.setDueDateTime(setEndWorkingHour(serviceRequestDueDate));
        }
		else {
			throw new BusinessServiceException(
					ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED_DUE_TO_NONSERVICEABLE_PINCODE
							.getErrorCode(),
					new Object[] { serviceRequestEntity.getServiceRequestId() });
		}

        ServiceAddressEntity addressEntity = new ServiceAddressEntity();
        addressEntity.setModifiedOn(new Date());
        addressEntity.setModifiedBy(serviceRequestDto.getCreatedBy());

        populateServiceAddressEntity(addressEntity, serviceRequestDto);

        serviceRequestEntity.setRefPrimaryTrackingNo(serviceRequestDto.getRefPrimaryTrackingNo());

        serviceAddressRepository.save(addressEntity);

        populateInspectionWorkFlowData(addressEntity, serviceRequestEntity, isPincodeServicable);
        serviceRequestEntity.setCreatedOn(new Date());
        serviceRequestEntity.setModifiedOn(new Date());
        serviceRequestEntity.setModifiedBy(serviceRequestDto.getCreatedBy());
        serviceRequestEntity.setThirdPartyProperties(mapper.writeValueAsString(serviceRequestDto.getThirdPartyProperties()));
        serviceRequestEntity = serviceRequestRepository.save(serviceRequestEntity);

        serviceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());

        communicationGatewayProxy.sendCommunication(Recipient.CUSTOMER, serviceRequestDto, CommunicationGatewayEventCode.WHC_INSPECTION_SUCCESSFULLY_SCHEDULED, null);

        Map<String, Object> additionalAttributes = new HashMap<>();
        additionalAttributes.put(CommunicationConstants.COMM_PARTNER_BU_CODE.getValue(), partnerBuCode);
        communicationGatewayProxy.sendCommunication(Recipient.SERVICEPARTNER, serviceRequestDto, CommunicationGatewayEventCode.WHC_SC_NOTIFICATION_INSPECTION_ASSIGN, additionalAttributes);

        return serviceRequestDto;
    }

    @Override
    public CommandResult<ServiceResponseDto> updateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
            ServiceRequestEntity serviceRequestEntity) throws Exception {

        // TODO: Step 1: Validation if serviceRequestUpdateAction is applicable for InspectionServiceTypeHandler
        // TODO: Step 2: Validate the events applicable for this handler
        // TODO: Step 3: Validate the list of stages applicable for this handler

        // Based on ServiceRequestUpdateAction it will call different action command
        BaseActionCommand<ServiceRequestDto, ServiceResponseDto> a = serviceRequestActionCommandFactory.getServiceRequestActionCommand(serviceRequestUpdateAction);

        CommandInput<ServiceRequestDto> ci = new CommandInput<>();
        ci.setData(serviceRequestDto);
        ci.setCommandEventCode(eventCode);
        ci.setServiceRequestEntity(serviceRequestEntity);
        return a.execute(ci);
    }

    private void validateDuplicateOrderId(String orderId, Long serviceRequestTypeId) throws BusinessServiceException {

        List<ServiceRequestEntity> serviceRequestEntity = serviceRequestRepository.findByRefPrimaryTrackingNoAndServiceRequestTypeId(orderId, serviceRequestTypeId);

        if (serviceRequestEntity != null && !serviceRequestEntity.isEmpty()) {
            throw new BusinessServiceException(ServiceRequestResponseCodes.CREATE_INSPECTION_REQUEST_ORDERID_ALREADY_EXISTS.getErrorCode(), new Object[] { orderId });
        }
    }

    private WorkflowData populateInspectionWorkFlowData(ServiceAddressEntity addressEntity, ServiceRequestEntity serviceRequestEntity, String isPincodeServicable) throws JsonProcessingException {

        Visit visit = new Visit();
        visit.setServiceAddress(String.valueOf(addressEntity.getServiceAddressId()));
        visit.setStatus(Constants.ACTIVE);
        visit.setIsSelfService(isPincodeServicable.equals(Constants.YES_FLAG) ? Constants.NO_FLAG : Constants.YES_FLAG);
        visit.setServiceStartCode(AuthCodeGenerator.generateFourDigitAuthPin());

        Repair repair = new Repair();
        repair.setServiceEndCode(AuthCodeGenerator.generateFourDigitAuthPin());

        ClaimSettlement claimSettlement = new ClaimSettlement();
        DocumentUpload documentUpload = new DocumentUpload();
        RepairAssessment repairAssessment = new RepairAssessment();
        SoftApproval softApproval = new SoftApproval();
        Verification verification = new Verification();
        ICDoc icDoc = new ICDoc();
        InspectionAssessment inspectionAssessment = new InspectionAssessment();
        PartnerStageStatus partnerStageStatus = new PartnerStageStatus();

        WorkflowData workFlowData = new WorkflowData();
        visit.setServiceStartCode(AuthCodeGenerator.generateFourDigitAuthPin());
        repair.setServiceEndCode(AuthCodeGenerator.generateFourDigitAuthPin());

        workFlowData.setVisit(visit);
        workFlowData.setRepair(repair);
        workFlowData.setClaimSettlement(claimSettlement);
        workFlowData.setDocumentUpload(documentUpload);
        workFlowData.setRepairAssessment(repairAssessment);
        workFlowData.setSoftApproval(softApproval);
        workFlowData.setVerification(verification);
        workFlowData.setIcDoc(icDoc);
        workFlowData.setInspectionAssessment(inspectionAssessment);
        workFlowData.setPartnerStageStatus(partnerStageStatus);

        String eventCode = Constants.YES_FLAG.equalsIgnoreCase(isPincodeServicable) ? ServiceRequestEventCode.CREATE_INSPECT_SR_ALLOCATE_SC.getServiceRequestEvent()
                : ServiceRequestEventCode.CREATE_INSPECT_SR_SC_NOTALLOCATED.getServiceRequestEvent();

        final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());
        if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {
            for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {
                if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode)) {
                    String workflowStage = serviceRequestTransitionConfigEntity.getTransitionToStage();
                    String workflowStageStatus = StringUtils.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());

                    final List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusEntities = serviceRequestStageStatusMstCache.get(this.serviceRequestType.getRequestType());
                    if (serviceRequestStageStatusEntities != null && !serviceRequestStageStatusEntities.isEmpty()) {
                        for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusMstEntity : serviceRequestStageStatusEntities) {
                            if (workflowStage.equals(serviceRequestStageStatusMstEntity.getStageCode())
                                    && workflowStageStatus.equals(StringUtils.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusCode()))) {
                                String partnerStage = serviceRequestStageStatusMstEntity.getServiceRequestStatus();
                                String partnerStageStatusName = StringUtils.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusDisplayName());
                                serviceRequestEntity.setWorkflowStage(workflowStage);
                                serviceRequestEntity.setWorkflowStageStatus(workflowStageStatus);
                                serviceRequestEntity.setStatus(partnerStage);
                                partnerStageStatus.setStatus(partnerStageStatusName);
                                break;
                            }
                        }
                    } else {
                        throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(), new InputValidationException());
                    }
                    break;
                }
            }
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_FAILED.getErrorCode(), new InputValidationException());
        }

        ObjectMapper mapper = new ObjectMapper();
        String workFlowJson = mapper.writeValueAsString(workFlowData);
        serviceRequestEntity.setWorkflowData(workFlowJson);

        logger.debug("Work Flow JSON " + workFlowJson);

        return workFlowData;
    }
}
