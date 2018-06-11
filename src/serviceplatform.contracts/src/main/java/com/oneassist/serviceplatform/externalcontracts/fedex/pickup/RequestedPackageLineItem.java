package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestedPackageLineItem")
public class RequestedPackageLineItem {

    @XmlElement(name = "SequenceNumber")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger sequenceNumber;
    @XmlElement(name = "GroupNumber")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger groupNumber;
    @XmlElement(name = "GroupPackageCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger groupPackageCount;
    @XmlElement(name = "InsuredValue")
    protected Money insuredValue;
    @XmlElement(name = "Weight")
    protected Weight weight;
    @XmlElement(name = "ItemDescription")
    protected String itemDescription;
    @XmlElement(name = "ItemDescriptionForClearance")
    protected String itemDescriptionForClearance;
    @XmlElement(name = "CustomerReferences")
    protected List<CustomerReference> customerReferences;

    public BigInteger getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(BigInteger sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigInteger getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(BigInteger groupNumber) {
        this.groupNumber = groupNumber;
    }

    public BigInteger getGroupPackageCount() {
        return groupPackageCount;
    }

    public void setGroupPackageCount(BigInteger groupPackageCount) {
        this.groupPackageCount = groupPackageCount;
    }

    public Money getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(Money insuredValue) {
        this.insuredValue = insuredValue;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescriptionForClearance() {
        return itemDescriptionForClearance;
    }

    public void setItemDescriptionForClearance(String itemDescriptionForClearance) {
        this.itemDescriptionForClearance = itemDescriptionForClearance;
    }

    public List<CustomerReference> getCustomerReferences() {
        if (customerReferences == null) {
            customerReferences = new ArrayList<CustomerReference>();
        }
        return customerReferences;
    }

    public void setCustomerReferences(List<CustomerReference> customerReferences) {
        this.customerReferences = customerReferences;
    }

    @Override
    public String toString() {
        return "RequestedPackageLineItem [sequenceNumber=" + sequenceNumber + ", groupNumber=" + groupNumber + ", groupPackageCount=" + groupPackageCount + ", insuredValue=" + insuredValue
                + ", weight=" + weight + ", itemDescription=" + itemDescription + ", itemDescriptionForClearance=" + itemDescriptionForClearance + ", customerReferences=" + customerReferences + "]";
    }

}
