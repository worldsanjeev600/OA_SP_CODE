package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class Issues {
	
	private String issueId;
	private String issueDescription;
	
	public String getIssueId() {
		return issueId;
	}
	
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	
	public String getIssueDescription() {
		return issueDescription;
	}
	
	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	
	@Override
	public String toString() {
		return "Issues [issueId=" + issueId + ", issueDescription="
				+ issueDescription + "]";
	}	
}