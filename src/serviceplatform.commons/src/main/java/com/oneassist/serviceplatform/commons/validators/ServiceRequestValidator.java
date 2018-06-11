package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetValueEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.InitiatingSystem;
import com.oneassist.serviceplatform.commons.enums.ServiceConfig;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.GenericKeySetCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceTaskMasterCache;
import com.oneassist.serviceplatform.commons.proxies.OasysProxy;
import com.oneassist.serviceplatform.commons.repositories.DocumentTypeConfigRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ImageStorageReference;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Inspection;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Issues;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.OASYSCustMemDetails;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestAddressDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceScheduleDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.CompleteWHCInspectionRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.ProductsForPlanRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.TechnicianDetailRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;

/**
 * @author Satish.Kumar This class will validate the Service Request Parameter
 */
@Component
public class ServiceRequestValidator extends InputValidator {

    private static final Logger logger = Logger.getLogger(ServiceRequestValidator.class);

   
    private static final String SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT = "SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT";
    private static final String INCIDENT_DESCRIPTION_MAX_LENGTH = "INCIDENT_DESCRIPTION_MAX_LENGTH";


    @Autowired
    private ServiceTaskMasterCache serviceTaskMasterCache ;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    private static String IS_MANDATORY = "Y";

    @Autowired
    private GenericKeySetCache genericKeySetCache;

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private DocumentTypeConfigRepository documentTypeConfigRepository;
    
    @Autowired
    private OasysProxy oasysProxy;

    private ServiceRequestValidator() {

    }

    public List<ErrorInfoDto> doValidateCreateServiceRequest(ServiceRequestDto request) {

        logger.info(">>> Validation for Mandatory data for Create Service Request  :" + request);
        return validateCommonFieldsforCreateServiceRequest(request);

    }

