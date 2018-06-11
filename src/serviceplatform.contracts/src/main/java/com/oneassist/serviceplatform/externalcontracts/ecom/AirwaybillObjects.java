/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Savita.kodli
 *
 */
@XmlRootElement(name = "AIRWAYBILL-OBJECTS")
@XmlAccessorType(XmlAccessType.FIELD)
public class AirwaybillObjects {

    @XmlElement(name = "AIRWAYBILL")
    private Airwaybill Airwaybill;

    @JsonProperty("AIRWAYBILL")
    public Airwaybill getAirwaybill() {
        return Airwaybill;
    }

    @JsonProperty("AIRWAYBILL")
    public void setAirwaybill(Airwaybill Airwaybill) {
        this.Airwaybill = Airwaybill;
    }

    @Override
    public String toString() {
        return "ClassPojo [Airwaybill = " + Airwaybill + "]";
    }

}
