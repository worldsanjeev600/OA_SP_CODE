package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "SVC_REQ_SHIPMENT_DETAILS_VIEW")
@Immutable
public class ServiceRequestShipmentDetailViewEntity implements Serializable{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5362225808215649938L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "shipmentId")
	private Long shipmentId;

	@Column(name = "shipmentType")
	private Integer shipmentType;
	
	@Column(name = "currentStage")
	private String currentStage;
	
	@Column(name = "logisticPartnerCode")
	private String logisticPartnerCode;

	@Column(name = "logisticPartnerRefTrackingNo")
	private String logisticPartnerRefTrackingNo;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "shipmentCreatedOn")
	private Date shipmentCreatedOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "shipmentModifiedOn")
	private Date shipmentModifiedOn;
	
	@Column(name = "hubId")
	private String hubId;
	
	@Column(name = "shipmentStatus")
	private String shipmentStatus;
	
	@Column(name = "shipRecipient")
	private String shipRecipient;
	
	@Column(name = "shipSender")
	private String shipSender;
	
	@Column(name = "shipSentBy")
	private String shipSentBy;
	
	@Column(name = "shipReceivedBy")
	private String shipReceivedBy;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "shipSchdPickupDate")
	private Date shipSchdPickupDate;
	
	@Column(name = "SHIPMENTBOXCOUNT")
	private Integer shipmentBoxCount;
	
	@Column(name = "BOXACTUALLENGTH")
	private Double boxActualLength;

	@Column(name = "BOXACTUALWIDTH")
	private Double boxActualWidth;

	@Column(name = "BOXACTUALHEIGHT")
	private Double boxActualHeight;

	@Column(name = "BOXACTUALWEIGHT")
	private Double boxActualWeight;
	
	@Column(name = "boxLengthUnit")
	private String boxLengthUnit;
	
	@Column(name = "boxWidthUnit")
	private String boxWidthUnit;
	
	@Column(name = "boxHeightUnit")
	private String boxHeightUnit;
	
	@Column(name = "boxWeightUnit")
	private String boxWeightUnit;
	
	@Column(name = "SHIPMENTDECLAREVALUE")
	private Double shipmentDeclareValue;
	
	@Column(name = "serviceRequestId")
	private Long serviceRequestId;
	
	@Column(name = "primaryTrackingNumber")
	private String primaryTrackingNumber;
	
	@Column(name = "refSvcSecTrackingNum")
	private String refSvcSecTrackingNum;
	
	@Column(name = "serviceReqDesc")
	private String serviceReqDesc;
	
	@Column(name = "initiatingSystem")
	private Long initiatingSystem;
	
	@Column(name = "createdby")
	private String createdBy;
	
	@Column(name = "serviceReqStatus")
	private String serviceReqStatus;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "serviceReqCreatedOn")
	private Date serviceReqCreatedOn;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "serviceReqModifiedOn")
	private Date serviceReqModifiedOn;
	
	@Column(name = "serviceRequestTypeId")
	private Long serviceRequestTypeId;
	
	@Column(name = "serviceName")
	private String serviceName;

	@Column(name = "originPincode")
	private String originPincode;

	@Column(name = "ORIGINALADDRESSID")
	private Long originalAddressId;

	@Column(name = "ORIGINSHIPMENTADDRLINE1")
	private String originAddressLine1;

	@Column(name = "ORIGINSHIPMENTADDRLINE2")
	private String originAddressLine2;

	@Column(name = "ORIGINLANDMARK")
	private String originLandmark;

	@Column(name = "ORIGINDISTRICT")
	private String originDistrict;

	@Column(name = "ORIGINMOBILENO")
	private BigDecimal originMobileNO;

	@Column(name = "ORIGINCOUNTRYCODE")
	private String originCountryCode;

	@Column(name = "ORIGINADDRESSEMAIL")
	private String originAddressEmail;

	@Column(name = "ORIGINADDRESSEEFULLNAME")
	private String originAddresseeFullName;
	
	@Column(name = "destinationPincode")
	private String destinationPincode;

	@Column(name = "DESTINATIONADDRESSID")
	private Long destinationAddressId;

	@Column(name = "DESTINATIONADDRLINE1")
	private String destinationAddressLine1;

	@Column(name = "DESTINATIONADDRLINE2")
	private String destinationAddressLine2;

	@Column(name = "DESTINATIONLANDMARK")
	private String destinationLandmark;

	@Column(name = "DESTINATIONDISTRICT")
	private String destinationDistrict;

	@Column(name = "DESTINATIONMOBILENO")
	private BigDecimal destinationMobileNO;

	@Column(name = "DESTINATIONCOUNTRYCODE")
	private String destinationCountryCode;

	@Column(name = "DESTINATIONADDRESSEMAIL")
	private String destinationAddressEmail;

	@Column(name = "DESTINATIONADDRESSEEFULLNAME")
	private String destinationAddresseeFullName;

	
	@Column(name = "SHIPMENTASSETID")
	private Long shipmentAssetId;
	
	@Column(name = "ASSETPIECECOUNT")
	private Integer assetPieceCount;

	@Column(name = "ASSETCATEGORYNAME")
	private String assetCategoryName;

	
	@Column(name = "ASSETMODELNAME")
	private String assetModelName;

	@Column(name = "ASSETMAKENAME")
	private String assetMakeName;

	@Column(name = "ASSETDECLAREVALUE")
	private Double assetDeclareValue;

	@Column(name = "ASSETACTUALLENGTH")
	private Double assetActualLength;

	@Column(name = "ASSETACTUALWIDTH")
	private Double assetActualWidth;

	@Column(name = "ASSETACTUALHEIGHT")
	private Double assetActualHeight;

	@Column(name = "ASSETACTUALWEIGHT")
	private Double assetActualWeight;

	@Column(name = "ASSETREMARKS")
	private String assetRemarks;
	
	@Column(name = "ASSETLENGTHUNIT")
	private String assetLengthUnit;
	
	@Column(name = "ASSETWIDTHUNIT")
	private String assetWidthUnit;
	
	@Column(name = "ASSETHEIGHTUNIT")
	private String assetHeightUnit;
	
	@Column(name = "ASSETWEIGHTUNIT")
	private String assetWeightUnit;
	
	@Column(name = "ASSETREFERENCENO")
	private String assetReferenceNo;
	
	public Long getId() {	
		return id;
	}
	
	public void setId(Long id) {	
		this.id = id;
	}
	
	public Long getShipmentId() {	
		return shipmentId;
	}
	
	public void setShipmentId(Long shipmentId) {	
		this.shipmentId = shipmentId;
	}
	
	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}
	
	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
	
		this.currentStage = currentStage;
	}

	
	public String getLogisticPartnerCode() {
	
		return logisticPartnerCode;
	}

	
	public void setLogisticPartnerCode(String logisticPartnerCode) {
	
		this.logisticPartnerCode = logisticPartnerCode;
	}

	
	public String getLogisticPartnerRefTrackingNo() {
	
		return logisticPartnerRefTrackingNo;
	}

	
	public void setLogisticPartnerRefTrackingNo(String logisticPartnerRefTrackingNo) {
	
		this.logisticPartnerRefTrackingNo = logisticPartnerRefTrackingNo;
	}

	
	public Date getShipmentCreatedOn() {
	
		return shipmentCreatedOn;
	}

	
	public void setShipmentCreatedOn(Date shipmentCreatedOn) {
	
		this.shipmentCreatedOn = shipmentCreatedOn;
	}

	
	public Date getShipmentModifiedOn() {
	
		return shipmentModifiedOn;
	}

	
	public void setShipmentModifiedOn(Date shipmentModifiedOn) {
	
		this.shipmentModifiedOn = shipmentModifiedOn;
	}

	
	public String getHubId() {
	
		return hubId;
	}

	
	public void setHubId(String hubId) {
	
		this.hubId = hubId;
	}

	
	public String getShipmentStatus() {
	
		return shipmentStatus;
	}

	
	public void setShipmentStatus(String shipmentStatus) {
	
		this.shipmentStatus = shipmentStatus;
	}

	
	public String getShipRecipient() {
	
		return shipRecipient;
	}

	
	public void setShipRecipient(String shipRecipient) {
	
		this.shipRecipient = shipRecipient;
	}

	
	public String getShipSender() {
	
		return shipSender;
	}

	
	public void setShipSender(String shipSender) {
	
		this.shipSender = shipSender;
	}

	
	public String getShipSentBy() {
	
		return shipSentBy;
	}

	
	public void setShipSentBy(String shipSentBy) {
	
		this.shipSentBy = shipSentBy;
	}

	
	public String getShipReceivedBy() {
	
		return shipReceivedBy;
	}

	
	public void setShipReceivedBy(String shipReceivedBy) {
	
		this.shipReceivedBy = shipReceivedBy;
	}

	
	public Date getShipSchdPickupDate() {
	
		return shipSchdPickupDate;
	}

	
	public void setShipSchdPickupDate(Date shipSchdPickupDate) {
	
		this.shipSchdPickupDate = shipSchdPickupDate;
	}

	
	public Integer getShipmentBoxCount() {
	
		return shipmentBoxCount;
	}

	
	public void setShipmentBoxCount(Integer shipmentBoxCount) {
	
		this.shipmentBoxCount = shipmentBoxCount;
	}

	
	public Double getBoxActualLength() {
	
		return boxActualLength;
	}

	
	public void setBoxActualLength(Double boxActualLength) {
	
		this.boxActualLength = boxActualLength;
	}

	
	public Double getBoxActualWidth() {
	
		return boxActualWidth;
	}

	
	public void setBoxActualWidth(Double boxActualWidth) {
	
		this.boxActualWidth = boxActualWidth;
	}

	
	public Double getBoxActualHeight() {
	
		return boxActualHeight;
	}

	
	public void setBoxActualHeight(Double boxActualHeight) {
	
		this.boxActualHeight = boxActualHeight;
	}

	
	public Double getBoxActualWeight() {
	
		return boxActualWeight;
	}

	
	public void setBoxActualWeight(Double boxActualWeight) {
	
		this.boxActualWeight = boxActualWeight;
	}

	
	public String getBoxLengthUnit() {
	
		return boxLengthUnit;
	}

	
	public void setBoxLengthUnit(String boxLengthUnit) {
	
		this.boxLengthUnit = boxLengthUnit;
	}

	
	public String getBoxWidthUnit() {
	
		return boxWidthUnit;
	}

	
	public void setBoxWidthUnit(String boxWidthUnit) {
	
		this.boxWidthUnit = boxWidthUnit;
	}

	
	public String getBoxHeightUnit() {
	
		return boxHeightUnit;
	}

	
	public void setBoxHeightUnit(String boxHeightUnit) {
	
		this.boxHeightUnit = boxHeightUnit;
	}

	
	public String getBoxWeightUnit() {
	
		return boxWeightUnit;
	}

	
	public void setBoxWeightUnit(String boxWeightUnit) {
	
		this.boxWeightUnit = boxWeightUnit;
	}

	
	public Double getShipmentDeclareValue() {
	
		return shipmentDeclareValue;
	}

	
	public void setShipmentDeclareValue(Double shipmentDeclareValue) {
	
		this.shipmentDeclareValue = shipmentDeclareValue;
	}

	
	public Long getServiceRequestId() {
	
		return serviceRequestId;
	}

	
	public void setServiceRequestId(Long serviceRequestId) {
	
		this.serviceRequestId = serviceRequestId;
	}

	
	public String getPrimaryTrackingNumber() {
	
		return primaryTrackingNumber;
	}

	
	public void setPrimaryTrackingNumber(String primaryTrackingNumber) {
	
		this.primaryTrackingNumber = primaryTrackingNumber;
	}

	
	public String getRefSvcSecTrackingNum() {
	
		return refSvcSecTrackingNum;
	}

	
	public void setRefSvcSecTrackingNum(String refSvcSecTrackingNum) {
	
		this.refSvcSecTrackingNum = refSvcSecTrackingNum;
	}

	
	public String getServiceReqDesc() {
	
		return serviceReqDesc;
	}

	
	public void setServiceReqDesc(String serviceReqDesc) {
	
		this.serviceReqDesc = serviceReqDesc;
	}

	
		
	public String getServiceReqStatus() {
	
		return serviceReqStatus;
	}

	
	public void setServiceReqStatus(String serviceReqStatus) {
	
		this.serviceReqStatus = serviceReqStatus;
	}

	
	public Date getServiceReqCreatedOn() {
	
		return serviceReqCreatedOn;
	}

	
	public void setServiceReqCreatedOn(Date serviceReqCreatedOn) {
	
		this.serviceReqCreatedOn = serviceReqCreatedOn;
	}

	
	public Date getServiceReqModifiedOn() {
	
		return serviceReqModifiedOn;
	}

	
	public void setServiceReqModifiedOn(Date serviceReqModifiedOn) {
	
		this.serviceReqModifiedOn = serviceReqModifiedOn;
	}

	
	public Long getServiceRequestTypeId() {
	
		return serviceRequestTypeId;
	}

	
	public void setServiceRequestTypeId(Long serviceRequestTypeId) {
	
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	
	public String getServiceName() {
	
		return serviceName;
	}

	
	public void setServiceName(String serviceName) {
	
		this.serviceName = serviceName;
	}

	
	public String getOriginPincode() {
	
		return originPincode;
	}

	
	public void setOriginPincode(String originPincode) {
	
		this.originPincode = originPincode;
	}

	
	public Long getOriginalAddressId() {
	
		return originalAddressId;
	}

	
	public void setOriginalAddressId(Long originalAddressId) {
	
		this.originalAddressId = originalAddressId;
	}

	
	

	
	public String getOriginAddressLine1() {
		return originAddressLine1;
	}


	public void setOriginAddressLine1(String originAddressLine1) {
		this.originAddressLine1 = originAddressLine1;
	}


	public String getOriginAddressLine2() {
		return originAddressLine2;
	}


	public void setOriginAddressLine2(String originAddressLine2) {
		this.originAddressLine2 = originAddressLine2;
	}


	public String getOriginLandmark() {
	
		return originLandmark;
	}

	
	public void setOriginLandmark(String originLandmark) {
	
		this.originLandmark = originLandmark;
	}

	
	public String getOriginDistrict() {
	
		return originDistrict;
	}

	
	public void setOriginDistrict(String originDistrict) {
	
		this.originDistrict = originDistrict;
	}

	
	public BigDecimal getOriginMobileNO() {
	
		return originMobileNO;
	}

	
	public void setOriginMobileNO(BigDecimal originMobileNO) {
	
		this.originMobileNO = originMobileNO;
	}

	
	public String getOriginCountryCode() {
	
		return originCountryCode;
	}

	
	public void setOriginCountryCode(String originCountryCode) {
	
		this.originCountryCode = originCountryCode;
	}

	
	public String getOriginAddressEmail() {
	
		return originAddressEmail;
	}

	
	public void setOriginAddressEmail(String originAddressEmail) {
	
		this.originAddressEmail = originAddressEmail;
	}

	
	public String getOriginAddresseeFullName() {
	
		return originAddresseeFullName;
	}

	
	public void setOriginAddresseeFullName(String originAddresseeFullName) {
	
		this.originAddresseeFullName = originAddresseeFullName;
	}

	
	public String getDestinationPincode() {
	
		return destinationPincode;
	}

	
	public void setDestinationPincode(String destinationPincode) {
	
		this.destinationPincode = destinationPincode;
	}

	
	public Long getDestinationAddressId() {
	
		return destinationAddressId;
	}

	
	public void setDestinationAddressId(Long destinationAddressId) {
	
		this.destinationAddressId = destinationAddressId;
	}

	
	public String getDestinationAddressLine1() {
	
		return destinationAddressLine1;
	}

	
	public void setDestinationAddressLine1(String destinationAddressLine1) {
	
		this.destinationAddressLine1 = destinationAddressLine1;
	}

	
	public String getDestinationAddressLine2() {
	
		return destinationAddressLine2;
	}

	
	public void setDestinationAddressLine2(String destinationAddressLine2) {
	
		this.destinationAddressLine2 = destinationAddressLine2;
	}

	
	public String getDestinationLandmark() {
	
		return destinationLandmark;
	}

	
	public void setDestinationLandmark(String destinationLandmark) {
	
		this.destinationLandmark = destinationLandmark;
	}

	
	public String getDestinationDistrict() {
	
		return destinationDistrict;
	}

	
	public void setDestinationDistrict(String destinationDistrict) {
	
		this.destinationDistrict = destinationDistrict;
	}

	
	public BigDecimal getDestinationMobileNO() {
	
		return destinationMobileNO;
	}

	
	public void setDestinationMobileNO(BigDecimal destinationMobileNO) {
	
		this.destinationMobileNO = destinationMobileNO;
	}

	
	public String getDestinationCountryCode() {
	
		return destinationCountryCode;
	}

	
	public void setDestinationCountryCode(String destinationCountryCode) {
	
		this.destinationCountryCode = destinationCountryCode;
	}

	
	public String getDestinationAddressEmail() {
	
		return destinationAddressEmail;
	}

	
	public void setDestinationAddressEmail(String destinationAddressEmail) {
	
		this.destinationAddressEmail = destinationAddressEmail;
	}

	
	public String getDestinationAddresseeFullName() {
	
		return destinationAddresseeFullName;
	}

	
	public void setDestinationAddresseeFullName(String destinationAddresseeFullName) {
	
		this.destinationAddresseeFullName = destinationAddresseeFullName;
	}

	
	public Long getShipmentAssetId() {
	
		return shipmentAssetId;
	}

	
	public void setShipmentAssetId(Long shipmentAssetId) {
	
		this.shipmentAssetId = shipmentAssetId;
	}

	
	public Integer getAssetPieceCount() {
	
		return assetPieceCount;
	}

	
	public void setAssetPieceCount(Integer assetPieceCount) {
	
		this.assetPieceCount = assetPieceCount;
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

	
	public String getAssetRemarks() {
	
		return assetRemarks;
	}

	
	public void setAssetRemarks(String assetRemarks) {
	
		this.assetRemarks = assetRemarks;
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

	public Long getInitiatingSystem() {
		return initiatingSystem;
	}

	public void setInitiatingSystem(Long initiatingSystem) {
		this.initiatingSystem = initiatingSystem;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}	
}
