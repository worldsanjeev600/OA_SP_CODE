package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestedShipment", propOrder = { "shipTimestamp", "dropoffType", "serviceType", "packagingType", "totalWeight", "totalInsuredValue", "preferredCurrency", "shipper", "recipient",
        "recipientLocationNumber", "origin", "soldTo", "shippingChargesPayment", "deliveryInstructions", "customsClearanceDetail", "blockInsightVisibility", "labelSpecification", "packageCount",
        "requestedPackageLineItems" })
public class RequestedShipment {

    @XmlElement(name = "ShipTimestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar shipTimestamp;
    @XmlElement(name = "DropoffType", required = true)
    protected String dropoffType;
    @XmlElement(name = "ServiceType", required = true)
    protected String serviceType;
    @XmlElement(name = "PackagingType", required = true)
    protected String packagingType;
    @XmlElement(name = "TotalWeight")
    protected Weight totalWeight;
    @XmlElement(name = "TotalInsuredValue")
    protected Money totalInsuredValue;
    @XmlElement(name = "PreferredCurrency")
    protected String preferredCurrency;
    @XmlElement(name = "Shipper", required = true)
    protected Party shipper;
    @XmlElement(name = "Recipient", required = true)
    protected Party recipient;
    @XmlElement(name = "RecipientLocationNumber")
    protected String recipientLocationNumber;
    @XmlElement(name = "Origin")
    protected ContactAndAddress origin;
    @XmlElement(name = "SoldTo")
    protected Party soldTo;
    @XmlElement(name = "ShippingChargesPayment")
    protected Payment shippingChargesPayment;
    @XmlElement(name = "DeliveryInstructions")
    protected String deliveryInstructions;
    @XmlElement(name = "BlockInsightVisibility")
    protected Boolean blockInsightVisibility;
    @XmlElement(name = "LabelSpecification", required = true)
    protected LabelSpecification labelSpecification;
    @XmlElement(name = "PackageCount", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger packageCount;
    @XmlElement(name = "RequestedPackageLineItems")
    protected List<RequestedPackageLineItem> requestedPackageLineItems;
    @XmlElement(name = "CustomsClearanceDetail")
    protected CustomsClearanceDetail customsClearanceDetail;

    public XMLGregorianCalendar getShipTimestamp() {
        return shipTimestamp;
    }

    public void setShipTimestamp(XMLGregorianCalendar shipTimestamp) {
        this.shipTimestamp = shipTimestamp;
    }

    public String getDropoffType() {
        return dropoffType;
    }

    public void setDropoffType(String dropoffType) {
        this.dropoffType = dropoffType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public Weight getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Weight totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Money getTotalInsuredValue() {
        return totalInsuredValue;
    }

    public void setTotalInsuredValue(Money totalInsuredValue) {
        this.totalInsuredValue = totalInsuredValue;
    }

    public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public Party getShipper() {
        return shipper;
    }

    public void setShipper(Party shipper) {
        this.shipper = shipper;
    }

    public Party getRecipient() {
        return recipient;
    }

    public void setRecipient(Party recipient) {
        this.recipient = recipient;
    }

    public String getRecipientLocationNumber() {
        return recipientLocationNumber;
    }

    public void setRecipientLocationNumber(String recipientLocationNumber) {
        this.recipientLocationNumber = recipientLocationNumber;
    }

    public ContactAndAddress getOrigin() {
        return origin;
    }

    public void setOrigin(ContactAndAddress origin) {
        this.origin = origin;
    }

    public Party getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(Party soldTo) {
        this.soldTo = soldTo;
    }

    public Payment getShippingChargesPayment() {
        return shippingChargesPayment;
    }

    public void setShippingChargesPayment(Payment shippingChargesPayment) {
        this.shippingChargesPayment = shippingChargesPayment;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public Boolean getBlockInsightVisibility() {
        return blockInsightVisibility;
    }

    public void setBlockInsightVisibility(Boolean blockInsightVisibility) {
        this.blockInsightVisibility = blockInsightVisibility;
    }

    public LabelSpecification getLabelSpecification() {
        return labelSpecification;
    }

    public void setLabelSpecification(LabelSpecification labelSpecification) {
        this.labelSpecification = labelSpecification;
    }

    public BigInteger getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(BigInteger packageCount) {
        this.packageCount = packageCount;
    }

    public List<RequestedPackageLineItem> getRequestedPackageLineItems() {
        if (requestedPackageLineItems == null) {
            requestedPackageLineItems = new ArrayList<RequestedPackageLineItem>();
        }
        return requestedPackageLineItems;
    }

    public void setRequestedPackageLineItems(List<RequestedPackageLineItem> requestedPackageLineItems) {
        this.requestedPackageLineItems = requestedPackageLineItems;
    }

    public CustomsClearanceDetail getCustomsClearanceDetail() {
        return customsClearanceDetail;
    }

    public void setCustomsClearanceDetail(CustomsClearanceDetail customsClearanceDetail) {
        this.customsClearanceDetail = customsClearanceDetail;
    }

    @Override
    public String toString() {
        return "RequestedShipment [shipTimestamp=" + shipTimestamp + ", dropoffType=" + dropoffType + ", serviceType=" + serviceType + ", packagingType=" + packagingType + ", totalWeight="
                + totalWeight + ", totalInsuredValue=" + totalInsuredValue + ", preferredCurrency=" + preferredCurrency + ", shipper=" + shipper + ", recipient=" + recipient
                + ", recipientLocationNumber=" + recipientLocationNumber + ", origin=" + origin + ", soldTo=" + soldTo + ", shippingChargesPayment=" + shippingChargesPayment
                + ", deliveryInstructions=" + deliveryInstructions + ", blockInsightVisibility=" + blockInsightVisibility + ", labelSpecification=" + labelSpecification + ", masterTrackingId="
                + ", packageCount=" + packageCount + ", requestedPackageLineItems=" + requestedPackageLineItems + ", customsClearanceDetail=" + customsClearanceDetail + "]";
    }

}
