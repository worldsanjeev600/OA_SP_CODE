package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ServiceAddressDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentAssetDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;

@XmlRootElement(name = "SHIPMENT")
@XmlAccessorType(XmlAccessType.FIELD)
public class Shipment extends BaseEcomShipmentRequest {

    private static final String ECOM_REVERSE_PICKUP_QC_CHECK_DESC = "CHECK";
    private static final String ECOM_REVERSE_PICKUP_QC_CHECK_CODE = "GEN_ITEM_DESC_CHECK";

    @XmlElement(name = "REVPICKUP_STATE")
    private String RevPickupState;

    @XmlElement(name = "REVPICKUP_MOBILE")
    private String RevPickupMobile;

    @XmlElement(name = "REVPICKUP_PINCODE")
    private String RevPickupPincode;

    @XmlElement(name = "PREFERED_FLAG")
    private String PreferedFlag;

    @XmlElement(name = "REVPICKUP_ADDRESS1")
    private String RevPickupAddress1;

    @XmlElement(name = "AWB_NUMBER")
    private String AWBNumber;

    @XmlElement(name = "REVPICKUP_ADDRESS2")
    private String RevPickupAddress2;

    @XmlElement(name = "DROP_PHONE")
    private String DropPhone;

    @XmlElement(name = "REVPICKUP_ADDRESS3")
    private String RevPickupAddress3;

    @XmlElement(name = "DROP_NAME")
    private String DropName;

    @XmlElement(name = "VENDOR_ID")
    private String VendorID;

    @XmlElement(name = "DROP_ADDRESS_LINE2")
    private String DropAdressLine2;

    @XmlElement(name = "EXTRA_INFORMATION")
    private String ExtraInformation;

    @XmlElement(name = "ITEM_DESCRIPTION")
    private String ItemDescription;

    @XmlElement(name = "REVPICKUP_NAME")
    private String RevPickupName;

    @XmlElement(name = "PREFERED_TIME")
    private String PreferedTime;

    @XmlElement(name = "HEIGHT")
    private String Height;

    @XmlElement(name = "REVPICKUP_TELEPHONE")
    private String RevPickupTelephone;

    @XmlElement(name = "REVPICKUP_CITY")
    private String RevPickupCity;

    @XmlElement(name = "DROP_PINCODE")
    private String DropPincode;

    @XmlElement(name = "COLLECTABLE_VALUE")
    private String CollectableValue;

    @XmlElement(name = "DROP_ADDRESS_LINE1")
    private String DropAddressLine1;

    @XmlElement(name = "DROP_MOBILE")
    private String DropMobile;

    @XmlElement(name = "ADDONSERVICE")
    private List<String> addOnService;

    @XmlElement(name = "QC")
    private List<ShipmentQC> qcs;

    @XmlElement(name = "ADDITIONAL_INFORMATION")
    private Object additionalInformation;

    @JsonProperty("REVPICKUP_STATE")
    public String getRevPickupState() {
        return RevPickupState;
    }

    @JsonProperty("REVPICKUP_STATE")
    public void setRevPickupState(String RevPickupState) {
        this.RevPickupState = RevPickupState;
    }

    @JsonProperty("REVPICKUP_MOBILE")
    public String getRevPickupMobile() {
        return RevPickupMobile;
    }

    @JsonProperty("REVPICKUP_MOBILE")
    public void setRevPickupMobile(String RevPickupMobile) {
        this.RevPickupMobile = RevPickupMobile;
    }

    @JsonProperty("REVPICKUP_PINCODE")
    public String getRevPickupPincode() {
        return RevPickupPincode;
    }

    @JsonProperty("REVPICKUP_PINCODE")
    public void setRevPickupPincode(String RevPickupPincode) {
        this.RevPickupPincode = RevPickupPincode;
    }

    @JsonProperty("PREFERED_FLAG")
    public String getPreferedFlag() {
        return PreferedFlag;
    }

    @JsonProperty("PREFERED_FLAG")
    public void setPreferedFlag(String PreferedFlag) {
        this.PreferedFlag = PreferedFlag;
    }

    @JsonProperty("REVPICKUP_ADDRESS1")
    public String getRevPickupAddress1() {
        return RevPickupAddress1;
    }

    @JsonProperty("REVPICKUP_ADDRESS1")
    public void setRevPickupAddress1(String RevPickupAddress1) {
        this.RevPickupAddress1 = RevPickupAddress1;
    }

    @JsonProperty("AWB_NUMBER")
    public String getAWBNumber() {
        return AWBNumber;
    }

    @JsonProperty("AWB_NUMBER")
    public void setAWBNumber(String AWBNumber) {
        this.AWBNumber = AWBNumber;
    }

    @JsonProperty("REVPICKUP_ADDRESS2")
    public String getRevPickupAddress2() {
        return RevPickupAddress2;
    }

