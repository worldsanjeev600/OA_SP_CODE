package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CustomerReferenceType")
@XmlEnum
public enum CustomerReferenceType {
    BILL_OF_LADING, CUSTOMER_REFERENCE, DEPARTMENT_NUMBER, ELECTRONIC_PRODUCT_CODE, INTRACOUNTRY_REGULATORY_REFERENCE, INVOICE_NUMBER, P_O_NUMBER, RMA_ASSOCIATION, SHIPMENT_INTEGRITY, STORE_NUMBER;

    public String value() {
        return name();
    }

    public static CustomerReferenceType fromValue(String v) {
        return valueOf(v);
    }
}
