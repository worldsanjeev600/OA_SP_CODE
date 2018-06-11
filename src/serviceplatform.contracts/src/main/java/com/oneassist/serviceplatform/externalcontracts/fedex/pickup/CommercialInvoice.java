package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommercialInvoice")
public class CommercialInvoice {

    @XmlElement(name = "Comments")
    protected List<String> comments;
    @XmlElement(name = "FreightCharge")
    protected Money freightCharge;
    @XmlElement(name = "TaxesOrMiscellaneousCharge")
    protected Money taxesOrMiscellaneousCharge;
    @XmlElement(name = "PackingCosts")
    protected Money packingCosts;
    @XmlElement(name = "HandlingCosts")
    protected Money handlingCosts;
    @XmlElement(name = "SpecialInstructions")
    protected String specialInstructions;
    @XmlElement(name = "DeclarationStatement")
    protected String declarationStatement;
    @XmlElement(name = "PaymentTerms")
    protected String paymentTerms;
    @XmlElement(name = "Purpose")
    protected String purpose;
    @XmlElement(name = "CustomerReferences")
    protected List<CustomerReference> customerReferences;
    @XmlElement(name = "OriginatorName")
    protected String originatorName;
    @XmlElement(name = "TermsOfSale")
    protected String termsOfSale;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public Money getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(Money freightCharge) {
        this.freightCharge = freightCharge;
    }

    public Money getTaxesOrMiscellaneousCharge() {
        return taxesOrMiscellaneousCharge;
    }

    public void setTaxesOrMiscellaneousCharge(Money taxesOrMiscellaneousCharge) {
        this.taxesOrMiscellaneousCharge = taxesOrMiscellaneousCharge;
    }

    public Money getPackingCosts() {
        return packingCosts;
    }

    public void setPackingCosts(Money packingCosts) {
        this.packingCosts = packingCosts;
    }

    public Money getHandlingCosts() {
        return handlingCosts;
    }

    public void setHandlingCosts(Money handlingCosts) {
        this.handlingCosts = handlingCosts;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getDeclarationStatement() {
        return declarationStatement;
    }

    public void setDeclarationStatement(String declarationStatement) {
        this.declarationStatement = declarationStatement;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public List<CustomerReference> getCustomerReferences() {
        return customerReferences;
    }

    public void setCustomerReferences(List<CustomerReference> customerReferences) {
        this.customerReferences = customerReferences;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getTermsOfSale() {
        return termsOfSale;
    }

    public void setTermsOfSale(String termsOfSale) {
        this.termsOfSale = termsOfSale;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "CommercialInvoice [comments=" + comments + ", freightCharge=" + freightCharge + ", taxesOrMiscellaneousCharge=" + taxesOrMiscellaneousCharge + ", packingCosts=" + packingCosts
                + ", handlingCosts=" + handlingCosts + ", specialInstructions=" + specialInstructions + ", declarationStatement=" + declarationStatement + ", paymentTerms=" + paymentTerms
                + ", customerReferences=" + customerReferences + ", originatorName=" + originatorName + ", termsOfSale=" + termsOfSale + "]";
    }

}
