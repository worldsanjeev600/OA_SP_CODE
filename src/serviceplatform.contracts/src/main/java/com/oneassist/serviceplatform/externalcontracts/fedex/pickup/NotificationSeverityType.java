package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "NotificationSeverityType")
@XmlEnum
public enum NotificationSeverityType {
    ERROR, FAILURE, NOTE, SUCCESS, WARNING;

    public String value() {
        return name();
    }

    public static NotificationSeverityType fromValue(String v) {
        return valueOf(v);
    }
}
