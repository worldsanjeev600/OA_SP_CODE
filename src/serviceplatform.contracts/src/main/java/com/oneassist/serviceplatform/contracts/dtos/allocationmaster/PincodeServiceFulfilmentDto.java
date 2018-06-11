package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import com.oneassist.serviceplatform.contracts.dtos.BaseBulkUploadDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Pincode Service Fulfilment Model")
public class PincodeServiceFulfilmentDto extends BaseBulkUploadDto {
	@ApiModelProperty(value = "Fulfilment ID")
	private Long fulfilmentId;

	@ApiModelProperty(value = "Pincode", required = true)
	private String pincode;

	@ApiModelProperty(value = "Service Request Type ID")
	private Long serviceRequestTypeId;

	@ApiModelProperty(value = "Partner Priority")
	private int partnerPriority;

	@ApiModelProperty(value = "Partner Code", required = true)
	private Long partnerCode;

	@ApiModelProperty(value = "Service TAT", required = true)
	private int serviceTat;

	@ApiModelProperty(value = "Sub category Code")
	private String subCategoryCode;

	/*
	 * @ApiModelProperty(value="Courtesy Device ID") private int
	 * courtesyDeviceId;
	 */

	@ApiModelProperty(value = "Status")
	private String status;

	@ApiModelProperty(value = "Service Request Type", required = true)
	private String serviceRequestType;

	public Long getFulfilmentId() {
		return fulfilmentId;
	}

	public void setFulfilmentId(Long fulfilmentId) {
		this.fulfilmentId = fulfilmentId;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}

	public int getPartnerPriority() {
		return partnerPriority;
	}

	public void setPartnerPriority(int partnerPriority) {
		this.partnerPriority = partnerPriority;
	}

	public Long getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(Long partnerCode) {
		this.partnerCode = partnerCode;
	}

	public int getServiceTat() {
		return serviceTat;
	}

	public void setServiceTat(int serviceTat) {
		this.serviceTat = serviceTat;
	}

	/*
	 * public int getCourtesyDeviceId() { return courtesyDeviceId; }
	 * 
	 * public void setCourtesyDeviceId(int courtesyDeviceId) {
	 * this.courtesyDeviceId = courtesyDeviceId; }
	 */

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	public void setServiceRequestTypeId(Long serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	public String getServiceRequestType() {
		return serviceRequestType;
	}

	public void setServiceRequestType(String serviceRequestType) {
		this.serviceRequestType = serviceRequestType;
	}

	@Override
	public String toString() {
		return "PincodeServiceFulfilmentDto [fulfilmentId=" + fulfilmentId
				+ ", pincode=" + pincode + ", serviceRequestTypeId="
				+ serviceRequestTypeId + ", partnerPriority=" + partnerPriority
				+ ", partnerCode=" + partnerCode + ", serviceTat=" + serviceTat
				+ ", subCategoryCode=" + subCategoryCode
				+ ", courtesyDeviceId=" + ", status=" + status + "]";
	}
}
