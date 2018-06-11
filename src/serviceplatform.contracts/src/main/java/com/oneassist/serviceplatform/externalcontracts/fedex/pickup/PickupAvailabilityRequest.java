package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement(name = "PickupAvailabilityRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupAvailabilityRequest")
public class PickupAvailabilityRequest {

    @XmlElement(name = "WebAuthenticationDetail", required = true)
    protected WebAuthenticationDetail webAuthenticationDetail;
    @XmlElement(name = "ClientDetail", required = true)
    protected ClientDetail clientDetail;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "PickupAddress")
    protected Address pickupAddress;
    @XmlElement(name = "PickupRequestType")
    protected List<String> pickupRequestType;
    @XmlElement(name = "DispatchDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dispatchDate;
    @XmlElement(name = "NumberOfBusinessDays")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger numberOfBusinessDays;
    @XmlElement(name = "PackageReadyTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar packageReadyTime;
    @XmlElement(name = "CustomerCloseTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar customerCloseTime;
    @XmlElement(name = "Carriers")
    protected List<String> carriers;
    @XmlElement(name = "ShipmentAttributes")
    protected PickupShipmentAttributes shipmentAttributes;

    public WebAuthenticationDetail getWebAuthenticationDetail() {
        return this.webAuthenticationDetail;
    }

    public void setWebAuthenticationDetail(WebAuthenticationDetail value) {
        this.webAuthenticationDetail = value;
    }

    public ClientDetail getClientDetail() {
        return this.clientDetail;
    }

    public void setClientDetail(ClientDetail value) {
        this.clientDetail = value;
    }

    public TransactionDetail getTransactionDetail() {
        return this.transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail value) {
        this.transactionDetail = value;
    }

    public VersionId getVersion() {
        return this.version;
    }

    public void setVersion(VersionId value) {
        this.version = value;
    }

    public Address getPickupAddress() {
        return this.pickupAddress;
    }

    public void setPickupAddress(Address value) {
        this.pickupAddress = value;
    }

    public List<String> getPickupRequestType() {
        if (this.pickupRequestType == null) {
            this.pickupRequestType = new ArrayList();
        }
        return this.pickupRequestType;
    }

    public XMLGregorianCalendar getDispatchDate() {
        return this.dispatchDate;
    }

    public void setDispatchDate(XMLGregorianCalendar value) {
        this.dispatchDate = value;
    }

    public BigInteger getNumberOfBusinessDays() {
        return this.numberOfBusinessDays;
    }

    public void setNumberOfBusinessDays(BigInteger value) {
        this.numberOfBusinessDays = value;
    }

    public XMLGregorianCalendar getPackageReadyTime() {
        return this.packageReadyTime;
    }

    public void setPackageReadyTime(XMLGregorianCalendar value) {
        this.packageReadyTime = value;
    }

    public XMLGregorianCalendar getCustomerCloseTime() {
        return this.customerCloseTime;
    }

    public void setCustomerCloseTime(XMLGregorianCalendar value) {
        this.customerCloseTime = value;
    }

    public List<String> getCarriers() {
        if (this.carriers == null) {
            this.carriers = new ArrayList();
        }
        return this.carriers;
    }

    public PickupShipmentAttributes getShipmentAttributes() {
        return this.shipmentAttributes;
    }

    public void setShipmentAttributes(PickupShipmentAttributes value) {
        this.shipmentAttributes = value;
    }

    @Override
    public String toString() {
        return

        "PickupAvailabilityRequest [webAuthenticationDetail=" + this.webAuthenticationDetail + ", clientDetail=" + this.clientDetail + ", transactionDetail=" + this.transactionDetail + ", version="
                + this.version + ", pickupAddress=" + this.pickupAddress + ", pickupRequestType=" + this.pickupRequestType + ", dispatchDate=" + this.dispatchDate + ", numberOfBusinessDays="
                + this.numberOfBusinessDays + ", packageReadyTime=" + this.packageReadyTime + ", customerCloseTime=" + this.customerCloseTime + ", carriers=" + this.carriers + ", shipmentAttributes="
                + this.shipmentAttributes + "]";
    }
}
