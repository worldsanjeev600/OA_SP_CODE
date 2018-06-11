package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

public class OASYSMembershipDetails {

	private List<AssetDetailDto> assets;
	private Long memId;
	private String startDate;
	private String endDate;

	public List<AssetDetailDto> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetDetailDto> assets) {
		this.assets = assets;
	}

	public Long getMemId() {
		return memId;
	}

	public void setMemId(Long memId) {
		this.memId = memId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "OASYSMembershipDetails [assets=" + assets + ", memId=" + memId + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
	
	

}
