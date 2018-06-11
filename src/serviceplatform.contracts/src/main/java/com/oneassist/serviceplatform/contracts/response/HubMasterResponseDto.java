package com.oneassist.serviceplatform.contracts.response;

import java.util.List;

public class HubMasterResponseDto {

	private List<HubMasterDataDto> data;

	public List<HubMasterDataDto> getData() {
		return data;
	}

	public void setData(List<HubMasterDataDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HubMasterResponseDto [data=" + data + "]";
	}
	
	
}