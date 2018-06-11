package com.oneassist.serviceplatform.commons.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author alok.singh
 */
@Entity
@Table(name = "OA_SERVICE_REQUEST_ASSETS_DTL")
public class ServiceRequestAssetEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -1486576349069944641L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ASSET_ID")
    private String assetId;
    
    @Column(name = "SERVICE_REQUEST_ID")
    private Long serviceRequestId;

    @Column(name = "PRODUCT_CODE")
    private String productCode;
    
    @Column(name = "IS_FUNCTIONAL")
	private String isFunctional;
    
    @Column(name = "IS_ACCIDENTAL_DAMAGE")
	private String isAccidentalDamage;
    
    @Column(name = "MAKE")
	private String make;
    
    @Column(name = "SERIAL_NO")
	private String serialNo;
    
    @Column(name = "MODEL_NO")
	private String modelNo;
    
    @Column(name = "ASSET_AGE")
	private String assetAge;
    
    @Column(name = "TECHNOLOGY")
	private String assetTechnology;
    
    @Column(name = "ASSET_SIZE")
	private String assetSize;
    
    @Column(name = "ASSET_UNIT")
	private String assetUnit;
    
    @Column(name = "IS_INFORMATION_CORRECT")
	private String isInformationCorrect;
    
    @Column(name = "INSPECTION_STATUS")
	private String assetInspectionStatus;
    
    @Column(name = "ASSET_REFERENCE_NO")
    private String assetReferenceNo;
    
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ASSET_ID", insertable = true, updatable = false )
    List<ServiceRequestEntityDocumentEntity> serviceRequestAssetDocuments;

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

	public List<ServiceRequestEntityDocumentEntity> getServiceRequestAssetDocuments() {
		return serviceRequestAssetDocuments;
	}

	public void setServiceRequestAssetDocuments(List<ServiceRequestEntityDocumentEntity> serviceRequestAssetDocuments) {
		this.serviceRequestAssetDocuments = serviceRequestAssetDocuments;
	}

	public String getAssetReferenceNo() {
		return assetReferenceNo;
	}

	public void setAssetReferenceNo(String assetReferenceNo) {
		this.assetReferenceNo = assetReferenceNo;
	}

}