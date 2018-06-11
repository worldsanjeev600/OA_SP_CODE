
package com.oneassist.serviceplatform.contracts.dtos.shipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(Include.NON_NULL)
@ApiModel(description="Shipment Assets Model")
public class ShipmentAssetDetailsDto extends BaseAuditDto {

	private static final long serialVersionUID = -666375746500280399L;
	
	private Long shipmentAssetId;

	private Long shipmentId;
	
	@ApiModelProperty(value="Asset Category Name",required=true)
	private String assetCategoryName;
	
	@ApiModelProperty(value="Asset Model Name",required=true)
	private String assetModelName;
	
	@ApiModelProperty(value="Asset Make Name",required=true)
	private String assetMakeName;
	
	@ApiModelProperty(value="Asset Piece Count",required=true)
	private Integer assetPieceCount;
	
	@ApiModelProperty(value="Asset Declare Value",required=true)
	private Double assetDeclareValue;
	
	@ApiModelProperty(value="Asset Actual Length",required=true)
	private Double assetActualLength;
	
	@ApiModelProperty(value="Asset Actual Width",required=true)
	private Double assetActualWidth;
	
	@ApiModelProperty(value="Asset Actual Height",required=true)
	private Double assetActualHeight;
	
	@ApiModelProperty(value="Asset Actual Weight",required=true)
	private Double assetActualWeight;
	
	private String assetLengthUnit;
	
	private String assetWidthUnit;
	
	private String assetHeightUnit;
	
	private String assetWeightUnit;
	
	private String assetReferenceNo;
	
	private String remarks;
	
	public Long getShipmentAssetId() {
		return shipmentAssetId;
	}
	public void setShipmentAssetId(Long shipmentAssetId) {
		this.shipmentAssetId = shipmentAssetId;
	}
	public Long getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
	public String getAssetCategoryName() {
		return assetCategoryName;
	}
	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}
	public String getAssetModelName() {
		return assetModelName;
	}
	public void setAssetModelName(String assetModelName) {
		this.assetModelName = assetModelName;
	}
	public String getAssetMakeName() {
		return assetMakeName;
	}
	public void setAssetMakeName(String assetMakeName) {
		this.assetMakeName = assetMakeName;
	}
	public Integer getAssetPieceCount() {
		return assetPieceCount;
	}
	public void setAssetPieceCount(Integer assetPieceCount) {
		this.assetPieceCount = assetPieceCount;
	}
	public Double getAssetDeclareValue() {
		return assetDeclareValue;
	}
	public void setAssetDeclareValue(Double assetDeclareValue) {
		this.assetDeclareValue = assetDeclareValue;
	}
	public Double getAssetActualLength() {
		return assetActualLength;
	}
	public void setAssetActualLength(Double assetActualLength) {
		this.assetActualLength = assetActualLength;
	}
	public Double getAssetActualWidth() {
		return assetActualWidth;
	}
	public void setAssetActualWidth(Double assetActualWidth) {
		this.assetActualWidth = assetActualWidth;
	}
	public Double getAssetActualHeight() {
		return assetActualHeight;
	}
	public void setAssetActualHeight(Double assetActualHeight) {
		this.assetActualHeight = assetActualHeight;
	}
	public Double getAssetActualWeight() {
		return assetActualWeight;
	}
	public void setAssetActualWeight(Double assetActualWeight) {
		this.assetActualWeight = assetActualWeight;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssetLengthUnit() {
		return assetLengthUnit;
	}
	
	public void setAssetLengthUnit(String assetLengthUnit) {
		this.assetLengthUnit = assetLengthUnit;
	}
	
	public String getAssetWidthUnit() {
		return assetWidthUnit;
	}
	
	public void setAssetWidthUnit(String assetWidthUnit) {
		this.assetWidthUnit = assetWidthUnit;
	}
	
	public String getAssetHeightUnit() {
		return assetHeightUnit;
	}
	
	public void setAssetHeightUnit(String assetHeightUnit) {
		this.assetHeightUnit = assetHeightUnit;
	}
	public String getAssetWeightUnit() {
		return assetWeightUnit;
	}
	public void setAssetWeightUnit(String assetWeightUnit) {
		this.assetWeightUnit = assetWeightUnit;
	}
	public String getAssetReferenceNo() {
		return assetReferenceNo;
	}
	public void setAssetReferenceNo(String assetReferenceNo) {
		this.assetReferenceNo = assetReferenceNo;
	}
	
	@Override
	public String toString() {
		return "ShipmentAssetDetailsDto [" +
			     "shipmentAssetId=" + shipmentAssetId + ", shipmentId="+ shipmentId 
			   + ", assetCategoryName=" + assetCategoryName + ", assetModelName="+ assetModelName 
			   + ", assetMakeName=" + assetMakeName + ", assetPieceCount="+ assetPieceCount 
			   + ", assetDeclareValue=" + assetDeclareValue + ", assetActualLength="+ assetActualLength 
			   + ", assetActualWidth=" + assetActualWidth + ", assetActualHeight="+ assetActualHeight 
			   + ", assetActualWeight=" + assetActualWeight + ", remarks="+ remarks 
 		       + "]";
	}
}