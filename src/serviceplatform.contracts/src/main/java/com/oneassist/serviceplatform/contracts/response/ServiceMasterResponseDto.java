package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class ServiceMasterResponseDto {
	
	private List<ServiceMasterDataDto> data;

	public List<ServiceMasterDataDto> getData() {
		return data;
	}

	public void setData(List<ServiceMasterDataDto> data) {
		this.data = data;
	}	
}