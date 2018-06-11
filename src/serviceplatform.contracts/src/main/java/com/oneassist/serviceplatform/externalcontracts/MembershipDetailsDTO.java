/*
 * 
 * This software is the confidential and proprietary information of oasys. 
 * ("Confidential Information").  You shall not disclose such Confidential Information
 *  and shall use it only in accordance with the terms of the license agreement you 
 *  entered into with Oasys Copyright (c) 2011 oasys.  All Rights    
 *  Reserved
 *
 * @ClassName  : UserAction.java
 * @version    : 1.0
 * @since      : To day Date
 * @author     : Srinivasa Krishna Mantha
 * @see        : 
 *
 * 
 * This class is used
 * 
 *
 * 
 */
package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;
import java.util.List;

public class MembershipDetailsDTO implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3534044807427014919L;

	/**
	 * 
	 */
	private long orderId;
	private long partnerCode;
	private long memNo;
	private long planId;	
	private long accountNo;
	private int createdBy;
	private String memStatus;
	private Date startDate;
	private Date endDate;
	private Date lastBillingDate;
	private Date nextBillingDate;
	private Date membershipCancellationDate;
	private String srcChannelCode;
	private String prodCode;
	private long planCode;
	private double listPrice;
	private double salesPrice;
	private Date memStartDate;
	private Date memEndDate;
	private long price;
	private long accNum;
	private long invoiceNo;
	private long trialDays;
	private double discount;
	private long balanceAmt;
	private long refundAmt;
	private String renewalPerformed;
	private String promoCode;
	private String renewalNo;
	private String policyId;
	private String updateReason;
	private String cancelReason;
	private	String planFreq;
	private double taxAmt;
	private String taxCode;
	private long graceDays;
	private Date modifiedOn;
	private String modifiedBy;
	
	private String trial;
	private String discountAllowed;
	private String downGradeAllowed;
	private String addedBy;
	private Date createdDate;
	private String dndFlag;
	private String crmPropgtnFlg;

	private String level1;
	private String level2;
	private String level3;
	private char  welcomeKitStatus;
	private String applicationNumber;
	private Date appDate;
	
	private List<HomeApplianceDetailDTO> assetDetails;
		
	
	
	
	public List<HomeApplianceDetailDTO> getAssetDetails() {
		return assetDetails;
	}
	public void setAssetDetails(List<HomeApplianceDetailDTO> assetDetails) {
		this.assetDetails = assetDetails;
	}
	/**
	 * @return the applicationNumber
	 */
	public String getApplicationNumber() {
		return applicationNumber;
	}
	/**
	 * @param applicationNumber the applicationNumber to set
	 */
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	/**
	 * @return the appDate
	 */
	public Date getAppDate() {
		return appDate;
	}
	/**
	 * @param appDate the appDate to set
	 */
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	/**
	 * @return the welcomeKitStatus
	 */
	public char getWelcomeKitStatus() {
		return welcomeKitStatus;
	}
	/**
	 * @param welcomeKitStatus the welcomeKitStatus to set
	 */
	public void setWelcomeKitStatus(char welcomeKitStatus) {
		this.welcomeKitStatus = welcomeKitStatus;
	}
	/**
	 * @return the level1
	 */
	public String getLevel1() {
		return level1;
	}
	/**
	 * @param level1 the level1 to set
	 */
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	/**
	 * @return the level2
	 */
	public String getLevel2() {
		return level2;
	}
	/**
	 * @param level2 the level2 to set
	 */
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	/**
	 * @return the level3
	 */
	public String getLevel3() {
		return level3;
	}
	/**
	 * @param level3 the level3 to set
	 */
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
    
	
	public String getCrmPropgtnFlg() {
		return crmPropgtnFlg;
	}

	public void setCrmPropgtnFlg(String crmPropgtnFlg) {
		this.crmPropgtnFlg = crmPropgtnFlg;
	}
	public String getDndFlag() {
		return dndFlag;
	}

	public void setDndFlag(String dndFlag) {
		this.dndFlag = dndFlag;
	}

	public String getTrial() {
		return trial;
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}

	public String getDiscountAllowed() {
		return discountAllowed;
	}

	public void setDiscountAllowed(String discountAllowed) {
		this.discountAllowed = discountAllowed;
	}

	public String getDownGradeAllowed() {
		return downGradeAllowed;
	}

	public void setDownGradeAllowed(String downGradeAllowed) {
		this.downGradeAllowed = downGradeAllowed;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	public long getGraceDays() {
		return graceDays;
	}

	public void setGraceDays(long graceDays) {
		this.graceDays = graceDays;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	private String chargeType;
	public String getUpdateReason() {
		return updateReason;
	}

	public void setUpdateReason(String updateReason) {
		this.updateReason = updateReason;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	
	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getRenewalNo() {
		return renewalNo;
	}

	public void setRenewalNo(String renewalNo) {
		this.renewalNo = renewalNo;
	}

	public Date getLastBillingDate() {
		return lastBillingDate;
	}

	public void setLastBillingDate(Date lastBillingDate) {
		this.lastBillingDate = lastBillingDate;
	}

	public long getPrice() {
		return price;
	}

	public Date getNextBillingDate() {
		return nextBillingDate;
	}
	public void setPrice(long price) {
		this.price = price;
	}

	public void setNextBillingDate(Date nextBillingDate) {
		this.nextBillingDate = nextBillingDate;
	}

	public long getAccNum() {
		return accNum;
	}

	public void setAccNum(long accNum) {
		this.accNum = accNum;
	}

	public Date getMemStartDate() {
		return memStartDate;
	}

	public void setMemStartDate(Date memStartDate) {
		this.memStartDate = memStartDate;
	}

	public Date getMemEndDate() {
		return memEndDate;
	}

	public void setMemEndDate(Date memEndDate) {
		this.memEndDate = memEndDate;
	}

	public String getSrcChannelCode() {
		return srcChannelCode;
	}

	public void setSrcChannelCode(String srcChannelCode) {
		this.srcChannelCode = srcChannelCode;
	}

	public long getPlanCode() {
		return planCode;
	}

	public void setPlanCode(long planCode) {
		this.planCode = planCode;
	}

	


	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}



public Date getMembershipCancellationDate() {
		return membershipCancellationDate;
	}

	public void setMembershipCancellationDate(Date membershipCancellationDate) {
		this.membershipCancellationDate = membershipCancellationDate;
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

	/**
	 * @return the createdBy
	 */
	

	public String getMemStatus() {
		return memStatus;
	}

	public void setMemStatus(String memStatus) {
		this.memStatus = memStatus;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	


	/**
	 * @return the createdBy
	 */
	public int getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the memId
	 */
	
	/**
	 * @return the memNo
	 */
 public long getMemNo() {
		return memNo;
	}

	
	/** @param memNo the memNo to set*/
	 
	public void setMemNo(long memNo) {
		this.memNo = memNo;
	}

	/**
	 * @return the planId
	 */


	/**
	 * @return the prodId
	 */

	
	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	/**
	 * @return the accountNo
	 */
	public long getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the invoiceNo
	 */
	public long getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/**
	 * @return the partnerCode
	 */
	public long getPartnerCode() {
		return partnerCode;
	}

	/**
	 * @param partnerCode the partnerCode to set
	 */
	public void setPartnerCode(long partnerCode) {
		this.partnerCode = partnerCode;
	}


	/**
	 * @return the balanceAmt
	 */
	public long getBalanceAmt() {
		return balanceAmt;
	}

	/**
	 * @param balanceAmt the balanceAmt to set
	 */
	public void setBalanceAmt(long balanceAmt) {
		this.balanceAmt = balanceAmt;
	}

	/**
	 * @return the refundAmt
	 */
	public long getRefundAmt() {
		return refundAmt;
	}

	/**
	 * @param refundAmt the refundAmt to set
	 */
	public void setRefundAmt(long refundAmt) {
		this.refundAmt = refundAmt;
	}

	/**
	 * @return the trailDays
	 */
	public long getTrialDays() {
		return trialDays;
	}

	/**
	 * @param trailDays the trailDays to set
	 */
	public void setTrialDays(long trialDays) {
		this.trialDays = trialDays;
	}

	/**
	 * @return the renewalPerformed
	 */
	public String getRenewalPerformed() {
		return renewalPerformed;
	}

	/**
	 * @param renewalPerformed the renewalPerformed to set
	 */
	public void setRenewalPerformed(String renewalPerformed) {
		this.renewalPerformed = renewalPerformed;
	}

	/**
	 * @return the promoCode
	 */
	public String getPromoCode() {
		return promoCode;
	}

	/**
	 * @param promoCode the promoCode to set
	 */
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getPlanFreq() {
		return planFreq;
	}

	public void setPlanFreq(String planFreq) {
		this.planFreq = planFreq;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MembershipDetailsDTO [orderId=");
		builder.append(orderId);
		builder.append(", partnerCode=");
		builder.append(partnerCode);
		builder.append(", memNo=");
		builder.append(memNo);
		builder.append(", planId=");
		builder.append(planId);
		builder.append(", accountNo=");
		builder.append(accountNo);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", memStatus=");
		builder.append(memStatus);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", lastBillingDate=");
		builder.append(lastBillingDate);
		builder.append(", nextBillingDate=");
		builder.append(nextBillingDate);
		builder.append(", membershipCancellationDate=");
		builder.append(membershipCancellationDate);
		builder.append(", srcChannelCode=");
		builder.append(srcChannelCode);
		builder.append(", prodCode=");
		builder.append(prodCode);
		builder.append(", planCode=");
		builder.append(planCode);
		builder.append(", listPrice=");
		builder.append(listPrice);
		builder.append(", salesPrice=");
		builder.append(salesPrice);
		builder.append(", memStartDate=");
		builder.append(memStartDate);
		builder.append(", memEndDate=");
		builder.append(memEndDate);
		builder.append(", price=");
		builder.append(price);
		builder.append(", accNum=");
		builder.append(accNum);
		builder.append(", invoiceNo=");
		builder.append(invoiceNo);
		builder.append(", trialDays=");
		builder.append(trialDays);
		builder.append(", discount=");
		builder.append(discount);
		builder.append(", balanceAmt=");
		builder.append(balanceAmt);
		builder.append(", refundAmt=");
		builder.append(refundAmt);
		builder.append(", renewalPerformed=");
		builder.append(renewalPerformed);
		builder.append(", promoCode=");
		builder.append(promoCode);
		builder.append(", renewalNo=");
		builder.append(renewalNo);
		builder.append(", policyId=");
		builder.append(policyId);
		builder.append(", updateReason=");
		builder.append(updateReason);
		builder.append(", cancelReason=");
		builder.append(cancelReason);
		builder.append(", planFreq=");
		builder.append(planFreq);
		builder.append(", taxAmt=");
		builder.append(taxAmt);
		builder.append(", taxCode=");
		builder.append(taxCode);
		builder.append(", graceDays=");
		builder.append(graceDays);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", trial=");
		builder.append(trial);
		builder.append(", discountAllowed=");
		builder.append(discountAllowed);
		builder.append(", downGradeAllowed=");
		builder.append(downGradeAllowed);
		builder.append(", addedBy=");
		builder.append(addedBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", dndFlag=");
		builder.append(dndFlag);
		builder.append(", crmPropgtnFlg=");
		builder.append(crmPropgtnFlg);
		builder.append(", level1=");
		builder.append(level1);
		builder.append(", level2=");
		builder.append(level2);
		builder.append(", level3=");
		builder.append(level3);
		builder.append(", welcomeKitStatus=");
		builder.append(welcomeKitStatus);
		builder.append(", applicationNumber=");
		builder.append(applicationNumber);
		builder.append(", appDate=");
		builder.append(appDate);
		builder.append(", assetDetails=");
		builder.append(assetDetails);
		builder.append(", chargeType=");
		builder.append(chargeType);
		builder.append("]");
		return builder.toString();
	}
}
