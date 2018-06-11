package com.oneassist.serviceplatform.contracts.dtos.shipment;

public class DashBoardRequestDto {

	private String shipmentModifiedDate;
	private String shipmentStatus;
	
	public String getShipmentModifiedDate() {	
		return shipmentModifiedDate;
	}
	
	public void setShipmentModifiedDate(String shipmentModifiedDate) {	
		this.shipmentModifiedDate = shipmentModifiedDate;
	}
	
	public String getShipmentStatus() {	
		return shipmentStatus;
	}
	
	public void setShipmentStatus(String shipmentStatus) {	
		this.shipmentStatus = shipmentStatus;
	}

	@Override
	public String toString() {
		return "DashBoardRequestDto [shipmentModifiedDate=" + shipmentModifiedDate + ", shipmentStatus="
				+ shipmentStatus + "]";
	}
	
}