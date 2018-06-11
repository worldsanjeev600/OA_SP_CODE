package com.oneassist.serviceplatform.externalcontracts.loginext;

import java.io.Serializable;
import java.util.List;

public class LoginextDeliveryRequest implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4875244026615618264L;
    private String orderNo;
    private String awbNumber;
    private String shipmentOrderTypeCd;
    private String orderState;
    private String shipmentOrderDt;
    private String distributionCenter;
    private String packageWeight;
    private String packageVolume;
    private String paymentType;
    private String packageValue;
    private String numberOfItems;
    private String partialDeliveryAllowedFl;
    private String returnAllowedFl;
    private String cancellationAllowedFl;
    private String deliverBranch;
    private String deliverServiceTime;
    private String deliverEndTimeWindow;
    private String deliverStartTimeWindow;
    private String deliveryType;
    private String deliveryLocationType;
    private String deliverAccountCode;
    private String deliverAccountName;
    private String deliverEmail;
    private String deliverPhoneNumber;
    private String deliverApartment;
    private String deliverStreetName;
    private String deliverLandmark;
    private String deliverLocality;
    private String deliverCity;
    private String deliverState;
    private String deliverCountry;
    private String deliverPinCode;
    private String deliverLatitude;
    private String deliverLongitude;
    private String returnBranch;
    private String pickupNotes;
    private String deliverNotes;
    private List<ShipmentCrateMapping> shipmentCrateMappings;

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAwbNumber() {
        return this.awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getShipmentOrderTypeCd() {
        return this.shipmentOrderTypeCd;
    }

    public void setShipmentOrderTypeCd(String shipmentOrderTypeCd) {
        this.shipmentOrderTypeCd = shipmentOrderTypeCd;
    }

    public String getOrderState() {
        return this.orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getShipmentOrderDt() {
        return this.shipmentOrderDt;
    }

    public void setShipmentOrderDt(String shipmentOrderDt) {
        this.shipmentOrderDt = shipmentOrderDt;
    }

    public String getDistributionCenter() {
        return this.distributionCenter;
    }

    public void setDistributionCenter(String distributionCenter) {
        this.distributionCenter = distributionCenter;
    }

    public String getPackageWeight() {
        return this.packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPackageVolume() {
        return this.packageVolume;
    }

    public void setPackageVolume(String packageVolume) {
        this.packageVolume = packageVolume;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPackageValue() {
        return this.packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNumberOfItems() {
        return this.numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getPartialDeliveryAllowedFl() {
        return this.partialDeliveryAllowedFl;
    }

    public void setPartialDeliveryAllowedFl(String partialDeliveryAllowedFl) {
        this.partialDeliveryAllowedFl = partialDeliveryAllowedFl;
    }

    public String getReturnAllowedFl() {
        return this.returnAllowedFl;
    }

    public void setReturnAllowedFl(String returnAllowedFl) {
        this.returnAllowedFl = returnAllowedFl;
    }

    public String getCancellationAllowedFl() {
        return this.cancellationAllowedFl;
    }

    public void setCancellationAllowedFl(String cancellationAllowedFl) {
        this.cancellationAllowedFl = cancellationAllowedFl;
    }

    public String getDeliverBranch() {
        return this.deliverBranch;
    }

    public void setDeliverBranch(String deliverBranch) {
        this.deliverBranch = deliverBranch;
    }

    public String getDeliverServiceTime() {
        return this.deliverServiceTime;
    }

    public void setDeliverServiceTime(String deliverServiceTime) {
        this.deliverServiceTime = deliverServiceTime;
    }

    public String getDeliverEndTimeWindow() {
        return this.deliverEndTimeWindow;
    }

    public void setDeliverEndTimeWindow(String deliverEndTimeWindow) {
        this.deliverEndTimeWindow = deliverEndTimeWindow;
    }

    public String getDeliverStartTimeWindow() {
        return this.deliverStartTimeWindow;
    }

    public void setDeliverStartTimeWindow(String deliverStartTimeWindow) {
        this.deliverStartTimeWindow = deliverStartTimeWindow;
    }

    public String getDeliveryType() {
        return this.deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryLocationType() {
        return this.deliveryLocationType;
    }

    public void setDeliveryLocationType(String deliveryLocationType) {
        this.deliveryLocationType = deliveryLocationType;
    }

    public String getDeliverAccountCode() {
        return this.deliverAccountCode;
    }

    public void setDeliverAccountCode(String deliverAccountCode) {
        this.deliverAccountCode = deliverAccountCode;
    }

    public String getDeliverAccountName() {
        return this.deliverAccountName;
    }

    public void setDeliverAccountName(String deliverAccountName) {
        this.deliverAccountName = deliverAccountName;
    }

    public String getDeliverEmail() {
        return this.deliverEmail;
    }

    public void setDeliverEmail(String deliverEmail) {
        this.deliverEmail = deliverEmail;
    }

    public String getDeliverPhoneNumber() {
        return this.deliverPhoneNumber;
    }

    public void setDeliverPhoneNumber(String deliverPhoneNumber) {
        this.deliverPhoneNumber = deliverPhoneNumber;
    }

    public String getDeliverApartment() {
        return this.deliverApartment;
    }

    public void setDeliverApartment(String deliverApartment) {
        this.deliverApartment = deliverApartment;
    }

    public String getDeliverStreetName() {
        return this.deliverStreetName;
    }

    public void setDeliverStreetName(String deliverStreetName) {
        this.deliverStreetName = deliverStreetName;
    }

    public String getDeliverLandmark() {
        return this.deliverLandmark;
    }

    public void setDeliverLandmark(String deliverLandmark) {
        this.deliverLandmark = deliverLandmark;
    }

    public String getDeliverLocality() {
        return this.deliverLocality;
    }

    public void setDeliverLocality(String deliverLocality) {
        this.deliverLocality = deliverLocality;
    }

    public String getDeliverCity() {
        return this.deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    public String getDeliverState() {
        return this.deliverState;
    }

    public void setDeliverState(String deliverState) {
        this.deliverState = deliverState;
    }

    public String getDeliverCountry() {
        return this.deliverCountry;
    }

    public void setDeliverCountry(String deliverCountry) {
        this.deliverCountry = deliverCountry;
    }

    public String getDeliverPinCode() {
        return this.deliverPinCode;
    }

    public void setDeliverPinCode(String deliverPinCode) {
        this.deliverPinCode = deliverPinCode;
    }

    public String getDeliverLatitude() {
        return this.deliverLatitude;
    }

    public void setDeliverLatitude(String deliverLatitude) {
        this.deliverLatitude = deliverLatitude;
    }

    public String getDeliverLongitude() {
        return this.deliverLongitude;
    }

    public void setDeliverLongitude(String deliverLongitude) {
        this.deliverLongitude = deliverLongitude;
    }

    public String getReturnBranch() {
        return this.returnBranch;
    }

    public void setReturnBranch(String returnBranch) {
        this.returnBranch = returnBranch;
    }

    public String getPickupNotes() {
        return this.pickupNotes;
    }

    public void setPickupNotes(String pickupNotes) {
        this.pickupNotes = pickupNotes;
    }

    public String getDeliverNotes() {
        return this.deliverNotes;
    }

    public void setDeliverNotes(String deliverNotes) {
        this.deliverNotes = deliverNotes;
    }

    public List<ShipmentCrateMapping> getShipmentCrateMappings() {
        return shipmentCrateMappings;
    }

    public void setShipmentCrateMappings(List<ShipmentCrateMapping> shipmentCrateMappings) {
        this.shipmentCrateMappings = shipmentCrateMappings;
    }

    @Override
    public String toString() {
        return "LoginextDeliveryRequest [orderNo=" + orderNo + ", awbNumber=" + awbNumber + ", shipmentOrderTypeCd=" + shipmentOrderTypeCd + ", orderState=" + orderState + ", shipmentOrderDt="
                + shipmentOrderDt + ", distributionCenter=" + distributionCenter + ", packageWeight=" + packageWeight + ", packageVolume=" + packageVolume + ", paymentType=" + paymentType
                + ", packageValue=" + packageValue + ", numberOfItems=" + numberOfItems + ", partialDeliveryAllowedFl=" + partialDeliveryAllowedFl + ", returnAllowedFl=" + returnAllowedFl
                + ", cancellationAllowedFl=" + cancellationAllowedFl + ", deliverBranch=" + deliverBranch + ", deliverServiceTime=" + deliverServiceTime + ", deliverEndTimeWindow="
                + deliverEndTimeWindow + ", deliverStartTimeWindow=" + deliverStartTimeWindow + ", deliveryType=" + deliveryType + ", deliveryLocationType=" + deliveryLocationType
                + ", deliverAccountCode=" + deliverAccountCode + ", deliverAccountName=" + deliverAccountName + ", deliverEmail=" + deliverEmail + ", deliverPhoneNumber=" + deliverPhoneNumber
                + ", deliverApartment=" + deliverApartment + ", deliverStreetName=" + deliverStreetName + ", deliverLandmark=" + deliverLandmark + ", deliverLocality=" + deliverLocality
                + ", deliverCity=" + deliverCity + ", deliverState=" + deliverState + ", deliverCountry=" + deliverCountry + ", deliverPinCode=" + deliverPinCode + ", deliverLatitude="
                + deliverLatitude + ", deliverLongitude=" + deliverLongitude + ", returnBranch=" + returnBranch + ", pickupNotes=" + pickupNotes + ", deliverNotes=" + deliverNotes
                + ", shipmentCrateMappings=" + shipmentCrateMappings + "]";
    }
}
