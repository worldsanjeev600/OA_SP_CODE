package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class ServiceRequestRuleDto {
	
	private Long servicePartnerCode;
	private int serviceRequestSLAInDays;
	private int serviceRequestSLAExtentionDays;
	private String criteria;
	private String productCode;
	private int isRuleValid;
	
	public Long getServicePartnerCode() {
		return servicePartnerCode;
	}

	public void setServicePartnerCode(Long servicePartnerCode) {
		this.servicePartnerCode = servicePartnerCode;
	}

	public int getServiceRequestSLAInDays() {
		return serviceRequestSLAInDays;
	}

	public void setServiceRequestSLAInDays(int serviceRequestSLAInDays) {
		this.serviceRequestSLAInDays = serviceRequestSLAInDays;
	}

	public int getServiceRequestSLAExtentionDays() {
		return serviceRequestSLAExtentionDays;
	}

	public void setServiceRequestSLAExtentionDays(int serviceRequestSLAExtentionDays) {
		this.serviceRequestSLAExtentionDays = serviceRequestSLAExtentionDays;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getIsRuleValid() {
		return isRuleValid;
	}

	public void setIsRuleValid(int isRuleValid) {
		this.isRuleValid = isRuleValid;
	}

	@Override
	public String toString() {
		return "ServiceRequestRuleDto [servicePartnerCode=" + servicePartnerCode 
				+ ", serviceRequestSLAInDays=" + serviceRequestSLAInDays 
				+ ", serviceRequestSLAExtentionDays=" + serviceRequestSLAExtentionDays
				+ ", criteria=" + criteria 
				+ ", productCode=" + productCode 
				+ ", isRuleValid=" + isRuleValid + "]";
	}	
}