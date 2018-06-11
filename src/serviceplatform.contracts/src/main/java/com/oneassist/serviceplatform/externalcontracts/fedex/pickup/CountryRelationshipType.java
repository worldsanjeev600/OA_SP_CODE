package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CountryRelationshipType")
@XmlEnum
public enum CountryRelationshipType {
    DOMESTIC, INTERNATIONAL;

    public String value() {
        return name();
    }

    public static CountryRelationshipType fromValue(String v) {
        return valueOf(v);
    }
}
