package com.oneassist.serviceplatform.contracts.dtos;

public class DashBoardShipmentDetailDto {

	private String shipmentStatus;
	
	private int count;
	
	public String getShipmentStatus() {
		return shipmentStatus;
	}
	
	public void setShipmentStatus(String shipmentStatus) {	
		this.shipmentStatus = shipmentStatus;
	}
	
	public int getCount() {	
		return count;
	}
	
	public void setCount(int count) {	
		this.count = count;
	}

	@Override
	public String toString() {
		return "DashBoardShipmentDetailDto [shipmentStatus=" + shipmentStatus + ", count=" + count + "]";
	}
	
}