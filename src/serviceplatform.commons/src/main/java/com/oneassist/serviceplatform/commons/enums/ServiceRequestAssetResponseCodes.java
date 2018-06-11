package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestAssetResponseCodes {
    
	CREATE_SERVICE_REQUEST_ASSET_FAILED(50002),
    CREATE_SERVICE_REQUEST_ASSET_SUCCESS(50001),
    UPDATE_SERVICE_REQUEST_ASSET_FAILED(50003),
    UPDATE_SERVICE_REQUEST_ASSET_SUCCESS(50004),
	
	DELETE_SERVICE_REQUEST_ASSET_FAILED(50005),
	DELETE_SERVICE_REQUEST_ASSET_SUCCESS(50006),
	INVALID_SERVICE_REQUEST_ASSET(50007),
	DUPLICATE_SERVICE_REQUEST_ASSET(50008),
	INVALID_SR_OR_DUPLICATE_SERVICE_REQUEST_ASSET(50009);
	
    private final int responseCode;
    
    ServiceRequestAssetResponseCodes(int responseCode) {
        this.responseCode = responseCode;
    }
    
    public int getErrorCode() {
        return this.responseCode;
    }
}
