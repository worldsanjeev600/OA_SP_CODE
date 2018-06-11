package com.oneassist.serviceplatform.contracts.dtos;

public class BaseBulkUploadDto {
	
	private boolean success;
	
	private String errorMessage;
	
	private Long recordId;
	
	public boolean isSuccess() {		
		return success;
	}
	
	public void setSuccess(boolean success) {	
		this.success = success;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public Long getRecordId() {
		return recordId;
	}
	
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	@Override
	public String toString() {
		return "BaseBulkUploadDto [success=" + success 
				+ ", errorMessage="	+ errorMessage 
				+ ", recordId=" + recordId + "]";
	}
}