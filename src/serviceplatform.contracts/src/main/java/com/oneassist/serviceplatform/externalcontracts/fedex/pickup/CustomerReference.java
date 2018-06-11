package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerReference")
public class CustomerReference {

    @XmlElement(name = "CustomerReferenceType", required = true)
    protected CustomerReferenceType customerReferenceType;
    @XmlElement(name = "Value", required = true)
    protected String value;

    public CustomerReferenceType getCustomerReferenceType() {
        return this.customerReferenceType;
    }

    public void setCustomerReferenceType(CustomerReferenceType value) {
        this.customerReferenceType = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomerReference [customerReferenceType=" + this.customerReferenceType + ", value=" + this.value + "]";
    }
}
