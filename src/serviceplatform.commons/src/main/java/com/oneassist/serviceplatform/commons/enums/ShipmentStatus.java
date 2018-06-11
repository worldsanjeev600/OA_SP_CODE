package com.oneassist.serviceplatform.commons.enums;

public enum ShipmentStatus {

	PENDING("P"), 
	CLOSED("C"), 
	UNASSIGNED("U"),
	REASSIGNED("R"),
	CANCELLED("X"),
	FAILED("F");
	
	private final String status;

	ShipmentStatus(String status) {

		this.status = status;
	}

	public String getStatus() {

		return this.status;
	}

	public static ShipmentStatus getShipmentStatus(String status) {
		for (ShipmentStatus enumstatus : ShipmentStatus.values()) {
			if (enumstatus.getStatus().equals(status)) {
				return enumstatus;
			}
		}
		return null;
	}
}