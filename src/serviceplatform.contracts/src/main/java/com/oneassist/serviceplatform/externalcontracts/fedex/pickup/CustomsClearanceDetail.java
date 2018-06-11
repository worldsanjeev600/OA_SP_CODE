package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomsClearanceDetail")
public class CustomsClearanceDetail {

    @XmlElement(name = "ImporterOfRecord")
    protected Party importerOfRecord;
    @XmlElement(name = "DutiesPayment")
    protected Payment dutiesPayment;
    @XmlElement(name = "DocumentContent")
    protected String documentContent;
    @XmlElement(name = "CustomsValue")
    protected Money customsValue;
    @XmlElement(name = "CommercialInvoice")
    protected CommercialInvoice commercialInvoice;
    @XmlElement(name = "InsuranceCharges")
    protected Money insuranceCharges;
    @XmlElement(name = "PartiesToTransactionAreRelated")
    protected Boolean partiesToTransactionAreRelated;
    @XmlElement(name = "Commodities")
    protected List<Commodity> commodities;

    public Party getImporterOfRecord() {
        return importerOfRecord;
    }

    public void setImporterOfRecord(Party importerOfRecord) {
        this.importerOfRecord = importerOfRecord;
    }

    public Payment getDutiesPayment() {
        return dutiesPayment;
    }

    public void setDutiesPayment(Payment dutiesPayment) {
        this.dutiesPayment = dutiesPayment;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    public Money getCustomsValue() {
        return customsValue;
    }

    public void setCustomsValue(Money customsValue) {
        this.customsValue = customsValue;
    }

    public CommercialInvoice getCommercialInvoice() {
        return commercialInvoice;
    }

    public void setCommercialInvoice(CommercialInvoice commercialInvoice) {
        this.commercialInvoice = commercialInvoice;
    }

    public Money getInsuranceCharges() {
        return insuranceCharges;
    }

    public void setInsuranceCharges(Money insuranceCharges) {
        this.insuranceCharges = insuranceCharges;
    }

    public Boolean getPartiesToTransactionAreRelated() {
        return partiesToTransactionAreRelated;
    }

    public void setPartiesToTransactionAreRelated(Boolean partiesToTransactionAreRelated) {
        this.partiesToTransactionAreRelated = partiesToTransactionAreRelated;
    }

    public List<Commodity> getCommodities() {
        if (commodities == null) {
            commodities = new ArrayList<Commodity>();
        }
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    @Override
    public String toString() {
        return "CustomsClearanceDetail [importerOfRecord=" + importerOfRecord + ", dutiesPayment=" + dutiesPayment + ", documentContent=" + documentContent + ", customsValue=" + customsValue
                + ", insuranceCharges=" + insuranceCharges + ", partiesToTransactionAreRelated=" + partiesToTransactionAreRelated + ", commodities=" + commodities + "]";
    }

}
