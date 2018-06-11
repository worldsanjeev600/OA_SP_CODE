package com.oneassist.serviceplatform.commons.exception;

import java.io.Serializable;
import org.springframework.core.NestedRuntimeException;

public class RestResourceException extends NestedRuntimeException implements Serializable {

    private static final long serialVersionUID = 1169426381288170661L;

    private int statusCode;

    private Object[] statusParamValues;

    public RestResourceException() {
        super("");
    }

    public RestResourceException(int statusCode, Exception ex) {

        this(statusCode, null, ex);
    }

    public RestResourceException(int statusCode, Object[] statusParamValues, Exception ex) {

        this(statusCode, null, statusParamValues, ex);
    }

    public RestResourceException(int statusCode, String msg, Object[] statusParamValues, Exception e) {

        super(msg, e == null ? new Exception() : e);

        this.setStatusCode(statusCode);
        this.setStatusParamValues(statusParamValues);
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
        return super.getMessage();
    }
}
