package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Address")
public class Address {

    @XmlElement(name = "StreetLines")
    protected List<String> streetLines;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "StateOrProvinceCode")
    protected String stateOrProvinceCode;
    @XmlElement(name = "PostalCode")
    protected String postalCode;
    @XmlElement(name = "CountryCode")
    protected String countryCode;
    @XmlElement(name = "CountryName")
    protected String countryName;

    public List<String> getStreetLines() {
        if (this.streetLines == null) {
            this.streetLines = new ArrayList();
        }
        return this.streetLines;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String value) {
        this.city = value;
    }

    public String getStateOrProvinceCode() {
        return this.stateOrProvinceCode;
    }

    public void setStateOrProvinceCode(String value) {
        this.stateOrProvinceCode = value;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String value) {
        this.countryName = value;
    }

    @Override
    public String toString() {
        return

        "Address [streetLines=" + this.streetLines + ", city=" + this.city + ", stateOrProvinceCode=" + this.stateOrProvinceCode + ", postalCode=" + this.postalCode + ", countryCode="
                + this.countryCode + ", countryName=" + this.countryName + "]";
    }
}