    @JsonProperty("REVPICKUP_ADDRESS2")
    public void setRevPickupAddress2(String RevPickupAddress2) {
        this.RevPickupAddress2 = RevPickupAddress2;
    }

    @JsonProperty("DROP_PHONE")
    public String getDropPhone() {
        return DropPhone;
    }

    @JsonProperty("DROP_PHONE")
    public void setDropPhone(String DropPhone) {
        this.DropPhone = DropPhone;
    }

    @JsonProperty("REVPICKUP_ADDRESS3")
    public String getRevPickupAddress3() {
        return RevPickupAddress3;
    }

    @JsonProperty("REVPICKUP_ADDRESS3")
    public void setRevPickupAddress3(String RevPickupAddress3) {
        this.RevPickupAddress3 = RevPickupAddress3;
    }

    @JsonProperty("DROP_NAME")
    public String getDropName() {
        return DropName;
    }

    @JsonProperty("DROP_NAME")
    public void setDropName(String DropName) {
        this.DropName = DropName;
    }

    @JsonProperty("VENDOR_ID")
    public String getVendorID() {
        return VendorID;
    }

    @JsonProperty("VENDOR_ID")
    public void setVendorID(String VendorID) {
        this.VendorID = VendorID;
    }

    @JsonProperty("DROP_ADDRESS_LINE2")
    public String getDropAdressLine2() {
        return DropAdressLine2;
    }

    @JsonProperty("DROP_ADDRESS_LINE2")
    public void setDropAdressLine2(String DropAdressLine2) {
        this.DropAdressLine2 = DropAdressLine2;
    }

    @JsonProperty("EXTRA_INFORMATION")
    public String getExtraInformation() {
        return ExtraInformation;
    }

    @JsonProperty("EXTRA_INFORMATION")
    public void setExtraInformation(String ExtraInformation) {
        this.ExtraInformation = ExtraInformation;
    }

    @JsonProperty("ITEM_DESCRIPTION")
    public String getItemDescription() {
        return ItemDescription;
    }

    @JsonProperty("ITEM_DESCRIPTION")
    public void setItemDescription(String ItemDescription) {
        this.ItemDescription = ItemDescription;
    }

    @JsonProperty("REVPICKUP_NAME")
    public String getRevPickupName() {
        return RevPickupName;
    }

    @JsonProperty("REVPICKUP_NAME")
    public void setRevPickupName(String RevPickupName) {
        this.RevPickupName = RevPickupName;
    }

    @JsonProperty("PREFERED_TIME")
    public String getPreferedTime() {
        return PreferedTime;
    }

    @JsonProperty("PREFERED_TIME")
    public void setPreferedTime(String PreferedTime) {
        this.PreferedTime = PreferedTime;
    }

    @JsonProperty("HEIGHT")
    public String getHeight() {
        return Height;
    }

    @Override
    @JsonProperty("HEIGHT")
    public void setHeight(String Height) {
        this.Height = Height;
    }

    @JsonProperty("REVPICKUP_TELEPHONE")
    public String getRevPickupTelephone() {
        return RevPickupTelephone;
    }

    @JsonProperty("REVPICKUP_TELEPHONE")
    public void setRevPickupTelephone(String RevPickupTelephone) {
        this.RevPickupTelephone = RevPickupTelephone;
    }

    @JsonProperty("REVPICKUP_CITY")
    public String getRevPickupCity() {
        return RevPickupCity;
    }

    @JsonProperty("REVPICKUP_CITY")
    public void setRevPickupCity(String RevPickupCity) {
        this.RevPickupCity = RevPickupCity;
    }

    @JsonProperty("DROP_PINCODE")
    public String getDropPincode() {
        return DropPincode;
    }

    @JsonProperty("DROP_PINCODE")
    public void setDropPincode(String DropPincode) {
        this.DropPincode = DropPincode;
    }

    @JsonProperty("COLLECTABLE_VALUE")
    public String getCollectableValue() {
        return CollectableValue;
    }

    @JsonProperty("COLLECTABLE_VALUE")
    public void setCollectableValue(String CollectableValue) {
        this.CollectableValue = CollectableValue;
    }

    @JsonProperty("DROP_ADDRESS_LINE1")
    public String getDropAddressLine1() {
        return DropAddressLine1;
    }

    @JsonProperty("DROP_ADDRESS_LINE1")
    public void setDropAddressLine1(String DropAddressLine1) {
        this.DropAddressLine1 = DropAddressLine1;
    }

    @JsonProperty("DROP_MOBILE")
    public String getDropMobile() {
        return DropMobile;
    }

    @JsonProperty("DROP_MOBILE")
    public void setDropMobile(String DropMobile) {
        this.DropMobile = DropMobile;
    }

    @JsonProperty("ADDONSERVICE")
    public List<String> getAddOnService() {
        return addOnService;
    }

