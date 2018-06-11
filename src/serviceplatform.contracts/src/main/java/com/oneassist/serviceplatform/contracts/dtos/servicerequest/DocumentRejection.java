package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class DocumentRejection {

	private String documentTypeId;
	private String documentName;
	private String documentId;
	private RejectionStatus documentStatus;

	public String getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public RejectionStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(RejectionStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
}
