package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class SpareParts {

    private String sparePartId;
    private String sparePartAvailable;
    private String sparePartDescription;
    private String documentId;
    private String cost;
    private String isInsuranceCovered;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getIsInsuranceCovered() {
        return isInsuranceCovered;
    }

    public void setIsInsuranceCovered(String isInsuranceCovered) {
        this.isInsuranceCovered = isInsuranceCovered;
    }

    public String getSparePartId() {
        return sparePartId;
    }

    public void setSparePartId(String sparePartId) {
        this.sparePartId = sparePartId;
    }

    public String getSparePartDescription() {
        return sparePartDescription;
    }

    public void setSparePartDescription(String sparePartDescription) {
        this.sparePartDescription = sparePartDescription;
    }

    public String getSparePartAvailable() {
        return sparePartAvailable;
    }

    public void setSparePartAvailable(String sparePartAvailable) {
        this.sparePartAvailable = sparePartAvailable;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "SpareParts [sparePartId=" + sparePartId + ", sparePartAvailable=" + sparePartAvailable + ", sparePartDescription=" + sparePartDescription + ", documentId=" + documentId + ", cost="
                + cost + ", isInsuranceCovered=" + isInsuranceCovered + "]";
    }

}
