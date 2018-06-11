package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name = "ECOMEXPRESS-OBJECTS")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("ECOMEXPRESS-OBJECTS")
public class EcomExpressObjects {

    @XmlElement(name = "SHIPMENT")
    @JsonProperty("SHIPMENT")
    private Shipment shipment;

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    /*
     * @Override public String toString() { return "ClassPojo [Shipment = "+shipment+"]"; }
     */
}
