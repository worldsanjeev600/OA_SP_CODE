package com.oneassist.serviceplatform.contracts.dtos.shipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

/**
 * @author Satish Kumar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@ApiModel(description = "Shipment Request Model")
public class ShipmentRequestDto implements Serializable {

    private static final long serialVersionUID = 8960499538640784446L;

    @ApiModelProperty(value = "Unique Identifier for a Shipment", required = false)
    private Long shipmentId;

    @ApiModelProperty(value = "Type of Shipment", required = true)
    private Integer shipmentType;

    @ApiModelProperty(value = "Shipment Sender", required = true)
    private String sender;

    @ApiModelProperty(value = "Shipment Recipient", required = true)
    private String recipient;

    private String sentBy;
    
    private String receivedBy;
    
    private String currentStage;
    
    private String logisticPartnerCode;
    
    private String logisticPartnerRefTrackingNumber;
    
    private Long originAddreesId;
    
    private Long destinationAddreesId;
    
    private Date scheduledPickupDate;

    private Double shipmentDeclareValue;

    @ApiModelProperty(value = "Box Actual Length", required = true)
    private Double boxActualLength;
    
    @ApiModelProperty(value = "Box Actual Width", required = true)
    private Double boxActualWidth;
    
    @ApiModelProperty(value = "Box Actual Height", required = true)
    private Double boxActualHeight;
    
    @ApiModelProperty(value = "Box Actual Weight", required = true)
    private Double boxActualWeight;
    
    private Date createdOn;

    @ApiModelProperty(value = "Shipment Created By", required = true)
    private String createdBy;

    private Date modifiedOn;
    
    private String modifiedBy;
    
    @ApiModelProperty(value = "Hub ID", required = true)
    private String hubId;
    
    private String status;
    
    private String boxLengthUnit;
    
    private String boxWidthUnit;
    
    private String boxHeightUnit;
    
    private String boxWeightUnit;
    
    private String boxCount;
    
    private String subCategoryCode;
    
    private String currency;
    
    private String customerPincode;

    private ServiceAddressDetailDto originAddressDetails;
    
    private ServiceAddressDetailDto destinationAddressDetails;
    
    private List<ShipmentAssetDetailsDto> shipmentAssetDetails;
    
    private ServiceResponseDto serviceRequestDetails;

    public ServiceAddressDetailDto getOriginAddressDetails() {
        return originAddressDetails;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
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

    public Date getCreatedOn() {
        return createdOn;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(String boxCount) {
        this.boxCount = boxCount;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerPincode() {
        return customerPincode;
    }

    public void setCustomerPincode(String customerPincode) {
        this.customerPincode = customerPincode;
    }

    public ServiceResponseDto getServiceRequestDetails() {
        return serviceRequestDetails;
    }

    public void setServiceRequestDetails(ServiceResponseDto serviceRequestDetails) {
        this.serviceRequestDetails = serviceRequestDetails;
    }

	@Override
	public String toString() {
		return "ShipmentRequestDto [shipmentId=" + shipmentId + ", shipmentType=" + shipmentType + ", sender=" + sender
				+ ", recipient=" + recipient + ", sentBy=" + sentBy + ", receivedBy=" + receivedBy + ", currentStage="
				+ currentStage + ", logisticPartnerCode=" + logisticPartnerCode + ", logisticPartnerRefTrackingNumber="
				+ logisticPartnerRefTrackingNumber + ", originAddreesId=" + originAddreesId + ", destinationAddreesId="
				+ destinationAddreesId + ", scheduledPickupDate=" + scheduledPickupDate + ", shipmentDeclareValue="
				+ shipmentDeclareValue + ", boxActualLength=" + boxActualLength + ", boxActualWidth=" + boxActualWidth
				+ ", boxActualHeight=" + boxActualHeight + ", boxActualWeight=" + boxActualWeight + ", createdOn="
				+ createdOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy
				+ ", hubId=" + hubId + ", status=" + status + ", boxLengthUnit=" + boxLengthUnit + ", boxWidthUnit="
				+ boxWidthUnit + ", boxHeightUnit=" + boxHeightUnit + ", boxWeightUnit=" + boxWeightUnit + ", boxCount="
				+ boxCount + ", subCategoryCode=" + subCategoryCode + ", currency=" + currency + ", customerPincode="
				+ customerPincode + ", originAddressDetails=" + originAddressDetails + ", destinationAddressDetails="
				+ destinationAddressDetails + ", shipmentAssetDetails=" + shipmentAssetDetails
				+ ", serviceRequestDetails=" + serviceRequestDetails + "]";
	}
    
}