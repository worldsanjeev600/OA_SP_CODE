
package com.oneassist.serviceplatform.commons.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author 
 */
@Entity
@Table(name = "OA_SHIPMENT_ASSET_DTL")
public class ShipmentAssetEntity {

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SHIPMENT_ASSET_DTL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "SHIPMENT_ASSET_ID")
	private Long shipmentAssetId;
	
	@Column(name = "SHIPMENT_ID")
	private Long shipmentId;
	
	@Column(name = "ASSET_CATEGORY_NAME")
	private String assetCategoryName;
	
	@Column(name = "ASSET_MODEL_NAME")
	private String assetModelName;
	
	@Column(name = "ASSET_MAKE_NAME")
	private String assetMakeName;
	
	@Column(name = "ASSET_PIECE_COUNT")
	private Integer assetPieceCount;
	
	@Column(name = "ASSET_DECLARE_VALUE")
	private Double assetDeclareValue;
	
	@Column(name = "ASSET_ACTUAL_LENGTH")
	private Double assetActualLength;
	
	@Column(name = "ASSET_ACTUAL_WIDTH")
	private Double assetActualWidth;
	
	@Column(name = "ASSET_ACTUAL_HEIGHT")
	private Double assetActualHeight;
	
	@Column(name = "ASSET_ACTUAL_WEIGHT")
	private Double assetActualWeight;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON",updatable = false)
	private Date createdOn;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "ASSET_LENGTH_UNIT")
	private String assetLengthUnit;
	
	@Column(name = "ASSET_WIDTH_UNIT")
	private String assetWidthUnit;
	
	@Column(name = "ASSET_HEIGHT_UNIT")
	private String assetHeightUnit;
	
	@Column(name = "ASSET_WEIGHT_UNIT")
	private String assetWeightUnit;
	
	@Column(name = "ASSET_REFERENCE_NO")
	private String assetReferenceNo;
	
	
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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
}