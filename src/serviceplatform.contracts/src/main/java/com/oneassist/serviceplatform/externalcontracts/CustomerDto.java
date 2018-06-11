package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;

public class CustomerDto implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1854636358662771650L;
    private long custId;
    private String vipFlag;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String dob;
    private String salutation;
    private String nationality;
    private String pancard;
    private String dlNo;
    private String passport;
    private String voterId;
    private String aadharId;
    private String email;
    private String rmCode;
    private String crmId;
    private String emailId2;
    private long contactNo;
    private String emgContact1;
    private String emgContact2;
    private long emgNumber1;
    private long emgNumber2;

    private String fraudStatus;

    private Date dateLastUpdated;

    private String createdBy;
    private Date createdDate;
    private String modifiedBy;

    private Date modifiedDate;
    private String contactFlag;
    private long telNo;
    private String dndFlag;
    private String isDedupe;
    private Date dedupePerformedOn;
    private String customerCategory;

    // Email Bounce check flag
    private Boolean isMailBounced;
    private Long mobile2;

    /**
     * @return the dedupePerformedOn
     */
    public Date getDedupePerformedOn() {
        return dedupePerformedOn;
    }

    /**
     * @param dedupePerformedOn
     *            the dedupePerformedOn to set
     */
    public void setDedupePerformedOn(Date dedupePerformedOn) {
        this.dedupePerformedOn = dedupePerformedOn;
    }

    public String getDndFlag() {
        return dndFlag;
    }

    public void setDndFlag(String dndFlag) {
        this.dndFlag = dndFlag;
    }

    public String getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(String vipFlag) {
        this.vipFlag = vipFlag;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getPancard() {
        return pancard;
    }

    public void setPancard(String pancard) {
        this.pancard = pancard;
    }

    public String getDlNo() {
        return dlNo;
    }

    public void setDlNo(String dlNo) {
        this.dlNo = dlNo;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getAadharId() {
        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }

    public String getRmCode() {
        return rmCode;
    }

    public void setRmCode(String rmCode) {
        this.rmCode = rmCode;
    }

    public String getEmailId2() {
        return emailId2;
    }

    public void setEmailId2(String emailId2) {
        this.emailId2 = emailId2;
    }

    public String getEmgContact1() {
        return emgContact1;
    }

    public void setEmgContact1(String emgContact1) {
        this.emgContact1 = emgContact1;
    }

    public String getEmgContact2() {
        return emgContact2;
    }

    public void setEmgContact2(String emgContact2) {
        this.emgContact2 = emgContact2;
    }

    public long getEmgNumber1() {
        return emgNumber1;
    }

    public void setEmgNumber1(long emgNumber1) {
        this.emgNumber1 = emgNumber1;
    }

    public long getEmgNumber2() {
        return emgNumber2;
    }

    public void setEmgNumber2(long emgNumber2) {
        this.emgNumber2 = emgNumber2;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * @return the createdBy
     */
    public long getCustId() {
        return custId;
    }

    /**
     * @param createdBy
     *            the createdBy to set
     */
    public void setCustId(long custId) {
        this.custId = custId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName
     *            the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob
     *            the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality
     *            the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the contactNo
     */
    public long getContactNo() {
        return contactNo;
    }

    /**
     * @param contactNo
     *            the contactNo to set
     */
    public void setContactNo(long contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * @return the contactFlag
     */
    public String getContactFlag() {
        return contactFlag;
    }

    /**
     * @param contactFlag
     *            the contactFlag to set
     */
    public void setContactFlag(String contactFlag) {
        this.contactFlag = contactFlag;
    }

    /**
     * @return the telNo
     */
    public long getTelNo() {
        return telNo;
    }

    /**
     * @param telNo
     *            the telNo to set
     */
    public void setTelNo(long telNo) {
        this.telNo = telNo;
    }

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    /**
     * @return the isDedupe
     */
    public String getIsDedupe() {
        return isDedupe;
    }

    /**
     * @param isDedupe
     *            the isDedupe to set
     */
    public void setIsDedupe(String isDedupe) {
        this.isDedupe = isDedupe;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public Boolean getIsMailBounced() {
        return isMailBounced;
    }

    public void setIsMailBounced(Boolean isMailBounced) {
        this.isMailBounced = isMailBounced;
    }

    public Long getMobile2() {
        return mobile2;
    }

    public void setMobile2(Long mobile2) {
        this.mobile2 = mobile2;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDto [custId=");
		builder.append(custId);
		builder.append(", vipFlag=");
		builder.append(vipFlag);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", middleName=");
		builder.append(middleName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", salutation=");
		builder.append(salutation);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", pancard=");
		builder.append(pancard);
		builder.append(", dlNo=");
		builder.append(dlNo);
		builder.append(", passport=");
		builder.append(passport);
		builder.append(", voterId=");
		builder.append(voterId);
		builder.append(", aadharId=");
		builder.append(aadharId);
		builder.append(", email=");
		builder.append(email);
		builder.append(", rmCode=");
		builder.append(rmCode);
		builder.append(", crmId=");
		builder.append(crmId);
		builder.append(", emailId2=");
		builder.append(emailId2);
		builder.append(", contactNo=");
		builder.append(contactNo);
		builder.append(", emgContact1=");
		builder.append(emgContact1);
		builder.append(", emgContact2=");
		builder.append(emgContact2);
		builder.append(", emgNumber1=");
		builder.append(emgNumber1);
		builder.append(", emgNumber2=");
		builder.append(emgNumber2);
		builder.append(", fraudStatus=");
		builder.append(fraudStatus);
		builder.append(", dateLastUpdated=");
		builder.append(dateLastUpdated);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", contactFlag=");
		builder.append(contactFlag);
		builder.append(", telNo=");
		builder.append(telNo);
		builder.append(", dndFlag=");
		builder.append(dndFlag);
		builder.append(", isDedupe=");
		builder.append(isDedupe);
		builder.append(", dedupePerformedOn=");
		builder.append(dedupePerformedOn);
		builder.append(", customerCategory=");
		builder.append(customerCategory);
		builder.append(", isMailBounced=");
		builder.append(isMailBounced);
		builder.append(", mobile2=");
		builder.append(mobile2);
		builder.append("]");
		return builder.toString();
	}
    
}
