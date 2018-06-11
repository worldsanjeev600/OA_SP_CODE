package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackPackageIdentifier")
public class TrackPackageIdentifier {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Value", required = true)
    protected String value;

    public String getType() {
        return this.type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TrackPackageIdentifier [type=" + this.type + ", value=" + this.value + "]";
    }
}
