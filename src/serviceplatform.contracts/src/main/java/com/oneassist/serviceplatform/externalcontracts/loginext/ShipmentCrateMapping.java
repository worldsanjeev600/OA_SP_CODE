package com.oneassist.serviceplatform.externalcontracts.loginext;

import java.io.Serializable;
import java.util.List;

public class ShipmentCrateMapping implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8852277513195194501L;
    private String crateCd;
    private Double crateAmount;
    private String crateType;
    private int noOfUnits;
    private List<ShipmentLineItem> shipmentlineitems;

    public String getCrateCd() {
        return crateCd;
    }

    public void setCrateCd(String crateCd) {
        this.crateCd = crateCd;
    }

    public Double getCrateAmount() {
        return crateAmount;
    }

    public void setCrateAmount(Double crateAmount) {
        this.crateAmount = crateAmount;
    }

    public String getCrateType() {
        return crateType;
    }

    public void setCrateType(String crateType) {
        this.crateType = crateType;
    }

    public int getNoOfUnits() {
        return noOfUnits;
    }

    public void setNoOfUnits(int noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public List<ShipmentLineItem> getShipmentlineitems() {
        return shipmentlineitems;
    }

    public void setShipmentlineitems(List<ShipmentLineItem> shipmentlineitems) {
        this.shipmentlineitems = shipmentlineitems;
    }

    @Override
    public String toString() {
        return "ShipmentCrateMapping [crateCd=" + crateCd + ", crateAmount=" + crateAmount + ", crateType=" + crateType + ", noOfUnits=" + noOfUnits + "]";
    }
}