    @JsonProperty("ADDONSERVICE")
    public void setAddOnService(List<String> addOnService) {
        this.addOnService = addOnService;
    }

    @JsonProperty("QC")
    public List<ShipmentQC> getQcs() {
        return qcs;
    }

    @JsonProperty("QC")
    public void setQcs(List<ShipmentQC> qcs) {
        this.qcs = qcs;
    }

    @JsonProperty("ADDITIONAL_INFORMATION")
    public Object getAdditionalInformation() {
        return additionalInformation;
    }

    @JsonProperty("ADDITIONAL_INFORMATION")
    public void setAdditionalInformation(Object additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return "Shipment [RevPickupState=" + RevPickupState + ", RevPickupMobile=" + RevPickupMobile + ", RevPickupPincode=" + RevPickupPincode + ", PreferedFlag=" + PreferedFlag
                + ", RevPickupAddress1=" + RevPickupAddress1 + ", AWBNumber=" + AWBNumber + ", RevPickupAddress2=" + RevPickupAddress2 + ", DropPhone=" + DropPhone + ", RevPickupAddress3="
                + RevPickupAddress3 + ", DropName=" + DropName + ", VendorID=" + VendorID + ", DropAdressLine2=" + DropAdressLine2 + ", ExtraInformation=" + ExtraInformation + ", ItemDescription="
                + ItemDescription + ", RevPickupName=" + RevPickupName + ", PreferedTime=" + PreferedTime + ", Height=" + Height + ", RevPickupTelephone=" + RevPickupTelephone + ", RevPickupCity="
                + RevPickupCity + ", DropPincode=" + DropPincode + ", CollectableValue=" + CollectableValue + ", DropAddressLine1=" + DropAddressLine1 + ", DropMobile=" + DropMobile
                + ", addOnService=" + addOnService + ", qcs=" + qcs + ", additionalInformation=" + additionalInformation + "]";
    }

    public void populateRevPickupAdress(ServiceAddressDetailDto originAddress, PincodeMasterDto stateCityResponse) {
        this.setRevPickupName(originAddress.getAddresseeFullName());
        List<String> formattedAddress = formatAddress(originAddress.getAddressLine1(), originAddress.getAddressLine2());
        this.RevPickupAddress1 = formattedAddress.get(0);
        this.RevPickupAddress2 = formattedAddress.get(1);

        if (stateCityResponse != null) {
            this.RevPickupCity = stateCityResponse.getCityName();
            this.RevPickupState = stateCityResponse.getStateName();
        }
        this.RevPickupPincode = (originAddress.getPincode());
        this.RevPickupMobile = String.valueOf(originAddress.getMobileNo());
    }

    public void populateDropAddress(ServiceAddressDetailDto destinationAddress) {

        this.DropName = destinationAddress.getAddresseeFullName();
        List<String> formattedAddress = formatAddress(destinationAddress.getAddressLine1(), destinationAddress.getAddressLine2());
        this.DropAddressLine1 = formattedAddress.get(0);
        this.DropAdressLine2 = formattedAddress.get(1);
        this.DropPincode = destinationAddress.getPincode();
        this.DropPhone = String.valueOf(destinationAddress.getMobileNo());
        this.DropMobile = String.valueOf(destinationAddress.getMobileNo());
    }

    public void populateAssetDetails(ShipmentRequestDto shipmentRequestDto, String invoiceValue) {
        this.CollectableValue = "0";
        this.declaredValue = invoiceValue;
        this.actualWeight = String.valueOf(shipmentRequestDto.getBoxActualWeight());
        this.breadth = String.valueOf(shipmentRequestDto.getBoxActualWidth());
        this.length = String.valueOf(shipmentRequestDto.getBoxActualLength());
        this.height = String.valueOf(shipmentRequestDto.getBoxActualHeight());
        List<ShipmentQC> qcs = new ArrayList<ShipmentQC>();
        ShipmentQC qc = new ShipmentQC();
        qc.setDesc(ECOM_REVERSE_PICKUP_QC_CHECK_DESC);
        qc.setQcCheckCode(ECOM_REVERSE_PICKUP_QC_CHECK_CODE);
        if (shipmentRequestDto.getShipmentAssetDetails() != null && shipmentRequestDto.getShipmentAssetDetails().size() > 0) {
            for (ShipmentAssetDetailsDto asset : shipmentRequestDto.getShipmentAssetDetails()) {
                qc.setValue(asset.getAssetMakeName() + " " + asset.getAssetModelName());
                break;
            }
            this.pieces = String.valueOf(shipmentRequestDto.getShipmentAssetDetails().size());
        } else {
            this.pieces = "1";
        }

        qcs.add(qc);
        this.qcs = qcs;
    }

    public static void main(String args[]) {
        System.out.println(Integer.valueOf(String.valueOf(null)));
    }
}
