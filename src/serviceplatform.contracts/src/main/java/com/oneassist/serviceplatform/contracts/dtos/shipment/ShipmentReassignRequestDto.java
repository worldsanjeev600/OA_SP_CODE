package com.oneassist.serviceplatform.contracts.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Satish Kumar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentReassignRequestDto {
	
	private String	cancellogisticPartnerCode;
	private String	assignlogisticPartnerCode;
	private String  modifiedBy;
	private Long shipmentId;
	
	
	

	public Long getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	public String getCancellogisticPartnerCode() {
		return cancellogisticPartnerCode;
	}
	public void setCancellogisticPartnerCode(String cancellogisticPartnerCode) {
		this.cancellogisticPartnerCode = cancellogisticPartnerCode;
	}
	public String getAssignlogisticPartnerCode() {
		return assignlogisticPartnerCode;
	}
	public void setAssignlogisticPartnerCode(String assignlogisticPartnerCode) {
		this.assignlogisticPartnerCode = assignlogisticPartnerCode;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	@Override
	public String toString() {
		return "ShipmentReassignRequestDto [cancellogisticPartnerCode="
				+ cancellogisticPartnerCode + ", assignlogisticPartnerCode="
				+ assignlogisticPartnerCode + ", modifiedBy=" + modifiedBy
				+ "]";
	}

	
	
}