package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.util.Date;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Divya
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentSearchViewResultDto {

    private Long id;
    private Long shipmentId;
    private Long serviceRequestTypeId;
    private String primaryTrackingNumber;
    private String currentStage;
    private String logisticPartnerCode;

    private String logisticPartnerRefTrackingNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    private Date createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    private Date modifiedOn;

    private String hubId;

    private String shipmentStatus;

    private String serviceName;

    private Long podDocTypeId;

    private String labelDocTypeId;

    private String labelDocTypeName;

    private String podDocTypeName;

    private String podMongoRefId;

    private String podDocumentName;

    private String labelMongoRefId;

    private String labelDocumentName;

    private String partnerName;

    private String shipmentStage;
    private String shipmentStageId;

    private Double shipmentDeclareValue;
    private Double boxActualLength;
    private Double boxActualWidth;
    private Double boxActualHeight;
    private Double boxActualWeight;

    private Integer boxCount;
    private String shipmentStatusCode;
    private String shipmentStageCode;
    private Long serviceRequestId;
    private Integer shipmentType;
    private String reasonForFailure;
    private String labelDocId;
    private Integer podDocId;
    private Map<String, String> stages;

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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getShipmentStage() {
        return shipmentStage;
    }

    public void setShipmentStage(String shipmentStage) {
        this.shipmentStage = shipmentStage;
    }

    public String getShipmentStageId() {
        return shipmentStageId;
    }

    public void setShipmentStageId(String shipmentStageId) {
        this.shipmentStageId = shipmentStageId;
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

    public String getShipmentStatusCode() {
        return shipmentStatusCode;
    }

    public void setShipmentStatusCode(String shipmentStatusCode) {
        this.shipmentStatusCode = shipmentStatusCode;
    }

    public String getShipmentStageCode() {
        return shipmentStageCode;
    }

    public void setShipmentStageCode(String shipmentStageCode) {
        this.shipmentStageCode = shipmentStageCode;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Integer getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(Integer shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getReasonForFailure() {
        return reasonForFailure;
    }

    public void setReasonForFailure(String reasonForFailure) {
        this.reasonForFailure = reasonForFailure;
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

    public Map<String, String> getStages() {
        return stages;
    }

    public void setStages(Map<String, String> stages) {
        this.stages = stages;
    }

	@Override
	public String toString() {
		return "ShipmentSearchViewResultDto [id=" + id + ", shipmentId=" + shipmentId + ", serviceRequestTypeId="
				+ serviceRequestTypeId + ", primaryTrackingNumber=" + primaryTrackingNumber + ", currentStage="
				+ currentStage + ", logisticPartnerCode=" + logisticPartnerCode + ", logisticPartnerRefTrackingNo="
				+ logisticPartnerRefTrackingNo + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", hubId="
				+ hubId + ", shipmentStatus=" + shipmentStatus + ", serviceName=" + serviceName + ", podDocTypeId="
				+ podDocTypeId + ", labelDocTypeId=" + labelDocTypeId + ", labelDocTypeName=" + labelDocTypeName
				+ ", podDocTypeName=" + podDocTypeName + ", podMongoRefId=" + podMongoRefId + ", podDocumentName="
				+ podDocumentName + ", labelMongoRefId=" + labelMongoRefId + ", labelDocumentName=" + labelDocumentName
				+ ", partnerName=" + partnerName + ", shipmentStage=" + shipmentStage + ", shipmentStageId="
				+ shipmentStageId + ", shipmentDeclareValue=" + shipmentDeclareValue + ", boxActualLength="
				+ boxActualLength + ", boxActualWidth=" + boxActualWidth + ", boxActualHeight=" + boxActualHeight
				+ ", boxActualWeight=" + boxActualWeight + ", boxCount=" + boxCount + ", shipmentStatusCode="
				+ shipmentStatusCode + ", shipmentStageCode=" + shipmentStageCode + ", serviceRequestId="
				+ serviceRequestId + ", shipmentType=" + shipmentType + ", reasonForFailure=" + reasonForFailure
				+ ", labelDocId=" + labelDocId + ", podDocId=" + podDocId + ", stages=" + stages + "]";
	}
    
}
