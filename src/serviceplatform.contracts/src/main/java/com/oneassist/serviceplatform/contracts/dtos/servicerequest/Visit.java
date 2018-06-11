package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visit {

	private String status;
	private String statusCode;
	private String serviceAddress;
	private String serviceStartCode;
	private List<Issues> issueReportedByCustomer;
	private String estimatedInvoiceUploadStaus;
	private String serviceCancelReason;
	private String dateOfIncident;
	private String placeOfIncident;
	private String isSelfService;
	private int maxIncorrectAttempts;
	private String incidentDescription;
	private HashMap<String, Object> additionalDetails;
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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getIncidentDescription() {
		return incidentDescription;
	}

	public void setIncidentDescription(String incidentDescription) {
		this.incidentDescription = incidentDescription;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getServiceStartCode() {
		return serviceStartCode;
	}

	public void setServiceStartCode(String serviceStartCode) {
		this.serviceStartCode = serviceStartCode;
	}

	public List<Issues> getIssueReportedByCustomer() {
		return issueReportedByCustomer;
	}

	public void setIssueReportedByCustomer(List<Issues> issueReportedByCustomer) {
		this.issueReportedByCustomer = issueReportedByCustomer;
	}

	public String getEstimatedInvoiceUploadStaus() {
		return estimatedInvoiceUploadStaus;
	}

	public void setEstimatedInvoiceUploadStaus(String estimatedInvoiceUploadStaus) {
		this.estimatedInvoiceUploadStaus = estimatedInvoiceUploadStaus;
	}

	public String getServiceCancelReason() {
		return serviceCancelReason;
	}

	public void setServiceCancelReason(String serviceCancelReason) {
		this.serviceCancelReason = serviceCancelReason;
	}

	public String getDateOfIncident() {
		return dateOfIncident;
	}

	public void setDateOfIncident(String dateOfIncident) {
		this.dateOfIncident = dateOfIncident;
	}

	public String getPlaceOfIncident() {
		return placeOfIncident;
	}

	public void setPlaceOfIncident(String placeOfIncident) {
		this.placeOfIncident = placeOfIncident;
	}

	public String getIsSelfService() {
		return isSelfService;
	}

	public void setIsSelfService(String isSelfService) {
		this.isSelfService = isSelfService;
	}

	public int getMaxIncorrectAttempts() {
		return maxIncorrectAttempts;
	}

	public void setMaxIncorrectAttempts(int maxIncorrectAttempts) {
		this.maxIncorrectAttempts = maxIncorrectAttempts;
	}

	public HashMap<String, Object> getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(HashMap<String, Object> additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

	@Override
	public String toString() {
		return "Visit [status=" + status + ", statusCode=" + statusCode + ", serviceAddress=" + serviceAddress
				+ ", serviceStartCode=" + serviceStartCode + ", issueReportedByCustomer=" + issueReportedByCustomer
				+ ", estimatedInvoiceUploadStaus=" + estimatedInvoiceUploadStaus + ", serviceCancelReason="
				+ serviceCancelReason + ", dateOfIncident=" + dateOfIncident + ", placeOfIncident=" + placeOfIncident
				+ ", isSelfService=" + isSelfService + ", maxIncorrectAttempts=" + maxIncorrectAttempts
				+ ", incidentDescription=" + incidentDescription + ", additionalDetails=" + additionalDetails + "]";
	}

}
