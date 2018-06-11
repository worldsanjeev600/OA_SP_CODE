package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

public class PincodeServiceFulfilmentResponse {
	private Long fulfilmentId;
	private String pincode;
	private Long serviceId;
	private Long partnerPriority;
	private Long partnerCode;
	private Long serviceTat;
	private String subCategoryCode;
	private Long courtesyDeviceId;

	public Long getFulfilmentId() {
		return fulfilmentId;
	}
	
	public void setFulfilmentId(Long fulfilmentId) {
		this.fulfilmentId = fulfilmentId;
	}
	
	public String getPincode() {
		return pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public Long getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	
	public Long getPartnerPriority() {
		return partnerPriority;
	}
	
	public void setPartnerPriority(Long partnerPriority) {
		this.partnerPriority = partnerPriority;
	}
	
	public Long getPartnerCode() {
		return partnerCode;
	}
	
	public void setPartnerCode(Long partnerCode) {
		this.partnerCode = partnerCode;
	}
	
	public Long getServiceTat() {
		return serviceTat;
	}
	
	public void setServiceTat(Long serviceTat) {
		this.serviceTat = serviceTat;
	}
	
	public String getSubCategoryCode() {
		return subCategoryCode;
	}
	
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
	
	public Long getCourtesyDeviceId() {
		return courtesyDeviceId;
	}
	
	public void setCourtesyDeviceId(Long courtesyDeviceId) {
		this.courtesyDeviceId = courtesyDeviceId;
	}	
}
