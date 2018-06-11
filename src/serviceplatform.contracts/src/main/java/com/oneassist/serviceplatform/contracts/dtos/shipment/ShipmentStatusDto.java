
package com.oneassist.serviceplatform.contracts.dtos.shipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.oneassist.serviceplatform.contracts.dtos.BaseBulkUploadDto;

@ApiModel(description="Shipment Status Model")
public class ShipmentStatusDto extends BaseBulkUploadDto {

	@ApiModelProperty(value="Logistic Partner Code",required=true)
	private Long	partnerCode;

	@ApiModelProperty(value="Unique Identifier for a Shipment",required=true)
	private Long	shipmentId;

	@ApiModelProperty(value="Saves AWB, Logistic Partner Reference Tracking Number")
	private String	logisticPartnerRefTrackingNumber;
	
	@ApiModelProperty(value="Shipment Stage",required=true)
	private String	currentStage;
	
	@ApiModelProperty(value="Shipment Modification Date",required=true)
	private String	statusUpdateDate;
	
	@ApiModelProperty(value="Shipment Modification Time",required=true)
	private String	statusUpdateTime;

	public Long getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(Long partnerCode) {
		this.partnerCode = partnerCode;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getLogisticPartnerRefTrackingNumber() {
		return logisticPartnerRefTrackingNumber;
	}

	public void setLogisticPartnerRefTrackingNumber(String logisticPartnerRefTrackingNumber) {
		this.logisticPartnerRefTrackingNumber = logisticPartnerRefTrackingNumber;
	}

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public String getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(String statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String getStatusUpdateTime() {
		return statusUpdateTime;
	}

	public void setStatusUpdateTime(String statusUpdateTime) {
		this.statusUpdateTime = statusUpdateTime;
	}

	@Override
	public String toString() {
		return "ShipmentStatusDto [partnerCode=" + partnerCode + ", shipmentId=" + shipmentId + ", logisticPartnerRefTrackingNumber=" + logisticPartnerRefTrackingNumber + ", currentStage=" + currentStage + ", statusUpdateDate=" + statusUpdateDate + ", statusUpdateTime=" + statusUpdateTime + ", getRecordId()=" + getRecordId() + "]";
	}
}
