package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class Inspection{
	private String productCode;
	private String isFunctional;
	private String isAccidentalDamage;
	private String brand;
	private String serialNo;
	private String modelNo;
	private String productVariantId;
	private String productAge;
	private String productTechnology;
	private String productSize;
	private String productUnit;
	private String isInformationCorrect;
	private String prodInspectionStatus;
	private List<ImageStorageReference> imageStorageRef;
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getIsFunctional() {
		return isFunctional;
	}
	
	public void setIsFunctional(String isFunctional) {
		this.isFunctional = isFunctional;
	}
	
	public String getIsAccidentalDamage() {
		return isAccidentalDamage;
	}
	
	public void setIsAccidentalDamage(String isAccidentalDamage) {
		this.isAccidentalDamage = isAccidentalDamage;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getModelNo() {
		return modelNo;
	}
	
	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
	
	public String getProductVariantId() {
		return productVariantId;
	}
	
	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}
	
	public String getProductAge() {
		return productAge;
	}
	
	public void setProductAge(String productAge) {
		this.productAge = productAge;
	}
	
	public String getIsInformationCorrect() {
		return isInformationCorrect;
	}
	/**
	 * @return the productSize
	 */
	public String getProductSize() {
		return productSize;
	}
	/**
	 * @param productSize the productSize to set
	 */
	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}
	/**
	 * @return the productUnit
	 */
	public String getProductUnit() {
		return productUnit;
	}
	/**
	 * @param productUnit the productUnit to set
	 */
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public void setIsInformationCorrect(String isInformationCorrect) {
		this.isInformationCorrect = isInformationCorrect;
	}
	
	public String getProdInspectionStatus() {
		return prodInspectionStatus;
	}
	
	public void setProdInspectionStatus(String prodInspectionStatus) {
		this.prodInspectionStatus = prodInspectionStatus;
	}
	
	public String getProductTechnology() {
		return productTechnology;
	}

	public void setProductTechnology(String productTechnology) {
		this.productTechnology = productTechnology;
	}

	public List<ImageStorageReference> getImageStorageRef() {
		return imageStorageRef;
	}
	
	public void setImageStorageRef(List<ImageStorageReference> imageStorageRef) {
		this.imageStorageRef = imageStorageRef;
	}

	@Override
	public String toString() {
		return "Inspection [productCode=" + productCode + ", isFunctional=" + isFunctional + ", isAccidentalDamage="
				+ isAccidentalDamage + ", brand=" + brand + ", serialNo=" + serialNo + ", modelNo=" + modelNo
				+ ", productVariantId=" + productVariantId + ", productAge=" + productAge + ", productTechnology="
				+ productTechnology + ", productSize=" + productSize + ", productUnit=" + productUnit
				+ ", isInformationCorrect=" + isInformationCorrect + ", prodInspectionStatus=" + prodInspectionStatus
				+ ", imageStorageRef=" + imageStorageRef + "]";
	}	
	
}