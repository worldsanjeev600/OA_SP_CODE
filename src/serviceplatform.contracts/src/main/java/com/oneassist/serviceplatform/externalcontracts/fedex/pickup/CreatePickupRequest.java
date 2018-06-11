package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "CreatePickupRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreatePickupRequest")
public class CreatePickupRequest {

    @XmlElement(name = "WebAuthenticationDetail", required = true)
    protected WebAuthenticationDetail webAuthenticationDetail;
    @XmlElement(name = "ClientDetail", required = true)
    protected ClientDetail clientDetail;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "TrackingNumber")
    protected String trackingNumber;
    @XmlElement(name = "OriginDetail")
    protected PickupOriginDetail originDetail;
    @XmlElement(name = "PackageCount")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger packageCount;
    @XmlElement(name = "TotalWeight")
    protected Weight totalWeight;
    @XmlElement(name = "CarrierCode")
    protected String carrierCode;
    @XmlElement(name = "OversizePackageCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger oversizePackageCount;
    @XmlElement(name = "Remarks")
    protected String remarks;
    @XmlElement(name = "CommodityDescription")
    protected String commodityDescription;

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

    public String getTrackingNumber() {
        return this.trackingNumber;
    }

    public void setTrackingNumber(String value) {
        this.trackingNumber = value;
    }

    public PickupOriginDetail getOriginDetail() {
        return this.originDetail;
    }

    public void setOriginDetail(PickupOriginDetail value) {
        this.originDetail = value;
    }

    public BigInteger getPackageCount() {
        return this.packageCount;
    }

    public void setPackageCount(BigInteger value) {
        this.packageCount = value;
    }

    public Weight getTotalWeight() {
        return this.totalWeight;
    }

    public void setTotalWeight(Weight value) {
        this.totalWeight = value;
    }

    public String getCarrierCode() {
        return this.carrierCode;
    }

    public void setCarrierCode(String value) {
        this.carrierCode = value;
    }

    public BigInteger getOversizePackageCount() {
        return this.oversizePackageCount;
    }

    public void setOversizePackageCount(BigInteger value) {
        this.oversizePackageCount = value;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String value) {
        this.remarks = value;
    }

    public String getCommodityDescription() {
        return this.commodityDescription;
    }

    public void setCommodityDescription(String value) {
        this.commodityDescription = value;
    }

    @Override
    public String toString() {
        return

        "CreatePickupRequest [webAuthenticationDetail=" + this.webAuthenticationDetail + ", clientDetail=" + this.clientDetail + ", transactionDetail=" + this.transactionDetail + ", version="
                + this.version + ", trackingNumber=" + this.trackingNumber + ", packageCount=" + this.packageCount + ", totalWeight=" + this.totalWeight + ", carrierCode=" + this.carrierCode
                + ", oversizePackageCount=" + this.oversizePackageCount + ", remarks=" + this.remarks + ", commodityDescription=" + this.commodityDescription + "]";
    }
}
