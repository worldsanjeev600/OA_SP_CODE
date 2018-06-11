package com.oneassist.serviceplatform.commons.enums;

public enum GenericResponseCodes {
	MANDATORY_FIELD(10), 
	SIZE_ERROR(11), 
	INVALID_MOBILENUM_FORMAT(12), 
	INVALID_DATA_FORMAT(13),
    INVALID_ALPHA_NUMERIC_FIELD(14), 
    INVALID_EMAIL_ID_FORMAT(15), 
    INVALID_CACHE_VALUE(16),
    VALIDATION_FAIL(17),
    INPUTDATE_GREATER_THAN_TODAY_ERROR(18),
    INVALID_DATE_FORMAT(19),
    INVALID_NUMBER_FIELD(20), 
    INVALID_REQUEST(21), 
    INPUTDATE_LESS_THAN_TODAY_ERROR(22), 
    FILTER_CRITERIA_ERROR(23),
    FROMDATE_LESS_THAN_TODATE_ERROR(24);

	private final int	responseCode;

	GenericResponseCodes(int responseCode) {

		this.responseCode = responseCode;
	}

	public int getErrorCode() {

		return this.responseCode;
	}
}	