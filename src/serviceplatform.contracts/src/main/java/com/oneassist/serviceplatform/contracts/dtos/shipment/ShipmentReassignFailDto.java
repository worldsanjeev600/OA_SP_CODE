package com.oneassist.serviceplatform.contracts.dtos.shipment;

public class ShipmentReassignFailDto {

	private Long shipmentid;

	private String failedReason;

	public Long getShipmentid() {
		return shipmentid;
	}

	public void setShipmentid(Long shipmentid) {
		this.shipmentid = shipmentid;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	@Override
	public String toString() {
		return "ShipmentReassignFailDto [shipmentid=" + shipmentid + ", failedReason=" + failedReason + "]";
	}
	
}
