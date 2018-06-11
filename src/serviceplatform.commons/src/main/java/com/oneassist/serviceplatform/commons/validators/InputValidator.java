package com.oneassist.serviceplatform.commons.validators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;

@Component
@Scope("prototype")
public class InputValidator extends BaseValidator {

    private static final Logger logger = Logger.getLogger(InputValidator.class);

    @Autowired
    CacheFactory cacheFactory;

    private static final Pattern PATTERN_DOUBLE = Pattern.compile("^[-+]?\\d+(\\.{0,1}(\\d+?))?$");

    private static final Pattern PATTERN_ALPHANUM = Pattern.compile("^[a-zA-Z0-9]+$");

    private static final Pattern PATTERN_PHONE = Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");

    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[\\.@'!?&,`*_\\-a-zA-Z0-9À-ÖØ-öø-ÿ ]+$");

    public void validateRequiredField(String fieldName, Object value, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {

        if (value == null) {
            populateMandatoryFieldError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        } else if (value instanceof String && StringUtils.isBlank((String) value)) {
            populateMandatoryFieldError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        }
    }

    public void validateFieldMinLength(String fieldName, String value, int minSize, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {

        validateRequiredField(fieldName, value, errorInfoDtosList);

        if (value.length() < minSize) {
            populateInputFieldSizeError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        }
    }
    
    public void validateFieldMaxLength(String fieldName, String value, int maxSize, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {

        validateRequiredField(fieldName, value, errorInfoDtosList);

        if (value.length() > maxSize) {
            populateInputFieldSizeError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        }
    }

    public void validateDateFormatAndFutureDate(String fieldName, String inputDate, String dateFormat, List<ErrorInfoDto> errorInfoDtosList) {

        if (inputDate != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
            Date date = null;

            try {
                date = dateFormatter.parse(inputDate);
            } catch (Exception e) {
                populateInputDateFormatError(fieldName, errorInfoDtosList);
            }

            if (date != null && date.after(new Date())) {
                populateInputDateIsaFutureDateError(fieldName, errorInfoDtosList);
            }
        }
    }

    public void validateFutureDate(String fieldName, Date inputDate, List<ErrorInfoDto> errorInfoDtosList) {

        if (inputDate != null && inputDate.after(new Date())) {
            populateInputDateIsaFutureDateError(fieldName, errorInfoDtosList);
        }
    }

    public void validateDateFormatAndPastDate(String fieldName, String inputDate, String dateFormat, List<ErrorInfoDto> errorInfoDtosList) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        Date date = null;

        if (inputDate != null) {

            try {
                date = dateFormatter.parse(inputDate);
            } catch (Exception e) {
                populateInputDateFormatError(fieldName, errorInfoDtosList);
            }

            if (date != null && date.before(new Date())) {
                populateInputDateIsaPastDateError(fieldName, errorInfoDtosList);
            }
        }
    }

    public void validateDateFormat(String fieldName, String inputDate, String dateFormat, List<ErrorInfoDto> errorInfoDtosList) {

        SimpleDateFormat dFormat = new SimpleDateFormat(dateFormat);

        if (inputDate == null) {
            populateMandatoryFieldError(fieldName, errorInfoDtosList);
        } else if (inputDate != null) {
            try {
                dFormat.parse(inputDate);
            } catch (Exception e) {
                populateInputDateFormatError(fieldName, errorInfoDtosList);
            }
        }
    }

    public void validateAlphaNumeric(String fieldName, String value, List<ErrorInfoDto> errorInfoDtosList) {

        if (PATTERN_ALPHANUM.matcher(value).matches()) {
            populateInvalidAlphaNumericError(fieldName, errorInfoDtosList);
        }
    }

    public void validateMobileNumberFormat(String fieldName, String value, List<ErrorInfoDto> errorInfoDtosList) {

        if (PATTERN_PHONE.matcher(value).matches()) {
            populateInvalidMobileFormatError(fieldName, errorInfoDtosList);
        }
    }

    public void validateEmailIdFormat(String fieldName, String value, List<ErrorInfoDto> errorInfoDtosList) {

        if (PATTERN_EMAIL.matcher(value).matches()) {
            populateEmailIdFormatError(fieldName, errorInfoDtosList);
        }
    }

    public void populateMandatoryFieldError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage(String.valueOf(GenericResponseCodes.MANDATORY_FIELD.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    private void populateInputFieldSizeError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(11, messageSource.getMessage(String.valueOf(GenericResponseCodes.SIZE_ERROR.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    public void populateInputDateFormatError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(13, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_DATE_FORMAT.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    private void populateInvalidAlphaNumericError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(14, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_ALPHA_NUMERIC_FIELD.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    private void populateInvalidMobileFormatError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(12, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_MOBILENUM_FORMAT.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    private void populateEmailIdFormatError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(15, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_EMAIL_ID_FORMAT.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    public void populateInputDateIsaFutureDateError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(18, messageSource.getMessage(String.valueOf(GenericResponseCodes.INPUTDATE_GREATER_THAN_TODAY_ERROR.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    private void populateInputDateIsaPastDateError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(22, messageSource.getMessage(String.valueOf(GenericResponseCodes.INPUTDATE_LESS_THAN_TODAY_ERROR.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    public void validateFromCache(String fieldName, String cacheKey, String cacheName, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {

        Object object = cacheFactory.get(cacheName).get(cacheKey);

        if (null == object) {
            cacheError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        }
    }

    public void validateFromSystemConfigCache(String fieldName, String cacheKey, String cacheValue, String cacheName, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {

        List<SystemConfigDto> systemConfigDtosList = (List<SystemConfigDto>) cacheFactory.get(Constants.SYSTEM_CONFIG_MASTER_CACHE).get(cacheKey);

        if (null == systemConfigDtosList) {
            cacheError(fieldName, errorInfoDtosList);
            throw new InputValidationException();
        } else if (systemConfigDtosList.size() > 0) {
            boolean isFound = false;
            for (SystemConfigDto systemConfigDto : systemConfigDtosList) {
                if (systemConfigDto.getParamName().equalsIgnoreCase(cacheValue)) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                cacheError(fieldName, errorInfoDtosList);
                throw new InputValidationException();
            }
        }
    }

    private void cacheError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {
        errorInfoDto = new ErrorInfoDto(16, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_CACHE_VALUE.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    public void validateDoubleFormat(String fieldName, String value, List<ErrorInfoDto> errorInfoDtosList) {

        if (!PATTERN_DOUBLE.matcher(value).matches()) {
            errorInfoDto = new ErrorInfoDto(20, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_NUMBER_FIELD.getErrorCode()), new Object[] { "" }, null), fieldName);
            errorInfoDtosList.add(errorInfoDto);
        }
    }

    public void populateInvalidData(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(16, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_CACHE_VALUE.getErrorCode()), new Object[] { "" }, null), fieldName);
        errorInfoDtosList.add(errorInfoDto);
    }

    public void validateNumericData(String fieldName, String value, List<ErrorInfoDto> errorInfoDtosList) {

        if (!StringUtils.isNumeric(value)) {
            populateInvalidAlphaNumericError(fieldName, errorInfoDtosList);
        }
    }

//    public void populateInvalidNumberError(String fieldName, List<ErrorInfoDto> errorInfoDtosList) {
//
//        errorInfoDto = new ErrorInfoDto(20, messageSource.getMessage(String.valueOf(GenericResponseCodes.INVALID_NUMBER_FIELD.getErrorCode()), new Object[] { "" }, null), fieldName);
//        errorInfoDtosList.add(errorInfoDto);
//    }

    protected void validateSearchFilterCriteria(List<ErrorInfoDto> errorInfoDtosList) {

        populateSearchFilterCriteriaError(errorInfoDtosList);
    }

    private void populateSearchFilterCriteriaError(List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(23, messageSource.getMessage(String.valueOf(GenericResponseCodes.FILTER_CRITERIA_ERROR.getErrorCode()), new Object[] { "" }, null), "");
        errorInfoDtosList.add(errorInfoDto);
    }

    public void validateDateFormatAndfromDateToDate(String fromDateFiled, String fromDate, String toDateField, String toDate, String dateFormat, List<ErrorInfoDto> errorInfoDtosList)
            throws InputValidationException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        Date fdate = null;
        Date tdate = null;
        if (fromDate != null && toDate != null && fromDate.length() >= 0 && toDate.length() >= 0) {
            boolean isFormatError = true;
            try {
                fdate = dateFormatter.parse(fromDate);
            } catch (Exception e) {
                populateInputDateFormatError(fromDateFiled, errorInfoDtosList);
                isFormatError = false;
            }

            try {
                tdate = dateFormatter.parse(toDate);
            } catch (Exception e) {
                populateInputDateFormatError(toDateField, errorInfoDtosList);
                isFormatError = false;
            }

            if (isFormatError) {
                if (fdate.after(tdate)) {
                    populateFromDateAndToDateError(fromDateFiled, toDateField, errorInfoDtosList);
                }
            }
        }
    }

    private void populateFromDateAndToDateError(String fromDateFiled, String toDateField, List<ErrorInfoDto> errorInfoDtosList) {

        errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage(String.valueOf(GenericResponseCodes.FROMDATE_LESS_THAN_TODATE_ERROR.getErrorCode()), new Object[] { fromDateFiled, toDateField },
                null), fromDateFiled);
        errorInfoDtosList.add(errorInfoDto);
    }

    protected void validateMandatoryField(String fieldName, Object value, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException {
        if (value == null) {
            populateMandatoryFieldError(fieldName, errorInfoDtosList);
        } else if (value instanceof String && StringUtils.isBlank((String) value)) {
            populateMandatoryFieldError(fieldName, errorInfoDtosList);
        }
    }

    public Object validateFromCache(String value, String cacheName) {

        Object cacheObject = null;
        if (cacheFactory == null || cacheFactory.get(cacheName) == null) {
            logger.error("Either Cache Factory is Null (or) its not registered with the specified Cache: " + cacheName);
            return cacheObject;
        }
        
        cacheObject = cacheFactory.get(cacheName).get(value);
        return cacheObject;
    }
    
    public void populateInvalidRequestError(int errorCode, List<ErrorInfoDto> errorInfoDtosList) throws InputValidationException{

        errorInfoDto = new ErrorInfoDto(errorCode, messageSource.getMessage(String.valueOf(errorCode), new Object[] { ""}, null));
        errorInfoDtosList.add(errorInfoDto);
        throw new InputValidationException();
    }
}