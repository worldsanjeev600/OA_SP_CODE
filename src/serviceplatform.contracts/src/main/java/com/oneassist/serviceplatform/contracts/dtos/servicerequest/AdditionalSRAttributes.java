package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;

public class AdditionalSRAttributes implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3301763332907044138L;
    private Long claimCount;
    private Long defaultHub;
    private String courtesyRequired;
    private String deviceWarranty;
    private String docsNeverUploaded;
    private String docsRecievedViaEmail;
    private String modelVersion;
    private String mu;
    private String muSigmaUpdatedDate;
    private String assetInvoiceNo;
    private Long hubId;
    private String icMarketValue;
    private Long serviceCntrId;
    private String spoorsPickupStatus;
    private String pickupFromAscStatus;
    private String planCode;
    private String requirementTriggered;
    private Long serviceId;
    private String sigma;
    private String startDate;
    private Double sumAssured;
    private String tat;
    private String tatValue;

    private Long partnerBUCode;
    private String expectedDeliveryDateForm;

    public Long getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(Long claimCount) {
        this.claimCount = claimCount;
    }

    public Long getDefaultHub() {
        return defaultHub;
    }

    public void setDefaultHub(Long defaultHub) {
        this.defaultHub = defaultHub;
    }

    public String getCourtesyRequired() {
        return courtesyRequired;
    }

    public void setCourtesyRequired(String courtesyRequired) {
        this.courtesyRequired = courtesyRequired;
    }

    public String getDeviceWarranty() {
        return deviceWarranty;
    }

    public void setDeviceWarranty(String deviceWarranty) {
        this.deviceWarranty = deviceWarranty;
    }

    public String getDocsNeverUploaded() {
        return docsNeverUploaded;
    }

    public void setDocsNeverUploaded(String docsNeverUploaded) {
        this.docsNeverUploaded = docsNeverUploaded;
    }

    public String getDocsRecievedViaEmail() {
        return docsRecievedViaEmail;
    }

    public void setDocsRecievedViaEmail(String docsRecievedViaEmail) {
        this.docsRecievedViaEmail = docsRecievedViaEmail;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getMuSigmaUpdatedDate() {
        return muSigmaUpdatedDate;
    }

    public void setMuSigmaUpdatedDate(String muSigmaUpdatedDate) {
        this.muSigmaUpdatedDate = muSigmaUpdatedDate;
    }

    public String getAssetInvoiceNo() {
        return assetInvoiceNo;
    }

    public void setAssetInvoiceNo(String assetInvoiceNo) {
        this.assetInvoiceNo = assetInvoiceNo;
    }

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public String getIcMarketValue() {
        return icMarketValue;
    }

    public void setIcMarketValue(String icMarketValue) {
        this.icMarketValue = icMarketValue;
    }

    public Long getServiceCntrId() {
        return serviceCntrId;
    }

    public void setServiceCntrId(Long serviceCntrId) {
        this.serviceCntrId = serviceCntrId;
    }

    public String getSpoorsPickupStatus() {
        return spoorsPickupStatus;
    }

    public void setSpoorsPickupStatus(String spoorsPickupStatus) {
        this.spoorsPickupStatus = spoorsPickupStatus;
    }

    public String getPickupFromAscStatus() {
        return pickupFromAscStatus;
    }

    public void setPickupFromAscStatus(String pickupFromAscStatus) {
        this.pickupFromAscStatus = pickupFromAscStatus;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getRequirementTriggered() {
        return requirementTriggered;
    }

    public void setRequirementTriggered(String requirementTriggered) {
        this.requirementTriggered = requirementTriggered;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getSigma() {
        return sigma;
    }

    public void setSigma(String sigma) {
        this.sigma = sigma;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Double getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(Double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public String getTat() {
        return tat;
    }

    public void setTat(String tat) {
        this.tat = tat;
    }

    public String getTatValue() {
        return tatValue;
    }

    public void setTatValue(String tatValue) {
        this.tatValue = tatValue;
    }

    public Long getPartnerBUCode() {
        return partnerBUCode;
    }

    public void setPartnerBUCode(Long partnerBUCode) {
        this.partnerBUCode = partnerBUCode;
    }

    public String getExpectedDeliveryDateForm() {
        return expectedDeliveryDateForm;
    }

    public void setExpectedDeliveryDateForm(String expectedDeliveryDateForm) {
        this.expectedDeliveryDateForm = expectedDeliveryDateForm;
    }

    @Override
    public String toString() {
        return "AdditionalSRAttributes [claimCount=" + claimCount + ", defaultHub=" + defaultHub + ", courtesyRequired=" + courtesyRequired + ", deviceWarranty=" + deviceWarranty
                + ", docsNeverUploaded=" + docsNeverUploaded + ", docsRecievedViaEmail=" + docsRecievedViaEmail + ", modelVersion=" + modelVersion + ", mu=" + mu + ", muSigmaUpdatedDate="
                + muSigmaUpdatedDate + ", assetInvoiceNo=" + assetInvoiceNo + ", hubId=" + hubId + ", icMarketValue=" + icMarketValue + ", serviceCntrId=" + serviceCntrId + ", spoorsPickupStatus="
                + spoorsPickupStatus + ", pickupFromAscStatus=" + pickupFromAscStatus + ", planCode=" + planCode + ", requirementTriggered=" + requirementTriggered + ", serviceId=" + serviceId
                + ", sigma=" + sigma + ", startDate=" + startDate + ", sumAssured=" + sumAssured + ", tat=" + tat + ", tatValue=" + tatValue + ", partnerBUCode=" + partnerBUCode
                + ", expectedDeliveryDateForm=" + expectedDeliveryDateForm + "]";
    }

}
