package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestSLABreachParams{
	
	ALLOCATE_TECHNICIAN_BREACH("AT"),
	VISIT_NOT_STARTED("VNS"),
	REPAIR_NOT_COMPLETED("RNC"),
	INSPECTION_CANCELLED_DUE_TO_SR_EXPIRATION("CEXP"),
	HA_BD_REPAIR_NOT_COMPLETED("RNC"),
	HA_AD_REPAIR_NOT_COMPLETED("RNC");

	private final String paramName;
	
	ServiceRequestSLABreachParams(String paramName){
		this.paramName = paramName;
	}
	
	public String getParamName(){
		return this.paramName;
	}	
}