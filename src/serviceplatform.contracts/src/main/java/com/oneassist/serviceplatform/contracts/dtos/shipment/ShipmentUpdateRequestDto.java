package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Satish Kumar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentUpdateRequestDto implements Serializable{
	private static final long serialVersionUID = 6243220483574442793L;
	private String  shipmentId;
	private String	logisticPartnerCode;
	private String	logisticPartnerRefTrackingNumber;
	private String	currentStage;
	private String  status;
	private String  modifiedBy;
	private String reasonForFailure;
	public String getLogisticPartnerCode() {
		return logisticPartnerCode;
	}
	public void setLogisticPartnerCode(String logisticPartnerCode) {
		this.logisticPartnerCode = logisticPartnerCode;
	}
	public String getLogisticPartnerRefTrackingNumber() {
		return logisticPartnerRefTrackingNumber;
	}
	public void setLogisticPartnerRefTrackingNumber(
			String logisticPartnerRefTrackingNumber) {
		this.logisticPartnerRefTrackingNumber = logisticPartnerRefTrackingNumber;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getReasonForFailure() {
		return reasonForFailure;
	}
	public void setReasonForFailuren(String reasonForFailure) {
		this.reasonForFailure = reasonForFailure;
	}
	@Override
	public String toString() {
		return "ShipmentUpdateRequestDto [shipmentId=" + shipmentId
				+ ", logisticPartnerCode=" + logisticPartnerCode
				+ ", logisticPartnerRefTrackingNumber="
				+ logisticPartnerRefTrackingNumber + ", currentStage="
				+ currentStage + ", status=" + status + ", modifiedBy="
				+ modifiedBy + ", failedReason=" + reasonForFailure + "]";
	}
}