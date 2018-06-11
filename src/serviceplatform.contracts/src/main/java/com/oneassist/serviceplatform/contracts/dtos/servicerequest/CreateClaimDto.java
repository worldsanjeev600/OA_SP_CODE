package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Map;

public class CreateClaimDto {

    private Long customerId;
    private String memId;
    private String placeOfIncident;
    private Long initiatingSystem;
    private String incidentDescription;
    private String dateOfIncident;
    private String createdBy;
    private String claimType;

    private ClaimAddressDetails claimAddressDetails;
    private Map<String, Object> claimMobileDamageDetails;

    private Map<String, Object> claimDeviceBreakDownDetails;

    private Map<String, Object> claimMobileLossDetails;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getPlaceOfIncident() {
        return placeOfIncident;
    }

    public void setPlaceOfIncident(String placeOfIncident) {
        this.placeOfIncident = placeOfIncident;
    }

    public Long getInitiatingSystem() {
        return initiatingSystem;
    }

    public void setInitiatingSystem(Long initiatingSystem) {
        this.initiatingSystem = initiatingSystem;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getDateOfIncident() {
        return dateOfIncident;
    }

    public void setDateOfIncident(String dateOfIncident) {
        this.dateOfIncident = dateOfIncident;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public ClaimAddressDetails getClaimAddressDetails() {
        return claimAddressDetails;
    }

    public void setClaimAddressDetails(ClaimAddressDetails claimAddressDetails) {
        this.claimAddressDetails = claimAddressDetails;
    }

    public Map<String, Object> getClaimMobileDamageDetails() {
        return claimMobileDamageDetails;
    }

    public void setClaimMobileDamageDetails(Map<String, Object> claimMobileDamageDetails) {
        this.claimMobileDamageDetails = claimMobileDamageDetails;
    }

    public Map<String, Object> getClaimDeviceBreakDownDetails() {
        return claimDeviceBreakDownDetails;
    }

    public void setClaimDeviceBreakDownDetails(Map<String, Object> claimDeviceBreakDownDetails) {
        this.claimDeviceBreakDownDetails = claimDeviceBreakDownDetails;
    }

    public Map<String, Object> getClaimMobileLossDetails() {
        return claimMobileLossDetails;
    }

    public void setClaimMobileLossDetails(Map<String, Object> claimMobileLossDetails) {
        this.claimMobileLossDetails = claimMobileLossDetails;
    }

    @Override
    public String toString() {
        return "CreateClaimDto [customerId=" + customerId + ", memId=" + memId + ", placeOfIncident=" + placeOfIncident + ", initiatingSystem=" + initiatingSystem + ", incidentDescription="
                + incidentDescription + ", dateOfIncident=" + dateOfIncident + ", createdBy=" + createdBy + ", claimType=" + claimType + ", claimAddressDetails=" + claimAddressDetails
                + ", claimMobileDamageDetails=" + claimMobileDamageDetails + ", claimDeviceBreakDownDetails=" + claimDeviceBreakDownDetails + ", claimMobileLossDetails=" + claimMobileLossDetails
                + "]";
    }

}
