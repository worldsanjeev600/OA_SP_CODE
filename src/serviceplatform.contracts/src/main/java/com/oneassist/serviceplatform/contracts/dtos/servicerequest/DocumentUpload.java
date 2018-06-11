package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentUpload {

    private String status;
    private String statusCode;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private String documentId;
    private String dateOfIncident;
    private String placeOfIncident;
    private Map<String, Object> deviceBreakdownDetail;
    private Map<String, Object> mobileLossDetails;
    private Map<String, Object> mobileDamageDetails;

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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public Map<String, Object> getDeviceBreakdownDetail() {
        return deviceBreakdownDetail;
    }

    public void setDeviceBreakdownDetail(Map<String, Object> deviceBreakdownDetail) {
        this.deviceBreakdownDetail = deviceBreakdownDetail;
    }

    public Map<String, Object> getMobileLossDetails() {
        return mobileLossDetails;
    }

    public void setMobileLossDetails(Map<String, Object> mobileLossDetails) {
        this.mobileLossDetails = mobileLossDetails;
    }

    public Map<String, Object> getMobileDamageDetails() {
        return mobileDamageDetails;
    }

    public void setMobileDamageDetails(Map<String, Object> mobileDamageDetails) {
        this.mobileDamageDetails = mobileDamageDetails;
    }

    @Override
    public String toString() {
        return "DocumentUpload [status=" + status + ", statusCode=" + statusCode + ", documentId=" + documentId + ", dateOfIncident=" + dateOfIncident + ", placeOfIncident=" + placeOfIncident
                + ", deviceBreakdownDetail=" + deviceBreakdownDetail + ", mobileLossDetails=" + mobileLossDetails + ", mobileDamageDetails=" + mobileDamageDetails + ", description=" + description
                + "]";
    }

}