    private List<ErrorInfoDto> validateCommonFieldsforCreateServiceRequest(ServiceRequestDto element) {

        logger.info(">>> Validation for Common Fields Create Service Request :" + element);

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {

            validateRequiredField("serviceRequestType", element.getServiceRequestType(), errorInfoDtoList);

            ServiceRequestType serviceRequestType = ServiceRequestType.getServiceRequestType(element.getServiceRequestType());
            if (null == serviceRequestType) {
                populateInvalidData("serviceRequestType", errorInfoDtoList);
            }

            if (serviceRequestType != null && serviceRequestType.getRequestType() != null) {

                // For Request Type - EW & HA_AD
                if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_EW.getRequestType())
                        || serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_AD.getRequestType())
                        || serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_BD.getRequestType())
                        || serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_FR.getRequestType())
                        || serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_BR.getRequestType())) {
                    validateRequiredField("refSecondaryTrackingNo", element.getRefSecondaryTrackingNo(), errorInfoDtoList);
                    validateRequiredField("initiatingSystem", element.getInitiatingSystem(), errorInfoDtoList);

                    if (element.getInitiatingSystem() != null && element.getInitiatingSystem() == InitiatingSystem.CRM.getInitiatingSystem()) {
                        validateRequiredField("refPrimaryTrackingNo", element.getRefPrimaryTrackingNo(), errorInfoDtoList);
                    }

                    validateRequiredField("referenceNo", element.getReferenceNo(), errorInfoDtoList);
                    validateRequiredField("createdBy", element.getCreatedBy(), errorInfoDtoList);

                    if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_EW.getRequestType())) {
                        validateRequiredField("workFlowValue", element.getWorkFlowValue(), errorInfoDtoList);
                        if (element.getWorkFlowValue() != null && element.getWorkFlowValue() == 3) {
                            validateRequiredField("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), errorInfoDtoList);
                            validateRequiredField("scheduleSlotEndDateTime", element.getScheduleSlotEndDateTime(), errorInfoDtoList);
                            String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);

                            validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit), errorInfoDtoList);
                        }

                        if (element.getIssueReportedByCustomer() == null || element.getIssueReportedByCustomer().isEmpty()) {
                            populateMandatoryFieldError("issueReportedByCustomer", errorInfoDtoList);
                        } else {
                            for (Issues issue : element.getIssueReportedByCustomer()) {
                                if (Strings.isNullOrEmpty(issue.getIssueId())) {
                                    errorInfoDto = new ErrorInfoDto(16, " issueId cannot be Empty");
                                    errorInfoDtoList.add(errorInfoDto);
                                } else if (serviceTaskMasterCache.get(issue.getIssueId()) == null) {
                                    errorInfoDto = new ErrorInfoDto(16, "Invalid issueId:" + issue.getIssueId());
                                    errorInfoDtoList.add(errorInfoDto);
                                }
                            }
                        }
                    } else if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_AD.getRequestType())) {
                        validateRequiredField("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), errorInfoDtoList);
                        validateRequiredField("scheduleSlotEndDateTime", element.getScheduleSlotEndDateTime(), errorInfoDtoList);
                        String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);

                        validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit), errorInfoDtoList);

                        if (element.getInitiatingSystem() != null && element.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()) {
                            validateRequiredField("incidentDescription", element.getIncidentDescription(), errorInfoDtoList);
                        }
                        if (!Strings.isNullOrEmpty(element.getIncidentDescription())) {
                            String incidentDescriptionMaxLengthStr = messageSource.getMessage(INCIDENT_DESCRIPTION_MAX_LENGTH, new Object[] { "" }, null);
                            int incidentDescriptionMaxLength = incidentDescriptionMaxLengthStr == null ? 250 : Integer.parseInt(incidentDescriptionMaxLengthStr);
                            validateFieldMaxLength("incidentDescription", element.getIncidentDescription(), incidentDescriptionMaxLength, errorInfoDtoList);
                        }

                    } else if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_BD.getRequestType())) {
                        validateRequiredField("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), errorInfoDtoList);
                        validateRequiredField("scheduleSlotEndDateTime", element.getScheduleSlotEndDateTime(), errorInfoDtoList);

                        String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);

                        validateServiceRequestDateAfterXHours("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit), errorInfoDtoList);
                        if (element.getIssueReportedByCustomer() == null || element.getIssueReportedByCustomer().isEmpty()) {
                            populateMandatoryFieldError("issueReportedByCustomer", errorInfoDtoList);
                        } else {
                            for (Issues issue : element.getIssueReportedByCustomer()) {
                                if (Strings.isNullOrEmpty(issue.getIssueId())) {
                                    errorInfoDto = new ErrorInfoDto(16, " issueId cannot be Empty");
                                    errorInfoDtoList.add(errorInfoDto);
                                } else if (serviceTaskMasterCache.get(issue.getIssueId()) == null) {
                                    errorInfoDto = new ErrorInfoDto(16, "Invalid issueId:" + issue.getIssueId());
                                    errorInfoDtoList.add(errorInfoDto);
                                }
                            }
                        }
                        if (element.getInitiatingSystem() != null && element.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()) {
                            validateRequiredField("incidentDescription", element.getIncidentDescription(), errorInfoDtoList);
                        }
                        if (!Strings.isNullOrEmpty(element.getIncidentDescription())) {
                            String incidentDescriptionMaxLengthStr = messageSource.getMessage(INCIDENT_DESCRIPTION_MAX_LENGTH, new Object[] { "" }, null);
                            int incidentDescriptionMaxLength = incidentDescriptionMaxLengthStr == null ? 250 : Integer.parseInt(incidentDescriptionMaxLengthStr);
                            validateFieldMaxLength("incidentDescription", element.getIncidentDescription(), incidentDescriptionMaxLength, errorInfoDtoList);
                        }
                    } else if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_FR.getRequestType())
                            || serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.HA_BR.getRequestType())) {
                        if (element.getInitiatingSystem() != null && element.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()) {
                            validateRequiredField("incidentDescription", element.getIncidentDescription(), errorInfoDtoList);
                        }
                        if (!Strings.isNullOrEmpty(element.getIncidentDescription())) {
                            String incidentDescriptionMaxLengthStr = messageSource.getMessage(INCIDENT_DESCRIPTION_MAX_LENGTH, new Object[] { "" }, null);
                            int incidentDescriptionMaxLength = incidentDescriptionMaxLengthStr == null ? 250 : Integer.parseInt(incidentDescriptionMaxLengthStr);
                            validateFieldMaxLength("incidentDescription", element.getIncidentDescription(), incidentDescriptionMaxLength, errorInfoDtoList);
                        }
                    }
                    validateRequiredField("serviceRequestAddressDetails", element.getServiceRequestAddressDetails(), errorInfoDtoList);
                    validateRequiredField("addresseeFullName", element.getServiceRequestAddressDetails().getAddresseeFullName(), errorInfoDtoList);
                    validateRequiredField("addressLine1", element.getServiceRequestAddressDetails().getAddressLine1(), errorInfoDtoList);
                    validateRequiredField("countryCode", element.getServiceRequestAddressDetails().getCountryCode(), errorInfoDtoList);
                    validateRequiredField("district", element.getServiceRequestAddressDetails().getDistrict(), errorInfoDtoList);
                    validateRequiredField("email", element.getServiceRequestAddressDetails().getEmail(), errorInfoDtoList);

                    if (element.getInitiatingSystem() != null && element.getInitiatingSystem() != InitiatingSystem.CRM.getInitiatingSystem()) {
                        validateRequiredField("landmark", element.getServiceRequestAddressDetails().getLandmark(), errorInfoDtoList);
                    }

                    validateRequiredField("mobileNo", element.getServiceRequestAddressDetails().getMobileNo(), errorInfoDtoList);
                    validateRequiredField("pincode", element.getServiceRequestAddressDetails().getPincode(), errorInfoDtoList);
                    validateRequiredField("createdBy", element.getCreatedBy(), errorInfoDtoList);
                    validateRequiredField("dateOfIncident", element.getDateOfIncident(), errorInfoDtoList);
                    validateFutureDate("dateOfIncident", element.getDateOfIncident(), errorInfoDtoList);
                    validateRequiredField("placeOfIncident", element.getPlaceOfIncident(), errorInfoDtoList);
                } else if (serviceRequestType.getRequestType().equalsIgnoreCase(ServiceRequestType.WHC_INSPECTION.getRequestType())) {
                    // VALIDATIONS FOR INSPECTION
                    validateRequiredField("initiatingSystem", element.getInitiatingSystem(), errorInfoDtoList);
                    validateRequiredField("scheduleSlotStartDateTime", element.getScheduleSlotStartDateTime(), errorInfoDtoList);
                    validateRequiredField("scheduleSlotEndDateTime", element.getScheduleSlotEndDateTime(), errorInfoDtoList);
                    validateRequiredField("refPrimaryTrackingNo", element.getRefPrimaryTrackingNo(), errorInfoDtoList);

                    validateInspectionAddress(element.getServiceRequestAddressDetails(), errorInfoDtoList);
                } else {
                    // For Other Request Types
                    validateRequiredField("requestDescription", element.getRequestDescription(), errorInfoDtoList);
                    validateRequiredField("refPrimaryTrackingNo", element.getRefPrimaryTrackingNo(), errorInfoDtoList);

                    if (element.getRefSecondaryTrackingNo() != null)
                        validateRequiredField("refSecondaryTrackingNo", element.getRefSecondaryTrackingNo(), errorInfoDtoList);

                    validateRequiredField("initiatingSystem", element.getInitiatingSystem(), errorInfoDtoList);
                }
            }
        } catch (InputValidationException exception) {
            logger.info("-- Create Service request validation failed --");
        }

        logger.info("<< Validation for Common Fields Service Request");
        return errorInfoDtoList;
    }

    public void validateInspectionAddress(ServiceRequestAddressDetailsDto serviceRequestAddressDetails, List<ErrorInfoDto> errorInfoDtoList) throws InputValidationException {

        validateRequiredField("serviceRequestAddressDetails", serviceRequestAddressDetails, errorInfoDtoList);
        validateRequiredField("addresseeFullName", serviceRequestAddressDetails.getAddresseeFullName(), errorInfoDtoList);
        validateRequiredField("email", serviceRequestAddressDetails.getEmail(), errorInfoDtoList);
        validateRequiredField("addressLine1", serviceRequestAddressDetails.getAddressLine1(), errorInfoDtoList);
        // validateRequiredField("landmark", serviceRequestAddressDetails.getLandmark(), errorInfoDtoList);
        validateRequiredField("mobileNo", serviceRequestAddressDetails.getMobileNo(), errorInfoDtoList);
        validateRequiredField("pincode", serviceRequestAddressDetails.getPincode(), errorInfoDtoList);
        validateRequiredField("countryCode", serviceRequestAddressDetails.getCountryCode(), errorInfoDtoList);
    }

    public List<ErrorInfoDto> doValidateUpdateServiceRequest(ServiceRequestDto request) {

        logger.info(">>> Validation for Mandatory Data Update Service Request :" + request);
        return validateCommonFields(request);
    }

    public List<ErrorInfoDto> doValidateUpdateServiceRequest(ServiceRequestUpdateAction action, ServiceRequestDto request, ServiceRequestEventCode updateEventCode) {

        return validateCommonFields(action, request, updateEventCode);
    }

    private List<ErrorInfoDto> validateCommonFields(ServiceRequestUpdateAction srUpdateAction, ServiceRequestDto element, ServiceRequestEventCode updateEventCode) {

        logger.info(">>> Validation for Common Fields Update Service Request for : " + srUpdateAction.getServiceRequestUpdateAction() + element + updateEventCode);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {
            switch (srUpdateAction) {
                case ASSIGN:
                    validateRequiredField("assignee", element.getAssignee(), errorInfoDtoList);
                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    break;
                case RESCHEDULE_SERVICE:
                    validateRequiredField(Constants.SERVICESCHEDULESTARTTIME, element.getScheduleSlotStartDateTime(), errorInfoDtoList);
                    validateRequiredField(Constants.SERVICESCHEDULEENDTIME, element.getScheduleSlotEndDateTime(), errorInfoDtoList);
                    String serviceRequestRaiseHourLimit = messageSource.getMessage(SERVICE_REQUEST_RAISE_MIN_HOUR_LIMIT, new Object[] { "" }, null);
                    validateServiceRequestDateAfterXHours(Constants.SERVICESCHEDULESTARTTIME, element.getScheduleSlotStartDateTime(), Double.valueOf(serviceRequestRaiseHourLimit), errorInfoDtoList);
                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    if (element.getServiceRequestAddressDetails() != null) {
                        validateInspectionAddress(element.getServiceRequestAddressDetails(), errorInfoDtoList);
                    }
                    break;
                case WF_DATA:
                    WorkflowStage workflowStage = WorkflowStage.getWorkflowStage(element.getWorkflowStage());
                    validateRequiredField(Constants.WORKFLOWSTAGE, element.getWorkflowStage(), errorInfoDtoList);
                    validateRequiredField(Constants.WORKFLOWDATA, element.getWorkflowData(), errorInfoDtoList);
                    switch (workflowStage) {
                        case VISIT:
                            validateRequiredField(WorkflowStage.VISIT.getWorkflowStageName().toLowerCase(), element.getWorkflowData().getVisit(), errorInfoDtoList);
                            break;
                        case DOCUMENT_UPLOAD:
                            validateRequiredField(WorkflowStage.DOCUMENT_UPLOAD.getWorkflowStageName(), element.getWorkflowData().getDocumentUpload(), errorInfoDtoList);
                            break;
                        case VERIFICATION:
                            validateRequiredField(WorkflowStage.VERIFICATION.getWorkflowStageName().toLowerCase(), element.getWorkflowData().getVerification(), errorInfoDtoList);
                            break;
                        case REPAIR_ASSESSMENT:
                            validateRequiredField(WorkflowStage.REPAIR_ASSESSMENT.getWorkflowStageName(), element.getWorkflowData().getRepairAssessment(), errorInfoDtoList);
                            break;
                        case SOFT_APPROVAL:
                            validateRequiredField("softApproval", element.getWorkflowData().getSoftApproval(), errorInfoDtoList);
                            break;
                        case REPAIR:
                            validateRequiredField(WorkflowStage.REPAIR.getWorkflowStageName(), element.getWorkflowData().getRepair(), errorInfoDtoList);
                            break;
                        case CLAIM_SETTLEMENT:
                            validateRequiredField(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName(), element.getWorkflowData().getClaimSettlement(), errorInfoDtoList);
                            break;
                        case INSPECTION_ASSESSMENT:
                            validateRequiredField(WorkflowStage.INSPECTION_ASSESSMENT.getWorkflowStageName(), element.getWorkflowData().getInspectionAssessment(), errorInfoDtoList);
                            break;
                        default:
                            logger.error("No valid workflowstage set to update workflow data.");
                    }

                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    break;
                case SERVICE_REQUEST_STATUS:
                    validateRequiredField("status", element.getStatus(), errorInfoDtoList);

                    if (Strings.isNullOrEmpty(element.getRefPrimaryTrackingNo())) {
                    } else if (null == element.getServiceRequestId()) {
                        validateRequiredField("serviceRequestId/refPrimaryTrackingNo", element.getServiceRequestId(), errorInfoDtoList);
                    }
                    break;
                case UPDATE_SERVICE_REQUEST_ON_EVENT:
                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    switch (updateEventCode) {
                        case COMPLETE_CLAIM_SETTLEMENT:
                            validateRequiredField(Constants.WORKFLOWDATA, element.getWorkflowData(), errorInfoDtoList);
                            validateRequiredField(WorkflowStage.CLAIM_SETTLEMENT.getWorkflowStageName(), element.getWorkflowData().getClaimSettlement(), errorInfoDtoList);
                            validateRequiredField("claimAmount", element.getWorkflowData().getClaimSettlement().getClaimAmount(), errorInfoDtoList);
                            validateDoubleFormat("claimAmount", element.getWorkflowData().getClaimSettlement().getClaimAmount(), errorInfoDtoList);
                            break;
                        case INSPECTION_COMPLETED:
                            validateRequiredField(Constants.WORKFLOWDATA, element.getWorkflowData(), errorInfoDtoList);
                            validateRequiredField("inspectionAssessment", element.getWorkflowData().getInspectionAssessment(), errorInfoDtoList);

                            break;

                        default:
                            logger.info("Event code validation is not requied.");// . Put your case here if needed.
                    }

                    break;

                case CANCEL_SERVICE:
                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    break;
                case SUBMIT_SERVICE_REQUEST_FEEDBACK:
                    validateRequiredField(Constants.SERVICE_REQUEST_ID_LOWER, element.getServiceRequestId(), errorInfoDtoList);
                    break;
                default:
                    errorInfoDto = new ErrorInfoDto(GenericResponseCodes.INVALID_REQUEST.getErrorCode(), messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_REQUEST.getErrorCode()),
                            new Object[] { "" }, null));
                    errorInfoDtoList.add(errorInfoDto);
                    throw new InputValidationException();
            }

            validateRequiredField("modifiedBy", element.getModifiedBy(), errorInfoDtoList);
        } catch (InputValidationException ex) {
            logger.info("-- Update Service request validation failed --", ex);
        }

        logger.info("<< Validation for Common Fields Service Request");
        return errorInfoDtoList;

    }

    private List<ErrorInfoDto> validateCommonFields(ServiceRequestDto element) {

        logger.info(">>> Validation for Common Fields Update Service Request :" + element);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {
            validateRequiredField("modifiedBy", element.getModifiedBy(), errorInfoDtoList);
            validateRequiredField("status", element.getStatus(), errorInfoDtoList);

            if (Strings.isNullOrEmpty(element.getRefPrimaryTrackingNo()) || Strings.isNullOrEmpty(element.getRefSecondaryTrackingNo())) {
                validateRequiredField("refPrimaryTrackingNo", element.getRefPrimaryTrackingNo(), errorInfoDtoList);
                validateRequiredField("refSecondaryTrackingNo", element.getRefSecondaryTrackingNo(), errorInfoDtoList);
            } else if (null == element.getServiceRequestId()) {
                validateRequiredField("serviceRequestId", element.getServiceRequestId(), errorInfoDtoList);
            }
        } catch (InputValidationException exception) {
            logger.info("-- Create Service request validation failed --");
        }

        logger.info("<< Validation for Common Fields Service Request");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateServiceRequestDetails(ShipmentSearchRequestDto shipmentSearchRequestDto) {

        logger.info(">>> Validation for Mandatory Data Get Service Request Details:" + shipmentSearchRequestDto);
        return validateCommonFields(shipmentSearchRequestDto);
    }

    private List<ErrorInfoDto> validateCommonFields(ShipmentSearchRequestDto shipmentSearchRequestDto) {

        logger.info(">>> Validation for Common Fields Update Service Request :" + shipmentSearchRequestDto);

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {

            if (Strings.isNullOrEmpty(shipmentSearchRequestDto.getTrackingNo())) {
                ErrorInfoDto errorInfo = new ErrorInfoDto();
                errorInfo.setErrorMessage("Primary Ref Tracking# is Mandatory");
                errorInfoDtoList.add(errorInfo);
            }
        } catch (Exception exception) {
            logger.error("-- Get Service request details validation failed --");
        }

        logger.info("<< Validation for Common Fields Get Service Request Details");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateProductDetailsforPlan(ProductsForPlanRequestDto productsForPlanRequestDto) {

        logger.info(">>> Validation for Mandatory Data Get Products Details for plan:" + productsForPlanRequestDto);
        return validateCommonFields(productsForPlanRequestDto);
    }

    private List<ErrorInfoDto> validateCommonFields(ProductsForPlanRequestDto shipmentSearchRequestDto) {

        logger.info(">>> Validation for Common Fields Update Service Request :" + shipmentSearchRequestDto);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {
            if (Strings.isNullOrEmpty(shipmentSearchRequestDto.getPlanCode())) {
                ErrorInfoDto errorInfo = new ErrorInfoDto();
                errorInfo.setErrorMessage("Primary Plan code# is Mandatory");
                errorInfoDtoList.add(errorInfo);
            }

        } catch (Exception exception) {
            logger.error("-- Get Products Details for plan validation failed --");
        }

        logger.info("<< Validation for Common Fields Get Products Details for plan");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateServiceRequest(ServiceRequestSearchDto serviceRequestSearchDto) {

        logger.info(">>> Validation for Mandatory Data Get Service Request Details:" + serviceRequestSearchDto);
        return validateServiceRequest(serviceRequestSearchDto);
    }

    private List<ErrorInfoDto> validateServiceRequest(ServiceRequestSearchDto serviceRequestSearchDto) {

        logger.info(">>> Validation for validateServiceRequest :" + serviceRequestSearchDto);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {

            if (Strings.isNullOrEmpty(serviceRequestSearchDto.getRefPrimaryTrackingNo())) {
                ErrorInfoDto errorInfo = new ErrorInfoDto();
                errorInfo.setErrorMessage("Primary Ref Tracking# is Mandatory");
                errorInfoDtoList.add(errorInfo);
            }

        } catch (Exception exception) {
            logger.error("-- Get Service request details validation failed --");
        }

        logger.info("<< Validation for Common Fields Get Service Request Details");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateServiceRequestCommonFields(ServiceRequestDto serviceRequestDto) {

        logger.info(">>> Validation for doValidateServiceRequestCommonFields :" + serviceRequestDto);
        return validateServiceRequestCommonFields(serviceRequestDto);
    }

    private List<ErrorInfoDto> validateServiceRequestCommonFields(ServiceRequestDto serviceRequestDto) {

        logger.info(">>> Validation for validateServiceRequestCommonFields :" + serviceRequestDto);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        try {

            if (serviceRequestDto.getServiceRequestId() == 0 || serviceRequestDto.getServiceRequestId() == 0L) {
                ErrorInfoDto errorInfo = new ErrorInfoDto();
                errorInfo.setErrorMessage("ServiceRequestID# is Mandatory");
                errorInfoDtoList.add(errorInfo);
            }
        } catch (Exception exception) {
            logger.error("-- Get Service request details validation failed --");
        }

        logger.info("<< Validation for Common Fields Get Service Request Details");
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateServiceRequestSearch(ServiceRequestSearchDto serviceRequestSearchDto) {

        logger.info(">>> Validation of Search Service Request for input data error:" + serviceRequestSearchDto);
        return validateServiceRequestSearchCommonFields(serviceRequestSearchDto);
    }

    public List<ErrorInfoDto> validateServiceRequestSearchCommonFields(ServiceRequestSearchDto serviceRequestSearchDto) {

        List<ErrorInfoDto> errorInfoList = new ArrayList<>();

        if (null == serviceRequestSearchDto.getServiceRequestId() && null == serviceRequestSearchDto.getRefPrimaryTrackingNo() && null == serviceRequestSearchDto.getAssignee()
                && null == serviceRequestSearchDto.getServicePartnerCode() && null == serviceRequestSearchDto.getServicePartnerBuCode()
                && (null == serviceRequestSearchDto.getWorkflowStage() || serviceRequestSearchDto.getWorkflowStage().length() == 0)
                && (null == serviceRequestSearchDto.getStatus() || serviceRequestSearchDto.getStatus().length() == 0) && (null == serviceRequestSearchDto.getFromDate())
                && (null == serviceRequestSearchDto.getToDate()) && null == serviceRequestSearchDto.getServiceRequestType() && null == serviceRequestSearchDto.getWorkFlowAlert()
                && (null == serviceRequestSearchDto.getReferenceNumbers())) {
            validateSearchFilterCriteria(errorInfoList);
        }

        try {
            if (serviceRequestSearchDto.getFromDate() == null && serviceRequestSearchDto.getToDate() != null) {
                validateRequiredField("fromDate", serviceRequestSearchDto.getFromDate(), errorInfoList);
            } else if (serviceRequestSearchDto.getFromDate() != null && serviceRequestSearchDto.getToDate() == null) {
                validateRequiredField("toDate", serviceRequestSearchDto.getToDate(), errorInfoList);
            } else if (serviceRequestSearchDto.getToTime() != null || serviceRequestSearchDto.getFromTime() != null) {
                if (serviceRequestSearchDto.getFromDate() == null || serviceRequestSearchDto.getToDate() == null) {
                    validateRequiredField("toDate", serviceRequestSearchDto.getToDate(), errorInfoList);
                    validateRequiredField("fromDate", serviceRequestSearchDto.getFromDate(), errorInfoList);
                    validateRequiredField("fromTime", serviceRequestSearchDto.getFromTime(), errorInfoList);
                    validateRequiredField("totime", serviceRequestSearchDto.getToTime(), errorInfoList);
                }
            }
        } catch (InputValidationException e) {
            e.printStackTrace();
        }

        return errorInfoList;
    }

    public List<ErrorInfoDto> doValidateAuthorizationValidationRequest(Long servicerequestId, String authorizationCode, int authorizationCodeSequence) {

        logger.info(">>> Validation of Authorization validationRequest for input data error:" + "servicerequestId[" + servicerequestId + "]" + "authorizationCode[" + authorizationCode + "]"
                + "authorizationCodeSequence[" + authorizationCodeSequence + "]");
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        try {
            validateRequiredField("servicerequestId", servicerequestId, errorInfoList);
            validateRequiredField("authorizationCode", authorizationCode, errorInfoList);
            validateRequiredField("authorizationCodeSequence", String.valueOf(authorizationCodeSequence), errorInfoList);
        } catch (InputValidationException e) {
            e.printStackTrace();
        }

        return errorInfoList;
    }

    public List<ErrorInfoDto> doValidateGetAuthorizationCodeRequest(Long servicerequestId) {

        logger.info(">>> Validation of Get Authorization Code Request for input data error:" + "servicerequestId[" + servicerequestId + "]");
        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        try {
            validateRequiredField("servicerequestId", servicerequestId, errorInfoList);
        } catch (InputValidationException e) {
            e.printStackTrace();
        }

        return errorInfoList;
    }

    public void validateServiceRequestDateAfterXHours(String fieldName, Date serviceScheduleStartTime, double hours, List<ErrorInfoDto> errorInfoDtoList) {

        Date currentDate = new Date();

        long diffInMillis = serviceScheduleStartTime.getTime() - currentDate.getTime();

        if (diffInMillis < hours * 60 * 60 * 1000) {
            errorInfoDto = new ErrorInfoDto(30009, messageSource.getMessage(String.valueOf(ServiceRequestResponseCodes.SERVICE_START_TIME_INVALID_ERROR.getErrorCode()), new Object[] { "" }, null),
                    fieldName);
            errorInfoDtoList.add(errorInfoDto);
        }
    }

    public List<ErrorInfoDto> doValidateServiceRequestId(String servicerequestId, String refPrimaryTrackingNo) {

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        if (Strings.isNullOrEmpty(servicerequestId) && Strings.isNullOrEmpty(refPrimaryTrackingNo)) {
            ErrorInfoDto errorInfo = new ErrorInfoDto();
            errorInfo.setErrorMessage("Either ServiceRequestID/Reference Primary tracking Number(SR No) is Mandatory");
            errorInfoDtoList.add(errorInfo);
        }
        if (!Strings.isNullOrEmpty(servicerequestId) && !StringUtils.isNumeric(servicerequestId)) {
            ErrorInfoDto errorInfo = new ErrorInfoDto();
            errorInfo.setErrorMessage("ServiceRequestID# should be Numeric");
            errorInfoDtoList.add(errorInfo);
        }

        if (!Strings.isNullOrEmpty(refPrimaryTrackingNo) && !StringUtils.isNumeric(refPrimaryTrackingNo)) {
            ErrorInfoDto errorInfo = new ErrorInfoDto();
            errorInfo.setErrorMessage("RefPrimaryTrackingNo# should be Numeric");
            errorInfoDtoList.add(errorInfo);
        }
        logger.info(">>> Validation for doValidateServiceRequestId :" + servicerequestId);
        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateTechnicianDetailRequest(TechnicianDetailRequestDto technicianDetailRequestDto) {

        logger.info(">>> Validation for ValidateTechnicianDetailRequest :" + technicianDetailRequestDto);

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {

            validateRequiredField("partnerCode", technicianDetailRequestDto.getPartnerCode(), errorInfoDtoList);
            validateRequiredField("partnerBUCode", technicianDetailRequestDto.getPartnerBUCode(), errorInfoDtoList);

        } catch (Exception exception) {
            logger.error("--TechnicianDetailRequest validation failed --");
        }

        return errorInfoDtoList;

    }

    public List<ErrorInfoDto> doValidatePincodeServicebleRequest(String pinCode, String serviceRequestType) {

        logger.info(">>> Validation for Mandatory Data for Pincode service request :" + " pinCode[" + pinCode + "]" + " serviceRequestType[" + serviceRequestType + "]");
        return validateCommonFields(pinCode, serviceRequestType);

    }

    private List<ErrorInfoDto> validateCommonFields(String pinCode, String serviceRequestType) {

        logger.info(">>> Validation for Common Fields for Pincode service request :" + " pinCode[" + pinCode + "]" + " serviceRequestType[" + serviceRequestType + "]");

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {
            validateRequiredField("pinCode", pinCode, errorInfoDtoList);
            // validateRequiredField("serviceRequestType",serviceRequestType,errorInfoDtoList);
            validateFromCache("pinCode", pinCode, Constants.PINCODE_MASTER_CACHE, errorInfoDtoList);

        } catch (InputValidationException exception) {
            logger.info("-- Validate Pincode service request failed --");
        }

        return errorInfoDtoList;

    }

    public List<ErrorInfoDto> doValidateGetPincodesRequest(String serviceRequestType) {

        logger.info(">>> Validation for Mandatory Data for get Pincodes request : serviceRequestType[" + serviceRequestType + "]");
        return validateCommonFields(serviceRequestType);
    }

    private List<ErrorInfoDto> validateCommonFields(String serviceRequestType) {

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {
            validateRequiredField("serviceRequestType", serviceRequestType, errorInfoDtoList);

        } catch (InputValidationException exception) {
            logger.info("-- Validate Pincode service request failed --");
        }

        return errorInfoDtoList;

    }

    public List<ErrorInfoDto> doValidateWHCInspection(CompleteWHCInspectionRequestDto whcInspectionSREventDto) {

        logger.info(">>> Validation for Mandatory Data for get Pincodes request : serviceRequestType[" + whcInspectionSREventDto + "]");
        return validateCommonFields(whcInspectionSREventDto);
    }

    private List<ErrorInfoDto> validateCommonFields(CompleteWHCInspectionRequestDto whcInspectionSREventDto) {

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {
            // validateRequiredField("serviceRequestNO", whcInspectionSREventDto.getServiceRequestId(), errorInfoDtoList);
            // validateRequiredField("orderId", whcInspectionSREventDto.getRefPrimaryTrackingNo(), errorInfoDtoList);
            // validateRequiredField("memStatus", whcInspectionSREventDto.getMemStatus(), errorInfoDtoList);
            validateRequiredField("modifiedBy", whcInspectionSREventDto.getModifiedBy(), errorInfoDtoList);
            // validateRequiredField("inspectionAssessment", whcInspectionSREventDto.getInspectionAssessment(), errorInfoDtoList);
            /*
             * if (whcInspectionSREventDto.getInspectionAssessment() != null) { if (whcInspectionSREventDto.getInspectionAssessment().getAssets() == null ||
             * whcInspectionSREventDto.getInspectionAssessment().getAssets().isEmpty()) { populateMandatoryFieldError("assets", errorInfoDtoList); } else { for (Inspection inscpection :
             * whcInspectionSREventDto.getInspectionAssessment().getAssets()) { validateRequiredField("productCode ", inscpection.getProductCode(), errorInfoDtoList); validateRequiredField("brand for"
             * + inscpection.getProductCode(), inscpection.getBrand(), errorInfoDtoList); validateRequiredField("serialNo for" + inscpection.getProductCode(), inscpection.getSerialNo(),
             * errorInfoDtoList); validateRequiredField("modelNo for" + inscpection.getProductCode(), inscpection.getModelNo(), errorInfoDtoList); validateRequiredField("productAge for" +
             * inscpection.getProductCode(), inscpection.getProductAge(), errorInfoDtoList); } } }
             */

        } catch (InputValidationException exception) {
            logger.info("-- Validate Pincode service request failed --");
        }

        return errorInfoDtoList;

    }

    public List<ErrorInfoDto> doValidateServiceScheduleRequest(ServiceScheduleDto serviceScheduleDto) {

        logger.info(">>> Validation for Schedule Request :");

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();
        try {

            validateRequiredField("serviceRequestDate", serviceScheduleDto.getServiceRequestDate(), errorInfoDtoList);
            validateRequiredField("serviceRequestType", serviceScheduleDto.getServiceRequestType(), errorInfoDtoList);

        } catch (Exception exception) {
            logger.error("--ScheduleRequest validation failed --");
        }

        return errorInfoDtoList;
    }


    public List<ErrorInfoDto> validteCompleteInspectionAssetDetails(CompleteWHCInspectionRequestDto completeWHCInspectionRequestDto, ServiceRequestEntity serviceRequestEntity) {

        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<ErrorInfoDto>();

        if (completeWHCInspectionRequestDto.getInspectionAssessment() != null && !CollectionUtils.isEmpty(completeWHCInspectionRequestDto.getInspectionAssessment().getAssets())) {

            ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);

            if (!CollectionUtils.isEmpty(serviceRequestDto.getThirdPartyProperties())
                    && (serviceRequestDto.getThirdPartyProperties().get(Constants.MAX_ASSET_COUNT_KEY) != null && Integer.parseInt(serviceRequestDto.getThirdPartyProperties()
                            .get(Constants.MAX_ASSET_COUNT_KEY).toString()) < completeWHCInspectionRequestDto.getInspectionAssessment().getAssets().size())) {

                errorInfoDto = new ErrorInfoDto(ServiceRequestResponseCodes.NUMBER_OF_ASSET_IS_GREATER_THAN_MAX_QTY.getErrorCode(), messageSource.getMessage(
                        String.valueOf(ServiceRequestResponseCodes.NUMBER_OF_ASSET_IS_GREATER_THAN_MAX_QTY.getErrorCode()), new Object[] {
                                completeWHCInspectionRequestDto.getInspectionAssessment().getAssets().size(), serviceRequestDto.getThirdPartyProperties().get(Constants.MAX_ASSET_COUNT_KEY) }, null),
                        "assets");

                errorInfoDtoList.add(errorInfoDto);
            } else {
                for (Inspection asset : completeWHCInspectionRequestDto.getInspectionAssessment().getAssets()) {
                    GenericKeySetEntity genericKeySetEntity = genericKeySetCache.get(ServiceRequestType.WHC_INSPECTION.getRequestType() + "_" + ServiceConfig.DOCUMENT_CONFIG.getServiceConfig() + "_"
                            + asset.getProductCode());

                    if (genericKeySetEntity != null && !CollectionUtils.isEmpty(genericKeySetEntity.getGenericKeySetValueDetails())) {
                        for (GenericKeySetValueEntity genericKeySetValue : genericKeySetEntity.getGenericKeySetValueDetails()) {
                            if (IS_MANDATORY.equals(genericKeySetValue.getValue())) {
                                if (!CollectionUtils.isEmpty(asset.getImageStorageRef())) {
                                    boolean docUploaded = false;
                                    for (ImageStorageReference image : asset.getImageStorageRef()) {
                                        if (image.getDocumentTypeId().longValue() == Long.parseLong(genericKeySetValue.getKey())) {
                                            docUploaded = true;
                                            break;
                                        }
                                    }

                                    if (!docUploaded) {
                                        errorInfoDto = new ErrorInfoDto(ServiceRequestResponseCodes.MISSING_DOCUMENT.getErrorCode(),
                                                messageSource.getMessage(String.valueOf(ServiceRequestResponseCodes.MISSING_DOCUMENT.getErrorCode()),
                                                        new Object[] { genericKeySetValue.getKey(), asset.getProductCode() }, null), "documents");
                                        errorInfoDtoList.add(errorInfoDto);
                                    }
                                } else {
                                    errorInfoDto = new ErrorInfoDto(ServiceRequestResponseCodes.MISSING_DOCUMENT.getErrorCode(), messageSource.getMessage(
                                            String.valueOf(ServiceRequestResponseCodes.MISSING_DOCUMENT.getErrorCode()), new Object[] { genericKeySetValue.getKey(), asset.getProductCode() }, null),
                                            "documents");
                                    errorInfoDtoList.add(errorInfoDto);
                                }
                            }
                        }
                    }
                }
            }
        }

        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> validateMandatoryDocuments(ServiceRequestEntity serviceRequestEntity) {
        Collection<ServiceRequestTypeMstEntity> serivceTypeEntities = serviceRequestTypeMasterCache.getAll().values();
        List<ErrorInfoDto> errorList = new ArrayList<ErrorInfoDto>();
        ServiceRequestTypeMstEntity typeEntity = null;
        for (ServiceRequestTypeMstEntity serviceRequestTypeEntity : serivceTypeEntities) {
            if (serviceRequestEntity.getServiceRequestTypeId().longValue() == serviceRequestTypeEntity.getServiceRequestTypeId().longValue()) {
                typeEntity = serviceRequestTypeEntity;
                break;
            }
        }

        if (typeEntity != null) {
			OASYSCustMemDetails custMembershipDetails = oasysProxy
					.getMembershipAssetDetails(serviceRequestEntity.getReferenceNo(),
							Long.parseLong(serviceRequestEntity.getServiceRequestAssetEntity().get(0).getAssetReferenceNo()),"PE", false);
			List<DocTypeConfigDetailEntity> documentCheckLists = documentTypeConfigRepository
					.findByServiceRequestTypeIdAndReferenceCodeAndInsurancePartnerCode(
							typeEntity.getServiceRequestType(),
							custMembershipDetails.getMemberships().get(0).getAssets().get(0).getProdCode(),
							serviceRequestEntity.getInsurancePartnerCode());
            if (!CollectionUtils.isEmpty(documentCheckLists)) {
                List<ServiceDocumentEntity> uploadedDocs = serviceDocumentRepository.findByServiceRequestIdAndStatus(serviceRequestEntity.getServiceRequestId(), Constants.ACTIVE);
                Map<Long, ServiceDocumentEntity> uploadDocMap = new HashMap<Long, ServiceDocumentEntity>();
                if (!CollectionUtils.isEmpty(uploadedDocs)) {
                    for (ServiceDocumentEntity uploadedDoc : uploadedDocs) {
                        uploadDocMap.put(uploadedDoc.getDocumentTypeId(), uploadedDoc);
                    }
                }
                for (DocTypeConfigDetailEntity documentCheckList : documentCheckLists) {
                    if (IS_MANDATORY.equals(documentCheckList.getIsMandatory()) && uploadDocMap.get(documentCheckList.getDocTypeId()) == null) {
                        errorInfoDto = new ErrorInfoDto(ServiceRequestResponseCodes.MANDATORY_DOCUMENT_MISSING.getErrorCode(), messageSource.getMessage(
                                String.valueOf(ServiceRequestResponseCodes.MANDATORY_DOCUMENT_MISSING.getErrorCode()), new Object[] { documentCheckList.getDocTypeId() }, null), "documents");
                        errorList.add(errorInfoDto);
                    }
                }
            }
        }
        return errorList;
    }

}
