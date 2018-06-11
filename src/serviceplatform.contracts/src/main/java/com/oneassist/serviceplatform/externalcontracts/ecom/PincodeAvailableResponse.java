/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Savita.kodli
 *
 */
public class PincodeAvailableResponse implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @JsonProperty("state")
    private String state;

    @JsonProperty("city")
    private String city;

    @JsonProperty("route")
    private String route;

    @JsonProperty("date_of_discontinuance")
    private String dateOfDiscontinuance;

    @JsonProperty("state_code")
    private String stateCode;

    @JsonProperty("pincode")
    private String pincode;

    @JsonProperty("city_code")
    private String cityCode;

    @JsonProperty("dccode")
    private String decode;

    @JsonProperty("active")
    private String active;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDateOfDiscontinuance() {
        return dateOfDiscontinuance;
    }

    public void setDateOfDiscontinuance(String dateOfDiscontinuance) {
        this.dateOfDiscontinuance = dateOfDiscontinuance;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDecode() {
        return decode;
    }

    public void setDecode(String decode) {
        this.decode = decode;
    }

    @Override
    public String toString() {
        return "PincodeAvailableResponse [state=" + state + ", city=" + city + ", route=" + route + ", dateOfDiscontinuance=" + dateOfDiscontinuance + ", stateCode=" + stateCode + ", pincode="
                + pincode + ", cityCode=" + cityCode + ", decode=" + decode + "]";
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
