package com.oneassist.serviceplatform.contracts.dtos.shipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@ApiModel(description="Shipment Search Request Model")
public class ShipmentSearchRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="Unique Identifier for a Shipment")
	private Long shipmentId;
	
	@ApiModelProperty(value="Logistic Partner Reference Tracking Number")
	private String logisticPartnerRefTrackingNumber;
	
	@ApiModelProperty(value="Service ID")
	private Long serviceId;
	
	@ApiModelProperty(value="Shipment Status")
	private String status;
	
	@ApiModelProperty(value="Shipment tracking no")
	private String trackingNo;

	@ApiModelProperty(value="Logistic Partner Code")
	private String logisticPartnerCode;
	
	@ApiModelProperty(value="Hub ID")
	private String hubId;
		
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	@ApiModelProperty(value="From Date",example="01-JAN-1990")
	private Date fromDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	@ApiModelProperty(value="To Date",example="01-JAN-1990")
	private Date toDate;
	
	@ApiModelProperty(value="Shipment Stage")
	private String stage;
	
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
	
	public Long getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTrackingNo() {
		return trackingNo;
	}
	
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	
	public String getLogisticPartnerCode() {
		return logisticPartnerCode;
	}
	
	public void setLogisticPartnerCode(String logisticPartnerCode) {
		this.logisticPartnerCode = logisticPartnerCode;
	}
		
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getStage() {
		return stage;
	}
	
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public String getHubId() {
		return hubId;
	}
	
	public void setHubId(String hubId) {
		this.hubId = hubId;
	}

	@Override
	public String toString() {
		return "ShipmentSearchRequestDto [shipmentId=" + shipmentId + ", logisticPartnerRefTrackingNumber="
				+ logisticPartnerRefTrackingNumber + ", serviceId=" + serviceId + ", status=" + status + ", trackingNo="
				+ trackingNo + ", logisticPartnerCode=" + logisticPartnerCode + ", hubId=" + hubId + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", stage=" + stage + "]";
	}	
	
}