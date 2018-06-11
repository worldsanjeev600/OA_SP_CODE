package com.oneassist.serviceplatform.externalcontracts.dhl;

import java.util.List;

public class DHLPickupRequest {

    private String itemDescription;
    private String orderType;

    private String orderNumber;
    private String addtionalInfo;
    private Integer quantity;

    private DHLAddressRequestDto originAddress;
    private DHLAddressRequestDto destinationAddress;

    private Double boxLength;
    private Double boxHeight;
    private Double boxWeight;
    private Double actualLength;
    private Double actualHeight;
    private Double actualWeight;
    private Double assetValue;

    private List<QualityCheck> qc;
    
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAddtionalInfo() {
        return addtionalInfo;
    }

    public void setAddtionalInfo(String addtionalInfo) {
        this.addtionalInfo = addtionalInfo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

  

    
    public DHLAddressRequestDto getOriginAddress() {
        return originAddress;
    }

    
    public void setOriginAddress(DHLAddressRequestDto originAddress) {
        this.originAddress = originAddress;
    }

    
    public DHLAddressRequestDto getDestinationAddress() {
        return destinationAddress;
    }

    
    public void setDestinationAddress(DHLAddressRequestDto destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Double getBoxLength() {
        return boxLength;
    }

    public void setBoxLength(Double boxLength) {
        this.boxLength = boxLength;
    }

    public Double getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(Double boxHeight) {
        this.boxHeight = boxHeight;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(Double boxWeight) {
        this.boxWeight = boxWeight;
    }

    public Double getActualLength() {
        return actualLength;
    }

    public void setActualLength(Double actualLength) {
        this.actualLength = actualLength;
    }

    public Double getActualHeight() {
        return actualHeight;
    }

    public void setActualHeight(Double actualHeight) {
        this.actualHeight = actualHeight;
    }

    public Double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public Double getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(Double assetValue) {
        this.assetValue = assetValue;
    }

    
    public List<QualityCheck> getQc() {
        return qc;
    }

    
    public void setQc(List<QualityCheck> qc) {
        this.qc = qc;
    }

    
}
