package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestSortEnum {

	CREATED_ON("createdOn"),
	ACTUAL_END_DATETIME("actualEndDateTime"),
	SERVICE_REQUEST_ID("serviceRequestId");
	
	private String sortBy;
	
	ServiceRequestSortEnum(String sortBy){
		 this.sortBy=sortBy;
}
	
	public String getSortBy(ServiceRequestSortEnum sortBy){
		return this.sortBy;
	}
	
	   public static ServiceRequestSortEnum getServiceRequestSortBy(String sortBy) {

	        for (ServiceRequestSortEnum enumValue : ServiceRequestSortEnum.values()) {
	            if (enumValue.getSortBy(enumValue).equals(sortBy)) {
	                return enumValue;
	            }
	        }
	        return null;
	    }
	
	
}
