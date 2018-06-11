package com.oneassist.serviceplatform.contracts.dtos.servicerequest.response;

/**
 * 
 * @author sanjeev.gupta
 *
 */
public class DocumentTypeConfigResponse {

	private Long docTypeId;
	private String docName;
	private String isMandatory;
	private String referenceCode;
	private Long insurancePartnerCode;
	private String description;
	private String docKey;

	public String getDocKey() {
		return docKey;
	}

	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the docTypeId
	 */
	public Long getDocTypeId() {
		return docTypeId;
	}

	/**
	 * @param docTypeId
	 *            the docTypeId to set
	 */
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName
	 *            the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * @return the isMandatory
	 */
	public String getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory
	 *            the isMandatory to set
	 */
	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
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
