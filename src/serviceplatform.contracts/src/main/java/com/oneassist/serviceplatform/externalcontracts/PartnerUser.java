package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;
import java.util.Set;

public class PartnerUser implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1515291707215191612L;
    private Long partnerUserId;
    private Long partnerCode;
    private Long buCode;

    private String userFirstName;
    private String userMidName;
    private String userLastName;
    private String userEmailId;

    private String passKey;
    private Long userLandline;
    private Long userMobile;
    private String status;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String authorizedBy;
    private Date authorizedOn;
    private Set productCodes;

    public Long getBuCode() {
        return buCode;
    }

    public void setBuCode(Long buCode) {
        this.buCode = buCode;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public Set getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(Set productCodes) {
        this.productCodes = productCodes;
    }

    /**
     * @return the partnerUserId
     */
    public Long getPartnerUserId() {
        return partnerUserId;
    }

    /**
     * @param partnerUserId
     *            the partnerUserId to set
     */
    public void setPartnerUserId(Long partnerUserId) {
        this.partnerUserId = partnerUserId;
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
     * @return the userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName
     *            the userFirstName to set
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @return the userMidName
     */
    public String getUserMidName() {
        return userMidName;
    }

    /**
     * @param userMidName
     *            the userMidName to set
     */
    public void setUserMidName(String userMidName) {
        this.userMidName = userMidName;
    }

    /**
     * @return the userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName
     *            the userLastName to set
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     * @return the userEmailId
     */
    public String getUserEmailId() {
        return userEmailId;
    }

    /**
     * @param userEmailId
     *            the userEmailId to set
     */
    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    /**
     * @return the userLandline
     */
    public Long getUserLandline() {
        return userLandline;
    }

    /**
     * @param userLandline
     *            the userLandline to set
     */
    public void setUserLandline(Long userLandline) {
        this.userLandline = userLandline;
    }

    /**
     * @return the userMobile
     */
    public Long getUserMobile() {
        return userMobile;
    }

    /**
     * @param userMobile
     *            the userMobile to set
     */
    public void setUserMobile(Long userMobile) {
        this.userMobile = userMobile;
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
     * @return the authorizedBy
     */
    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * @param authorizedBy
     *            the authorizedBy to set
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    /**
     * @return the authorizedOn
     */
    public Date getAuthorizedOn() {
        return authorizedOn;
    }

    /**
     * @param authorizedOn
     *            the authorizedOn to set
     */
    public void setAuthorizedOn(Date authorizedOn) {
        this.authorizedOn = authorizedOn;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
