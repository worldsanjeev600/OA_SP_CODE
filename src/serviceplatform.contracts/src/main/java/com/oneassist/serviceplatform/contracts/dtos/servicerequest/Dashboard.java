package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class Dashboard {
	private String stageName;
	private String stageCode;
	private Long countOfServiceRequests;
	
	public String getStageName() {
		return stageName;
	}
	
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	public String getStageCode() {
		return stageCode;
	}
	
	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}
	
	public Long getCountOfServiceRequests() {
		return countOfServiceRequests;
	}
	
	public void setCountOfServiceRequests(Long countOfServiceRequests) {
		this.countOfServiceRequests = countOfServiceRequests;
	}
	
	@Override
	public String toString() {
		return "Dashboard [stageName=" + stageName 
				+ ", stageCode=" + stageCode 
				+ ", countOfServiceRequests=" + countOfServiceRequests + "]";
	}	
}
