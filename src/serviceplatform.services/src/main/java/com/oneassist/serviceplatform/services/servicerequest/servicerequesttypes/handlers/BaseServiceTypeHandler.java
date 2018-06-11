package com.oneassist.serviceplatform.services.servicerequest.servicerequesttypes.handlers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commands.dtos.CommandResult;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageStatusMstCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTransitionConfigCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceTaskMasterCache;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceFulfilmentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestAssetRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestEntityToServiceRequestDtoMapper;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.utils.StringUtils;
import com.oneassist.serviceplatform.commons.validators.InputValidator;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestValidator;
import com.oneassist.serviceplatform.commons.workflowmanager.IWorkflowManager;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CostToServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PartnerStageStatus;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.AssigneeRepairCostRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;

// This provides the default functionality for CRUD operations for service request.
@Component
public abstract class BaseServiceTypeHandler {

    private final Logger logger = Logger.getLogger(BaseServiceTypeHandler.class);

    protected ServiceRequestType serviceRequestType;

    @Autowired
    protected ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    @Autowired
    protected ServiceRequestRepository serviceRequestRepository;

    @Autowired
    protected CommunicationGatewayProxy communicationGatewayProxy;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected IWorkflowManager workflowManager;

    @Autowired
    protected PincodeServiceFulfilmentRepository fulfilmentRepository;

    @Autowired
    protected ServiceAddressRepository serviceAddressRepository;

    @Autowired
    protected ServiceRequestHelper serviceRequestHelper;

    @Autowired
    protected InputValidator inputValidator;

    @Autowired
    protected ServiceRequestValidator serviceRequestValidator;

    @Autowired
    protected ServiceTaskMasterCache serviceTaskMasterCache;

    @Autowired
    protected ServiceRequestTransitionConfigCache serviceRequestTransitionConfigCache;

    @Autowired
    protected ServiceRequestStageStatusMstCache serviceRequestStageStatusMstCache;

    @Autowired
    protected ServiceRequestStageMasterCache serviceRequestStageMasterCache;

    @Autowired
    private GenericKeySetCache genericKeySetCache;

    @Autowired
    protected ServiceRequestAssetRepository serviceRequestAssetRepository;

    @Autowired
    protected IServiceRequestService serviceRequestService;

    protected List<ErrorInfoDto> errorInfoDtoList;

    private Map<ServiceRequestType, List<ServiceRequestUpdateAction>> applicableActionsForServiceTypeMap = null;

    private final String SELFREPAIR = "SELFREPAIR";

    private final String NON_SELFREPAIR = "NON-SELFREPAIR";

    protected final String CRM = "CRM";

