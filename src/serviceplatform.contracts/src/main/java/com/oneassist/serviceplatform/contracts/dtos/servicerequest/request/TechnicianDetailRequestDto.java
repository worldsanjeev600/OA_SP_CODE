package com.oneassist.serviceplatform.contracts.dtos.servicerequest.request;

public class TechnicianDetailRequestDto {

	private String partnerCode;
	private String partnerBUCode;
	
	public String getPartnerCode() {
		return partnerCode;
	}
	
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	
	public String getPartnerBUCode() {
		return partnerBUCode;
	}
	
	public void setPartnerBUCode(String partnerBUCode) {
		this.partnerBUCode = partnerBUCode;
	}

	@Override
	public String toString() {
		return "TechnicianDetailRequestDto [partnerCode=" + partnerCode + ", partnerBUCode=" + partnerBUCode + "]";
	}
	
}