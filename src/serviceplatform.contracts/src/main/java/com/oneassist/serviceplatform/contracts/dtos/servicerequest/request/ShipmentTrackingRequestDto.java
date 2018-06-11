package com.oneassist.serviceplatform.contracts.dtos.servicerequest.request;

public class ShipmentTrackingRequestDto {

	private Long shipmentId;
	
	private String logisticPartnerRefTrackingNumber;

	
	public Long getShipmentId() {
		return shipmentId;
	}
	
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	public String getLogisticPartnerRefTrackingNumber() {
		return logisticPartnerRefTrackingNumber;
	}
	
	public void setLogisticPartnerRefTrackingNumber(String logisticPartnerRefTrackingNumber) {
		this.logisticPartnerRefTrackingNumber = logisticPartnerRefTrackingNumber;
	}

	@Override
	public String toString() {
		return "ShipmentTrackingRequestDto [shipmentId=" + shipmentId + ", logisticPartnerRefTrackingNumber="
				+ logisticPartnerRefTrackingNumber + "]";
	}
	
}