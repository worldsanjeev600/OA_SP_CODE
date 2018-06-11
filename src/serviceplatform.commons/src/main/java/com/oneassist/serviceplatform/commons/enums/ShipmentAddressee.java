package com.oneassist.serviceplatform.commons.enums;

public enum ShipmentAddressee {

	ASC("ASC"), 
	HUB("HUB"), 
	CUSTOMER("CUSTOMER"),
	OA_OFFICE("OA_OFFICE");
	
	private final String shipmentAddressee;

	ShipmentAddressee(String shipmentAddressee) {

		this.shipmentAddressee = shipmentAddressee;
	}

	public String getshipmentAddressee() {

		return this.shipmentAddressee;
	}

	public static ShipmentAddressee getShipmentAddressee(String addressee) {
		for (ShipmentAddressee shipmentAddressee : ShipmentAddressee.values()) {
			if (shipmentAddressee.getshipmentAddressee().equals(addressee)) {
				return shipmentAddressee;
			}
		}
		
		return null;
	}
}