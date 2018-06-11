package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;

public class TrackShipmentResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2056503649638323373L;

    private String awb;
    private boolean status;
    private String reason;
    private String status_update_number;

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus_update_number() {
        return status_update_number;
    }

    public void setStatus_update_number(String status_update_number) {
        this.status_update_number = status_update_number;
    }
}
