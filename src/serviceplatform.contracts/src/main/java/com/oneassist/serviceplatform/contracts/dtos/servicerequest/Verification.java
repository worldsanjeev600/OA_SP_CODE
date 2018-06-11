package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Verification {

	private String status;
	private String statusCode;
	private String remarks;
	private String documentId;
	private String damageLossDateTimeVerRemarks;
	private String damageLossDateTimeVerStat;
	private String verificationNewCases;
	private String description;
	private PaymentDto payment;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDamageLossDateTimeVerRemarks() {
		return damageLossDateTimeVerRemarks;
	}

	public void setDamageLossDateTimeVerRemarks(String damageLossDateTimeVerRemarks) {
		this.damageLossDateTimeVerRemarks = damageLossDateTimeVerRemarks;
	}

	public String getDamageLossDateTimeVerStat() {
		return damageLossDateTimeVerStat;
	}

	public void setDamageLossDateTimeVerStat(String damageLossDateTimeVerStat) {
		this.damageLossDateTimeVerStat = damageLossDateTimeVerStat;
	}

	public String getVerificationNewCases() {
		return verificationNewCases;
	}

	public void setVerificationNewCases(String verificationNewCases) {
		this.verificationNewCases = verificationNewCases;
	}
	
	public PaymentDto getPayment() {
		return payment;
	}

	public void setPayment(PaymentDto payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return "Verification [status=" + status + ", statusCode=" + statusCode + ", remarks=" + remarks
				+ ", documentId=" + documentId + ", damageLossDateTimeVerRemarks=" + damageLossDateTimeVerRemarks
				+ ", damageLossDateTimeVerStat=" + damageLossDateTimeVerStat + ", verificationNewCases="
				+ verificationNewCases + ", description=" + description + ", payment=" + payment + "]";
	}

}
