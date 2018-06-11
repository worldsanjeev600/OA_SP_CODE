package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackingId", namespace = "http://fedex.com/ws/ship/v19")
public class TrackingId {

    @XmlElement(name = "FormId")
    protected String formId;
    @XmlElement(name = "UspsApplicationId")
    protected String uspsApplicationId;
    @XmlElement(name = "TrackingNumber")
    protected String trackingNumber;

    public String getFormId() {
        return this.formId;
    }

    public void setFormId(String value) {
        this.formId = value;
    }

    public String getUspsApplicationId() {
        return this.uspsApplicationId;
    }

    public void setUspsApplicationId(String value) {
        this.uspsApplicationId = value;
    }

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String value) {
        this.trackingNumber = value;
    }

    @Override
    public String toString() {
        return

        "TrackingId [ formId=" + this.formId + ", uspsApplicationId=" + this.uspsApplicationId + ", trackingNumber=" + this.trackingNumber + "]";
    }
}
