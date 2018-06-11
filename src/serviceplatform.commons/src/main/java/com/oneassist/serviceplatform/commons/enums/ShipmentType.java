package com.oneassist.serviceplatform.commons.enums;

public enum ShipmentType {
	PICKUP(1),
	REVERSE_PICKUP(2);
	
	private final int shipmenttype;
	
	ShipmentType(int shipmenttype) {

		this.shipmenttype = shipmenttype;
	}

	public int getShipmentType() {

		return this.shipmenttype;
	}
	
	public static ShipmentType getShipmentType(int shipmentTypeId) {
        for (ShipmentType shipmentType : ShipmentType.values()) {
            if (shipmentType.getShipmentType() == shipmentTypeId) {
                return shipmentType;
            }
        }
        
        return null;
	}
}	