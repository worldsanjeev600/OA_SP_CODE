package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Alok Singh
 */
public class ServiceRequestSearchDto implements Serializable {

    private static final long serialVersionUID = -7265551396715963033L;

    private Long serviceRequestId;
    private String refPrimaryTrackingNo;
    private String refSecondaryTrackingNo;
    private Long servicePartnerCode;
    private Long servicePartnerBuCode;
    private String serviceRequestType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
    private Date fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
    private Date toDate;
    private Long assignee;
    private String status;
    
    private String fromTime;
    private String toTime;
    
    private String workflowStage;
    private String requiresAdditionalDetails;
    private String workFlowAlert;
    
    private String referenceNumbers;
    private String technicianProfileRequired ;
    
    private String feedbackStatus;
    private String sortBy;
    private String sortOrder;
    private String workFlowStageStatus;
    private Integer initiatingSystem;
    private Set<String> options;
    
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

    public String getServiceRequestType() {

        return serviceRequestType;
    }

    public void setServiceRequestType(String serviceRequestType) {

        this.serviceRequestType = serviceRequestType;
    }

    public Date getFromDate() {

        return fromDate;
    }

    public void setFromDate(Date fromDate) {

        this.fromDate = fromDate;
    }

    public Date getToDate() {

        return toDate;
    }

    public void setToDate(Date toDate) {

        this.toDate = toDate;
    }

    public Long getAssignee() {

        return assignee;
    }

    public void setAssignee(Long assignee) {

        this.assignee = assignee;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getWorkflowStage() {

        return workflowStage;
    }

    public void setWorkflowStage(String workflowStage) {

        this.workflowStage = workflowStage;
    }

    public Long getServiceRequestId() {

        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {

        this.serviceRequestId = serviceRequestId;
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

    public String getRequiresAdditionalDetails() {
        return requiresAdditionalDetails;
    }

    public void setRequiresAdditionalDetails(String requiresAdditionalDetails) {
        this.requiresAdditionalDetails = requiresAdditionalDetails;
    }

    /**
	 * @return the fromTime
	 */
	public String getFromTime() {
		return fromTime;
	}

	/**
	 * @param fromTime the fromTime to set
	 */
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	/**
	 * @return the toTime
	 */
	public String getToTime() {
		return toTime;
	}

	/**
	 * @param toTime the toTime to set
	 */
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	
	
    public String getWorkFlowAlert() {
        return workFlowAlert;
    }

    
    public void setWorkFlowAlert(String workFlowAlert) {
        this.workFlowAlert = workFlowAlert;
    }

	public String getReferenceNumbers() {
		return referenceNumbers;
	}

	public void setReferenceNumbers(String referenceNumbers) {
		this.referenceNumbers = referenceNumbers;
	}

	public String getTechnicianProfileRequired() {
		return technicianProfileRequired;
	}

	public void setTechnicianProfileRequired(String technicianProfileRequired) {
		this.technicianProfileRequired = technicianProfileRequired;
	}

	public String getFeedbackStatus() {
		return feedbackStatus;
	}

	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	
	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public String getWorkFlowStageStatus() {
		return workFlowStageStatus;
	}

	public void setWorkFlowStageStatus(String workFlowStageStatus) {
		this.workFlowStageStatus = workFlowStageStatus;
	}

	public Integer getInitiatingSystem() {
		return initiatingSystem;
	}

	public void setInitiatingSystem(Integer initiatingSystem) {
		this.initiatingSystem = initiatingSystem;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "ServiceRequestSearchDto [serviceRequestId=" + serviceRequestId + ", refPrimaryTrackingNo="
				+ refPrimaryTrackingNo + ", refSecondaryTrackingNo=" + refSecondaryTrackingNo + ", servicePartnerCode="
				+ servicePartnerCode + ", servicePartnerBuCode=" + servicePartnerBuCode + ", serviceRequestType="
				+ serviceRequestType + ", fromDate=" + fromDate + ", toDate=" + toDate + ", assignee=" + assignee
				+ ", status=" + status + ", fromTime=" + fromTime + ", toTime=" + toTime + ", workflowStage="
				+ workflowStage + ", requiresAdditionalDetails=" + requiresAdditionalDetails + ", workFlowAlert="
				+ workFlowAlert + ", referenceNumbers=" + referenceNumbers + ", technicianProfileRequired="
				+ technicianProfileRequired + ", feedbackStatus=" + feedbackStatus + ", sortBy=" + sortBy
				+ ", sortOrder=" + sortOrder + ", workFlowStageStatus=" + workFlowStageStatus + ", initiatingSystem="
				+ initiatingSystem + "]";
	}

}
