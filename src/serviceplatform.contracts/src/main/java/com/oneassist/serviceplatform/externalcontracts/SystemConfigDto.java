package com.oneassist.serviceplatform.externalcontracts;

public class SystemConfigDto {

    private String paramName;
    private String paramCode;
    private String paramValue;
    private String status;
    private String event;
    private String paramType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

	@Override
	public String toString() {
		return "SystemConfigDto [paramName=" + paramName + ", paramCode=" + paramCode + ", paramValue=" + paramValue
				+ ", status=" + status + ", event=" + event + ", paramType=" + paramType + "]";
	}
    
}
