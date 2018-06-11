package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AwbGenerationResponse {

    @JsonProperty("reference_id")
    private String refernceId;
    private String success;
    private List<String> awb;

    public String getRefernceId() {
        return refernceId;
    }

    public void setRefernceId(String refernceId) {
        this.refernceId = refernceId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<String> getAwb() {
        return awb;
    }

    public void setAwb(List<String> awb) {
        this.awb = awb;
    }

}
