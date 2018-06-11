package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupShipmentAttributes")
public class PickupShipmentAttributes {

    @XmlElement(name = "ServiceType")
    protected String serviceType;
    @XmlElement(name = "PackagingType")
    protected String packagingType;
    @XmlElement(name = "Weight")
    protected Weight weight;

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(String value) {
        this.serviceType = value;
    }

    public String getPackagingType() {
        return this.packagingType;
    }

    public void setPackagingType(String value) {
        this.packagingType = value;
    }

    public Weight getWeight() {
        return this.weight;
    }

    public void setWeight(Weight value) {
        this.weight = value;
    }

    @Override
    public String toString() {
        return

        "PickupShipmentAttributes [serviceType=" + this.serviceType + ", packagingType=" + this.packagingType + ", , weight=" + this.weight + "]";
    }
}
