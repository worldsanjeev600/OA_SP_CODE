package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionDetail")
public class TransactionDetail {

    @XmlElement(name = "CustomerTransactionId")
    protected String customerTransactionId;

    public String getCustomerTransactionId() {
        return this.customerTransactionId;
    }

    public void setCustomerTransactionId(String value) {
        this.customerTransactionId = value;
    }

    @Override
    public String toString() {
        return

        "TransactionDetail [customerTransactionId=" + this.customerTransactionId + "]";
    }
}
