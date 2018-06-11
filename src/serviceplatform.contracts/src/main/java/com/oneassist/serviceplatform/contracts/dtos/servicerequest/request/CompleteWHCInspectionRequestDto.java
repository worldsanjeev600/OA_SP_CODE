package com.oneassist.serviceplatform.contracts.dtos.servicerequest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.*;

/**
 * @author Suresh Uppathala
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description="CompleteWHC inspection  Request Model")
public class CompleteWHCInspectionRequestDto {
	
	private String refPrimaryTrackingNo;
	private Long serviceRequestId;
	private String modifiedBy;	
	private String memStatus;
	private InspectionAssessment  inspectionAssessment;
	
	/**
	 * @return the memStatus
	 */
	public String getMemStatus() {
		return memStatus;
	}
	public String getRefPrimaryTrackingNo() {
		return refPrimaryTrackingNo;
	}
	public void setRefPrimaryTrackingNo(String refPrimaryTrackingNo) {
		this.refPrimaryTrackingNo = refPrimaryTrackingNo;
	}
	/**
	 * @param memStatus the memStatus to set
	 */
	public void setMemStatus(String memStatus) {
		this.memStatus = memStatus;
	}
	/**
	 * @return the inspectionAssessment
	 */
	public InspectionAssessment getInspectionAssessment() {
		return inspectionAssessment;
	}
	/**
	 * @param inspectionAssessment the inspectionAssessment to set
	 */
	public void setInspectionAssessment(InspectionAssessment inspectionAssessment) {
		this.inspectionAssessment = inspectionAssessment;
	}
	/**
	 * @return the serviceRequestId
	 */
	public Long getServiceRequestId() {
		return serviceRequestId;
	}
	/**
	 * @param serviceRequestId the serviceRequestId to set
	 */
	public void setServiceRequestId(Long serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	@Override
	public String toString() {
		return "CompleteWHCInspectionRequestDto [refPrimaryTrackingNo=" + refPrimaryTrackingNo + ", serviceRequestId="
				+ serviceRequestId + ", modifiedBy=" + modifiedBy + ", memStatus=" + memStatus
				+ ", inspectionAssessment=" + inspectionAssessment + "]";
	}	
	
	
}
