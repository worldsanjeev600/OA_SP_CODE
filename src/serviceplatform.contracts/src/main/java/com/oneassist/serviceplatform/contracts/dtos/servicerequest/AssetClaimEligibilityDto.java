package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class AssetClaimEligibilityDto {

	private String assetReferenceNo;
	private String statusCode;
	private String message;
	
	public String getAssetReferenceNo() {
		return assetReferenceNo;
	}
	public void setAssetReferenceNo(String assetReferenceNo) {
		this.assetReferenceNo = assetReferenceNo;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "AssetClaimEligibilityDto [assetReferenceNo=" + assetReferenceNo + ", statusCode=" + statusCode
				+ ", message=" + message + "]";
	}
	
	
	
	
	
}
