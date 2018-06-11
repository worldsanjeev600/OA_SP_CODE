package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class SystemConfigResponseDto {

	private List<SystemConfigDataDto> data;

	public List<SystemConfigDataDto> getData() {
		return data;
	}

	public void setData(List<SystemConfigDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "SystemConfigResponseDto [data=" + data + "]";
	}	
	
}