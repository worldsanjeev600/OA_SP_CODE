package com.oneassist.serviceplatform.commons.entities;

public class UserEntity {

	private String	userLoginId;

	private String	userPassword;	
	
	private String	userDescription;
	
	private String	status;
	
	public String getUserLoginId() {
		return this.userLoginId;	
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;	
	}	
	
	public String getUserPassword() {
		return this.userPassword;	
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;	
	}	
	
	public String getUserDescription() {
		return this.userDescription;	
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;	
	}	
	
	public String getStatus() {
		return this.status;	
	}

	public void setStatus(String status) {
		this.status = status;	
	}	
}