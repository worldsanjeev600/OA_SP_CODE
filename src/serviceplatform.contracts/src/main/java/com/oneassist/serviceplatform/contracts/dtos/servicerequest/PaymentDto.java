package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class PaymentDto {
	private int chargeId;
	private Double adviceAmount;
	private String membershipId;
	private String adviceId;
	private String accountNumber;
	private int partnerType;
	private String chargeType;
	private String adviceDate;
	private Double transactionAmount;
	private String orderId;
	private String status;
	private String transactionDate;
	
	public int getChargeId() {
		return chargeId;
	}
	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}
	public Double getAdviceAmount() {
		return adviceAmount;
	}
	public void setAdviceAmount(Double adviceAmount) {
		this.adviceAmount = adviceAmount;
	}
	public String getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}
	public String getAdviceId() {
		return adviceId;
	}
	public void setAdviceId(String adviceId) {
		this.adviceId = adviceId;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(int partnerType) {
		this.partnerType = partnerType;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public String getAdviceDate() {
		return adviceDate;
	}
	public void setAdviceDate(String adviceDate) {
		this.adviceDate = adviceDate;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	@Override
	public String toString() {
		return "AdviceDto [chargeId=" + chargeId + ", adviceAmount=" + adviceAmount + ", membershipId=" + membershipId
				+ ", adviceId=" + adviceId + ", accountNumber=" + accountNumber + ", partnerType=" + partnerType
				+ ", chargeType=" + chargeType + ", adviceDate=" + adviceDate + ", transactionAmount="
				+ transactionAmount + ", orderId=" + orderId + ", status=" + status + ", transactionDate="
				+ transactionDate + "]";
	}
	
	
}
