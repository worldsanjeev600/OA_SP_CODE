/*
 * 
 * This software is the confidential and proprietary information of oasys. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with Oasys Copyright (c) 2011 oasys. All Rights Reserved
 * 
 * @ClassName : PartnerBusinessUnit.java
 * 
 * @version : 1.0
 * 
 * @since : To day Date
 * 
 * @author : Ajaya Samanta
 * 
 * @see :
 * 
 * 
 * This class is used
 */
package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;
import java.util.List;

public class PartnerBusinessUnit implements java.io.Serializable, Comparable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private Long partnerBusinessUnitCode;
    private Long partnerCode;
    private String businessUnitCode;
    private String businessUnitName;
    private String corporateAltEmailId;
    private Long landline;
    private Long mobile;
    private Long fax;
    private String corporateEmailId;
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
    private Date authorisedOn;
    private String line1;
    private String line2;
    private String landmark;
    private String pincode;
    private String state;
    private String city;
    private String industry;
    private String businessPartnerType;
    private String businessUnitType;
    private Date createdStartDate;
    private Date createdEndDate;
    private List<String> businessUnitPlans;
    private String delFrom;
    private String partnerName;
    private String createdOnString;

    private String stateName;
    private String cityName;
    private String industryName;
    private String businessUnitTypeName;
    private String emailId;

    @Override
    public int compareTo(Object obj) {
        PartnerBusinessUnit p = (PartnerBusinessUnit) obj;
        return this.businessUnitName.compareTo(p.businessUnitName);
    }

    public Long getPartnerBusinessUnitCode() {
        return partnerBusinessUnitCode;
    }

    /**
     * @param partnerBusinessUnitCode
     *            the partnerBusinessUnitCode to set
     */
    public void setPartnerBusinessUnitCode(Long partnerBusinessUnitCode) {
        this.partnerBusinessUnitCode = partnerBusinessUnitCode;
    }

    /**
     * @return the partnerCode
     */
    public Long getPartnerCode() {
        return partnerCode;
    }

    /**
     * @param partnerCode
     *            the partnerCode to set
     */
    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    /**
     * @return the businessUnitCode
     */
    public String getBusinessUnitCode() {
        return businessUnitCode;
    }

    /**
     * @param businessUnitCode
     *            the businessUnitCode to set
     */
    public void setBusinessUnitCode(String businessUnitCode) {
        this.businessUnitCode = businessUnitCode;
    }

    /**
     * @return the businessUnitName
     */
    public String getBusinessUnitName() {
        return businessUnitName;
    }

    /**
     * @param businessUnitName
     *            the businessUnitName to set
     */
    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    /**
     * @return the landline
     */
    public Long getLandline() {
        return landline;
    }

    /**
     * @param landline
     *            the landline to set
     */
    public void setLandline(Long landline) {
        this.landline = landline;
    }

    /**
     * @return the mobile
     */
    public Long getMobile() {
        // return mobile;
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the fax
     */
    public Long getFax() {
        // return fax;
        return fax;
    }

    /**
     * @param fax
     *            the fax to set
     */
    public void setFax(Long fax) {
        this.fax = fax;
    }

    /**
     * @return the businessUnitStatus
     */
    public char getBusinessUnitStatus() {
        return businessUnitStatus;
    }

    /**
     * @param businessUnitStatus
     *            the businessUnitStatus to set
     */
    public void setBusinessUnitStatus(char businessUnitStatus) {
        this.businessUnitStatus = businessUnitStatus;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     *            the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdOn
     */

    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn
     *            the createdOn to set
     */

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy
     *            the modifiedBy to set
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modifiedOn
     */

    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn
     *            the modifiedOn to set
     */

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the authorisedBy
     */
    public String getAuthorisedBy() {
        return authorisedBy;
    }

    /**
     * @param authorisedBy
     *            the authorisedBy to set
     */
    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    /**
     * @return the authorisedOn
     */

    public Date getAuthorisedOn() {
        return authorisedOn;
    }

    /**
     * @param authorisedOn
     *            the authorisedOn to set
     */

    public void setAuthorisedOn(Date authorisedOn) {
        this.authorisedOn = authorisedOn;
    }

    /**
     * @return the line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     * @param line1
     *            the line1 to set
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     * @return the line2
     */
    public String getLine2() {
        return line2;
    }

    /**
     * @param line2
     *            the line2 to set
     */
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
     * @return the landmark
     */
    public String getLandmark() {
        return landmark;
    }

    /**
     * @param landmark
     *            the landmark to set
     */
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    /**
     * @return the pincode
     */
    public String getPincode() {
        return pincode;
    }

    /**
     * @param pincode
     *            the pincode to set
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the industry
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * @param industry
     *            the industry to set
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     * @return the businessPartnerType
     */
    public String getBusinessPartnerType() {
        return businessPartnerType;
    }

    /**
     * @param businessPartnerType
     *            the businessPartnerType to set
     */
    public void setBusinessPartnerType(String businessPartnerType) {
        this.businessPartnerType = businessPartnerType;
    }

    /**
     * @return the createdStartDate
     */

    public Date getCreatedStartDate() {
        return createdStartDate;
    }

    /**
     * @param createdStartDate
     *            the createdStartDate to set
     */

    public void setCreatedStartDate(Date createdStartDate) {
        this.createdStartDate = createdStartDate;
    }

    /**
     * @return the createdEndDate
     */

    public Date getCreatedEndDate() {
        return createdEndDate;
    }

    /**
     * @param createdEndDate
     *            the createdEndDate to set
     */

    public void setCreatedEndDate(Date createdEndDate) {
        this.createdEndDate = createdEndDate;
    }

    /**
     * @return the businessUnitPlans
     */
    public List<String> getBusinessUnitPlans() {
        return businessUnitPlans;
    }

    /**
     * @param businessUnitPlans
     *            the businessUnitPlans to set
     */
    public void setBusinessUnitPlans(List<String> businessUnitPlans) {
        this.businessUnitPlans = businessUnitPlans;
    }

    /**
     * @param businessUnitType
     *            the businessUnitType to set
     */
    public void setBusinessUnitType(String businessUnitType) {
        this.businessUnitType = businessUnitType;
    }

    /**
     * @return the businessUnitType
     */
    public String getBusinessUnitType() {
        return businessUnitType;
    }

    /**
     * @return the delFrom
     */
    public String getDelFrom() {
        return delFrom;
    }

    /**
     * @param delFrom
     *            the delFrom to set
     */
    public void setDelFrom(String delFrom) {
        this.delFrom = delFrom;
    }

    /**
     * @return the partnerName
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * @param partnerName
     *            the partnerName to set
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * @return the createdOnString
     */
    public String getCreatedOnString() {
        return createdOnString;
    }

    /**
     * @param createdOnString
     *            the createdOnString to set
     */
    public void setCreatedOnString(String createdOnString) {
        this.createdOnString = createdOnString;
    }

    /**
     * @return the stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * @param stateName
     *            the stateName to set
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * @return the cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName
     *            the cityName to set
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return the industryName
     */
    public String getIndustryName() {
        return industryName;
    }

    /**
     * @param industryName
     *            the industryName to set
     */
    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    /**
     * @return the businessUnitTypeName
     */
    public String getBusinessUnitTypeName() {
        return businessUnitTypeName;
    }

    /**
     * @param businessUnitTypeName
     *            the businessUnitTypeName to set
     */
    public void setBusinessUnitTypeName(String businessUnitTypeName) {
        this.businessUnitTypeName = businessUnitTypeName;
    }

    /**
     * @param corporateEmailId
     *            the corporateEmailId to set
     */
    public void setCorporateEmailId(String corporateEmailId) {
        this.corporateEmailId = corporateEmailId;
    }

    /**
     * @return the corporateAlternateEmailId
     */
    public String getCorporateAltEmailId() {
        return corporateAltEmailId;
    }

    /**
     * @param corporateAlternateEmailId
     *            the corporateAlternateEmailId to set
     */
    public void setCorporateAltEmailId(String corporateAltEmailId) {
        this.corporateAltEmailId = corporateAltEmailId;
    }

    /**
     * @return the corporateLandlineNumber
     */
    public long getCorporateLandlineNumber() {
        return corporateLandlineNumber;
    }

    /**
     * @param corporateLandlineNumber
     *            the corporateLandlineNumber to set
     */
    public void setCorporateLandlineNumber(long corporateLandlineNumber) {
        this.corporateLandlineNumber = corporateLandlineNumber;
    }

    /**
     * @return the corporateMobileNumber
     */
    public long getCorporateMobileNumber() {
        return corporateMobileNumber;
    }

    /**
     * @param corporateMobileNumber
     *            the corporateMobileNumber to set
     */
    public void setCorporateMobileNumber(long corporateMobileNumber) {
        this.corporateMobileNumber = corporateMobileNumber;
    }

    /**
     * @return the corporateFaxNumber
     */
    public long getCorporateFaxNumber() {
        return corporateFaxNumber;
    }

    /**
     * @param corporateFaxNumber
     *            the corporateFaxNumber to set
     */
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

    public String getCorporateEmailId() {
        return corporateEmailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
