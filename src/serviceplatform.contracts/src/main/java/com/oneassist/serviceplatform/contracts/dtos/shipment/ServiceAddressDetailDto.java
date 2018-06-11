
package com.oneassist.serviceplatform.contracts.dtos.shipment;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */

@ApiModel(description="Address Detail Model")
public class ServiceAddressDetailDto extends BaseAuditDto {

	private static final long serialVersionUID = -2514000736298954919L;

	private Long serviceAddressId;
	
	@ApiModelProperty(value="Address Line-1",required=true)
	private String addressLine1 ;
	
	@ApiModelProperty(value="Address Line-2")
	private String addressLine2;
	
	@ApiModelProperty(value="Landmark")
	private String landmark;
	
	@ApiModelProperty(value="District")
	private String district;
	
	@ApiModelProperty(value="Mobile Number",required=true)
	private Long mobileNo ;     
	
	@ApiModelProperty(value="Pincode",required=true)
	private String pincode;
	
	@ApiModelProperty(value="Country Code")
	private String countryCode;
	
	@ApiModelProperty(value="Addressess Full Name")
	private String addresseeFullName;
	
	@ApiModelProperty(value="Email ID")
	private String email;
	
	@ApiModelProperty(value="State")
	private String state;
	
	public Long getServiceAddressId() {
		return serviceAddressId;
	}
	public void setServiceAddressId(Long serviceAddressId) {
		this.serviceAddressId = serviceAddressId;
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
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "ServiceAddressDetailDto [serviceAddressId=" + serviceAddressId + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", landmark=" + landmark + ", district=" + district + ", mobileNo="
				+ mobileNo + ", pincode=" + pincode + ", countryCode=" + countryCode + ", addresseeFullName="
				+ addresseeFullName + ", email=" + email + ", state=" + state + "]";
	}
	
}

