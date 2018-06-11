package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;

public class ForwardShipmentRequest extends BaseEcomShipmentRequest {

    private static final String ITEM_DESC = "MOBILE";

    private String state;

    private String pickupAddressLine1;

    private String destinationCity;

    private String AWBNmuber;

    private String collectableValue;

    private String returnAddressLine2;

    private String pincode;

    private String itemDescription;

    private String pickupAddressLine2;

    private String pickupPincode;

    private String returnAddressLine1;

    private String pickupName;

    private String returnMobile;

    private String telephone;

    private String returnPincode;

    private String consigneeAddress2;

    private String ConsigneeAddress3;

    private String returnPhone;

    private String returnName;

    private String consigneeAddress1;

    private String mobile;

    private String actualWeight;

    private String pickupPhone;

    private String volumetricWeight;

    private String consignee;

    private String pickupMobile;

    @JsonProperty("STATE")
    public String getSTATE() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("PICKUP_ADDRESS_LINE1")
    public String getPICKUP_ADDRESS_LINE1() {
        return pickupAddressLine1;
    }

    public void setPickupAddressLine1(String pickupAddressLine1) {
        this.pickupAddressLine1 = pickupAddressLine1;
    }

    @JsonProperty("DESTINATION_CITY")
    public String getDESTINATION_CITY() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    @JsonProperty("AWB_NUMBER")
    public String getAWB_NUMBER() {
        return AWBNmuber;
    }

    public void setAWBNmuber(String aWBNmuber) {
        AWBNmuber = aWBNmuber;
    }

    @JsonProperty("COLLECTABLE_VALUE")
    public String getCOLLECTABLE_VALUE() {
        return collectableValue;
    }

    public void setCollectableValue(String collectableValue) {
        this.collectableValue = collectableValue;
    }

    @JsonProperty("RETURN_ADDRESS_LINE2")
    public String getRETURN_ADDRESS_LINE2() {
        return returnAddressLine2;
    }

    public void setReturnAddressLine2(String returnAddressLine2) {
        this.returnAddressLine2 = returnAddressLine2;
    }

    @JsonProperty("PINCODE")
    public String getPINCODE() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @JsonProperty("ITEM_DESCRIPTION")
    public String getITEM_DESCRIPTION() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @JsonProperty("PICKUP_ADDRESS_LINE2")
    public String getPICKUP_ADDRESS_LINE2() {
        return pickupAddressLine2;
    }

    public void setPickupAddressLine2(String pickupAddressLine2) {
        this.pickupAddressLine2 = pickupAddressLine2;
    }

    @JsonProperty("PICKUP_PINCODE")
    public String getPICKUP_PINCODE() {
        return pickupPincode;
    }

    public void setPickupPincode(String pickupPincode) {
        this.pickupPincode = pickupPincode;
    }

    @JsonProperty("RETURN_ADDRESS_LINE1")
    public String getRETURN_ADDRESS_LINE1() {
        return returnAddressLine1;
    }

    public void setReturnAddressLine1(String returnAddressLine1) {
        this.returnAddressLine1 = returnAddressLine1;
    }

    @JsonProperty("PICKUP_NAME")
    public String getPICKUP_NAME() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    @JsonProperty("RETURN_MOBILE")
    public String getRETURN_MOBILE() {
        return returnMobile;
    }

    public void setReturnMobile(String returnMobile) {
        this.returnMobile = returnMobile;
    }

    @JsonProperty("TELEPHONE")
    public String getTELEPHONE() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @JsonProperty("RETURN_PINCODE")
    public String getRETURN_PINCODE() {
        return returnPincode;
    }

    public void setReturnPincode(String returnPincode) {
        this.returnPincode = returnPincode;
    }

    @JsonProperty("CONSIGNEE_ADDRESS2")
    public String getCONSIGNEE_ADDRESS2() {
        return consigneeAddress2;
    }

    public void setConsigneeAddress2(String consigneeAddress2) {
        this.consigneeAddress2 = consigneeAddress2;
    }

