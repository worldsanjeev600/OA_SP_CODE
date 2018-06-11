package com.oneassist.serviceplatform.contracts.response;

public class StateCityResponseDto {
	
	private String pinCode;
	private String cityCode;
	private String cityName;
	private String stateCode;
	private String stateName;
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
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
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	@Override
	public String toString() {
		return "StateCityResponseDto [pinCode=" + pinCode + ", cityCode=" + cityCode + ", cityName=" + cityName
				+ ", stateCode=" + stateCode + ", stateName=" + stateName + "]";
	}
}
