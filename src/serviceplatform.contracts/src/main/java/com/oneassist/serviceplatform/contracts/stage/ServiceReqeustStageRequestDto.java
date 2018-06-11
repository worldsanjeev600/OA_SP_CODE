package com.oneassist.serviceplatform.contracts.stage;

public class ServiceReqeustStageRequestDto {
	private String serviceReqeustType;

	public String getServiceReqeustType() {
		return serviceReqeustType;
	}

	public void setServiceReqeustType(String serviceReqeustType) {
		this.serviceReqeustType = serviceReqeustType;
	}

	@Override
	public String toString() {
		return "ServiceReqeustStageRequestDto [serviceReqeustType=" + serviceReqeustType + "]";
	}
	
	
	
}
