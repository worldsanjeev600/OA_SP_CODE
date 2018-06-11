package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;

public class ServiceConfigDto implements Serializable {

    private String serviceCode;
    private String configKey;
    private String paramType;
    private String paramName;
    private String paramValue;
    private String status;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
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

    @Override
    public String toString() {
        return "ServiceConfigDto [serviceCode=" + serviceCode + ", configKey=" + configKey + ", paramType=" + paramType + ", paramName=" + paramName + ", paramValue=" + paramValue + ", status="
                + status + "]";
    }

}
