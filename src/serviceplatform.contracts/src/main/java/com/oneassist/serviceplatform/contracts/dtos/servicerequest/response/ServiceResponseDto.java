package com.oneassist.serviceplatform.contracts.dtos.servicerequest.response;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;
import com.oneassist.serviceplatform.contracts.dtos.activiti.ActivitiHistoryTaskInstanceDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Pendency;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestFeedbackResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceDocumentDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.response.UserProfileData;

/**
 * @author Satish Kumar
 */

public class ServiceResponseDto extends BaseAuditDto implements Serializable {

    private static final long serialVersionUID = -7265551396715963031L;

    @ApiModelProperty(value = "Service request id")
    private Long serviceRequestId;

    @ApiModelProperty(value = "Service Request Type")
    private String serviceRequestType;

    @ApiModelProperty(value = "Service Request Type Name")
    private String serviceRequestTypeName;

    @ApiModelProperty(value = "Request Description")
    private String requestDescription;

    @ApiModelProperty(value = "Reference number can be membership id or account no")
    private String referenceNo;

    @ApiModelProperty(value = "Reference Primary Tracking Number eg. SR no")
    private String refPrimaryTrackingNo;

    @ApiModelProperty(value = "Reference Secondary Tracking Number eg. CSR ")
    private String refSecondaryTrackingNo;

    @ApiModelProperty(value = "Technician or runner ID")
    private Long assignee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Schedule Slot Start Date time")
    private Date scheduleSlotStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Schedule Slot End Date time")
    private Date scheduleSlotEndDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Service Request Due Datetime")
    private Date dueDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Actual Start Date time")
    private Date actualStartDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss", timezone = "IST")
    @ApiModelProperty(value = "Actual End Date time")
    private Date actualEndDateTime;

    @ApiModelProperty(value = "Partner code which will fullfill/service this request")
    private Long servicePartnerCode;

    @ApiModelProperty(value = "Service center id / Partner BU")
    private Long servicePartnerBuCode;

    @ApiModelProperty(value = "Activiti BPM Id. Unique workflow process id for this service request")
    private String workflowProcessId;

    @ApiModelProperty(value = "Workflow Data")
    private WorkflowData workflowData;

    @ApiModelProperty(value = "Initiating System")
    private Long initiatingSystem;

    @ApiModelProperty(value = "Workflow Stage/ CMS Status")
    private String workflowStage;

    @ApiModelProperty(value = "Workflow Stage status/ CMS Sub-status")
    private String workflowStageStatus;

    @ApiModelProperty(value = "Workflow alert message")
    private String workflowAlert;

    @ApiModelProperty(value = "Workflow Process Definition Key")
    private String workflowProcDefKey;

    @ApiModelProperty(value = "Service Address")
    private ServiceAddressDetailDto serviceAddress;

    @ApiModelProperty(value = "Service Document")
    private List<ServiceDocumentDto> serviceDocuments;

    @ApiModelProperty(value = "Service request feedback details")
    private ServiceRequestFeedbackResponseDto serviceRequestFeedback;

    @ApiModelProperty(value = "Activity Task")
    private List<ActivitiHistoryTaskInstanceDto> activitiHistoryTasks;

    @ApiModelProperty(value = "Shipment Details")
    private List<ShipmentDetailsDto> shipmentDetails;

    @ApiModelProperty(value = "Additional Attributes")
    private List<String> additionalAttributes;

    @ApiModelProperty(value = "Technician Details")
    private UserProfileData technicianProfileDetails;

    @ApiModelProperty(value = "Third Party Properties")
    private Map<String, Object> thirdPartyProperties;

    @ApiModelProperty(value = "List of assets for which SR is raised")
    private List<ServiceRequestAssetResponseDto> assets;

    @ApiModelProperty(value = "Date of Incident")
    private String dateOfIncident;

    @ApiModelProperty(value = "Pendency")
    @JsonInclude(Include.NON_NULL)
    private Map<String, Pendency> pendency;

    @ApiModelProperty(value = "Archival Id")
    private String cloudStorageArchiveId;

    @ApiModelProperty(value = "Advice Id")
    private String adviceId;

    @ApiModelProperty(value = "Insurance Partner code")
    private Long insurancePartnerCode;

    public String getCloudStorageArchiveId() {
        return cloudStorageArchiveId;
    }

