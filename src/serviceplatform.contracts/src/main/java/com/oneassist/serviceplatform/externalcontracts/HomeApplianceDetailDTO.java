package com.oneassist.serviceplatform.externalcontracts;


public class HomeApplianceDetailDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5646188029122099528L;
	private Long assetId;
	private Long custId;
	private String productCode;
	private String categoryCode;
	private String createdBy;
	private String createdOn;
	private String modifiedOn;
	private String modifiedBy;
	private String status;
	private String make;
    private String model;
    private String deviceId;
    private String invoiceDate;
    private String invoiceValue;
    private Long warrantyPeriod;
    private String age;
    private String technology;
    private String size;
    private String sizeUnit;
    
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	public Long getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(Long warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getSizeUnit() {
		return sizeUnit;
	}
	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HomeApplianceDetailDTO [assetId=");
		builder.append(assetId);
		builder.append(", custId=");
		builder.append(custId);
		builder.append(", productCode=");
		builder.append(productCode);
		builder.append(", categoryCode=");
		builder.append(categoryCode);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", status=");
		builder.append(status);
		builder.append(", make=");
		builder.append(make);
		builder.append(", model=");
		builder.append(model);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", invoiceDate=");
		builder.append(invoiceDate);
		builder.append(", invoiceValue=");
		builder.append(invoiceValue);
		builder.append(", warrantyPeriod=");
		builder.append(warrantyPeriod);
		builder.append(", age=");
		builder.append(age);
		builder.append(", technology=");
		builder.append(technology);
		builder.append(", size=");
		builder.append(size);
		builder.append(", sizeUnit=");
		builder.append(sizeUnit);
		builder.append("]");
		return builder.toString();
	}
	
}
