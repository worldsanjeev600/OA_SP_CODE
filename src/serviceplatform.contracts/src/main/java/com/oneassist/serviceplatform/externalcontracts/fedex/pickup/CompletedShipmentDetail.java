package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompletedShipmentDetail", namespace = "http://fedex.com/ws/ship/v19")
public class CompletedShipmentDetail {

    @XmlElement(name = "UsDomestic")
    protected Boolean usDomestic;
    @XmlElement(name = "MasterTrackingId")
    protected TrackingId masterTrackingId;
    @XmlElement(name = "ServiceTypeDescription")
    protected String serviceTypeDescription;
    @XmlElement(name = "PackagingDescription")
    protected String packagingDescription;
    @XmlElement(name = "ExportComplianceStatement")
    protected String exportComplianceStatement;
    @XmlElement(name = "ShipmentDocuments")
    protected List<ShippingDocument> shipmentDocuments;
    @XmlElement(name = "CompletedPackageDetails")
    protected List<CompletedPackageDetail> completedPackageDetails;

    public Boolean isUsDomestic() {
        return this.usDomestic;
    }

    public void setUsDomestic(Boolean value) {
        this.usDomestic = value;
    }

    public TrackingId getMasterTrackingId() {
        return this.masterTrackingId;
    }

    public void setMasterTrackingId(TrackingId value) {
        this.masterTrackingId = value;
    }

    public String getServiceTypeDescription() {
        return this.serviceTypeDescription;
    }

    public void setServiceTypeDescription(String value) {
        this.serviceTypeDescription = value;
    }

    public String getPackagingDescription() {
        return this.packagingDescription;
    }

    public void setPackagingDescription(String value) {
        this.packagingDescription = value;
    }

    public String getExportComplianceStatement() {
        return this.exportComplianceStatement;
    }

    public void setExportComplianceStatement(String value) {
        this.exportComplianceStatement = value;
    }

    public List<ShippingDocument> getShipmentDocuments() {
        if (this.shipmentDocuments == null) {
            this.shipmentDocuments = new ArrayList();
        }
        return this.shipmentDocuments;
    }

    public List<CompletedPackageDetail> getCompletedPackageDetails() {
        if (this.completedPackageDetails == null) {
            this.completedPackageDetails = new ArrayList();
        }
        return this.completedPackageDetails;
    }

    public Boolean getUsDomestic() {
        return usDomestic;
    }

    public void setShipmentDocuments(List<ShippingDocument> shipmentDocuments) {
        this.shipmentDocuments = shipmentDocuments;
    }

    public void setCompletedPackageDetails(List<CompletedPackageDetail> completedPackageDetails) {
        this.completedPackageDetails = completedPackageDetails;
    }

    @Override
    public String toString() {
        return "CompletedShipmentDetail [usDomestic=" + usDomestic + ", masterTrackingId=" + masterTrackingId + ", serviceTypeDescription=" + serviceTypeDescription + ", packagingDescription="
                + packagingDescription + ", exportComplianceStatement=" + exportComplianceStatement + ", shipmentDocuments=" + shipmentDocuments + ", completedPackageDetails="
                + completedPackageDetails + "]";
    }

}
