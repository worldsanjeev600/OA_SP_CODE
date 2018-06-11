/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Savita.kodli
 *
 */
public class Shipments {

    @JsonProperty("order_number")
    private String orderNumber;

    private String awb;

    private String reason;

    private String success;

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ClassPojo [order_number = " + orderNumber + ", awb = " + awb + ", reason = " + reason + ", success = " + success + "]";
    }
}
