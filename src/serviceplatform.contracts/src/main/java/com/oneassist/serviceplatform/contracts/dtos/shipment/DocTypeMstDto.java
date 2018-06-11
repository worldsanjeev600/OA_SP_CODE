package com.oneassist.serviceplatform.contracts.dtos.shipment;

public class DocTypeMstDto {
	private Long	docTypeId;
	private String	docName;
	private String	isMandatory;
	private String	description;
	private String	status;
	
	public Long getDocTypeId() {
		return docTypeId;
	}
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "DocTypeMstDto [docTypeId=" + docTypeId + ", docName=" + docName + ", isMandatory=" + isMandatory
				+ ", description=" + description + ", status=" + status + "]";
	}	
	
}
