package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Date;

public class ServiceTechnicianDetailsDto {

	private Long partnerUserId;
	private Long partnerCode;
	private Long buCode;
	
	private String userFirstName;
	private String userMidName;
	private String userLastName;
	private String userEmailId;
	
	private Long userLandline;
	private Long userMobile;
	private String status;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	public Long getPartnerUserId() {	
		return partnerUserId;
	}
	
	public void setPartnerUserId(Long partnerUserId) {	
		this.partnerUserId = partnerUserId;
	}
	
	public Long getPartnerCode() {	
		return partnerCode;
	}
	
	public void setPartnerCode(Long partnerCode) {	
		this.partnerCode = partnerCode;
	}
	
	public Long getBuCode() {	
		return buCode;
	}
	
	public void setBuCode(Long buCode) {	
		this.buCode = buCode;
	}
	
	public String getUserFirstName() {	
		return userFirstName;
	}
	
	public void setUserFirstName(String userFirstName) {	
		this.userFirstName = userFirstName;
	}
	
	public String getUserMidName() {	
		return userMidName;
	}
	
	public void setUserMidName(String userMidName) {	
		this.userMidName = userMidName;
	}
	
	public String getUserLastName() {	
		return userLastName;
	}
	
	public void setUserLastName(String userLastName) {	
		this.userLastName = userLastName;
	}
	
	public String getUserEmailId() {	
		return userEmailId;
	}
	
	public void setUserEmailId(String userEmailId) {	
		this.userEmailId = userEmailId;
	}
	
	public Long getUserLandline() {	
		return userLandline;
	}
	
	public void setUserLandline(Long userLandline) {	
		this.userLandline = userLandline;
	}
	
	public Long getUserMobile() {	
		return userMobile;
	}
	
	public void setUserMobile(Long userMobile) {	
		this.userMobile = userMobile;
	}
	
	public String getStatus() {	
		return status;
	}
	
	public void setStatus(String status) {	
		this.status = status;
	}
	
	public String getCreatedBy() {	
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {	
		this.createdBy = createdBy;
	}
	
	public Date getCreatedOn() {	
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {	
		this.createdOn = createdOn;
	}
	
	public String getModifiedBy() {	
		return modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {	
		this.modifiedBy = modifiedBy;
	}
	
	public Date getModifiedOn() {	
		return modifiedOn;
	}
	
	public void setModifiedOn(Date modifiedOn) {	
		this.modifiedOn = modifiedOn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceTechnicianDetailsDto [partnerUserId=");
		builder.append(partnerUserId);
		builder.append(", partnerCode=");
		builder.append(partnerCode);
		builder.append(", buCode=");
		builder.append(buCode);
		builder.append(", userFirstName=");
		builder.append(userFirstName);
		builder.append(", userMidName=");
		builder.append(userMidName);
		builder.append(", userLastName=");
		builder.append(userLastName);
		builder.append(", userEmailId=");
		builder.append(userEmailId);
		builder.append(", userLandline=");
		builder.append(userLandline);
		builder.append(", userMobile=");
		builder.append(userMobile);
		builder.append(", status=");
		builder.append(status);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append("]");
		return builder.toString();
	}
	
}