package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.externalcontracts.BaseEventDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class WHCInspectionSREventDto extends BaseEventDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7432113307771989693L;
	private Long serviceRequestId;	
	private String refPrimaryTrackingNo;	
	private String memStatus;	
	private InspectionAssessment  inspectionAssessment;
	private ServiceAddressDetailDto addressDetails;
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
	 * @return the refPrimaryTrackingNo
	 */
	public String getRefPrimaryTrackingNo() {
		return refPrimaryTrackingNo;
	}
	/**
	 * @param refPrimaryTrackingNo the refPrimaryTrackingNo to set
	 */
	public void setRefPrimaryTrackingNo(String refPrimaryTrackingNo) {
		this.refPrimaryTrackingNo = refPrimaryTrackingNo;
	}
	/**
	 * @return the memStatus
	 */
	public String getMemStatus() {
		return memStatus;
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
	 * @return the addressDetails
	 */
	public ServiceAddressDetailDto getAddressDetails() {
		return addressDetails;
	}
	/**
	 * @param addressDetails the addressDetails to set
	 */
	public void setAddressDetails(ServiceAddressDetailDto addressDetails) {
		this.addressDetails = addressDetails;
	}
	@Override
	public String toString() {
		return "WHCInspectionSREventDto [serviceRequestId=" + serviceRequestId + ", refPrimaryTrackingNo="
				+ refPrimaryTrackingNo + ", memStatus=" + memStatus + ", inspectionAssessment=" + inspectionAssessment
				+ ", addressDetails=" + addressDetails + "]";
	}
	
}
