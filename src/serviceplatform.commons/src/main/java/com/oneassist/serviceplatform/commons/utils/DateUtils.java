package com.oneassist.serviceplatform.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;

@Component
public class DateUtils {

	private static String shortDateFormat;

	private static String longDateFormat;

	private static int businessStartTime24HrFormat;

	private static int businessEndTime24HrFormat;

	private static String dhlDateTimeFormat;

	private static String dayMonthDateFormat;

	public static String toShortFormattedString() {

		return toString(new Date(), shortDateFormat);
	}

	public static String toShortFormattedString(Date inputDate) {

		return toString(inputDate, shortDateFormat);
	}

	public static Date fromShortFormattedString(String inputDate) throws BusinessServiceException {
		try {
			return fromString(inputDate, shortDateFormat);
		} catch (ParseException ex) {
			throw new BusinessServiceException(ServiceRequestResponseCodes.INVALID_DATE_FORMAT.getErrorCode());
		}
	}

	public static String toDayMonthFormattedString(Date inputDate) {
		return toString(inputDate, dayMonthDateFormat);
	}

	public static String toLongFormattedString() {

		return toString(new Date(), longDateFormat);
	}

	public static String toLongFormattedString(Date inputDate) {

		return toString(inputDate, longDateFormat);
	}

	public static Date fromLongFormattedString(String inputDate) throws ParseException {

		return fromString(inputDate, longDateFormat);
	}

	public static Date fromString(String inputDate, String dateFormat) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		Date date = simpleDateFormat.parse(inputDate);

		return date;
	}

	public static String toString(Date inputDate, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		String dateString = simpleDateFormat.format(inputDate);

		return dateString;
	}

	@Value("${SHORT_DATE_FORMAT}")
	public void setShortDateFormat(String shortDateFormat) {
		DateUtils.shortDateFormat = shortDateFormat;
	}

	@Value("${DAY_MONTH_DATE_FORMAT}")
	public void setDayMonthDateFormat(String dayMonthDateFormat) {
		DateUtils.dayMonthDateFormat = dayMonthDateFormat;
	}

	@Value("${LONG_DATE_FORMAT}")
	public void setLongDateFormat(String longDateFormat) {
		DateUtils.longDateFormat = longDateFormat;
	}

	@Value("${BUSINESS_START_TIME_24HR_FORMAT}")
	public void setBusinessStartTime24HrFormat(int businessStartTime24HrFormat) {
		DateUtils.businessStartTime24HrFormat = businessStartTime24HrFormat;
	}

	@Value("${BUSINESS_END_TIME_24HR_FORMAT}")
	public void setBusinessEndTime24HrFormat(int businessEndTime24HrFormat) {
		DateUtils.businessEndTime24HrFormat = businessEndTime24HrFormat;
	}

	@Value("${DHL_DATE_FORMAT}")
	public void setDhlDateTimeFormat(String dhlDateTimeFormat) {
		DateUtils.dhlDateTimeFormat = dhlDateTimeFormat;
	}

	public static Date getNextBusinessDateTime(Date inputDate) {

		Date nextBusinessDate = org.apache.commons.lang.time.DateUtils.addDays(inputDate, 1);
		nextBusinessDate = org.apache.commons.lang.time.DateUtils.setHours(nextBusinessDate,
				businessStartTime24HrFormat);
		nextBusinessDate = org.apache.commons.lang.time.DateUtils.setMinutes(nextBusinessDate, 0);
		nextBusinessDate = org.apache.commons.lang.time.DateUtils.setSeconds(nextBusinessDate, 0);
		nextBusinessDate = org.apache.commons.lang.time.DateUtils.setMilliseconds(nextBusinessDate, 0);

		return nextBusinessDate;
	}

	public static Date getCurrentBusinessEndDateTime(Date inputDate) {
		Date businessEndTimeToday = org.apache.commons.lang.time.DateUtils.setHours(inputDate,
				businessEndTime24HrFormat);
		businessEndTimeToday = org.apache.commons.lang.time.DateUtils.setMinutes(businessEndTimeToday, 0);
		businessEndTimeToday = org.apache.commons.lang.time.DateUtils.setSeconds(businessEndTimeToday, 0);
		businessEndTimeToday = org.apache.commons.lang.time.DateUtils.setMilliseconds(businessEndTimeToday, 0);
		return businessEndTimeToday;
	}

	public static String toDhlDateFormatString(Date inputDate) {

		return toString(inputDate, dhlDateTimeFormat);
	}

	public static Date fromDhlFormattedString(String inputDate) throws ParseException {

		return fromString(inputDate, dhlDateTimeFormat);

	}

}
