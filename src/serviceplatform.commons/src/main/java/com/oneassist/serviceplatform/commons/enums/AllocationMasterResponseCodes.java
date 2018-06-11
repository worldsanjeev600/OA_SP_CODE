package com.oneassist.serviceplatform.commons.enums;

public enum AllocationMasterResponseCodes {

	PINCODE_FULLFILMENTS_SUCCESS(10015),
	PINCODE_FULLFILMENTS_FAILS(10016);

	private final int	responseCode;

	AllocationMasterResponseCodes(int responseCode) {

		this.responseCode = responseCode;
	}

	public int getErrorCode() {

		return this.responseCode;
	}
}