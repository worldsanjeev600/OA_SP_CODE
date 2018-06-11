package com.oneassist.serviceplatform.externalcontracts;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

public class CustomerAssetDocumentDetails implements Serializable,
		 Comparable<CustomerAssetDocumentDetails> {

	private static final long serialVersionUID = 1L;
	private Long assetId;
	private Long assetDocId;
	private String docKey;
	private String docValue;
	private String docRemarks;
	private String docStatus;
	private String docMongoRefId;
	private Date docCreatedDate;
	private String docCreatedBy;
	private Date docModifiedDate;
	private String docModifiedBy;
	private InputStream inputStream;
	private File docFile;
	private String docFileFileName;
	private String docFileContentType;
	private String docDescription;
	private String docMandatory;
	private String docIntimationSentStatus;
	private String categoryCode;

	public String getIntimationSendDateString() {
		return intimationSendDateString;
	}

	public void setIntimationSendDateString(String intimationSendDateString) {
		this.intimationSendDateString = intimationSendDateString;
	}

	private Date docIntimationSentDate;
	private String intimationSendDateString;

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public Long getAssetDocId() {
		return assetDocId;
	}

	public void setAssetDocId(Long assetDocId) {
		this.assetDocId = assetDocId;
	}

	public String getDocKey() {
		return docKey;
	}

	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}

	public String getDocValue() {
		return docValue;
	}

	public void setDocValue(String docValue) {
		this.docValue = docValue;
	}

	public String getDocRemarks() {
		return docRemarks;
	}

	public void setDocRemarks(String docRemarks) {
		this.docRemarks = docRemarks;
	}

	public String getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	public String getDocMongoRefId() {
		return docMongoRefId;
	}

	public void setDocMongoRefId(String docMongoRefId) {
		this.docMongoRefId = docMongoRefId;
	}

	public Date getDocCreatedDate() {
		return docCreatedDate;
	}

	public void setDocCreatedDate(Date docCreatedDate) {
		this.docCreatedDate = docCreatedDate;
	}

	public String getDocCreatedBy() {
		return docCreatedBy;
	}

	public void setDocCreatedBy(String docCreatedBy) {
		this.docCreatedBy = docCreatedBy;
	}

	public Date getDocModifiedDate() {
		return docModifiedDate;
	}

	public String getDocModifiedBy() {
		return docModifiedBy;
	}

	public void setDocModifiedBy(String docModifiedBy) {
		this.docModifiedBy = docModifiedBy;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public File getDocFile() {
		return docFile;
	}

	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}

	public String getDocFileFileName() {
		return docFileFileName;
	}

	public void setDocFileFileName(String docFileFileName) {
		this.docFileFileName = docFileFileName;
	}

	public String getDocFileContentType() {
		return docFileContentType;
	}

	public void setDocFileContentType(String docFileContentType) {
		this.docFileContentType = docFileContentType;
	}

	@Override
	public String toString() {
		return "CustomerAssetDocumentDetails [assetId=" + assetId
				+ ", assetDocId=" + assetDocId + ", docKey=" + docKey
				+ ", docValue=" + docValue + ", docRemarks=" + docRemarks
				+ ", docStatus=" + docStatus + ", docMongoRefId="
				+ docMongoRefId + ", docCreatedDate=" + docCreatedDate
				+ ", docCreatedBy=" + docCreatedBy + ", docModifiedDate="
				+ docModifiedDate + ", docModifiedBy=" + docModifiedBy
				+ ", inputStream=" + inputStream + ", docFile=" + docFile
				+ ", docFileString=" + docFileFileName
				+ ", docFileContentType=" + docFileContentType + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docKey == null) ? 0 : docKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerAssetDocumentDetails other = (CustomerAssetDocumentDetails) obj;
		if (docKey == null) {
			if (other.docKey != null)
				return false;
		} else if (!docKey.equals(other.docKey))
			return false;
		return true;
	}

	@Override
	public int compareTo(CustomerAssetDocumentDetails o) {
		return o.getDocKey().compareTo(this.getDocKey());
	}

	public void setDocModifiedDate(Date docModifiedDate) {
		this.docModifiedDate = docModifiedDate;
	}

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public String getDocIntimationSentStatus() {

		return docIntimationSentStatus;
	}

	public void setDocIntimationSentStatus(String docIntimationSentStatus) {

		this.docIntimationSentStatus = docIntimationSentStatus;
	}

	public Date getDocIntimationSentDate() {

		return docIntimationSentDate;
	}

	public void setDocIntimationSentDate(Date docIntimationSentDate) {

		this.docIntimationSentDate = docIntimationSentDate;

	
	}

	public String getDocMandatory() {

		return docMandatory;
	}

	public void setDocMandatory(String docMandatory) {

		this.docMandatory = docMandatory;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

}
