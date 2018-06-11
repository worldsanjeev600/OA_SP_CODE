package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

/**
 * @author Divya
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentSearchResultDto extends BaseAuditDto {

    private Long shipmentId;
    private Integer shipmentType;
    private Long serviceId;
    private String trackingNumber;
    private String sender;
    private String recipient;
    private String sentBy;
    private String receivedBy;
    private String currentStage;
    private String logisticPartnerCode;
    private String logisticPartnerRefTrackingNumber;
    private Long originAddreesId;
    private Long destinationAddreesId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    private Date scheduledPickupDate;

    private Double shipmentDeclareValue;
    private Double boxActualLength;
    private Double boxActualWidth;
    private Double boxActualHeight;
    private Double boxActualWeight;

    private String hubId;

    private String partnerCode;
    private String partnerName;
    private String shipmentStatus;
    private String shipmentStage;
    private String serviceName;
    private ServiceAddressDetailDto originAddressDetails;
    private ServiceAddressDetailDto destinationAddressDetails;
    private List<ShipmentAssetDetailsDto> shipmentAssetDetails;
    private ServiceResponseDto serviceRequestDetails;

    public ServiceAddressDetailDto getOriginAddressDetails() {
        return originAddressDetails;
    }

    public ServiceResponseDto getServiceRequestDetails() {
        return serviceRequestDetails;
    }

    public void setServiceRequestDetails(ServiceResponseDto serviceRequestDetails) {
        this.serviceRequestDetails = serviceRequestDetails;
    }

    public void setOriginAddressDetails(ServiceAddressDetailDto originAddressDetails) {
        this.originAddressDetails = originAddressDetails;
    }

    public ServiceAddressDetailDto getDestinationAddressDetails() {
        return destinationAddressDetails;
    }

    public void setDestinationAddressDetails(ServiceAddressDetailDto destinationAddressDetails) {
        this.destinationAddressDetails = destinationAddressDetails;
    }

    public List<ShipmentAssetDetailsDto> getShipmentAssetDetails() {
        return shipmentAssetDetails;
    }

    public void setShipmentAssetDetails(List<ShipmentAssetDetailsDto> shipmentAssetDetails) {
        this.shipmentAssetDetails = shipmentAssetDetails;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getShipmentStage() {
        return shipmentStage;
    }

    public void setShipmentStage(String shipmentStage) {
        this.shipmentStage = shipmentStage;
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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
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

    public String getLogisticPartnerRefTrackingNumber() {
        return logisticPartnerRefTrackingNumber;
    }

    public void setLogisticPartnerRefTrackingNumber(String logisticPartnerRefTrackingNumber) {
        this.logisticPartnerRefTrackingNumber = logisticPartnerRefTrackingNumber;
    }

    public Long getOriginAddreesId() {
        return originAddreesId;
    }

    public void setOriginAddreesId(Long originAddreesId) {
        this.originAddreesId = originAddreesId;
    }

    public Long getDestinationAddreesId() {
        return destinationAddreesId;
    }

    public void setDestinationAddreesId(Long destinationAddreesId) {
        this.destinationAddreesId = destinationAddreesId;
    }

    public Date getScheduledPickupDate() {
        return scheduledPickupDate;
    }

    public void setScheduledPickupDate(Date scheduledPickupDate) {
        this.scheduledPickupDate = scheduledPickupDate;
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

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

	@Override
	public String toString() {
		return "ShipmentSearchResultDto [shipmentId=" + shipmentId + ", shipmentType=" + shipmentType + ", serviceId="
				+ serviceId + ", trackingNumber=" + trackingNumber + ", sender=" + sender + ", recipient=" + recipient
				+ ", sentBy=" + sentBy + ", receivedBy=" + receivedBy + ", currentStage=" + currentStage
				+ ", logisticPartnerCode=" + logisticPartnerCode + ", logisticPartnerRefTrackingNumber="
				+ logisticPartnerRefTrackingNumber + ", originAddreesId=" + originAddreesId + ", destinationAddreesId="
				+ destinationAddreesId + ", scheduledPickupDate=" + scheduledPickupDate + ", shipmentDeclareValue="
				+ shipmentDeclareValue + ", boxActualLength=" + boxActualLength + ", boxActualWidth=" + boxActualWidth
				+ ", boxActualHeight=" + boxActualHeight + ", boxActualWeight=" + boxActualWeight + ", hubId=" + hubId
				+ ", partnerCode=" + partnerCode + ", partnerName=" + partnerName + ", shipmentStatus=" + shipmentStatus
				+ ", shipmentStage=" + shipmentStage + ", serviceName=" + serviceName + ", originAddressDetails="
				+ originAddressDetails + ", destinationAddressDetails=" + destinationAddressDetails
				+ ", shipmentAssetDetails=" + shipmentAssetDetails + ", serviceRequestDetails=" + serviceRequestDetails
				+ "]";
	}
    
}
