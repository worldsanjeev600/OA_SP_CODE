package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupScheduleOption")
public class PickupScheduleOption {

    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "ScheduleDay")
    protected String scheduleDay;
    @XmlElement(name = "Available")
    protected Boolean available;
    @XmlElement(name = "PickupDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pickupDate;
    @XmlElement(name = "CutOffTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar cutOffTime;
    @XmlElement(name = "AccessTime")
    protected Duration accessTime;
    @XmlElement(name = "ResidentialAvailable")
    protected Boolean residentialAvailable;
    @XmlElement(name = "CountryRelationship")
    protected CountryRelationshipType countryRelationship;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getScheduleDay() {
        return this.scheduleDay;
    }

    public void setScheduleDay(String value) {
        this.scheduleDay = value;
    }

    public Boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(Boolean value) {
        this.available = value;
    }

    public XMLGregorianCalendar getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(XMLGregorianCalendar value) {
        this.pickupDate = value;
    }

    public XMLGregorianCalendar getCutOffTime() {
        return this.cutOffTime;
    }

    public void setCutOffTime(XMLGregorianCalendar value) {
        this.cutOffTime = value;
    }

    public Duration getAccessTime() {
        return this.accessTime;
    }

    public void setAccessTime(Duration value) {
        this.accessTime = value;
    }

    public Boolean isResidentialAvailable() {
        return this.residentialAvailable;
    }

    public void setResidentialAvailable(Boolean value) {
        this.residentialAvailable = value;
    }

    public CountryRelationshipType getCountryRelationship() {
        return countryRelationship;
    }

    public void setCountryRelationship(CountryRelationshipType countryRelationship) {
        this.countryRelationship = countryRelationship;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Boolean getResidentialAvailable() {
        return residentialAvailable;
    }

    @Override
    public String toString() {
        return "PickupScheduleOption [ description=" + description + ", scheduleDay=" + scheduleDay + ", available=" + available + ", pickupDate=" + pickupDate + ", cutOffTime=" + cutOffTime
                + ", accessTime=" + accessTime + ", residentialAvailable=" + residentialAvailable + "]";
    }
}
