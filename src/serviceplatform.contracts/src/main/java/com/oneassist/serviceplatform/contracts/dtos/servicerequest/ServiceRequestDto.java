package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;

/**
 * @author Satish Kumar
 */
public class ServiceRequestDto extends BaseAuditDto implements Serializable {

    private static final long serialVersionUID = -7265551396715963031L;

    @ApiModelProperty(value = "Request Description")
    private String requestDescription;

    @ApiModelProperty(value = "Reference Primary Tracking Number eg. SR no")
    private String refPrimaryTrackingNo;

    @ApiModelProperty(value = "Reference Secondary Tracking Number eg. CSR ")
    private String refSecondaryTrackingNo;

    @ApiModelProperty(value = "Unique Identifier for the Service Request")
    private Long serviceRequestId;

    @ApiModelProperty(value = "Technician or runner ID")
    private Long assignee;

    @ApiModelProperty(value = "Schedule Slot Start Date Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    private Date scheduleSlotStartDateTime;

    @ApiModelProperty(value = "Schedule Slot End Date Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    private Date scheduleSlotEndDateTime;

    @ApiModelProperty(value = "Partner code which will fullfill service")
    private Long servicePartnerCode;

    @ApiModelProperty(value = "service_center_id/partner_bu")
    private Long servicePartnerBuCode;

    @ApiModelProperty(value = "Service Request Type")
    private String serviceRequestType;

    @ApiModelProperty(value = "Service Request Type Id")
    private Long serviceRequestTypeId;

    @ApiModelProperty(value = "Activiti BPM Id")
    private String workflowProcessId;

    @ApiModelProperty(value = "Workflow Data")
    private WorkflowData workflowData;

    @ApiModelProperty(value = "Initiating System", allowableValues = "1,2,3,4,5", notes = "PORTAL(1), OPS(2), CRM(3), BATCH(4), BUSINESS_PARTNER(5)")
    private Long initiatingSystem;

    @ApiModelProperty(value = "Reference Number of external system/ Membership Id for HA_EW/HA_BD/HA_AD")
    private String referenceNo;

    @ApiModelProperty(value = "Workflow Stage/ CMS Status")
    private String workflowStage;

    @ApiModelProperty(value = "Workflow Stage status/ CMS Sub-status")
    private String workflowStageStatus;

    @ApiModelProperty(value = "Workflow Alert")
    private String workflowAlert;

    @ApiModelProperty(value = "Workflow Definition key")
    private String workflowProcDefKey;

    @ApiModelProperty(value = "Workflow Data string format")
    @JsonIgnore
    private String workflowJsonString;

    @ApiModelProperty(value = "Service Due Date time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    private Date dueDateTime;

    @ApiModelProperty(value = "Service Request cancel reason")
    private String serviceCancelReason;

    @ApiModelProperty(value = "Service Request Address Details")
    private ServiceRequestAddressDetailsDto serviceRequestAddressDetails;

    @ApiModelProperty(value = "Service request feedback details")
    private ServiceRequestFeedbackDto serviceRequestFeedback;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Date of Breakdown/ Loss/ Accidental Damage/ Fire etc.")
    private Date dateOfIncident;

    @ApiModelProperty(value = "Place of Breakdown/ Loss/ Accidental Damage/ Fire etc.")
    private String placeOfIncident;

    @ApiModelProperty(value = "Workflow Value")
    private Integer workFlowValue;

    @ApiModelProperty(value = "List of issues reported by the Customer")
    private List<Issues> issueReportedByCustomer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Actual Start Date time")
    private Date actualStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Actual End Date time")
    private Date actualEndDateTime;

    @ApiModelProperty(value = "Incident Description")
    private String incidentDescription;

    @ApiModelProperty(value = "Start/End OTP")
    private String otp;

    @ApiModelProperty(value = "Remarks")
    private String remarks;

    @ApiModelProperty(value = "Pendency")
    private Map<String, Pendency> pendency;

    @ApiModelProperty(value = "External System Service Request Id")
    private Long externalSRReferenceId;

    @ApiModelProperty(value = "Cloud Storage Archive Id")
    private String cloudStorageArchiveId;

    @ApiModelProperty(value = "Cloud Storage Archive Status")
    private String cloudStorageJobStatus;

    @ApiModelProperty(value = "Customer Id")
    private Long customerId;

    @ApiModelProperty(value = "Service Documents")
    private List<ServiceDocumentDto> serviceDocuments;

    @ApiModelProperty(value = "Service Request Assets")
    private List<ServiceRequestAssetRequestDto> assets;

    @ApiModelProperty(value = "Incident Detail")
    private Map<String, Object> incidentDetail;

    @ApiModelProperty(value = "Advice Id")
    private String adviceId;

    @ApiModelProperty(value = "Insurance Partner code")
    private Long insurancePartnerCode;

    private String source;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    private String message;

    @ApiModelProperty(value = "Third Party property Related Data")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Map<String, Object> thirdPartyProperties;

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public List<Issues> getIssueReportedByCustomer() {
        return issueReportedByCustomer;
    }

    public void setIssueReportedByCustomer(List<Issues> issueReportedByCustomer) {
        this.issueReportedByCustomer = issueReportedByCustomer;
    }

    public Integer getWorkFlowValue() {
        return workFlowValue;
    }

    public void setWorkFlowValue(Integer workFlowValue) {
        this.workFlowValue = workFlowValue;
    }

    public Date getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(Date dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
    }

    public String getPlaceOfIncident() {
        return placeOfIncident;
    }

    public void setPlaceOfIncident(String placeOfIncident) {
        this.placeOfIncident = placeOfIncident;
    }

    public String getRequestDescription() {

        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {

        this.requestDescription = requestDescription;
    }

    public String getRefPrimaryTrackingNo() {
        return refPrimaryTrackingNo;
    }

    public void setRefPrimaryTrackingNo(String refPrimaryTrackingNo) {
        this.refPrimaryTrackingNo = refPrimaryTrackingNo;
    }

    public String getRefSecondaryTrackingNo() {
        return refSecondaryTrackingNo;
    }

    public void setRefSecondaryTrackingNo(String refSecondaryTrackingNo) {
        this.refSecondaryTrackingNo = refSecondaryTrackingNo;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public Long getServicePartnerCode() {
        return servicePartnerCode;
    }

    public void setServicePartnerCode(Long servicePartnerCode) {
        this.servicePartnerCode = servicePartnerCode;
    }

    public Long getServicePartnerBuCode() {
        return servicePartnerBuCode;
    }

    public void setServicePartnerBuCode(Long servicePartnerBuCode) {
        this.servicePartnerBuCode = servicePartnerBuCode;
    }

    public String getWorkflowProcessId() {
        return workflowProcessId;
    }

    public void setWorkflowProcessId(String workflowProcessId) {
        this.workflowProcessId = workflowProcessId;
    }

    public WorkflowData getWorkflowData() {
        return workflowData;
    }

    public void setWorkflowData(WorkflowData workflowData) {
        this.workflowData = workflowData;
    }

    public Long getInitiatingSystem() {
        return initiatingSystem;
    }

    public void setInitiatingSystem(Long initiatingSystem) {
        this.initiatingSystem = initiatingSystem;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getWorkflowStage() {
        return workflowStage;
    }

    public void setWorkflowStage(String workflowStage) {
        this.workflowStage = workflowStage;
    }

    public String getWorkflowStageStatus() {
        return workflowStageStatus;
    }

    public void setWorkflowStageStatus(String workflowStageStatus) {
        this.workflowStageStatus = workflowStageStatus;
    }

    public String getWorkflowAlert() {
        return workflowAlert;
    }

    public void setWorkflowAlert(String workflowAlert) {
        this.workflowAlert = workflowAlert;
    }

    public String getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(String serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public String getWorkflowProcDefKey() {
        return workflowProcDefKey;
    }

    public void setWorkflowProcDefKey(String workflowProcDefKey) {
        this.workflowProcDefKey = workflowProcDefKey;
    }

    public Date getScheduleSlotStartDateTime() {
        return scheduleSlotStartDateTime;
    }

    public void setScheduleSlotStartDateTime(Date scheduleSlotStartDateTime) {
        this.scheduleSlotStartDateTime = scheduleSlotStartDateTime;
    }

    public Date getScheduleSlotEndDateTime() {
        return scheduleSlotEndDateTime;
    }

    public void setScheduleSlotEndDateTime(Date scheduleSlotEndDateTime) {
        this.scheduleSlotEndDateTime = scheduleSlotEndDateTime;
    }

    public String getWorkflowJsonString() {
        return workflowJsonString;
    }

    public void setWorkflowJsonString(String workflowJsonString) {
        this.workflowJsonString = workflowJsonString;
    }

    public Date getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Date dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getServiceCancelReason() {
        return serviceCancelReason;
    }

    public void setServiceCancelReason(String serviceCancelReason) {
        this.serviceCancelReason = serviceCancelReason;
    }

    public ServiceRequestAddressDetailsDto getServiceRequestAddressDetails() {
        return serviceRequestAddressDetails;
    }

    public void setServiceRequestAddressDetails(ServiceRequestAddressDetailsDto serviceRequestAddressDetails) {
        this.serviceRequestAddressDetails = serviceRequestAddressDetails;
    }

    public ServiceRequestFeedbackDto getServiceRequestFeedback() {
        return serviceRequestFeedback;
    }

    public void setServiceRequestFeedback(ServiceRequestFeedbackDto serviceRequestFeedback) {
        this.serviceRequestFeedback = serviceRequestFeedback;
    }

    public Date getActualStartDateTime() {
        return actualStartDateTime;
    }

    public void setActualStartDateTime(Date actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
    }

    public Date getActualEndDateTime() {
        return actualEndDateTime;
    }

    public void setActualEndDateTime(Date actualEndDateTime) {
        this.actualEndDateTime = actualEndDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getServiceRequestTypeId() {
        return serviceRequestTypeId;
    }

    public void setServiceRequestTypeId(Long serviceRequestTypeId) {
        this.serviceRequestTypeId = serviceRequestTypeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Map<String, Object> getThirdPartyProperties() {
        return thirdPartyProperties;
    }

    public void setThirdPartyProperties(Map<String, Object> thirdPartyProperties) {
        this.thirdPartyProperties = thirdPartyProperties;
    }

    public Map<String, Pendency> getPendency() {
        return pendency;
    }

    public void setPendency(Map<String, Pendency> pendency) {
        this.pendency = pendency;
    }

    public Long getExternalSRReferenceId() {
        return externalSRReferenceId;
    }

    public void setExternalSRReferenceId(Long externalSRReferenceId) {
        this.externalSRReferenceId = externalSRReferenceId;
    }

    public String getCloudStorageArchiveId() {
        return cloudStorageArchiveId;
    }

    public void setCloudStorageArchiveId(String cloudStorageArchiveId) {
        this.cloudStorageArchiveId = cloudStorageArchiveId;
    }

    public String getCloudStorageJobStatus() {
        return cloudStorageJobStatus;
    }

    public void setCloudStorageJobStatus(String cloudStorageJobStatus) {
        this.cloudStorageJobStatus = cloudStorageJobStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<ServiceDocumentDto> getServiceDocuments() {
        return serviceDocuments;
    }

    public void setServiceDocuments(List<ServiceDocumentDto> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
    }

    public List<ServiceRequestAssetRequestDto> getAssets() {
        return assets;
    }

    public void setAssets(List<ServiceRequestAssetRequestDto> assets) {
        this.assets = assets;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getIncidentDetail() {
        return incidentDetail;
    }

    public void setIncidentDetail(Map<String, Object> incidentDetail) {
        this.incidentDetail = incidentDetail;
    }

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public Long getInsurancePartnerCode() {
        return insurancePartnerCode;
    }

    public void setInsurancePartnerCode(Long insurancePartnerCode) {
        this.insurancePartnerCode = insurancePartnerCode;
    }

    @Override
    public String toString() {
        return "ServiceRequestDto [requestDescription=" + requestDescription + ", refPrimaryTrackingNo=" + refPrimaryTrackingNo + ", refSecondaryTrackingNo=" + refSecondaryTrackingNo
                + ", serviceRequestId=" + serviceRequestId + ", assignee=" + assignee + ", scheduleSlotStartDateTime=" + scheduleSlotStartDateTime + ", scheduleSlotEndDateTime="
                + scheduleSlotEndDateTime + ", servicePartnerCode=" + servicePartnerCode + ", servicePartnerBuCode=" + servicePartnerBuCode + ", serviceRequestType=" + serviceRequestType
                + ", serviceRequestTypeId=" + serviceRequestTypeId + ", workflowProcessId=" + workflowProcessId + ", workflowData=" + workflowData + ", initiatingSystem=" + initiatingSystem
                + ", referenceNo=" + referenceNo + ", workflowStage=" + workflowStage + ", workflowStageStatus=" + workflowStageStatus + ", workflowAlert=" + workflowAlert + ", workflowProcDefKey="
                + workflowProcDefKey + ", workflowJsonString=" + workflowJsonString + ", dueDateTime=" + dueDateTime + ", serviceCancelReason=" + serviceCancelReason
                + ", serviceRequestAddressDetails=" + serviceRequestAddressDetails + ", serviceRequestFeedback=" + serviceRequestFeedback + ", dateOfIncident=" + dateOfIncident + ", placeOfIncident="
                + placeOfIncident + ", workFlowValue=" + workFlowValue + ", issueReportedByCustomer=" + issueReportedByCustomer + ", actualStartDateTime=" + actualStartDateTime
                + ", actualEndDateTime=" + actualEndDateTime + ", incidentDescription=" + incidentDescription + ", otp=" + otp + ", remarks=" + remarks + ", pendency=" + pendency
                + ", externalSRReferenceId=" + externalSRReferenceId + ", cloudStorageArchiveId=" + cloudStorageArchiveId + ", cloudStorageJobStatus=" + cloudStorageJobStatus + ", customerId="
                + customerId + ", serviceDocuments=" + serviceDocuments + ", assets=" + assets + ", incidentDetail=" + incidentDetail + ", adviceId=" + adviceId + ", insurancePartnerCode="
                + insurancePartnerCode + ", source=" + source + ", message=" + message + ", thirdPartyProperties=" + thirdPartyProperties + "]";
    }

}
