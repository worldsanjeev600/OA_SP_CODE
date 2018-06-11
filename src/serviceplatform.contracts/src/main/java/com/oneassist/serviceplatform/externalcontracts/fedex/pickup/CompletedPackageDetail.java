package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompletedPackageDetail", namespace = "http://fedex.com/ws/ship/v19")
public class CompletedPackageDetail {

    @XmlElement(name = "SequenceNumber")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger sequenceNumber;
    @XmlElement(name = "TrackingIds")
    protected List<TrackingId> trackingIds;
    @XmlElement(name = "GroupNumber")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger groupNumber;
    @XmlElement(name = "Label")
    protected ShippingDocument label;
    @XmlElement(name = "PackageDocuments")
    protected List<ShippingDocument> packageDocuments;
    @XmlElement(name = "DryIceWeight")
    protected Weight dryIceWeight;

    public BigInteger getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(BigInteger value) {
        this.sequenceNumber = value;
    }

    public List<TrackingId> getTrackingIds() {
        if (this.trackingIds == null) {
            this.trackingIds = new ArrayList();
        }
        return this.trackingIds;
    }

    public BigInteger getGroupNumber() {
        return this.groupNumber;
    }

    public void setGroupNumber(BigInteger value) {
        this.groupNumber = value;
    }

    public ShippingDocument getLabel() {
        return this.label;
    }

    public void setLabel(ShippingDocument value) {
        this.label = value;
    }

    public List<ShippingDocument> getPackageDocuments() {
        if (this.packageDocuments == null) {
            this.packageDocuments = new ArrayList();
        }
        return this.packageDocuments;
    }

    public Weight getDryIceWeight() {
        return this.dryIceWeight;
    }

    public void setDryIceWeight(Weight value) {
        this.dryIceWeight = value;
    }

    @Override
    public String toString() {
        return "CompletedPackageDetail [sequenceNumber=" + sequenceNumber + ", trackingIds=" + trackingIds + ", groupNumber=" + groupNumber + ", label=" + label + ", packageDocuments="
                + packageDocuments + ", dryIceWeight=" + dryIceWeight + "]";
    }

}