    public void setCloudStorageArchiveId(String cloudStorageArchiveId) {
        this.cloudStorageArchiveId = cloudStorageArchiveId;
    }

    public Map<String, Object> getThirdPartyProperties() {
        return thirdPartyProperties;
    }

    public void setThirdPartyProperties(Map<String, Object> thirdPartyProperties) {
        this.thirdPartyProperties = thirdPartyProperties;
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

    public Date getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Date dueDateTime) {
        this.dueDateTime = dueDateTime;
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

    public String getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(String serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public ServiceRequestFeedbackResponseDto getServiceRequestFeedback() {
        return serviceRequestFeedback;
    }

    public void setServiceRequestFeedback(ServiceRequestFeedbackResponseDto serviceRequestFeedback) {
        this.serviceRequestFeedback = serviceRequestFeedback;
    }

    public List<ShipmentDetailsDto> getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(List<ShipmentDetailsDto> shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

    public ServiceAddressDetailDto getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(ServiceAddressDetailDto serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public List<ServiceDocumentDto> getServiceDocuments() {
        return serviceDocuments;
    }

    public void setServiceDocuments(List<ServiceDocumentDto> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
    }

    public List<ActivitiHistoryTaskInstanceDto> getActivitiHistoryTasks() {
        return activitiHistoryTasks;
    }

    public void setActivitiHistoryTasks(List<ActivitiHistoryTaskInstanceDto> activitiHistoryTasks) {
        this.activitiHistoryTasks = activitiHistoryTasks;
    }

    public String getServiceRequestTypeName() {
        return serviceRequestTypeName;
    }

    public void setServiceRequestTypeName(String serviceRequestTypeName) {
        this.serviceRequestTypeName = serviceRequestTypeName;
    }

    public List<String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public UserProfileData getTechnicianProfileDetails() {
        return technicianProfileDetails;
    }

    public void setTechnicianProfileDetails(UserProfileData technicianProfileDetails) {
        this.technicianProfileDetails = technicianProfileDetails;
    }

    public List<ServiceRequestAssetResponseDto> getAssets() {
        return assets;
    }

    public void setAssets(List<ServiceRequestAssetResponseDto> assets) {
        this.assets = assets;
    }

    public Map<String, Pendency> getPendency() {
        return pendency;
    }

    public void setPendency(Map<String, Pendency> pendency) {
        this.pendency = pendency;
    }

    public String getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(String dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
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
        return "ServiceResponseDto [serviceRequestId=" + serviceRequestId + ", serviceRequestType=" + serviceRequestType + ", serviceRequestTypeName=" + serviceRequestTypeName
                + ", requestDescription=" + requestDescription + ", referenceNo=" + referenceNo + ", refPrimaryTrackingNo=" + refPrimaryTrackingNo + ", refSecondaryTrackingNo="
                + refSecondaryTrackingNo + ", assignee=" + assignee + ", scheduleSlotStartDateTime=" + scheduleSlotStartDateTime + ", scheduleSlotEndDateTime=" + scheduleSlotEndDateTime
                + ", dueDateTime=" + dueDateTime + ", actualStartDateTime=" + actualStartDateTime + ", actualEndDateTime=" + actualEndDateTime + ", servicePartnerCode=" + servicePartnerCode
                + ", servicePartnerBuCode=" + servicePartnerBuCode + ", workflowProcessId=" + workflowProcessId + ", workflowData=" + workflowData + ", initiatingSystem=" + initiatingSystem
                + ", workflowStage=" + workflowStage + ", workflowStageStatus=" + workflowStageStatus + ", workflowAlert=" + workflowAlert + ", workflowProcDefKey=" + workflowProcDefKey
                + ", serviceAddress=" + serviceAddress + ", serviceDocuments=" + serviceDocuments + ", serviceRequestFeedback=" + serviceRequestFeedback + ", activitiHistoryTasks="
                + activitiHistoryTasks + ", shipmentDetails=" + shipmentDetails + ", additionalAttributes=" + additionalAttributes + ", technicianProfileDetails=" + technicianProfileDetails
                + ", thirdPartyProperties=" + thirdPartyProperties + ", assets=" + assets + ", dateOfIncident=" + dateOfIncident + ", pendency=" + pendency + ", cloudStorageArchiveId="
                + cloudStorageArchiveId + ", adviceId=" + adviceId + ", insurancePartnerCode=" + insurancePartnerCode + "]";
    }

}
