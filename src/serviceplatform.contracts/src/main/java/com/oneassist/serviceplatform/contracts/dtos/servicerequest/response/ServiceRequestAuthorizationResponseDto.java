
package com.oneassist.serviceplatform.contracts.dtos.servicerequest.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Service Request Authorization Model")
public class ServiceRequestAuthorizationResponseDto {
	
	@ApiModelProperty(value="Service Start Authorization Code")
	private String serviceStartCode;
	@ApiModelProperty(value="Service End Authorization Code")
	private String serviceEndCode;
	public String getServiceStartCode() {
		return serviceStartCode;
	}
	public void setServiceStartCode(String serviceStartCode) {
		this.serviceStartCode = serviceStartCode;
	}
	public String getServiceEndCode() {
		return serviceEndCode;
	}
	public void setServiceEndCode(String serviceEndCode) {
		this.serviceEndCode = serviceEndCode;
	}
	@Override
	public String toString() {
		return "ServiceRequestAuthorizationResponseDto [serviceStartCode=" + serviceStartCode + ", serviceEndCode="
				+ serviceEndCode + "]";
	}
}
