package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

/**
 * Create request for getting document type config data
 * 
 * @author sanjeev.gupta
 *
 */
public class DocumentTypeConfigDto {

	private String serviceRequestTypeId;
	private String referenceCode;
	private Long insurancePartnerCode;
	private String isMandatory ;
	
	

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the serviceRequestTypeId
	 */
	public String getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	/**
	 * @param serviceRequestTypeId
	 *            the serviceRequestTypeId to set
	 */
	public void setServiceRequestTypeId(String serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	/**
	 * @return the referenceCode
	 */
	public String getReferenceCode() {
		return referenceCode;
	}

	/**
	 * @param referenceCode
	 *            the referenceCode to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * @return the insurancePartnerCode
	 */
	public Long getInsurancePartnerCode() {
		return insurancePartnerCode;
	}

	/**
	 * @param insurancePartnerCode
	 *            the insurancePartnerCode to set
	 */
	public void setInsurancePartnerCode(Long insurancePartnerCode) {
		this.insurancePartnerCode = insurancePartnerCode;
	}

}
