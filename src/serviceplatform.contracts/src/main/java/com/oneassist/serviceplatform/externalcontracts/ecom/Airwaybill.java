/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Savita.kodli
 *
 */
@XmlRootElement(name = "AIRWAYBILL")
@XmlAccessorType(XmlAccessType.FIELD)
public class Airwaybill {

    @XmlElement(name = "airwaybill_number")
    private String airwaybillNumber;
    @XmlElement(name = "success")
    private String success;
    @XmlElement(name = "order_id")
    private String orderId;
    @XmlElement(name = "error_list")
    private ErrorList errorList;

    @JsonProperty("airwaybill_number")
    public String getAirwaybillNumber() {
        return airwaybillNumber;
    }

    @JsonProperty("airwaybill_number")
    public void setAirwaybillNumber(String airwaybillNumber) {
        this.airwaybillNumber = airwaybillNumber;
    }

    @JsonProperty("success")
    public String getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(String success) {
        this.success = success;
    }

    @JsonProperty("order_id")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("error_list")
    public ErrorList getErrorList() {
        return errorList;
    }

    @JsonProperty("error_list")
    public void setErrorList(ErrorList errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        return "ClassPojo [airwaybillNumber = " + airwaybillNumber + ", success = " + success + ", orderId = " + orderId + "]";
    }

}
