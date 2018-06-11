package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Completed {

	private String status;
	private String statusCode;
	private String refundAmount;
	private String serviceCancelReason;
	private String remarks;
	private String description;

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

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getServiceCancelReason() {
		return serviceCancelReason;
	}

	public void setServiceCancelReason(String serviceCancelReason) {
		this.serviceCancelReason = serviceCancelReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "Completed [status=" + status + ", statusCode=" + statusCode + ", refundAmount=" + refundAmount
				+ ", serviceCancelReason=" + serviceCancelReason + ", remarks=" + remarks + "]";
	}

}
