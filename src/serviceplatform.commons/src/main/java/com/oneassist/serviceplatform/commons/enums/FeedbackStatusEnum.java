package com.oneassist.serviceplatform.commons.enums;

public enum FeedbackStatusEnum {
	A("A"),NA("NA");
	
	private final String feedBackStatus;
	
	FeedbackStatusEnum(String status){
		this.feedBackStatus=status;
	}
	
	public String getFeedbackStatusEnum() {
		return this.feedBackStatus;
	}
}
