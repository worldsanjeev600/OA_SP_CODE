package com.oneassist.serviceplatform.contracts.response;

import java.util.List;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;

public class SystemConfigDataDto {

    private String paramCode;
    private List<SystemConfigDto> systemConfig;

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public List<SystemConfigDto> getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(List<SystemConfigDto> systemConfig) {
        this.systemConfig = systemConfig;
    }

	@Override
	public String toString() {
		return "SystemConfigDataDto [paramCode=" + paramCode + ", systemConfig=" + systemConfig + "]";
	}
    
}