    public BaseServiceTypeHandler(ServiceRequestType serviceRequestType) {
        this.serviceRequestType = serviceRequestType;

        List<ServiceRequestUpdateAction> applicableActions = null;
        applicableActionsForServiceTypeMap = new HashMap<ServiceRequestType, List<ServiceRequestUpdateAction>>();

        switch (this.serviceRequestType) {
            case HA_EW:
            case HA_BD:
            case HA_AD:
                applicableActions = new ArrayList<>();
                applicableActions.add(ServiceRequestUpdateAction.ASSIGN);
                applicableActions.add(ServiceRequestUpdateAction.RESCHEDULE_SERVICE);
                applicableActions.add(ServiceRequestUpdateAction.SUBMIT_SERVICE_REQUEST_FEEDBACK);
                applicableActions.add(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT);
                applicableActions.add(ServiceRequestUpdateAction.WF_DATA);
                applicableActions.add(ServiceRequestUpdateAction.UPDATE_WF_DATA_ON_EVENT);
                applicableActions.add(ServiceRequestUpdateAction.SERVICE_REQUEST_STATUS);
                applicableActions.add(ServiceRequestUpdateAction.CLOSE_SERVICE_REQUEST);
                applicableActionsForServiceTypeMap.put(this.serviceRequestType, applicableActions);
                break;
            case HA_FR:
            case HA_BR:
                applicableActions = new ArrayList<>();
                applicableActions.add(ServiceRequestUpdateAction.SUBMIT_SERVICE_REQUEST_FEEDBACK);
                applicableActions.add(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT);
                applicableActions.add(ServiceRequestUpdateAction.WF_DATA);
                applicableActions.add(ServiceRequestUpdateAction.CLOSE_SERVICE_REQUEST);
                applicableActionsForServiceTypeMap.put(this.serviceRequestType, applicableActions);
                break;
            case WHC_INSPECTION:
                applicableActions = new ArrayList<>();
                applicableActions.add(ServiceRequestUpdateAction.ASSIGN);
                applicableActions.add(ServiceRequestUpdateAction.RESCHEDULE_SERVICE);
                applicableActions.add(ServiceRequestUpdateAction.SUBMIT_SERVICE_REQUEST_FEEDBACK);
                applicableActions.add(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT);
                applicableActions.add(ServiceRequestUpdateAction.CANCEL_SERVICE);
                applicableActionsForServiceTypeMap.put(this.serviceRequestType, applicableActions);
                break;
            case PE_ADLD:
            case PE_EW:
            case PE_THEFT:
                applicableActions = new ArrayList<>();
                applicableActions.add(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT);
                applicableActions.add(ServiceRequestUpdateAction.SERVICE_REQUEST_STATUS);
                applicableActions.add(ServiceRequestUpdateAction.WF_DATA);
                applicableActions.add(ServiceRequestUpdateAction.SUBMIT_SERVICE_REQUEST_FEEDBACK);
                applicableActionsForServiceTypeMap.put(this.serviceRequestType, applicableActions);
                break;
            default:
                throw new BusinessServiceException("Invalid action to update the Service Request!!", new InputValidationException());
        }
    }

    public void validateOnServiceRequestCreate(ServiceRequestDto serviceRequestDto) throws BusinessServiceException {
        errorInfoDtoList = new ArrayList<>();

        if (serviceRequestDto.getInitiatingSystem() == null) {
            inputValidator.populateMandatoryFieldError("initiatingSystem", errorInfoDtoList);
        }
    }

