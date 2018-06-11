package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VersionId")
public class VersionId {

    @XmlElement(name = "ServiceId", required = true)
    protected String serviceId = "disp";
    @XmlElement(name = "Major")
    protected int major = 13;
    @XmlElement(name = "Intermediate")
    protected int intermediate = 0;
    @XmlElement(name = "Minor")
    protected int minor = 0;

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String value) {
        this.serviceId = value;
    }

    public int getMajor() {
        return this.major;
    }

    public void setMajor(int value) {
        this.major = value;
    }

    public int getIntermediate() {
        return this.intermediate;
    }

    public void setIntermediate(int value) {
        this.intermediate = value;
    }

    public int getMinor() {
        return this.minor;
    }

    public void setMinor(int value) {
        this.minor = value;
    }

    @Override
    public String toString() {
        return "VersionId [serviceId=" + this.serviceId + ", major=" + this.major + ", intermediate=" + this.intermediate + ", minor=" + this.minor + "]";
    }
}
