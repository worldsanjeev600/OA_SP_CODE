package com.oneassist.serviceplatform.contracts.dtos.hub;

public class HubAllocationDto {

    private String pincode;
    private String make;
    private String stateCode;
    private Integer hubId;
    private String pincodeCategory;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getHubId() {
        return hubId;
    }

    public void setHubId(Integer hubId) {
        this.hubId = hubId;
    }

    public String getPincodeCategory() {
        return pincodeCategory;
    }

    public void setPincodeCategory(String pincodeCategory) {
        this.pincodeCategory = pincodeCategory;
    }

    @Override
    public String toString() {
        return "HubAllocationDto [pincode=" + pincode + ", make=" + make + ", stateCode=" + stateCode + ", hubId=" + hubId + ", pincodeCategory=" + pincodeCategory + "]";
    }

}
