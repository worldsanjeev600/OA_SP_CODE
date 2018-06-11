package com.oneassist.serviceplatform.externalcontracts.ecom;


public class ForwardShipmentResponse {

    private Shipments[] shipments;

    public Shipments[] getShipments() {
        return shipments;
    }

    public void setShipments(Shipments[] shipments) {
        this.shipments = shipments;
    }

    @Override
    public String toString() {
        return "ClassPojo [shipments = " + shipments + "]";
    }

}
