package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsuranceDecision {

    private String status;
    private String statusCode;
    private String deliveryICApproved;
    private String deliveryICBerApproved;
    private String deliveryICRejected;
    private String deliveryToAscStatus;
    private Double salvageAmt;
    private Double reinstallPrem;
    private String paymentDate;
    private Double paymentAmt;
    private String excessAmtApproved;
    private String excessAmtReceived;
    private String icDecision;
    private Double icDepriciation;
    private Double icEstimateAmt;
    private Double icExcessAmt;
    private Double icUnderInsur;
    private PaymentDto payment;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getDeliveryICApproved() {
        return deliveryICApproved;
    }

    public void setDeliveryICApproved(String deliveryICApproved) {
        this.deliveryICApproved = deliveryICApproved;
    }

    public String getDeliveryICBerApproved() {
        return deliveryICBerApproved;
    }

    public void setDeliveryICBerApproved(String deliveryICBerApproved) {
        this.deliveryICBerApproved = deliveryICBerApproved;
    }

    public String getDeliveryICRejected() {
        return deliveryICRejected;
    }

    public void setDeliveryICRejected(String deliveryICRejected) {
        this.deliveryICRejected = deliveryICRejected;
    }

    public String getDeliveryToAscStatus() {
        return deliveryToAscStatus;
    }

    public void setDeliveryToAscStatus(String deliveryToAscStatus) {
        this.deliveryToAscStatus = deliveryToAscStatus;
    }

    public Double getSalvageAmt() {
        return salvageAmt;
    }

    public void setSalvageAmt(Double salvageAmt) {
        this.salvageAmt = salvageAmt;
    }

    public Double getReinstallPrem() {
        return reinstallPrem;
    }

    public void setReinstallPrem(Double reinstallPrem) {
        this.reinstallPrem = reinstallPrem;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPaymentAmt() {
        return paymentAmt;
    }

    public void setPaymentAmt(Double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getExcessAmtApproved() {
        return excessAmtApproved;
    }

    public void setExcessAmtApproved(String excessAmtApproved) {
        this.excessAmtApproved = excessAmtApproved;
    }

    public String getExcessAmtReceived() {
        return excessAmtReceived;
    }

    public void setExcessAmtReceived(String excessAmtReceived) {
        this.excessAmtReceived = excessAmtReceived;
    }

    public String getIcDecision() {
        return icDecision;
    }

    public void setIcDecision(String icDecision) {
        this.icDecision = icDecision;
    }

    public Double getIcDepriciation() {
        return icDepriciation;
    }

    public void setIcDepriciation(Double icDepriciation) {
        this.icDepriciation = icDepriciation;
    }

    public Double getIcEstimateAmt() {
        return icEstimateAmt;
    }

    public void setIcEstimateAmt(Double icEstimateAmt) {
        this.icEstimateAmt = icEstimateAmt;
    }

    public Double getIcExcessAmt() {
        return icExcessAmt;
    }

    public void setIcExcessAmt(Double icExcessAmt) {
        this.icExcessAmt = icExcessAmt;
    }

    public Double getIcUnderInsur() {
        return icUnderInsur;
    }

    public void setIcUnderInsur(Double icUnderInsur) {
        this.icUnderInsur = icUnderInsur;
    }

    public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "InsuranceDecision [status=" + status + ", statusCode=" + statusCode + ", deliveryICApproved=" + deliveryICApproved + ", deliveryICBerApproved=" + deliveryICBerApproved
                + ", deliveryICRejected=" + deliveryICRejected + ", deliveryToAscStatus=" + deliveryToAscStatus + ", salvageAmt=" + salvageAmt + ", reinstallPrem=" + reinstallPrem + ", paymentDate="
                + paymentDate + ", paymentAmt=" + paymentAmt + ", excessAmtApproved=" + excessAmtApproved + ", excessAmtReceived=" + excessAmtReceived + ", icDecision=" + icDecision
                + ", icDepriciation=" + icDepriciation + ", icEstimateAmt=" + icEstimateAmt + ", icExcessAmt=" + icExcessAmt + ", icUnderInsur=" + icUnderInsur + ", payment=" + payment
                + ", description=" + description + "]";
    }

}
