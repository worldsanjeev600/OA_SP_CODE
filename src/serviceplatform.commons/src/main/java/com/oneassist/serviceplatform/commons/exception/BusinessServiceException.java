package com.oneassist.serviceplatform.commons.exception;

import java.io.Serializable;
import java.util.List;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Component;

@Component
public class BusinessServiceException extends NestedRuntimeException implements Serializable {

    private static final long serialVersionUID = 1169426381288170661L;

    private List<ErrorInfoDto> invalidData;

    private int statusCode;

    private Object[] statusParamValues;

    @Autowired
    private MessageSource messageSource;

    public BusinessServiceException() {
        super("");
    }

    public BusinessServiceException(int statusCode) {
        this(statusCode, new Object[] { "" }, null, null, null);
    }

    public BusinessServiceException(int statusCode, Object[] statusParamValues) {
        this(statusCode, statusParamValues, null, null, null);
    }

    public BusinessServiceException(int statusCode, List<ErrorInfoDto> invalidData) {
        this(statusCode, new Object[] { "" }, null, null, invalidData);
    }

    public BusinessServiceException(int statusCode, Exception ex) {
        this(statusCode, new Object[] { "" }, null, ex, null);
    }

    public BusinessServiceException(int statusCode, List<ErrorInfoDto> invalidData, Exception ex) {
        this(statusCode, null, null, ex, invalidData);
    }

    public BusinessServiceException(int statusCode, Object[] statusParamValues, List<ErrorInfoDto> invalidData, Exception ex) {
        this(statusCode, statusParamValues, null, ex, invalidData);
    }

    public BusinessServiceException(int statusCode, Object[] statusParamValues, Exception ex) {
        this(statusCode, statusParamValues, null, ex, null);
    }

    public BusinessServiceException(String message) {
        this(0, new Object[] { "" }, message, null, null);
    }

    public BusinessServiceException(String message, Exception e) {
        this(0, new Object[] { "" }, message, e, null);
    }

    public BusinessServiceException(int statusCode, Object[] statusParamValues, String message, Exception ex, List<ErrorInfoDto> invalidData) {

        super(message, ex);

        this.setStatusCode(statusCode);
        this.setStatusParamValues(statusParamValues);
        this.setInvalidData(invalidData);
    }

    public List<ErrorInfoDto> getInvalidData() {
        return invalidData;
    }

    public void setInvalidData(List<ErrorInfoDto> invalidData) {
        this.invalidData = invalidData;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object[] getStatusParamValues() {
        return statusParamValues;
    }

    public void setStatusParamValues(Object[] statusParamValues) {
        this.statusParamValues = statusParamValues;
    }

    @Override
    public String getMessage() {

        if (this.getStatusCode() > 0) {
            if (messageSource == null) {
                return "msgSrc null " + this.getStatusCode();
            } else if (this.getStatusParamValues() == null) {
                return "getStatusParamValues null " + this.getStatusCode();
            } else {
                return messageSource.getMessage(String.valueOf(this.getStatusCode()), this.getStatusParamValues(), null);
            }
        } else {
            return super.getMessage();
        }
    }
}
