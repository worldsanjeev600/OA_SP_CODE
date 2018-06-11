package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Date;

public class ClaimDetail {
	private String ocdClaimId;
	private String ocdCustId;
	private String claimType;
	private Date ocdDamageLossDateTime;
	public String getOcdClaimId() {
		return ocdClaimId;
	}
	public void setOcdClaimId(String ocdClaimId) {
		this.ocdClaimId = ocdClaimId;
	}
	public String getOcdCustId() {
		return ocdCustId;
	}
	public void setOcdCustId(String ocdCustId) {
		this.ocdCustId = ocdCustId;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Date getOcdDamageLossDateTime() {
		return ocdDamageLossDateTime;
	}
	public void setOcdDamageLossDateTime(Date ocdDamageLossDateTime) {
		this.ocdDamageLossDateTime = ocdDamageLossDateTime;
	}
	@Override
	public String toString() {
		return "ClaimDetail [ocdClaimId=" + ocdClaimId + ", ocdCustId=" + ocdCustId + ", claimType=" + claimType
				+ ", ocdDamageLossDateTime=" + ocdDamageLossDateTime + "]";
	}
}
