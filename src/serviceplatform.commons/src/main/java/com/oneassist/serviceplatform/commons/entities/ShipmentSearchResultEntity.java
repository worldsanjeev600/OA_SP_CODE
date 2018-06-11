package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "SHIPMENT_SEARCH_RESULT_VIEW")
public class ShipmentSearchResultEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "shipmentId")
    private Long shipmentId;

    @Column(name = "serviceRequestTypeId")
    private Long serviceRequestTypeId;

    @Column(name = "primaryTrackingNumber")
    private String primaryTrackingNumber;

    @Column(name = "currentStage")
    private String currentStage;

    @Column(name = "logisticPartnerCode")
    private String logisticPartnerCode;

    @Column(name = "logisticPartnerRefTrackingNo")
    private String logisticPartnerRefTrackingNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    @Column(name = "createdOn")
    private Date createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    @Column(name = "modifiedOn")
    private Date modifiedOn;

    @Column(name = "hubId")
    private String hubId;

    @Column(name = "shipmentStatus")
    private String shipmentStatus;

    @Column(name = "serviceName")
    private String serviceName;

    @Column(name = "PODDOCTYPEID")
    private Long podDocTypeId;

    @Column(name = "LABELDOCTYPEID")
    private String labelDocTypeId;

    @Column(name = "LABLDOCTYPENAME")
    private String labelDocTypeName;

    @Column(name = "PODDOCTYPENAME")
    private String podDocTypeName;

    @Column(name = "PODMONGOREFID")
    private String podMongoRefId;

    @Column(name = "PODDOCUMENTNAME")
    private String podDocumentName;

    @Column(name = "LABELMONGOREFID")
    private String labelMongoRefId;

    @Column(name = "LABELDOCUMENTNAME")
    private String labelDocumentName;

    @Column(name = "SHIPMENTDECLAREVALUE")
    private Double shipmentDeclareValue;

    @Column(name = "BOXACTUALLENGTH")
    private Double boxActualLength;

    @Column(name = "BOXACTUALWIDTH")
    private Double boxActualWidth;

    @Column(name = "BOXACTUALHEIGHT")
    private Double boxActualHeight;

    @Column(name = "BOXACTUALWEIGHT")
    private Double boxActualWeight;

    @Column(name = "SHIPMENTBOXCOUNT")
    private Integer boxCount;

    @Column(name = "SERVICE_REQUEST_ID")
    private Long serviceRequestId;

    @Column(name = "REASONFORFAILURE")
    private String reasonForFailure;

    @Column(name = "SHIPMENTTYPE")
    private Integer shipmentType;

    @Column(name = "LABELDOCID")
    private String labelDocId;

    @Column(name = "PODDOCID")
    private Integer podDocId;

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

	public Long getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	public void setServiceRequestTypeId(Long serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	public String getPrimaryTrackingNumber() {
		return primaryTrackingNumber;
	}

	public void setPrimaryTrackingNumber(String primaryTrackingNumber) {
		this.primaryTrackingNumber = primaryTrackingNumber;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getPodDocTypeId() {
		return podDocTypeId;
	}

	public void setPodDocTypeId(Long podDocTypeId) {
		this.podDocTypeId = podDocTypeId;
	}

	public String getLabelDocTypeId() {
		return labelDocTypeId;
	}

	public void setLabelDocTypeId(String labelDocTypeId) {
		this.labelDocTypeId = labelDocTypeId;
	}

	public String getLabelDocTypeName() {
		return labelDocTypeName;
	}

	public void setLabelDocTypeName(String labelDocTypeName) {
		this.labelDocTypeName = labelDocTypeName;
	}

	public String getPodDocTypeName() {
		return podDocTypeName;
	}

	public void setPodDocTypeName(String podDocTypeName) {
		this.podDocTypeName = podDocTypeName;
	}

	public String getPodMongoRefId() {
		return podMongoRefId;
	}

	public void setPodMongoRefId(String podMongoRefId) {
		this.podMongoRefId = podMongoRefId;
	}

	public String getPodDocumentName() {
		return podDocumentName;
	}

	public void setPodDocumentName(String podDocumentName) {
		this.podDocumentName = podDocumentName;
	}

	public String getLabelMongoRefId() {
		return labelMongoRefId;
	}

	public void setLabelMongoRefId(String labelMongoRefId) {
		this.labelMongoRefId = labelMongoRefId;
	}

	public String getLabelDocumentName() {
		return labelDocumentName;
	}

	public void setLabelDocumentName(String labelDocumentName) {
		this.labelDocumentName = labelDocumentName;
	}

	public Double getShipmentDeclareValue() {
		return shipmentDeclareValue;
	}

	public void setShipmentDeclareValue(Double shipmentDeclareValue) {
		this.shipmentDeclareValue = shipmentDeclareValue;
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

	public Integer getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Integer boxCount) {
		this.boxCount = boxCount;
	}

	public Long getServiceRequestId() {
		return serviceRequestId;
	}

	public void setServiceRequestId(Long serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public String getReasonForFailure() {
		return reasonForFailure;
	}

	public void setReasonForFailure(String reasonForFailure) {
		this.reasonForFailure = reasonForFailure;
	}

	public Integer getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(Integer shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getLabelDocId() {
		return labelDocId;
	}

	public void setLabelDocId(String labelDocId) {
		this.labelDocId = labelDocId;
	}

	public Integer getPodDocId() {
		return podDocId;
	}

	public void setPodDocId(Integer podDocId) {
		this.podDocId = podDocId;
	}
}
