package com.oneassist.serviceplatform.externalcontracts.dhl;


public class DHLAddressRequestDto {
	private String name;
	private String line1;
	private String city;
	private String state;
	private Integer pincode;
	private String mobile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "DHLAddressRequestDto [name=" + name + ", line1=" + line1 + ", city=" + city + ", state=" + state
				+ ", pincode=" + pincode + ", mobile=" + mobile + "]";
	}
	
}
