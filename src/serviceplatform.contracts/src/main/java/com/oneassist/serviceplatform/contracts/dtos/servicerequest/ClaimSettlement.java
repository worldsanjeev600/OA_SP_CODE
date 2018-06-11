package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimSettlement {
	
	private String status;
	private String statusCode;
	private String claimAmount;
	private String settlementDate;
	private String icPaymentConfirmation;
	private String remarks;
	private String description;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getIcPaymentConfirmation() {
		return icPaymentConfirmation;
	}

	public void setIcPaymentConfirmation(String icPaymentConfirmation) {
		this.icPaymentConfirmation = icPaymentConfirmation;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ClaimSettlement [status=" + status + ", statusCode=" + statusCode + ", claimAmount=" + claimAmount
				+ ", settlementDate=" + settlementDate + ", icPaymentConfirmation=" + icPaymentConfirmation
				+ ", remarks=" + remarks + ", description=" + description + "]";
	}

}
