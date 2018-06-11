package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

public class AssetDetailDto {

    private Long assetId;
    private Long custId;
    private String name;
    private String prodCode;
    private String category;
    private String brand;
    private String model;
    private String serialNo;
    private List<AssetDocument> assetDocuments;

    public List<AssetDocument> getAssetDocuments() {
        return assetDocuments;
    }

    public void setAssetDocuments(List<AssetDocument> assetDocuments) {
        this.assetDocuments = assetDocuments;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public String toString() {
        return "AssetDetailDto [assetId=" + assetId + ", custId=" + custId + ", name=" + name + ", prodCode=" + prodCode + ", category=" + category + ", brand=" + brand + ", model=" + model
                + ", serialNo=" + serialNo + ", assetDocuments=" + assetDocuments + "]";
    }

}
