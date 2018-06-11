package com.oneassist.serviceplatform.commons.utils;

import java.text.DecimalFormat;
import java.util.Objects;

public class DecimalFormatter {

	private static final String DECIMAL_FORMAT_INR = "0.00";
	
	public static String getFormattedValue(Double inputValue) {
		// RVW: Please check if this also handles null values
		DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT_INR);
		String value = decimalFormat.format(inputValue);
		return value;
	}
	
	public static String getEmptyDecimalIfNull(String input) {
        return Objects.toString(input, DECIMAL_FORMAT_INR);
    }
}
