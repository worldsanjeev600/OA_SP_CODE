package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestCurrentStage {
	
	ASSIGNED("ASG"), 
	INTRANSIT("INT"),
	DELIVERED("DEL"), 
	RETURNED("RTO"),
	EXCEPTION("EXC"),
	UNASSIGNED("USG"),
	PICK_UP_DONE("PUD"),
	PICK_UP_FAILED("PUF");
	
	private final String currentstage;

	ServiceRequestCurrentStage(String currentstage) {

		this. currentstage =  currentstage;
	}

	public String getCurrentstage() {

		return this.currentstage;
	}

	public static ServiceRequestCurrentStage getServiceRequestStatus(String currentstage) {
		for (ServiceRequestCurrentStage currentStage : ServiceRequestCurrentStage.values()) {
			if (currentStage.getCurrentstage().equals(currentstage)) {
				return currentStage;
			}
		}
		return null;
	}
}