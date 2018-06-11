package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payment")
public class Payment {

    @XmlElement(name = "PaymentType", required = true)
    protected String paymentType;
    @XmlElement(name = "Payor")
    protected Payor payor;

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String value) {
        this.paymentType = value;
    }

    public Payor getPayor() {
        return this.payor;
    }

    public void setPayor(Payor value) {
        this.payor = value;
    }

    @Override
    public String toString() {
        return "Payment [paymentType=" + this.paymentType + ", payor=" + this.payor + "]";
    }
}
