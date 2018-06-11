package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ICDoc {

    private String estimatedInvoiceVerificationStatus;

    private String docSentToIC;

    private String docSentToICDate;
    private String intimated;
    private String allDocSentIc;
    private String status;
    private String statusCode;

    public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getEstimatedInvoiceVerificationStatus() {
        return estimatedInvoiceVerificationStatus;
    }

    public void setEstimatedInvoiceVerificationStatus(String estimatedInvoiceVerificationStatus) {
        this.estimatedInvoiceVerificationStatus = estimatedInvoiceVerificationStatus;
    }

    public String getDocSentToIC() {
        return docSentToIC;
    }

    public void setDocSentToIC(String docSentToIC) {
        this.docSentToIC = docSentToIC;
    }

    public String getDocSentToICDate() {
        return docSentToICDate;
    }

    public void setDocSentToICDate(String docSentToICDate) {
        this.docSentToICDate = docSentToICDate;
    }

    public String getIntimated() {
        return intimated;
    }

    public void setIntimated(String intimated) {
        this.intimated = intimated;
    }

    public String getAllDocSentIc() {
        return allDocSentIc;
    }

    public void setAllDocSentIc(String allDocSentIc) {
        this.allDocSentIc = allDocSentIc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
	public String toString() {
		return "ICDoc [estimatedInvoiceVerificationStatus=" + estimatedInvoiceVerificationStatus + ", docSentToIC="
				+ docSentToIC + ", docSentToICDate=" + docSentToICDate + ", intimated=" + intimated + ", allDocSentIc="
				+ allDocSentIc + ", status=" + status + ", statusCode=" + statusCode + "]";
	}

}
