package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

/**
 * Asset document
 * @author sanjeev.gupta
 *
 */
public class AssetDocument {

	private String docKey;
	private String fileName;
	private String docReferenceId;
	private String categoryCode;

	public String getDocKey() {
		return docKey;
	}

	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocReferenceId() {
		return docReferenceId;
	}

	public void setDocReferenceId(String docReferenceId) {
		this.docReferenceId = docReferenceId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

}
