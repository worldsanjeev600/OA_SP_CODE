package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairAssessment {

    private List<Diagnosis> diagonosisReportedbyAssignee;
    private List<SpareParts> sparePartsRequired;
    private Transport transport;
    private LabourCost labourCost;

    private String accidentalDamage;
    private String costToCustomer;
    private String costToCompany;
    private String isCustomerAgree;
    private String customerDecision;
    private String isSpareRequestRaisedWithIC;
    private String isTransportationRaisedWithIC;
    private String invoiceValue;
    private String endTime;
    private String status;
    private Double estimateAmt;
    private String statusCode;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Diagnosis> getDiagonosisReportedbyAssignee() {
        return diagonosisReportedbyAssignee;
    }

    public void setDiagonosisReportedbyAssignee(List<Diagnosis> diagonosisReportedbyAssignee) {
        this.diagonosisReportedbyAssignee = diagonosisReportedbyAssignee;
    }

    public List<SpareParts> getSparePartsRequired() {
        return sparePartsRequired;
    }

    public void setSparePartsRequired(List<SpareParts> sparePartsRequired) {
        this.sparePartsRequired = sparePartsRequired;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public LabourCost getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(LabourCost labourCost) {
        this.labourCost = labourCost;
    }

    public String getAccidentalDamage() {
        return accidentalDamage;
    }

    public void setAccidentalDamage(String accidentalDamage) {
        this.accidentalDamage = accidentalDamage;
    }

    public String getCostToCustomer() {
        return costToCustomer;
    }

    public void setCostToCustomer(String costToCustomer) {
        this.costToCustomer = costToCustomer;
    }

    public String getCostToCompany() {
        return costToCompany;
    }

    public void setCostToCompany(String costToCompany) {
        this.costToCompany = costToCompany;
    }

    public String getIsCustomerAgree() {
        return isCustomerAgree;
    }

    public void setIsCustomerAgree(String isCustomerAgree) {
        this.isCustomerAgree = isCustomerAgree;
    }

    public String getCustomerDecision() {
        return customerDecision;
    }

    public void setCustomerDecision(String customerDecision) {
        this.customerDecision = customerDecision;
    }

    public String getIsSpareRequestRaisedWithIC() {
        return isSpareRequestRaisedWithIC;
    }

    public void setIsSpareRequestRaisedWithIC(String isSpareRequestRaisedWithIC) {
        this.isSpareRequestRaisedWithIC = isSpareRequestRaisedWithIC;
    }

    public String getIsTransportationRaisedWithIC() {
        return isTransportationRaisedWithIC;
    }

    public void setIsTransportationRaisedWithIC(String isTransportationRaisedWithIC) {
        this.isTransportationRaisedWithIC = isTransportationRaisedWithIC;
    }

    public String getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(String invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getEstimateAmt() {
        return estimateAmt;
    }

    public void setEstimateAmt(Double estimateAmt) {
        this.estimateAmt = estimateAmt;
    }

    @Override
    public String toString() {
        return "RepairAssessment [diagonosisReportedbyAssignee=" + diagonosisReportedbyAssignee + ", sparePartsRequired=" + sparePartsRequired + ", transport=" + transport + ", labourCost="
                + labourCost + ", accidentalDamage=" + accidentalDamage + ", costToCustomer=" + costToCustomer + ", costToCompany=" + costToCompany + ", isCustomerAgree=" + isCustomerAgree
                + ", customerDecision=" + customerDecision + ", isSpareRequestRaisedWithIC=" + isSpareRequestRaisedWithIC + ", isTransportationRaisedWithIC=" + isTransportationRaisedWithIC
                + ", invoiceValue=" + invoiceValue + ", endTime=" + endTime + ", status=" + status + ", estimateAmt=" + estimateAmt + ", statusCode=" + statusCode + ", description=" + description
                + "]";
    }

}
