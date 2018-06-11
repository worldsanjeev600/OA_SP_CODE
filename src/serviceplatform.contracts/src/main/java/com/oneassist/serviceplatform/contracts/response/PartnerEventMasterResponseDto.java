package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class PartnerEventMasterResponseDto {

	private List<PartnerEventDetailDataDto> data;

	public List<PartnerEventDetailDataDto> getData() {
		return data;
	}

	public void setData(List<PartnerEventDetailDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PartnerEventMasterResponseDto [data=" + data + "]";
	}	
	
}