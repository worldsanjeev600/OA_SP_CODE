package com.oneassist.serviceplatform.externalcontracts.loginext;

import java.io.Serializable;

public class ShipmentLineItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3299957877967909632L;

    private String itemCd;
    private String itemName;
    private Double itemPrice;
    private int itemQuantity;
    private String itemType;
    private Double itemWeight;

    public String getItemCd() {
        return itemCd;
    }

    public void setItemCd(String itemCd) {
        this.itemCd = itemCd;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Double getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(Double itemWeight) {
        this.itemWeight = itemWeight;
    }

    @Override
    public String toString() {
        return "ShipmentLineItem [itemCd=" + itemCd + ", itemName=" + itemName + ", itemPrice=" + itemPrice + ", itemQuantity=" + itemQuantity + ", itemType=" + itemType + ", itemWeight="
                + itemWeight + "]";
    }
}
