package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

public class PincodeCourtesyResponse{
	
	private String pincode;
	
	private Long hubId;
	
	private String isCourtesyApplicable;
	
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
	
	public String getIsCourtesyApplicable() {
		return isCourtesyApplicable;
	}
	
	public void setIsCourtesyApplicable(String isCourtesyApplicable) {
		this.isCourtesyApplicable = isCourtesyApplicable;
	}	
}