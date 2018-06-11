package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ShipmentStatusHistoryDto {

	private String shipmentStatus;
	
	@JsonFormat     (shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a",timezone="IST") 
	private Date fromDate;
	
	@JsonFormat     (shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a",timezone="IST") 
	private Date toDate;

	
	public String getShipmentStatus() {
	
		return shipmentStatus;
	}

	
	public void setShipmentStatus(String shipmentStatus) {
	
		this.shipmentStatus = shipmentStatus;
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


	@Override
	public String toString() {
		return "ShipmentStatusHistoryDto [shipmentStatus=" + shipmentStatus + ", fromDate=" + fromDate + ", toDate="
				+ toDate + "]";
	}

	
}