    @JsonProperty("CONSIGNEE_ADDRESS3")
    public String getCONSIGNEE_ADDRESS3() {
        return ConsigneeAddress3;
    }

    public void setConsigneeAddress3(String consigneeAddress3) {
        ConsigneeAddress3 = consigneeAddress3;
    }

    @JsonProperty("RETURN_PHONE")
    public String getRETURN_PHONE() {
        return returnPhone;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone;
    }

    @JsonProperty("RETURN_NAME")
    public String getRETURN_NAME() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    @JsonProperty("CONSIGNEE_ADDRESS1")
    public String getCONSIGNEE_ADDRESS1() {
        return consigneeAddress1;
    }

    public void setConsigneeAddress1(String consigneeAddress1) {
        this.consigneeAddress1 = consigneeAddress1;
    }

    @JsonProperty("MOBILE")
    public String getMOBILE() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty("ACTUAL_WEIGHT")
    public String getACTUAL_WEIGHT() {
        return actualWeight;
    }

    @Override
    public void setActualWeight(String actualWeight) {
        this.actualWeight = actualWeight;
    }

    @JsonProperty("PICKUP_PHONE")
    public String getPICKUP_PHONE() {
        return pickupPhone;
    }

    public void setPickupPhone(String pickupPhone) {
        this.pickupPhone = pickupPhone;
    }

    @JsonProperty("VOLUMETRIC_WEIGHT")
    public String getVOLUMETRIC_WEIGHT() {
        return volumetricWeight;
    }

    @Override
    public void setVolumetricWeight(String volumetricWeight) {
        this.volumetricWeight = volumetricWeight;
    }

    @JsonProperty("CONSIGNEE")
    public String getCONSIGNEE() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @JsonProperty("PICKUP_MOBILE")
    public String getPICKUP_MOBILE() {
        return pickupMobile;
    }

    public void setPickupMobile(String pickupMobile) {
        this.pickupMobile = pickupMobile;
    }

    public void populateAssetDetails(ShipmentRequestDto shipment, String invoiceValue) {
        this.declaredValue = invoiceValue;
        this.height = String.valueOf(shipment.getBoxActualHeight());
        this.length = String.valueOf(shipment.getBoxActualLength());
        this.breadth = String.valueOf(shipment.getBoxActualWidth());
        this.actualWeight = String.valueOf(shipment.getBoxActualWeight());
        this.setCollectableValue("0");
        this.setItemDescription(ITEM_DESC);

    }

    public void populateConsigneeAddress(ServiceAddressDetailDto destinationAddress, PincodeMasterDto stateCityResponse) {
        this.consignee = destinationAddress.getAddresseeFullName();
        List<String> formattedAddress = formatAddress(destinationAddress.getAddressLine1(), destinationAddress.getAddressLine2());
        this.consigneeAddress1 = formattedAddress.get(0);
        this.consigneeAddress2 = formattedAddress.get(1);
        this.mobile = String.valueOf(destinationAddress.getMobileNo());
        this.pincode = destinationAddress.getPincode();

        if (stateCityResponse != null) {
            this.destinationCity = stateCityResponse.getCityName();
            this.state = stateCityResponse.getStateName();
        }
    }

    public void populatePickupAndReturnAddress(ServiceAddressDetailDto originAddress) {
        List<String> formattedAddress = formatAddress(originAddress.getAddressLine1(), originAddress.getAddressLine2());
        this.pickupAddressLine1 = formattedAddress.get(0);
        this.pickupAddressLine2 = formattedAddress.get(1);
        this.pickupMobile = String.valueOf(originAddress.getMobileNo());
        this.pickupPhone = String.valueOf(originAddress.getMobileNo());
        this.pickupPincode = originAddress.getPincode();

        // need to verify whose details need to enter
        this.returnAddressLine1 = formattedAddress.get(0);
        this.returnAddressLine2 = formattedAddress.get(1);
        this.returnMobile = String.valueOf(originAddress.getMobileNo());
        this.returnName = originAddress.getAddresseeFullName();
        this.returnPincode = originAddress.getPincode();
    }

}
