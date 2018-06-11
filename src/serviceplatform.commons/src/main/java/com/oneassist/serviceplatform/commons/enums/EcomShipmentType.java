package com.oneassist.serviceplatform.commons.enums;

public enum EcomShipmentType {
    REVERSE_PICKUP("REV"), FORWARD_PICKUP("PPD");

    private final String shipmentType;

    EcomShipmentType(String shipmentType) {

        this.shipmentType = shipmentType;
    }

    public String getShipmentType() {

        return this.shipmentType;
    }
}
