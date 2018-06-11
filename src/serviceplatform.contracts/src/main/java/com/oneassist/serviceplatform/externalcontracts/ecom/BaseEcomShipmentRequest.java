package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

public class BaseEcomShipmentRequest {

    protected static int ECOM_ADDRESS_LINE_CONSTANT = 100;

    protected static String REGEX_FOR_ADDRESS_FORMATING = "[ ,-;]";

    protected static final String REGEX_FOR_NON_ASCII_CHARS = "[^\\x00-\\x7F]";

    @XmlElement(name = "HEIGHT")
    protected String height;

    @XmlElement(name = "ORDER_NUMBER")
    protected String orderNumber;

    @XmlElement(name = "DG_SHIPMENT")
    protected String dgShipment = "false";

    @XmlElement(name = "BREADTH")
    protected String breadth;

    @XmlElement(name = "PIECES")
    protected String pieces;

    @XmlElement(name = "LENGTH")
    protected String length;

    @XmlElement(name = "PRODUCT")
    protected String product;

    @XmlElement(name = "DECLARED_VALUE")
    protected String declaredValue;

    @XmlElement(name = "ACTUAL_WEIGHT")
    protected String actualWeight;

    @XmlElement(name = "VOLUMETRIC_WEIGHT")
    protected String volumetricWeight;

    @JsonProperty("ORDER_NUMBER")
    public String getORDER_NUMBER() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @JsonProperty("HEIGHT")
    public String getHEIGHT() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @JsonProperty("DG_SHIPMENT")
    public String getDG_SHIPMENT() {
        return dgShipment;
    }

    public void setDgShipment(String dgShipment) {
        this.dgShipment = dgShipment;
    }

    @JsonProperty("BREADTH")
    public String getBREADTH() {
        return breadth;
    }

    public void setBreadth(String breadth) {
        this.breadth = breadth;
    }

    @JsonProperty("PIECES")
    public String getPIECES() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    @JsonProperty("LENGTH")
    public String getLENGTH() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("PRODUCT")
    public String getPRODUCT() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @JsonProperty("DECLARED_VALUE")
    public String getDECLARED_VALUE() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
    }

    @JsonProperty("ACTUAL_WEIGHT")
    public String getActualWeight() {
        return actualWeight;
    }

    @JsonProperty("ACTUAL_WEIGHT")
    public void setActualWeight(String ActualWeight) {
        this.actualWeight = ActualWeight;
    }

    @JsonProperty("VOLUMETRIC_WEIGHT")
    public String getVolumetricWeight() {
        return volumetricWeight;
    }

    @JsonProperty("VOLUMETRIC_WEIGHT")
    public void setVolumetricWeight(String VolumetricWeight) {
        this.volumetricWeight = VolumetricWeight;
    }

    public List<String> formatAddress(String addressLine1, String addressLine2) {
        List<String> addressList = new ArrayList<String>();
        try {
            addressLine1 = addressLine1 != null ? addressLine1.replaceAll("&", "and").replaceAll(REGEX_FOR_NON_ASCII_CHARS, "") : addressLine1;
            addressLine2 = addressLine2 != null ? addressLine2.replaceAll("&", "and").replaceAll(REGEX_FOR_NON_ASCII_CHARS, "") : addressLine2;

            if (!StringUtils.isEmpty(addressLine1) && addressLine1.length() > ECOM_ADDRESS_LINE_CONSTANT) {
                String tempAddress1 = addressLine1.substring(0, ECOM_ADDRESS_LINE_CONSTANT);
                Matcher matcher = Pattern.compile(REGEX_FOR_ADDRESS_FORMATING).matcher(tempAddress1);
                int splitIndex = ECOM_ADDRESS_LINE_CONSTANT;

                while (matcher.find()) {
                    splitIndex = matcher.start();
                }

                String preparedAddressLine1 = addressLine1.substring(0, splitIndex);
                String preparedAddressLine2 = addressLine1.substring(splitIndex) + (StringUtils.isEmpty(addressLine2) ? "" : (" " + addressLine2));
                if (preparedAddressLine2.length() > ECOM_ADDRESS_LINE_CONSTANT) {
                    preparedAddressLine2 = preparedAddressLine2.substring(0, ECOM_ADDRESS_LINE_CONSTANT - 1);
                }
                addressList.add(preparedAddressLine1);
                addressList.add(preparedAddressLine2);
            } else {
                if (addressLine2 != null && addressLine2.length() > ECOM_ADDRESS_LINE_CONSTANT) {
                    addressLine2 = addressLine2.substring(0, ECOM_ADDRESS_LINE_CONSTANT - 1);
                }
                addressList.add(addressLine1);
                addressList.add(addressLine2);
            }
        } catch (Exception e) {
            addressList.add(addressLine1);
            addressList.add(addressLine2);
        }
        return addressList;
    }
}
