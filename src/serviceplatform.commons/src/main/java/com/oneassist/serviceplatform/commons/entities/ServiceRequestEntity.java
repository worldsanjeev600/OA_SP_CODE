package com.oneassist.serviceplatform.commons.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author Satish Kumar
 */
@Entity
@Table(name = "OA_SERVICE_REQUEST_DTL")
public class ServiceRequestEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -1486576349069944611L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERVICE_REQUEST_DTL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "SERVICE_REQUEST_ID")
    private Long serviceRequestId;

    @Column(name = "REQUEST_DESCRIPTION")
    private String requestDescription;

    @Column(name = "REF_PRIMARY_TRACKING_NO")
    private String refPrimaryTrackingNo;

    @Column(name = "REF_SECONDARY_TRACKING_NO")
    private String refSecondaryTrackingNo;

    @Column(name = "SERVICE_REQUEST_TYPE_ID")
    private Long serviceRequestTypeId;

    @Column(name = "ASSIGNEE")
    private Long assignee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCHEDULE_SLOT_START_DATETIME")
    private Date scheduleSlotStartDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SCHEDULE_SLOT_END_DATETIME")
    private Date scheduleSlotEndDateTime;

    @Column(name = "SERVICE_PARTNER_CODE")
    private Long servicePartnerCode;

    @Column(name = "SERVICE_PARTNER_BU_CODE")
    private Long servicePartnerBuCode;

    @Column(name = "WORKFLOW_PROCESS_ID")
    private String workflowProcessId;

    @Column(name = "WORKFLOW_DATA")
    private String workflowData;

    @Column(name = "INITIATING_SYSTEM")
    private Long initiatingSystem;

    @Column(name = "REFERENCE_NO")
    private String referenceNo;

    @Column(name = "WORKFLOW_STAGE")
    private String workflowStage;

    @Column(name = "WORKFLOW_ALERT")
    private String workflowAlert;

    @Column(name = "WORKFLOW_STAGE_STATUS")
    private String workflowStageStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATETIME")
    private Date dueDateTime;

    @Column(name = "WORKFLOW_PROC_DEF_KEY")
    private String workflowProcDefKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_START_DATETIME")
    private Date actualStartDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTUAL_END_DATETIME")
    private Date actualEndDateTime;

    @Column(name = "FEEDBACK_RATING")
    private String feedbackRating;

    @Column(name = "FEEDBACK_CODE")
    private String feedbackCode;

    @Column(name = "FEEDBACK_COMMENTS")
    private String feedbackComments;

    @Column(name = "THIRDPARTY_PROPERTIES")
    private String thirdPartyProperties;

    @Column(name = "CLOUD_STORAGE_ARCHIVE_ID")
    private String cloudStorageArchiveId;

    @Column(name = "CLOUD_JOB_STATUS")
    private String cloudJobStatus;

    @Column(name = "REMARK")
    private String remarks;

    @Column(name = "EXTERNAL_SR_REFERENCE_ID")
    private String externalSRReferenceId;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "SERVICE_REQUEST_ID", referencedColumnName = "SERVICE_REQUEST_ID", insertable = false, updatable = false)
    private List<ServiceRequestAssetEntity> serviceRequestAssetEntity;

    @Column(name = "PENDENCY")
    private String pendency;

    @Column(name = "ADVICE_ID")
    private String adviceId;

    @Column(name = "INSURANCE_PARTNER_CODE")
    private Long insurancePartnerCode;

    public String getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(String adviceId) {
        this.adviceId = adviceId;
    }

    public String getCloudStorageArchiveId() {
        return cloudStorageArchiveId;
    }

    public void setCloudStorageArchiveId(String cloudStorageArchiveId) {
        this.cloudStorageArchiveId = cloudStorageArchiveId;
    }

    public Long getServiceRequestId() {

        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {

        this.serviceRequestId = serviceRequestId;
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

    public Long getServiceRequestTypeId() {

        return serviceRequestTypeId;
    }

    public void setServiceRequestTypeId(Long serviceRequestTypeId) {

        this.serviceRequestTypeId = serviceRequestTypeId;
    }

    public Long getAssignee() {

        return assignee;
    }

    public void setAssignee(Long assignee) {

        this.assignee = assignee;
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

    public String getWorkflowData() {

        return workflowData;
    }

    public void setWorkflowData(String workflowData) {

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

    public String getWorkflowAlert() {

        return workflowAlert;
    }

    public void setWorkflowAlert(String workflowAlert) {

        this.workflowAlert = workflowAlert;
    }

    public String getWorkflowStageStatus() {

        return workflowStageStatus;
    }

    public void setWorkflowStageStatus(String workflowStageStatus) {

        this.workflowStageStatus = workflowStageStatus;
    }

    public Date getDueDateTime() {

        return dueDateTime;
    }

    public void setDueDateTime(Date dueDateTime) {

        this.dueDateTime = dueDateTime;
    }

    public String getWorkflowProcDefKey() {

        return workflowProcDefKey;
    }

    public void setWorkflowProcDefKey(String workflowProcDefKey) {

        this.workflowProcDefKey = workflowProcDefKey;
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

    public void setFeedbackRating(String feedbackRating) {

        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackRating() {

        return feedbackRating;
    }

    public void setFeedbackCode(String feedbackCode) {

        this.feedbackCode = feedbackCode;
    }

    public String getFeedbackCode() {

        return feedbackCode;
    }

    public void setFeedbackComments(String feedbackComments) {

        this.feedbackComments = feedbackComments;
    }

    public String getFeedbackComments() {

        return feedbackComments;
    }

    public String getCloudJobStatus() {
        return cloudJobStatus;
    }

    public void setCloudJobStatus(String cloudJobStatus) {
        this.cloudJobStatus = cloudJobStatus;
    }

    public String getThirdPartyProperties() {
        return thirdPartyProperties;
    }

    public void setThirdPartyProperties(String thirdPartyProperties) {
        this.thirdPartyProperties = thirdPartyProperties;
    }

    public List<ServiceRequestAssetEntity> getServiceRequestAssetEntity() {
        return serviceRequestAssetEntity;
    }

    public void setServiceRequestAssetEntity(List<ServiceRequestAssetEntity> serviceRequestAssetEntity) {
        this.serviceRequestAssetEntity = serviceRequestAssetEntity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getExternalSRReferenceId() {
        return externalSRReferenceId;
    }

    public void setExternalSRReferenceId(String externalSRReferenceId) {
        this.externalSRReferenceId = externalSRReferenceId;
    }

    public String getPendency() {
        return pendency;
    }

    public void setPendency(String pendency) {
        this.pendency = pendency;
    }

    public Long getInsurancePartnerCode() {
        return insurancePartnerCode;
    }

    public void setInsurancePartnerCode(Long insurancePartnerCode) {
        this.insurancePartnerCode = insurancePartnerCode;
    }

}
