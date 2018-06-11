
package com.oneassist.serviceplatform.commons.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author
 */
@Entity
@Table(name = "OA_SERVICE_ADDRESS_DTL")
public class ServiceAddressEntity {

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERVICE_ADDRESS_DTL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "SERVICE_ADDRESS_ID")
	private Long				serviceAddressId;

	@Column(name = "ADDRESS_LINE_1")
	private String				addressLine1;

	@Column(name = "ADDRESS_LINE_2")
	private String				addressLine2;

	@Column(name = "LANDMARK")
	private String				landmark;

	@Column(name = "DISTRICT")
	private String				district;

	@Column(name = "MOBILE_NO")
	private BigDecimal			mobileNo;

	@Column(name = "PINCODE")
	private String				pincode;

	@Column(name = "COUNTRY_CODE")
	private String				countryCode;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date				createdOn;

	@Column(name = "CREATED_BY")
	private String				createdBy;

	@Column(name = "MODIFIED_ON")
	private Date				modifiedOn;

	@Column(name = "MODIFIED_BY")
	private String				modifiedBy;

	@Column(name = "ADDRESSEE_FULL_NAME")
	private String				addresseeFullName;

	@Column(name = "EMAIL")
	private String				email;

	public Long getServiceAddressId() {

		return serviceAddressId;
	}

	public void setServiceAddressId(Long serviceAddressId) {

		this.serviceAddressId = serviceAddressId;
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

	public BigDecimal getMobileNo() {

		return mobileNo;
	}

	public void setMobileNo(BigDecimal mobileNo) {

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

	public Date getCreatedOn() {

		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {

		this.createdOn = createdOn;
	}

	public String getCreatedBy() {

		return createdBy;
	}

	public void setCreatedBy(String createdBy) {

		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {

		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {

		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {

		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {

		this.modifiedBy = modifiedBy;
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
}