    public void validateOnServiceRequestUpdate(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
            ServiceRequestEntity serviceRequestEntity) throws BusinessServiceException {

        errorInfoDtoList = new ArrayList<>();

        try {
            if (serviceRequestDto.getServiceRequestId() == null) {
                inputValidator.populateMandatoryFieldError(Constants.SERVICE_REQUEST_ID_LOWER, errorInfoDtoList);
            }

            if (Strings.isNullOrEmpty(serviceRequestDto.getModifiedBy())) {
                inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoDtoList);
            }
            if (serviceRequestEntity == null) {
                logger.error("Invalid Service Request Id");
                inputValidator.populateInvalidRequestError(ServiceRequestResponseCodes.UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID.getErrorCode(), errorInfoDtoList);
            } else {
                // Validating updateAction
                boolean isValidAction = false;
                List<ServiceRequestUpdateAction> applicableActions = getApplicableActions();
                if (applicableActions != null && !applicableActions.isEmpty()) {
                    for (ServiceRequestUpdateAction action : applicableActions) {
                        if (action.equals(serviceRequestUpdateAction)) {
                            isValidAction = !isValidAction;
                            break;
                        }
                    }
                }
                if (!isValidAction) {
                    inputValidator.populateInvalidData("inputAction", errorInfoDtoList);
                }
                // updateAction is valid, populating serviceRequest data that will be used in the flow.
                populateInitialServiceRequestDetails(serviceRequestDto, serviceRequestEntity, serviceRequestUpdateAction);
            }
        } catch (InputValidationException ex) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, ex);
        }

        // Validate Event code for update if update request is coming using EVENT_CODE
        if (eventCode == null) {
            inputValidator.populateInvalidData("inputEventCode", errorInfoDtoList);
        }

        if (!ServiceRequestType.PE_ADLD.getRequestType().equals(serviceRequestDto.getServiceRequestType())
                && !ServiceRequestType.PE_EW.getRequestType().equals(serviceRequestDto.getServiceRequestType())
                && !ServiceRequestType.PE_THEFT.getRequestType().equals(serviceRequestDto.getServiceRequestType()) && eventCode != null && !ServiceRequestEventCode.DEFAULT.equals(eventCode)) {
            boolean isValidEvent = false;
            List<ServiceRequestTransitionConfigEntity> serviceReqeustTransitionConfigEventList = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());

            if (serviceReqeustTransitionConfigEventList != null && !serviceReqeustTransitionConfigEventList.isEmpty()) {
                for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceReqeustTransitionConfigEventList) {
                    if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
                        ModelMapper modelMapper = new ModelMapper();
                        modelMapper.addMappings(new ServiceRequestEntityToServiceRequestDtoMapper());
                        ServiceRequestDto serviceRequestDtoTemp = modelMapper.map(serviceRequestEntity, new TypeToken<ServiceRequestDto>() {
                        }.getType());
                        String isSelfService = serviceRequestDtoTemp.getWorkflowData().getVisit().getIsSelfService();
                        if ((serviceRequestDto.getWorkflowStage() == null)
                                || (serviceRequestTransitionConfigEntity.getTransitionFromStage() == null)
                                || (serviceRequestDto.getWorkflowStage().equals(serviceRequestTransitionConfigEntity.getTransitionFromStage()) && (isSelfService.equals(Constants.YES_FLAG)
                                        && (eventCode.getEventApplicability().equals(SELFREPAIR) || eventCode.getEventApplicability().equals("BOTH")) || (isSelfService.equals(Constants.NO_FLAG) && (eventCode
                                        .getEventApplicability().equals(NON_SELFREPAIR) || eventCode.getEventApplicability().equals("BOTH")))))) {
                            isValidEvent = true;
                            System.out.println("Event name validated:: " + serviceRequestTransitionConfigEntity.getEventName());
                        }

                        break;
                    }
                }
            }

            if (!isValidEvent) {
                inputValidator.populateInvalidData("inputEventCode", errorInfoDtoList);
            }
        }
        if (!CollectionUtils.isEmpty(errorInfoDtoList)) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, new InputValidationException());
        }
    }

    private void populateInitialServiceRequestDetails(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, ServiceRequestUpdateAction serviceRequestUpdateAction) {
        if (!ServiceRequestType.PE_ADLD.getRequestType().equals(this.serviceRequestType.getRequestType())
                && !ServiceRequestType.PE_THEFT.getRequestType().equals(this.serviceRequestType.getRequestType())
                && !ServiceRequestType.PE_EW.getRequestType().equals(this.serviceRequestType.getRequestType())) {
            switch (serviceRequestUpdateAction) {

                case UPDATE_SERVICE_REQUEST_ON_EVENT:
                    serviceRequestDto.setWorkflowStage(serviceRequestEntity.getWorkflowStage());
                    serviceRequestDto.setWorkflowStageStatus(serviceRequestEntity.getWorkflowStageStatus());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setScheduleSlotStartDateTime(serviceRequestEntity.getScheduleSlotStartDateTime());
                    serviceRequestDto.setScheduleSlotEndDateTime(serviceRequestEntity.getScheduleSlotEndDateTime());
                    serviceRequestDto.setDueDateTime(serviceRequestEntity.getDueDateTime());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    serviceRequestDto.setRefPrimaryTrackingNo(serviceRequestEntity.getRefPrimaryTrackingNo());
                    serviceRequestDto.setReferenceNo(serviceRequestEntity.getReferenceNo());
                    serviceRequestDto.setServiceRequestType(this.serviceRequestType.getRequestType());
                    serviceRequestDto.setAdviceId(serviceRequestEntity.getAdviceId());
                    break;
                case RESCHEDULE_SERVICE:
                    serviceRequestDto.setAssignee(serviceRequestEntity.getAssignee());
                    serviceRequestDto.setWorkflowStage(serviceRequestEntity.getWorkflowStage());
                    serviceRequestDto.setWorkflowStageStatus(serviceRequestEntity.getWorkflowStageStatus());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setDueDateTime(serviceRequestEntity.getDueDateTime());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    serviceRequestDto.setServicePartnerBuCode(serviceRequestEntity.getServicePartnerBuCode());
                    serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
                    serviceRequestDto.setServiceRequestType(this.serviceRequestType.getRequestType());
                    break;
                case ASSIGN:
                    serviceRequestDto.setServiceRequestType(this.serviceRequestType.getRequestType());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    break;
                case SERVICE_REQUEST_STATUS:
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    break;
                case WF_DATA:
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    break;
                case UPDATE_WF_DATA_ON_EVENT:
                    serviceRequestDto.setWorkflowStage(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setScheduleSlotStartDateTime(serviceRequestEntity.getScheduleSlotStartDateTime());
                    serviceRequestDto.setScheduleSlotEndDateTime(serviceRequestEntity.getScheduleSlotEndDateTime());
                    serviceRequestDto.setDueDateTime(serviceRequestEntity.getDueDateTime());
                    serviceRequestDto.setServicePartnerCode(serviceRequestEntity.getServicePartnerCode());
                    serviceRequestDto.setRefPrimaryTrackingNo(serviceRequestEntity.getRefPrimaryTrackingNo());
                    serviceRequestDto.setReferenceNo(serviceRequestEntity.getReferenceNo());
                    serviceRequestDto.setServiceRequestType(this.serviceRequestType.getRequestType());
                    serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
                    break;
                case CLOSE_SERVICE_REQUEST:
                    serviceRequestDto.setWorkflowStage(serviceRequestEntity.getWorkflowStage());
                    serviceRequestDto.setWorkflowJsonString(serviceRequestEntity.getWorkflowData());
                    serviceRequestDto.setWorkflowProcessId(serviceRequestEntity.getWorkflowProcessId());
                    serviceRequestDto.setStatus(serviceRequestEntity.getStatus());
                    serviceRequestDto.setRefPrimaryTrackingNo(serviceRequestEntity.getRefPrimaryTrackingNo());
                    serviceRequestDto.setServiceRequestType(this.serviceRequestType.getRequestType());
                    break;
                default:
                    logger.info("No SR details are required to be set..");
            }
        }
    }

    protected abstract ServiceRequestDto createServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String requestType) throws JsonProcessingException,
            BusinessServiceException;

    public ServiceRequestDto doCreateServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity serviceRequestEntity, String requestType) throws JsonProcessingException,
            BusinessServiceException {

        this.validateOnServiceRequestCreate(serviceRequestDto);

        return this.createServiceRequest(serviceRequestDto, serviceRequestEntity, requestType);
    }

    public CostToServiceDto doCalculateCostToCustomer(Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto, ServiceRequestEntity serviceRequestEntity)
            throws BusinessServiceException {
        if (this.serviceRequestType.equals(ServiceRequestType.HA_AD) || this.serviceRequestType.equals(ServiceRequestType.HA_BD) || this.serviceRequestType.equals(ServiceRequestType.HA_EW)) {
            this.validateOnCostToCustomerCalculate(serviceRequestId, assigneeRepairCostRequestDto);
            return this.calcualteCostToCustomer(serviceRequestId, assigneeRepairCostRequestDto, serviceRequestEntity);
        } else {
            throw new BusinessServiceException(GenericResponseCodes.INVALID_REQUEST.getErrorCode(), new InputValidationException());
        }
    }

    protected CostToServiceDto calcualteCostToCustomer(Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto, ServiceRequestEntity serviceRequestEntity)
            throws BusinessServiceException {
        return null;
    }

    public void validateOnCostToCustomerCalculate(Long serviceRequestId, AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) throws BusinessServiceException {
        errorInfoDtoList = new ArrayList<>();
        if (Strings.isNullOrEmpty(assigneeRepairCostRequestDto.getModifiedBy())) {
            inputValidator.populateMandatoryFieldError("modifiedBy", errorInfoDtoList);
        }
    }

    public CommandResult<ServiceResponseDto> doUpdateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto, ServiceRequestEventCode eventCode,
            ServiceRequestEntity serviceRequestEntity) throws Exception {

        this.validateOnServiceRequestUpdate(serviceRequestUpdateAction, serviceRequestDto, eventCode, serviceRequestEntity);

        return this.updateServiceRequest(serviceRequestUpdateAction, serviceRequestDto, eventCode, serviceRequestEntity);
    }

    protected abstract CommandResult<ServiceResponseDto> updateServiceRequest(ServiceRequestUpdateAction serviceRequestUpdateAction, ServiceRequestDto serviceRequestDto,
            ServiceRequestEventCode eventCode, ServiceRequestEntity serviceRequestEntity) throws Exception;

    protected List<ServiceRequestUpdateAction> getApplicableActions() throws BusinessServiceException {
        return applicableActionsForServiceTypeMap.get(this.serviceRequestType);
    }

    protected List<ServiceRequestEventCode> getApplicableEvents() {
        List<ServiceRequestEventCode> applicableEvents = new ArrayList<ServiceRequestEventCode>();
        List<ServiceRequestTransitionConfigEntity> serviceReqeustTransitionConfigEventList = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());
        if (serviceReqeustTransitionConfigEventList != null && !serviceReqeustTransitionConfigEventList.isEmpty()) {
            for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceReqeustTransitionConfigEventList) {
                applicableEvents.add(ServiceRequestEventCode.getServiceRequestEvent(serviceRequestTransitionConfigEntity.getEventName()));
            }
        }
        return applicableEvents;
    }

    protected List<WorkflowStage> getApplicableStages() {
        List<WorkflowStage> applicableStages = new ArrayList<WorkflowStage>();
        List<ServiceRequestStageMstEntity> serviceReqeustStageMasterList = serviceRequestStageMasterCache.get(this.serviceRequestType.getRequestType());
        if (serviceReqeustStageMasterList != null && !serviceReqeustStageMasterList.isEmpty()) {
            for (ServiceRequestStageMstEntity serviceRequestStageMstEntity : serviceReqeustStageMasterList) {
                applicableStages.add(WorkflowStage.getWorkflowStage(serviceRequestStageMstEntity.getStageName()));
            }
        }

        return applicableStages;
    }

    protected void populateServiceAddressEntity(ServiceAddressEntity addressEntity, ServiceRequestDto serviceRequestDto) {

        addressEntity.setAddresseeFullName(serviceRequestDto.getServiceRequestAddressDetails().getAddresseeFullName());
        addressEntity.setAddressLine1(serviceRequestDto.getServiceRequestAddressDetails().getAddressLine1());
        addressEntity.setAddressLine2(serviceRequestDto.getServiceRequestAddressDetails().getAddressLine2());
        addressEntity.setCountryCode(serviceRequestDto.getServiceRequestAddressDetails().getCountryCode());
        addressEntity.setCreatedBy(serviceRequestDto.getCreatedBy());
        addressEntity.setCreatedOn(new Date());
        addressEntity.setDistrict(serviceRequestDto.getServiceRequestAddressDetails().getDistrict());
        addressEntity.setEmail(serviceRequestDto.getServiceRequestAddressDetails().getEmail());
        addressEntity.setLandmark(serviceRequestDto.getServiceRequestAddressDetails().getLandmark());
        addressEntity.setMobileNo(BigDecimal.valueOf(serviceRequestDto.getServiceRequestAddressDetails().getMobileNo()));
        addressEntity.setPincode(serviceRequestDto.getServiceRequestAddressDetails().getPincode());
    }

    protected void doValidateServiceAddress(ServiceRequestDto serviceRequestDto) {

        if (serviceRequestDto.getServiceRequestAddressDetails() == null) {
            inputValidator.populateMandatoryFieldError("serviceRequestAddressDetails", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getServiceRequestAddressDetails().getAddresseeFullName())) {
            inputValidator.populateMandatoryFieldError("addresseeFullName", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getServiceRequestAddressDetails().getAddressLine1())) {
            inputValidator.populateMandatoryFieldError("addressLine1", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getServiceRequestAddressDetails().getEmail())) {
            inputValidator.populateMandatoryFieldError("email", errorInfoDtoList);
        }

        if (serviceRequestDto.getServiceRequestAddressDetails().getMobileNo() == null) {
            inputValidator.populateMandatoryFieldError("mobileNo", errorInfoDtoList);
        }

        if (Strings.isNullOrEmpty(serviceRequestDto.getServiceRequestAddressDetails().getPincode())) {
            inputValidator.populateMandatoryFieldError("pincode", errorInfoDtoList);
        }
    }

    protected Date setEndWorkingHour(Date inputDate) {
        inputDate = DateUtils.setHours(inputDate, 21);
        inputDate = DateUtils.setMinutes(inputDate, 0);
        inputDate = DateUtils.setSeconds(inputDate, 0);
        inputDate = DateUtils.setMilliseconds(inputDate, 0);

        return inputDate;
    }

    protected void populateAllStatusesBasedOnEventCode(ServiceRequestDto serviceRequestUpdateDto, ServiceRequestEventCode eventCode, ServiceRequestEntity serviceRequestEntity) throws Exception {

        logger.info(" BaseServiceTypeHandler>>> populateAllStatusesBasedOnEventCode() >>> " + eventCode);
        final List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntities = serviceRequestTransitionConfigCache.get(this.serviceRequestType.getRequestType());

        if (serviceRequestTransitionConfigEntities != null && !serviceRequestTransitionConfigEntities.isEmpty()) {

            for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigEntity : serviceRequestTransitionConfigEntities) {

                if (serviceRequestTransitionConfigEntity.getEventName().equals(eventCode.getServiceRequestEvent())) {
                    String workflowStage = serviceRequestTransitionConfigEntity.getTransitionToStage();
                    String workflowStageStatus = StringUtils.getEmptyIfNull(serviceRequestTransitionConfigEntity.getTransitionToStageStatus());

                    final List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusEntities = serviceRequestStageStatusMstCache.get(this.serviceRequestType.getRequestType());

                    if (serviceRequestStageStatusEntities != null && !serviceRequestStageStatusEntities.isEmpty()) {
                        for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusMstEntity : serviceRequestStageStatusEntities) {
                            if (workflowStage.equals(serviceRequestStageStatusMstEntity.getStageCode())
                                    && workflowStageStatus.equals(StringUtils.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusCode()))) {
                                String partnerStage = serviceRequestStageStatusMstEntity.getServiceRequestStatus();
                                String partnerStageStatus = StringUtils.getEmptyIfNull(serviceRequestStageStatusMstEntity.getStageStatusDisplayName());

                                WorkflowData workflowData = serviceRequestUpdateDto.getWorkflowData() == null ? new WorkflowData() : serviceRequestUpdateDto.getWorkflowData();
                                PartnerStageStatus partnerStageStatusObj = workflowData.getPartnerStageStatus() == null ? new PartnerStageStatus() : workflowData.getPartnerStageStatus();
                                partnerStageStatusObj.setStatus(partnerStageStatus);

                                workflowData.setPartnerStageStatus(partnerStageStatusObj);

                                ServiceRequestDto serviceRequestUpdateRequestDtoForStatus = new ServiceRequestDto();
                                serviceRequestUpdateRequestDtoForStatus.setWorkflowData(workflowData);
                                serviceRequestUpdateRequestDtoForStatus.setWorkflowStage(WorkflowStage.PARTNER_STAGE_STATUS.getWorkflowStageName());
                                serviceRequestUpdateRequestDtoForStatus.setWorkflowJsonString(serviceRequestUpdateDto.getWorkflowJsonString());
                                serviceRequestUpdateRequestDtoForStatus.setServiceRequestId(serviceRequestUpdateDto.getServiceRequestId());

                                final String updatedWithPartnerStatus = serviceRequestHelper.populateJsonWithRequestParameters(serviceRequestUpdateRequestDtoForStatus, serviceRequestEntity);
                                serviceRequestUpdateDto.setWorkflowJsonString(updatedWithPartnerStatus);

                                serviceRequestUpdateDto.setWorkflowStage(workflowStage);
                                serviceRequestUpdateDto.setWorkflowStageStatus(workflowStageStatus);
                                serviceRequestUpdateDto.setStatus(partnerStage);
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
    }

    protected Map<String, Object> getActivitiVariables() {
        String activitiVariableKey = null;
        switch (this.serviceRequestType) {
            case HA_EW:
                activitiVariableKey = "HA_EW_ACTIVITI_VARIABLES";
                break;
            case HA_BD:
                activitiVariableKey = "HA_BD_ACTIVITI_VARIABLES";
                break;
            case HA_AD:
                activitiVariableKey = "HA_AD_ACTIVITI_VARIABLES";
                break;
            case HA_FR:
                activitiVariableKey = "HA_FR_ACTIVITI_VARIABLES";
                break;
            case HA_BR:
                activitiVariableKey = "HA_BR_ACTIVITI_VARIABLES";
                break;
            default:
                throw new BusinessServiceException("Invalid Service Type exception", new InputValidationException());
        }

        GenericKeySetEntity genericKeySet = genericKeySetCache.get(activitiVariableKey);
        List<GenericKeySetValueEntity> genericKeySetValues = genericKeySet.getGenericKeySetValueDetails();

        if (genericKeySetValues != null & !genericKeySetValues.isEmpty()) {
            Map<String, Object> activitiVariablesMap = new HashMap<>();

            for (GenericKeySetValueEntity genericKeySetValueEntity : genericKeySetValues) {
                activitiVariablesMap.put(genericKeySetValueEntity.getKey(), genericKeySetValueEntity.getValue());
            }

            return activitiVariablesMap;
        }

        return null;
    }

    protected List<PincodeServiceFulfilmentEntity> validatePincodeServicibility(String pincode, Long serviceRequestTypeId) {
        List<PincodeServiceFulfilmentEntity> pincodeFulfilments = fulfilmentRepository.findServiceCentreByPincodeAndServiceRequestTypeIdAndStatus(pincode, serviceRequestTypeId, Constants.ACTIVE);

        if (CollectionUtils.isNotEmpty(pincodeFulfilments)) {
            return pincodeFulfilments;
        } else {
            throw new BusinessServiceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED_DUE_TO_NONSERVICEABLE_PINCODE.getErrorCode(), new Object[] {});
        }
    }

	@Transactional(rollbackFor = { Exception.class })
	protected void saveAssetsforSR(String assetIds, Long serviceRequestId) throws BusinessServiceException {
		ModelMapper mapper = new ModelMapper();
		List<String> assetIdList = StringUtils.getListFromString(assetIds);
		List<ServiceRequestAssetEntity> assetList = new ArrayList<>();
		
		for (String assetId : assetIdList) {

			ServiceRequestAssetEntity assetEntity = new ServiceRequestAssetEntity();
			String uuId = UUID.randomUUID().toString().replaceAll("-", "");
			assetEntity.setAssetId(uuId);
			assetEntity.setAssetReferenceNo(assetId);
			assetEntity.setServiceRequestId(serviceRequestId);
			assetList.add(assetEntity);

		}

		serviceRequestAssetRepository.save(assetList);

	}

    public ServiceRequestDto updateServiceRequest(ServiceRequestDto serviceRequestDto, ServiceRequestEntity newServiceRequestEntity, ServiceRequestEntity existingServiceRequestEntity)
            throws Exception {
        return null;
    }
    
    public void updateDeliveryDescriptionForUnresolvedSR(ShipmentEntity shipment){}
}
