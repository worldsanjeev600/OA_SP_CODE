package com.oneassist.serviceplatform.contracts.dtos.servicerequest.response;

import java.util.List;

public class PincodeServicabilityResponse {

    private String pincode;
    private String isPincodeServicable;
    private List<String> supportedRequestTypes;
    private Long hubId;
    private String isCourtesyApplicable;
    private String pincodeCategory;
    private String stateCode;
    private String cityCode;
    private String cityName;
    private String stateName;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIsPincodeServicable() {
        return isPincodeServicable;
    }

    public void setIsPincodeServicable(String isPincodeServicable) {
        this.isPincodeServicable = isPincodeServicable;
    }

    public List<String> getSupportedRequestTypes() {

        return supportedRequestTypes;
    }

    public void setSupportedRequestTypes(List<String> supportedRequestTypes) {

        this.supportedRequestTypes = supportedRequestTypes;
    }

    
    public Long getHubId() {
        return hubId;
    }

    
    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    
    public String getIsCourtesyApplicable() {
        return isCourtesyApplicable;
    }

    
    public void setIsCourtesyApplicable(String isCourtesyApplicable) {
        this.isCourtesyApplicable = isCourtesyApplicable;
    }

	public String getPincodeCategory() {
		return pincodeCategory;
	}

	public void setPincodeCategory(String pincodeCategory) {
		this.pincodeCategory = pincodeCategory;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
}
