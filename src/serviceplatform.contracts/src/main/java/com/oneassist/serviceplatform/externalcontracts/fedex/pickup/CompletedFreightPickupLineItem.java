package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompletedFreightPickupLineItem")
public class CompletedFreightPickupLineItem {

    @XmlElement(name = "SequenceNumber")
    protected Integer sequenceNumber;
    @XmlElement(name = "TotalTravelTime")
    protected Duration totalTravelTime;
    @XmlElement(name = "EtaDeliveryTimestamp")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar etaDeliveryTimestamp;

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(Integer value) {
        this.sequenceNumber = value;
    }

    public Duration getTotalTravelTime() {
        return this.totalTravelTime;
    }

    public void setTotalTravelTime(Duration value) {
        this.totalTravelTime = value;
    }

    public XMLGregorianCalendar getEtaDeliveryTimestamp() {
        return this.etaDeliveryTimestamp;
    }

    public void setEtaDeliveryTimestamp(XMLGregorianCalendar value) {
        this.etaDeliveryTimestamp = value;
    }

    @Override
    public String toString() {
        return

        "CompletedFreightPickupLineItem [sequenceNumber=" + this.sequenceNumber + ", totalTravelTime=" + this.totalTravelTime + ", etaDeliveryTimestamp=" + this.etaDeliveryTimestamp + "]";
    }
}
