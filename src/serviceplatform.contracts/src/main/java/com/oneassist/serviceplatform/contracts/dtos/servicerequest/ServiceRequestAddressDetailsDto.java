package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Service Request Address Detail Model")
public class ServiceRequestAddressDetailsDto extends BaseAuditDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addressId;
	
	private String addressType;
	
	@ApiModelProperty(value="Service Request Address Full Name",required = true)
	private String	addresseeFullName;
	
	@ApiModelProperty(value="Customer Email Address",required = true)
	private String	email;
	
	@ApiModelProperty(value="Customer Address Line-1",required = true)
	private String	addressLine1;
	
	@ApiModelProperty(value="Customer Address Line-2",required = true)
	private String	addressLine2;
	
	@ApiModelProperty(value="Customer Address Landmark",required = true)
	private String	landmark;
	
	@ApiModelProperty(value="Customer Address District",required = true)
	private String	district;
	
	@ApiModelProperty(value="Customer Mobile Number",required = true)
	private Long	mobileNo;
	
	@ApiModelProperty(value="Customer Pincode",required = true)
	private String	pincode;
	
	@ApiModelProperty(value="Customer Country code",required = true)
	private String	countryCode;
	
	private String state;
	
	private String city;
	
	public String getAddresseeFullName() {	
		return addresseeFullName;
	}
	
	public void setAddresseeFullName(String addresseeFullName) {	
		this.addresseeFullName = addresseeFullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddressLine1() {
		return addressLine1;
	}
	
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	public String getAddressLine2() {
		return addressLine2;
	}
	
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	public String getLandmark() {
		return landmark;
	}
	
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	
	public String getDistrict() {
		return district;
	}
	
	public void setDistrict(String district) {
		this.district = district;
	}
	
	public Long getMobileNo() {
		return mobileNo;
	}
	
	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPincode() {
		return pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {	
		this.countryCode = countryCode;
	}

	
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	
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

	@Override
	public String toString() {
		return "ServiceRequestAddressDetailsDto [addresseeFullName=" + addresseeFullName + ", email=" + email
				+ ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", landmark=" + landmark
				+ ", district=" + district + ", mobileNo=" + mobileNo + ", pincode=" + pincode + ", countryCode="
				+ countryCode + "]";
	}
	
}
