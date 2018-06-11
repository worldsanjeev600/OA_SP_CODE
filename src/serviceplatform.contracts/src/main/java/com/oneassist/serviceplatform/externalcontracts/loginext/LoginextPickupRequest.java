package com.oneassist.serviceplatform.externalcontracts.loginext;

import java.io.Serializable;
import java.util.List;

public class LoginextPickupRequest implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5105792235851212565L;
    private String orderNo;
    private String awbNumber;
    private String shipmentOrderTypeCd;
    private String orderState;
    private String shipmentOrderDt;
    private String distributionCenter;
    private String packageWeight;
    private String packageVolume;
    private String packageValue;
    private String paymentType;
    private String numberOfItems;
    private String deliveryType;
    private String partialDeliveryAllowedFl;
    private String returnAllowedFl;
    private String cancellationAllowedFl;
    private String pickupBranch;
    private String pickupServiceTime;
    private String pickupStartTimeWindow;
    private String pickupEndTimeWindow;
    private String pickupAccountCode;
    private String pickupAccountName;
    private String pickupEmail;
    private String pickupPhoneNumber;
    private String pickupApartment;
    private String pickupStreetName;
    private String pickupLandmark;
    private String pickupLocality;
    private String pickupCity;
    private String pickupState;
    private String pickupCountry;
    private String pickupPinCode;
    private String pickupLatitude;
    private String pickupLongitude;
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

    public String getPackageValue() {
        return this.packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getNumberOfItems() {
        return this.numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getDeliveryType() {
        return this.deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
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

    public String getPickupBranch() {
        return this.pickupBranch;
    }

    public void setPickupBranch(String pickupBranch) {
        this.pickupBranch = pickupBranch;
    }

    public String getPickupServiceTime() {
        return this.pickupServiceTime;
    }

    public void setPickupServiceTime(String pickupServiceTime) {
        this.pickupServiceTime = pickupServiceTime;
    }

    public String getPickupStartTimeWindow() {
        return this.pickupStartTimeWindow;
    }

    public void setPickupStartTimeWindow(String pickupStartTimeWindow) {
        this.pickupStartTimeWindow = pickupStartTimeWindow;
    }

    public String getPickupEndTimeWindow() {
        return this.pickupEndTimeWindow;
    }

    public void setPickupEndTimeWindow(String pickupEndTimeWindow) {
        this.pickupEndTimeWindow = pickupEndTimeWindow;
    }

    public String getPickupAccountCode() {
        return this.pickupAccountCode;
    }

    public void setPickupAccountCode(String pickupAccountCode) {
        this.pickupAccountCode = pickupAccountCode;
    }

    public String getPickupAccountName() {
        return this.pickupAccountName;
    }

    public void setPickupAccountName(String pickupAccountName) {
        this.pickupAccountName = pickupAccountName;
    }

    public String getPickupEmail() {
        return this.pickupEmail;
    }

    public void setPickupEmail(String pickupEmail) {
        this.pickupEmail = pickupEmail;
    }

    public String getPickupPhoneNumber() {
        return this.pickupPhoneNumber;
    }

    public void setPickupPhoneNumber(String pickupPhoneNumber) {
        this.pickupPhoneNumber = pickupPhoneNumber;
    }

    public String getPickupApartment() {
        return this.pickupApartment;
    }

    public void setPickupApartment(String pickupApartment) {
        this.pickupApartment = pickupApartment;
    }

    public String getPickupStreetName() {
        return this.pickupStreetName;
    }

    public void setPickupStreetName(String pickupStreetName) {
        this.pickupStreetName = pickupStreetName;
    }

    public String getPickupLandmark() {
        return this.pickupLandmark;
    }

    public void setPickupLandmark(String pickupLandmark) {
        this.pickupLandmark = pickupLandmark;
    }

    public String getPickupLocality() {
        return this.pickupLocality;
    }

    public void setPickupLocality(String pickupLocality) {
        this.pickupLocality = pickupLocality;
    }

    public String getPickupCity() {
        return this.pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPickupState() {
        return this.pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public String getPickupCountry() {
        return this.pickupCountry;
    }

    public void setPickupCountry(String pickupCountry) {
        this.pickupCountry = pickupCountry;
    }

    public String getPickupPinCode() {
        return this.pickupPinCode;
    }

    public void setPickupPinCode(String pickupPinCode) {
        this.pickupPinCode = pickupPinCode;
    }

    public String getPickupLatitude() {
        return this.pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return this.pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
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
        return "LoginextPickupRequest [orderNo=" + orderNo + ", awbNumber=" + awbNumber + ", shipmentOrderTypeCd=" + shipmentOrderTypeCd + ", orderState=" + orderState + ", shipmentOrderDt="
                + shipmentOrderDt + ", distributionCenter=" + distributionCenter + ", packageWeight=" + packageWeight + ", packageVolume=" + packageVolume + ", packageValue=" + packageValue
                + ", paymentType=" + paymentType + ", numberOfItems=" + numberOfItems + ", deliveryType=" + deliveryType + ", partialDeliveryAllowedFl=" + partialDeliveryAllowedFl
                + ", returnAllowedFl=" + returnAllowedFl + ", cancellationAllowedFl=" + cancellationAllowedFl + ", pickupBranch=" + pickupBranch + ", pickupServiceTime=" + pickupServiceTime
                + ", pickupStartTimeWindow=" + pickupStartTimeWindow + ", pickupEndTimeWindow=" + pickupEndTimeWindow + ", pickupAccountCode=" + pickupAccountCode + ", pickupAccountName="
                + pickupAccountName + ", pickupEmail=" + pickupEmail + ", pickupPhoneNumber=" + pickupPhoneNumber + ", pickupApartment=" + pickupApartment + ", pickupStreetName=" + pickupStreetName
                + ", pickupLandmark=" + pickupLandmark + ", pickupLocality=" + pickupLocality + ", pickupCity=" + pickupCity + ", pickupState=" + pickupState + ", pickupCountry=" + pickupCountry
                + ", pickupPinCode=" + pickupPinCode + ", pickupLatitude=" + pickupLatitude + ", pickupLongitude=" + pickupLongitude + ", pickupNotes=" + pickupNotes + ", deliverNotes="
                + deliverNotes + ", shipmentCrateMappings=" + shipmentCrateMappings + "]";
    }
}
