package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class PartnerMasterResponseDto {

	private List<PartnerMasterDataDto> data;

	public List<PartnerMasterDataDto> getData() {
		return data;
	}

	public void setData(List<PartnerMasterDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PartnerMasterResponseDto [data=" + data + "]";
	}	
	
}