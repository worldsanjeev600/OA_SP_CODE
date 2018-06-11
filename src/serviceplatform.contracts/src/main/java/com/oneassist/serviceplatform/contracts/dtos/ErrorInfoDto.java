package com.oneassist.serviceplatform.contracts.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sanjeev.gupta
 */
@JsonInclude(Include.NON_NULL)
public class ErrorInfoDto {

    private int errorCode;
    
    private String errorMessage;
    
    private String errorField;

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorCode
     * @param errorMessage
     */
    public ErrorInfoDto(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     * @param errorCode
     * @param errorMessage
     */
    public ErrorInfoDto(int errorCode, String errorMessage , String errorFiled) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorField = errorFiled;
    }


    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
	 * 
	 */
    public ErrorInfoDto() {
        super();
    }

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorFiled) {
		this.errorField = errorFiled;
	}
}