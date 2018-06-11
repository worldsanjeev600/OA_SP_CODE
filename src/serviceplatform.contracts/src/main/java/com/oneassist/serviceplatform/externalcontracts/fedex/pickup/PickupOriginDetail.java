package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupOriginDetail")
public class PickupOriginDetail {

    @XmlElement(name = "UseAccountAddress")
    protected Boolean useAccountAddress;
    @XmlElement(name = "AddressId")
    protected String addressId;
    @XmlElement(name = "PickupLocation")
    protected ContactAndAddress pickupLocation;
    @XmlElement(name = "BuildingPartDescription")
    protected String buildingPartDescription;
    @XmlElement(name = "ReadyTimestamp")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar readyTimestamp;
    @XmlElement(name = "CompanyCloseTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar companyCloseTime;
    @XmlElement(name = "StayLate")
    protected Boolean stayLate;
    @XmlElement(name = "PickupDateType")
    protected String pickupDateType;
    @XmlElement(name = "LastAccessTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar lastAccessTime;
    @XmlElement(name = "GeographicalPostalCode")
    protected String geographicalPostalCode;
    @XmlElement(name = "Location")
    protected String location;
    @XmlElement(name = "DeleteLastUsed")
    protected Boolean deleteLastUsed;
    @XmlElement(name = "SuppliesRequested")
    protected String suppliesRequested;
    @XmlElement(name = "EarlyPickup")
    protected Boolean earlyPickup;

    public Boolean isUseAccountAddress() {
        return this.useAccountAddress;
    }

    public void setUseAccountAddress(Boolean value) {
        this.useAccountAddress = value;
    }

    public String getAddressId() {
        return this.addressId;
    }

    public void setAddressId(String value) {
        this.addressId = value;
    }

    public ContactAndAddress getPickupLocation() {
        return this.pickupLocation;
    }

    public void setPickupLocation(ContactAndAddress value) {
        this.pickupLocation = value;
    }

    public String getBuildingPartDescription() {
        return this.buildingPartDescription;
    }

    public void setBuildingPartDescription(String value) {
        this.buildingPartDescription = value;
    }

    public XMLGregorianCalendar getReadyTimestamp() {
        return this.readyTimestamp;
    }

    public void setReadyTimestamp(XMLGregorianCalendar value) {
        this.readyTimestamp = value;
    }

    public XMLGregorianCalendar getCompanyCloseTime() {
        return this.companyCloseTime;
    }

    public void setCompanyCloseTime(XMLGregorianCalendar value) {
        this.companyCloseTime = value;
    }

    public Boolean isStayLate() {
        return this.stayLate;
    }

    public void setStayLate(Boolean value) {
        this.stayLate = value;
    }

    public String getPickupDateType() {
        return this.pickupDateType;
    }

    public void setPickupDateType(String value) {
        this.pickupDateType = value;
    }

    public XMLGregorianCalendar getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(XMLGregorianCalendar value) {
        this.lastAccessTime = value;
    }

    public String getGeographicalPostalCode() {
        return this.geographicalPostalCode;
    }

    public void setGeographicalPostalCode(String value) {
        this.geographicalPostalCode = value;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String value) {
        this.location = value;
    }

    public Boolean isDeleteLastUsed() {
        return this.deleteLastUsed;
    }

    public void setDeleteLastUsed(Boolean value) {
        this.deleteLastUsed = value;
    }

    public String getSuppliesRequested() {
        return this.suppliesRequested;
    }

    public void setSuppliesRequested(String value) {
        this.suppliesRequested = value;
    }

    public Boolean isEarlyPickup() {
        return this.earlyPickup;
    }

    public void setEarlyPickup(Boolean value) {
        this.earlyPickup = value;
    }

    @Override
    public String toString() {
        return

        "PickupOriginDetail [useAccountAddress=" + this.useAccountAddress + ", addressId=" + this.addressId + ", pickupLocation=" + this.pickupLocation + ", buildingPartDescription="
                + this.buildingPartDescription + ", readyTimestamp=" + this.readyTimestamp + ", companyCloseTime=" + this.companyCloseTime + ", stayLate=" + this.stayLate + ", pickupDateType="
                + this.pickupDateType + ", lastAccessTime=" + this.lastAccessTime + ", geographicalPostalCode=" + this.geographicalPostalCode + ", location=" + this.location + ", deleteLastUsed="
                + this.deleteLastUsed + ", suppliesRequested=" + this.suppliesRequested + ", earlyPickup=" + this.earlyPickup + "]";
    }
}
