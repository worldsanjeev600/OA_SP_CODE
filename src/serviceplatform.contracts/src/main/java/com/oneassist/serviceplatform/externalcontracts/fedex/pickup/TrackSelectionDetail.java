package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackSelectionDetail")
public class TrackSelectionDetail {

    @XmlElement(name = "CarrierCode")
    protected String carrierCode;
    @XmlElement(name = "PackageIdentifier")
    protected TrackPackageIdentifier packageIdentifier;
    @XmlElement(name = "TrackingNumberUniqueIdentifier")
    protected String trackingNumberUniqueIdentifier;
    @XmlElement(name = "ShipDateRangeBegin")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar shipDateRangeBegin;
    @XmlElement(name = "ShipDateRangeEnd")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar shipDateRangeEnd;
    @XmlElement(name = "ShipmentAccountNumber")
    protected String shipmentAccountNumber;
    @XmlElement(name = "SecureSpodAccount")
    protected String secureSpodAccount;
    @XmlElement(name = "Destination")
    protected Address destination;
    @XmlElement(name = "CustomerSpecifiedTimeOutValueInMilliseconds")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger customerSpecifiedTimeOutValueInMilliseconds;

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public TrackPackageIdentifier getPackageIdentifier() {
        return packageIdentifier;
    }

    public void setPackageIdentifier(TrackPackageIdentifier packageIdentifier) {
        this.packageIdentifier = packageIdentifier;
    }

    public String getTrackingNumberUniqueIdentifier() {
        return trackingNumberUniqueIdentifier;
    }

    public void setTrackingNumberUniqueIdentifier(String trackingNumberUniqueIdentifier) {
        this.trackingNumberUniqueIdentifier = trackingNumberUniqueIdentifier;
    }

    public XMLGregorianCalendar getShipDateRangeBegin() {
        return shipDateRangeBegin;
    }

    public void setShipDateRangeBegin(XMLGregorianCalendar shipDateRangeBegin) {
        this.shipDateRangeBegin = shipDateRangeBegin;
    }

    public XMLGregorianCalendar getShipDateRangeEnd() {
        return shipDateRangeEnd;
    }

    public void setShipDateRangeEnd(XMLGregorianCalendar shipDateRangeEnd) {
        this.shipDateRangeEnd = shipDateRangeEnd;
    }

    public String getShipmentAccountNumber() {
        return shipmentAccountNumber;
    }

    public void setShipmentAccountNumber(String shipmentAccountNumber) {
        this.shipmentAccountNumber = shipmentAccountNumber;
    }

    public String getSecureSpodAccount() {
        return secureSpodAccount;
    }

    public void setSecureSpodAccount(String secureSpodAccount) {
        this.secureSpodAccount = secureSpodAccount;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public BigInteger getCustomerSpecifiedTimeOutValueInMilliseconds() {
        return customerSpecifiedTimeOutValueInMilliseconds;
    }

    public void setCustomerSpecifiedTimeOutValueInMilliseconds(BigInteger customerSpecifiedTimeOutValueInMilliseconds) {
        this.customerSpecifiedTimeOutValueInMilliseconds = customerSpecifiedTimeOutValueInMilliseconds;
    }

    @Override
    public String toString() {
        return "TrackSelectionDetail [carrierCode=" + carrierCode + ", packageIdentifier=" + packageIdentifier + ", trackingNumberUniqueIdentifier=" + trackingNumberUniqueIdentifier
                + ", shipDateRangeBegin=" + shipDateRangeBegin + ", shipDateRangeEnd=" + shipDateRangeEnd + ", shipmentAccountNumber=" + shipmentAccountNumber + ", secureSpodAccount="
                + secureSpodAccount + ", destination=" + destination + ", customerSpecifiedTimeOutValueInMilliseconds=" + customerSpecifiedTimeOutValueInMilliseconds + "]";
    }

}
