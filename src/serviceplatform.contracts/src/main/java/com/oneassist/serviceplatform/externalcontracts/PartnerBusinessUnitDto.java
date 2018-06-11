package com.oneassist.serviceplatform.externalcontracts;

import java.sql.Timestamp;
import java.util.Date;

public class PartnerBusinessUnitDto {

    private long partnerBusinessUnitCode;
    private long partnerCode;
    private String businessUnitCode;
    private String businessUnitName;
    private String corporateEmailId;
    private String corporateAltEmailId;
    private long corporateLandlineNumber;
    private long corporateMobileNumber;
    private long corporateFaxNumber;
    private Long corporateLandlineNo;  // Added to avoid corporateLandlineNumber being set as null
    private Long corporateMobileNo;  // Added to avoid corporateMobileNumber being set as null
    private Long corporateFaxN0;  // Added to avoid corporateFaxNumber being set as null
    private char businessUnitStatus;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String authorisedBy;
    private Timestamp authorisedOn;
    private String city;
    private String businessUnitType;

    public long getPartnerBusinessUnitCode() {
        return partnerBusinessUnitCode;
    }

    public void setPartnerBusinessUnitCode(long partnerBusinessUnitCode) {
        this.partnerBusinessUnitCode = partnerBusinessUnitCode;
    }

    public long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getBusinessUnitCode() {
        return businessUnitCode;
    }

    public void setBusinessUnitCode(String businessUnitCode) {
        this.businessUnitCode = businessUnitCode;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public String getCorporateEmailId() {
        return corporateEmailId;
    }

    public void setCorporateEmailId(String corporateEmailId) {
        this.corporateEmailId = corporateEmailId;
    }

    public String getCorporateAltEmailId() {
        return corporateAltEmailId;
    }

    public void setCorporateAltEmailId(String corporateAltEmailId) {
        this.corporateAltEmailId = corporateAltEmailId;
    }

    public long getCorporateLandlineNumber() {
        return corporateLandlineNumber;
    }

    public void setCorporateLandlineNumber(long corporateLandlineNumber) {
        this.corporateLandlineNumber = corporateLandlineNumber;
    }

    public long getCorporateMobileNumber() {
        return corporateMobileNumber;
    }

    public void setCorporateMobileNumber(long corporateMobileNumber) {
        this.corporateMobileNumber = corporateMobileNumber;
    }

    public long getCorporateFaxNumber() {
        return corporateFaxNumber;
    }

    public void setCorporateFaxNumber(long corporateFaxNumber) {
        this.corporateFaxNumber = corporateFaxNumber;
    }

    public Long getCorporateLandlineNo() {
        return corporateLandlineNo;
    }

    public void setCorporateLandlineNo(Long corporateLandlineNo) {
        this.corporateLandlineNo = corporateLandlineNo;
    }

    public Long getCorporateMobileNo() {
        return corporateMobileNo;
    }

    public void setCorporateMobileNo(Long corporateMobileNo) {
        this.corporateMobileNo = corporateMobileNo;
    }

    public Long getCorporateFaxN0() {
        return corporateFaxN0;
    }

    public void setCorporateFaxN0(Long corporateFaxN0) {
        this.corporateFaxN0 = corporateFaxN0;
    }

    public char getBusinessUnitStatus() {
        return businessUnitStatus;
    }

    public void setBusinessUnitStatus(char businessUnitStatus) {
        this.businessUnitStatus = businessUnitStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public Timestamp getAuthorisedOn() {
        return authorisedOn;
    }

    public void setAuthorisedOn(Timestamp authorisedOn) {
        this.authorisedOn = authorisedOn;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBusinessUnitType() {

        return businessUnitType;
    }

    public void setBusinessUnitType(String businessUnitType) {

        this.businessUnitType = businessUnitType;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PartnerBusinessUnitDto [partnerBusinessUnitCode=");
		builder.append(partnerBusinessUnitCode);
		builder.append(", partnerCode=");
		builder.append(partnerCode);
		builder.append(", businessUnitCode=");
		builder.append(businessUnitCode);
		builder.append(", businessUnitName=");
		builder.append(businessUnitName);
		builder.append(", corporateEmailId=");
		builder.append(corporateEmailId);
		builder.append(", corporateAltEmailId=");
		builder.append(corporateAltEmailId);
		builder.append(", corporateLandlineNumber=");
		builder.append(corporateLandlineNumber);
		builder.append(", corporateMobileNumber=");
		builder.append(corporateMobileNumber);
		builder.append(", corporateFaxNumber=");
		builder.append(corporateFaxNumber);
		builder.append(", corporateLandlineNo=");
		builder.append(corporateLandlineNo);
		builder.append(", corporateMobileNo=");
		builder.append(corporateMobileNo);
		builder.append(", corporateFaxN0=");
		builder.append(corporateFaxN0);
		builder.append(", businessUnitStatus=");
		builder.append(businessUnitStatus);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", authorisedBy=");
		builder.append(authorisedBy);
		builder.append(", authorisedOn=");
		builder.append(authorisedOn);
		builder.append(", city=");
		builder.append(city);
		builder.append(", businessUnitType=");
		builder.append(businessUnitType);
		builder.append("]");
		return builder.toString();
	}
    
}
