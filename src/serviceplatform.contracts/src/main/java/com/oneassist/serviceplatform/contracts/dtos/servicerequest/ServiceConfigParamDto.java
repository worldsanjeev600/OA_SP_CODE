package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Map;


public class ServiceConfigParamDto {

	private Map<String,Map<String,String>> paramTypeMap;
	
	public Map<String, Map<String, String>> getParamTypeMap() {	
		return paramTypeMap;
	}
	
	public void setParamTypeMap(Map<String, Map<String, String>> paramTypeMap) {	
		this.paramTypeMap = paramTypeMap;
	}

	
}