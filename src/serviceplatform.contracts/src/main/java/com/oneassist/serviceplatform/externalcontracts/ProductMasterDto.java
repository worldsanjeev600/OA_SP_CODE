package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductMasterDto implements Serializable {

    private static final long serialVersionUID = 5082070366159644721L;
    private String productCode;
    private String productName;
    private String productShrotName;
    private String productType;
    private String productDesc;
    private Long productBundleId;
    private String productStatus;
    private List<String> bundleProducts;

    private String addedBy;
    private Date addedOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String authorisedBy;
    private Date authorisedOn;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShrotName() {
        return productShrotName;
    }

    public void setProductShrotName(String productShrotName) {
        this.productShrotName = productShrotName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Long getProductBundleId() {
        return productBundleId;
    }

    public void setProductBundleId(Long productBundleId) {
        this.productBundleId = productBundleId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public List<String> getBundleProducts() {
        return bundleProducts;
    }

    public void setBundleProducts(List<String> bundleProducts) {
        this.bundleProducts = bundleProducts;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public Date getAuthorisedOn() {
        return authorisedOn;
    }

    public void setAuthorisedOn(Date authorisedOn) {
        this.authorisedOn = authorisedOn;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductMasterDto [productCode=");
		builder.append(productCode);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", productShrotName=");
		builder.append(productShrotName);
		builder.append(", productType=");
		builder.append(productType);
		builder.append(", productDesc=");
		builder.append(productDesc);
		builder.append(", productBundleId=");
		builder.append(productBundleId);
		builder.append(", productStatus=");
		builder.append(productStatus);
		builder.append(", bundleProducts=");
		builder.append(bundleProducts);
		builder.append(", addedBy=");
		builder.append(addedBy);
		builder.append(", addedOn=");
		builder.append(addedOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", authorisedBy=");
		builder.append(authorisedBy);
		builder.append(", authorisedOn=");
		builder.append(authorisedOn);
		builder.append("]");
		return builder.toString();
	}
    
}
