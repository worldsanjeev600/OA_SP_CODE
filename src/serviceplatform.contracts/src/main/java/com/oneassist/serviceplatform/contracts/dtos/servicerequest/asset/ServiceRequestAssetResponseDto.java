package com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset;

import java.io.Serializable;
import java.util.List;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author alok.singh
 */
public class ServiceRequestAssetResponseDto extends BaseAuditDto implements Serializable {

    private static final long serialVersionUID = -7265551396715963251L;

    @ApiModelProperty(value = "Unique Identifier for Asset")
    private String assetId;
    
    @ApiModelProperty(value = "Service Request Id")
    private Long serviceRequestId;

    @ApiModelProperty(value = "Product Code")
    private String productCode;
    
    @ApiModelProperty(value = "Asset is functional (Y/N)")
	private String isFunctional;
    
    @ApiModelProperty(value = "Asset is Damaged (Y/N)")
	private String isAccidentalDamage;
    
    @ApiModelProperty(value = "Asset Make")
	private String make;
    
    @ApiModelProperty(value = "Asset Serial Number")
	private String serialNo;
    
    @ApiModelProperty(value = "Asset Model Number")
	private String modelNo;
    
    @ApiModelProperty(value = "Asset Age")
	private String assetAge;
    
    @ApiModelProperty(value = "Asset Technology")
	private String assetTechnology;
    
    @ApiModelProperty(value = "Asset Size")
	private String assetSize;
    
    @ApiModelProperty(value = "Asset Unit")
	private String assetUnit;
    
    @ApiModelProperty(value = "Is Information correct (Y/N)")
	private String isInformationCorrect;
    
    @ApiModelProperty(value = "Asset Inspection status")
	private String assetInspectionStatus;
    
    @ApiModelProperty(value = "Ids list of images")
	private List<ServiceRequestEntityDocumentResponseDto> serviceRequestAssetDocuments;
    
    @ApiModelProperty(value = "Asset Reference No")
    private String assetReferenceNo;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Long getServiceRequestId() {
		return serviceRequestId;
	}

	public void setServiceRequestId(Long serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

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

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
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

	public String getAssetAge() {
		return assetAge;
	}

	public void setAssetAge(String assetAge) {
		this.assetAge = assetAge;
	}

	public String getAssetTechnology() {
		return assetTechnology;
	}

	public void setAssetTechnology(String assetTechnology) {
		this.assetTechnology = assetTechnology;
	}

	public String getAssetSize() {
		return assetSize;
	}

	public void setAssetSize(String assetSize) {
		this.assetSize = assetSize;
	}

	public String getAssetUnit() {
		return assetUnit;
	}

	public void setAssetUnit(String assetUnit) {
		this.assetUnit = assetUnit;
	}

	public String getIsInformationCorrect() {
		return isInformationCorrect;
	}

	public void setIsInformationCorrect(String isInformationCorrect) {
		this.isInformationCorrect = isInformationCorrect;
	}

	public String getAssetInspectionStatus() {
		return assetInspectionStatus;
	}

	public void setAssetInspectionStatus(String assetInspectionStatus) {
		this.assetInspectionStatus = assetInspectionStatus;
	}

	public List<ServiceRequestEntityDocumentResponseDto> getServiceRequestAssetDocuments() {
		return serviceRequestAssetDocuments;
	}

	public void setServiceRequestAssetDocuments(List<ServiceRequestEntityDocumentResponseDto> serviceRequestAssetDocuments) {
		this.serviceRequestAssetDocuments = serviceRequestAssetDocuments;
	}
	
	public String getAssetReferenceNo() {
		return assetReferenceNo;
	}

	public void setAssetReferenceNo(String assetReferenceNo) {
		this.assetReferenceNo = assetReferenceNo;
	}

	@Override
	public String toString() {
		return "ServiceRequestAssetResponseDto [assetId=" + assetId + ", serviceRequestId=" + serviceRequestId
				+ ", productCode=" + productCode + ", isFunctional=" + isFunctional + ", isAccidentalDamage="
				+ isAccidentalDamage + ", make=" + make + ", serialNo=" + serialNo + ", modelNo=" + modelNo
				+ ", assetAge=" + assetAge + ", assetTechnology=" + assetTechnology + ", assetSize=" + assetSize
				+ ", assetUnit=" + assetUnit + ", isInformationCorrect=" + isInformationCorrect
				+ ", assetInspectionStatus=" + assetInspectionStatus + ", serviceRequestAssetDocuments="
				+ serviceRequestAssetDocuments + ", assetReferenceNo=" + assetReferenceNo + "]";
	}

}
