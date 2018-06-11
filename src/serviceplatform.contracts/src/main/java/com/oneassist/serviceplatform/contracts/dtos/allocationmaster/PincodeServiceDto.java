package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import com.oneassist.serviceplatform.contracts.dtos.BaseBulkUploadDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Pincode Service Model")
public class PincodeServiceDto extends BaseBulkUploadDto {
	
	@ApiModelProperty(value="Pincode Service ID")
	private Long pincodeServiceId;
	
	@ApiModelProperty(value="Pincode",required=true)
	private String pincode;
	
	@ApiModelProperty(value="Hub ID",required=true)
	private Long hubId;
	
	@ApiModelProperty(value="Courtesy Applicability for a Pincode",required=true)
	private String isCourtesyApplicable;
	
	@ApiModelProperty(value="Status")
	private String status;
	
	public Long getPincodeServiceId() {
		return pincodeServiceId;
	}
	
	public void setPincodeServiceId(Long pincodeServiceId) {
		this.pincodeServiceId = pincodeServiceId;
	}
	
	public String getPincode() {
		return pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public Long getHubId() {
		return hubId;
	}
	
	public void setHubId(Long hubId) {
		this.hubId = hubId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getIsCourtesyApplicable() {
		return isCourtesyApplicable;
	}
	
	@Override
	public String toString() {
		return "PincodeServiceDto [pincodeServiceId=" + pincodeServiceId
				+ ", pincode=" + pincode 
				+ ", hubId=" + hubId
				+ ", isCourtesyApplicable=" + isCourtesyApplicable
				+ ", status=" + status + "]";
	}

	public void setIsCourtesyApplicable(String isCourtesyApplicable) {
		this.isCourtesyApplicable = isCourtesyApplicable;
	}
}
