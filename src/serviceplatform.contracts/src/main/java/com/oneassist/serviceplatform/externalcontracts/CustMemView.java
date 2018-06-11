/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;
import java.util.Date;

/**
 * @author piyush.purohit
 * 
 */
public class CustMemView implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private long accountNo;
    private long memId;
    private long custId;
    private String fName;
    private String mName;
    private String gender;

    // Added by jithesh - Start
    private String lName;
    private Date DOB;
    private long mobileNo;
    private String emailId;
    private String productName;
    private String planName;
    private String productType;
    // Added by jithesh - End

    private long orderId;
    private String prodCode;
    private long invoiceNo;
    private long partnerCode;
    private long trialDays;
    private double listPrice;
    private double discount;
    private double salesPrice;
    private String policyId;
    private Date startDate;
    private Date endDate;
    private Date lastBillingDate;
    private Date nextBillingtDate;
    private long planCode;
    private String promoCode;
    private double balanceAmt;
    private String paytChannelCode;
    private double refundAmt;

    private String renewalNo;
    private Date memCancDate;
    private char memStatus;
    private char renewalPerformed;
    private String primaryAcc;
    private String accReln;
    private String relnStatus;

    private String panCard;
    private String DLNo;
    private String passportNo;
    private String paymtFrequency;

    private String UID;
    private String DNDFlag;
    private Date aplnDate;
    private String srcChannelType;
    private String partnerBuCode;
    private String srcChannelCode;
    private String appFormNo;
    private String customerCategory;
    private String salutation;
    private char annuity;
    private long renewalWindow;
    private long graceDays;

    private String partnerInvoiceNo;

    private Date membershipModifiedDate;
    private String membershipModifiedBy;
    private String refundAmount;
    private String selected;
    private String status;
    private String renewalStatus;
    private char renewalFlag;
    private char anyPendingActivities;
    private Date memSince;
    private char downgradeAllowed;
    private String activityFlag;
    private String planStatus;
    private Long partneBuCodeL;
    private long partneBuCode;
    private Character isExtendedWarranty;
    private Date createdDate;
    private Character upgradeAllowed;
    private Double minInsuranceValue;

    private Double maxInsuranceValue;

    public Long getPartneBuCodeL() {
        if (getPartnerBuCode() != null)
            return Long.getLong(getPartnerBuCode());
        else
            return null;
    }

    public void setPartneBuCodeL(Long partneBuCodeL) {
        if (partneBuCodeL != null)
            setPartnerBuCode(partneBuCodeL.toString());
        this.partneBuCodeL = partneBuCodeL;
    }

    private Double handsetInsuranceValue; // This changes to apply
    // handsetminimum validation in R26.

    private Double handsetmininsurancevalue; // end

    private Double handsetInsuranceBuffer;

    private String isPremium;

    /* OAS-42 */
    private String anyPendingSubActivities;
    private String anyPendingActivityStatus;

    /* OAS-42 */

    public String getPartnerInvoiceNo() {
        return partnerInvoiceNo;
    }

    public void setPartnerInvoiceNo(String partnerInvoiceNo) {
        this.partnerInvoiceNo = partnerInvoiceNo;
    }

    /**
     * 
     * @return
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getMembershipModifiedDate() {
        return membershipModifiedDate;
    }

    public void setMembershipModifiedDate(Date membershipModifiedDate) {
        this.membershipModifiedDate = membershipModifiedDate;
    }

    public String getMembershipModifiedBy() {
        return membershipModifiedBy;
    }

    public void setMembershipModifiedBy(String membershipModifiedBy) {
        this.membershipModifiedBy = membershipModifiedBy;
    }

    public String getAppFormNo() {
        return appFormNo;
    }

    public void setAppFormNo(String appFormNo) {
        this.appFormNo = appFormNo;
    }

    public String getSrcChannelCode() {
        return srcChannelCode;
    }

    public void setSrcChannelCode(String srcChannelCode) {
        this.srcChannelCode = srcChannelCode;
    }

    public String getPartnerBuCode() {
        return partnerBuCode;
    }

    public void setPartnerBuCode(String partnerBuCode) {
        if (partnerBuCode != null /* && StringUtils.isNumeric(partnerBuCode) */) {
            this.partneBuCodeL = Long.valueOf(partnerBuCode);
            this.partneBuCode = partneBuCodeL.longValue();
        }
        this.partnerBuCode = partnerBuCode;
    }

    public Date getAplnDate() {
        return aplnDate;
    }

    public void setAplnDate(Date aplnDate) {
        this.aplnDate = aplnDate;
    }

    public String getSrcChannelType() {
        return srcChannelType;
    }

    public void setSrcChannelType(String srcChannelType) {
        this.srcChannelType = srcChannelType;
    }

    public String getDNDFlag() {
        return DNDFlag;
    }

    public void setDNDFlag(String dNDFlag) {
        DNDFlag = dNDFlag;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String uID) {
        UID = uID;
    }

    public String getPaymtFrequency() {
        return paymtFrequency;
    }

    public void setPaymtFrequency(String paymtFrequency) {
        this.paymtFrequency = paymtFrequency;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getDLNo() {
        return DLNo;
    }

    public void setDLNo(String dLNo) {
        DLNo = dLNo;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getPrimaryAcc() {
        return primaryAcc;
    }

    public void setPrimaryAcc(String primaryAcc) {
        this.primaryAcc = primaryAcc;
    }

    public String getAccReln() {
        return accReln;
    }

    public void setAccReln(String accReln) {
        this.accReln = accReln;
    }

    public String getRelnStatus() {
        return relnStatus;
    }

    public void setRelnStatus(String relnStatus) {
        this.relnStatus = relnStatus;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(long accountNo) {
        this.accountNo = accountNo;
    }

    public long getMemId() {
        return memId;
    }

    public void setMemId(long memId) {
        this.memId = memId;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public long getTrialDays() {
        return trialDays;
    }

    public void setTrialDays(long trialDays) {
        this.trialDays = trialDays;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastBillingDate() {
        return lastBillingDate;
    }

    public void setLastBillingDate(Date lastBillingDate) {
        this.lastBillingDate = lastBillingDate;
    }

    public Date getNextBillingtDate() {
        return nextBillingtDate;
    }

    public void setNextBillingtDate(Date nextBillingtDate) {
        this.nextBillingtDate = nextBillingtDate;
    }

    public long getPlanCode() {
        return planCode;
    }

    public void setPlanCode(long planCode) {
        this.planCode = planCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public String getPaytChannelCode() {
        return paytChannelCode;
    }

    public void setPaytChannelCode(String paytChannelCode) {
        this.paytChannelCode = paytChannelCode;
    }

    public double getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(double refundAmt) {
        this.refundAmt = refundAmt;
    }

    public char getMemStatus() {
        return memStatus;
    }

    public void setMemStatus(char memStatus) {
        this.memStatus = memStatus;
    }

    public char getRenewalPerformed() {
        return renewalPerformed;
    }

    public void setRenewalPerformed(char renewalPerformed) {
        this.renewalPerformed = renewalPerformed;
    }

    public String getRenewalNo() {
        return renewalNo;
    }

    public void setRenewalNo(String renewalNo) {
        this.renewalNo = renewalNo;
    }

    public Date getMemCancDate() {
        return memCancDate;
    }

    public void setMemCancDate(Date memCancDate) {
        this.memCancDate = memCancDate;
    }

    /**
     * @return the lName
     */
    public String getlName() {
        return lName;
    }

    /**
     * @param lName
     *            the lName to set
     */
    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getLName() {
        return lName;
    }

    /**
     * @param lName
     *            the lName to set
     */
    public void setLName(String lName) {
        this.lName = lName;
    }

    /**
     * @return the dOB
     */
    public Date getDOB() {
        return DOB;
    }

    /**
     * @param dOB
     *            the dOB to set
     */
    public void setDOB(Date dOB) {
        DOB = dOB;
    }

    /**
     * @return the mobileNo
     */
    public long getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo
     *            the mobileNo to set
     */
    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return the emailId
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * @param emailId
     *            the emailId to set
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the planName
     */
    public String getPlanName() {
        return planName;
    }

    /**
     * @param planName
     *            the planName to set
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public char getRenewalFlag() {
        return renewalFlag;
    }

    public void setRenewalFlag(char renewalFlag) {
        this.renewalFlag = renewalFlag;
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public Date getMemSince() {
        return memSince;
    }

    public void setMemSince(Date memSince) {
        this.memSince = memSince;
    }

    public char getDowngradeAllowed() {
        return downgradeAllowed;
    }

    public void setDowngradeAllowed(char downgradeAllowed) {
        this.downgradeAllowed = downgradeAllowed;
    }

    public String getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }

    public char getAnyPendingActivities() {
        return anyPendingActivities;
    }

    public void setAnyPendingActivities(char anyPendingActivities) {
        this.anyPendingActivities = anyPendingActivities;
    }

    public char getAnnuity() {
        return annuity;
    }

    public void setAnnuity(char annuity) {
        this.annuity = annuity;
    }

    public String getIsPremium() {

        return isPremium;
    }

    public void setIsPremium(String isPremium) {

        this.isPremium = isPremium;
    }

    public long getRenewalWindow() {
        return renewalWindow;
    }

    public void setRenewalWindow(long renewalWindow) {
        this.renewalWindow = renewalWindow;
    }

    public long getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(long graceDays) {
        this.graceDays = graceDays;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public long getPartneBuCode() {

        return partneBuCode;
    }

    public void setPartneBuCode(long partneBuCode) {
        if (partneBuCode == 0) {
            partneBuCodeL = null;
        } else {
            setPartneBuCodeL(partneBuCode);

        }
    }

    public Character getIsExtendedWarranty() {
        return isExtendedWarranty;
    }

    public void setIsExtendedWarranty(Character isExtendedWarranty) {
        this.isExtendedWarranty = isExtendedWarranty;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAnyPendingSubActivities() {
        return anyPendingSubActivities;
    }

    public void setAnyPendingSubActivities(String anyPendingSubActivities) {
        this.anyPendingSubActivities = anyPendingSubActivities;
    }

    public String getAnyPendingActivityStatus() {
        return anyPendingActivityStatus;
    }

    public void setAnyPendingActivityStatus(String anyPendingActivityStatus) {
        this.anyPendingActivityStatus = anyPendingActivityStatus;
    }

    public void setMinInsuranceValue(Double minInsuranceValue) {
        this.minInsuranceValue = minInsuranceValue;
    }

    public Double getMaxInsuranceValue() {
        return maxInsuranceValue;
    }

    public void setMaxInsuranceValue(Double maxInsuranceValue) {
        this.maxInsuranceValue = maxInsuranceValue;
    }

    public Double getMinInsuranceValue() {
        return minInsuranceValue;
    }

    public Character getUpgradeAllowed() {
        return upgradeAllowed;
    }

    public void setUpgradeAllowed(Character upgradeAllowed) {
        this.upgradeAllowed = upgradeAllowed;
    }

    public Double getHandsetInsuranceValue() {
        return handsetInsuranceValue;
    }

    public void setHandsetInsuranceValue(Double handsetInsuranceValue) {
        this.handsetInsuranceValue = handsetInsuranceValue;
    }

    public Double getHandsetmininsurancevalue() {
        return handsetmininsurancevalue;
    }

    public void setHandsetmininsurancevalue(Double handsetmininsurancevalue) {
        this.handsetmininsurancevalue = handsetmininsurancevalue;
    }

    public Double getHandsetInsuranceBuffer() {
        return handsetInsuranceBuffer;
    }

    public void setHandsetInsuranceBuffer(Double handsetInsuranceBuffer) {
        this.handsetInsuranceBuffer = handsetInsuranceBuffer;
    }

    /**
     * @return productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType
     *            to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }
}
