package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;
import java.util.List;

public class PlanMasterDto {

    private String planCode;
    private String planName;
    private String planDesc;
    private String productCode;
    private String frequency;
    private char annuity;
    private char backedByInsurance;
    private long freeLookin;
    private long graceDays;
    private char refundAllowed;
    private double price;
    private String taxcode;
    private char downgrade;
    private char discountAllowed;
    private char trail;
    private long quantity;
    private String planStatus;
    private String addedBy;
    private Date addedOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String authorisedBy;
    private Date authorisedOn;
    private String taxName;
    private String taxValue;
    private double totalAmount;
    private double taxAmount;
    private double graceAmount;
    private String planServiceName;
    private Double minInsuranceValue;
    private Double maxInsuranceValue;
    private List<String> products;
    private long numberOfDevices;

    /**
     * @return the planCode
     */
    public String getPlanCode() {
        return planCode;
    }

    /**
     * @param planCode
     *            the planCode to set
     */
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
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

    /**
     * @return the planDesc
     */
    public String getPlanDesc() {
        return planDesc;
    }

    /**
     * @param planDesc
     *            the planDesc to set
     */
    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode
     *            the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param frequency
     *            the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the annuity
     */
    public char getAnnuity() {
        return annuity;
    }

    /**
     * @param annuity
     *            the annuity to set
     */
    public void setAnnuity(char annuity) {
        this.annuity = annuity;
    }

    /**
     * @return the backedByInsurance
     */
    public char getBackedByInsurance() {
        return backedByInsurance;
    }

    /**
     * @param backedByInsurance
     *            the backedByInsurance to set
     */
    public void setBackedByInsurance(char backedByInsurance) {
        this.backedByInsurance = backedByInsurance;
    }

    /**
     * @return the freeLookin
     */
    public long getFreeLookin() {
        return freeLookin;
    }

    /**
     * @param freeLookin
     *            the freeLookin to set
     */
    public void setFreeLookin(long freeLookin) {
        this.freeLookin = freeLookin;
    }

    /**
     * @return the graceDays
     */
    public long getGraceDays() {
        return graceDays;
    }

    /**
     * @param graceDays
     *            the graceDays to set
     */
    public void setGraceDays(long graceDays) {
        this.graceDays = graceDays;
    }

    /**
     * @return the refundAllowed
     */
    public char getRefundAllowed() {
        return refundAllowed;
    }

    /**
     * @param refundAllowed
     *            the refundAllowed to set
     */
    public void setRefundAllowed(char refundAllowed) {
        this.refundAllowed = refundAllowed;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the taxcode
     */
    public String getTaxcode() {
        return taxcode;
    }

    /**
     * @param taxcode
     *            the taxcode to set
     */
    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    /**
     * @return the downgrade
     */
    public char getDowngrade() {
        return downgrade;
    }

    /**
     * @param downgrade
     *            the downgrade to set
     */
    public void setDowngrade(char downgrade) {
        this.downgrade = downgrade;
    }

    /**
     * @return the discountAllowed
     */
    public char getDiscountAllowed() {
        return discountAllowed;
    }

    /**
     * @param discountAllowed
     *            the discountAllowed to set
     */
    public void setDiscountAllowed(char discountAllowed) {
        this.discountAllowed = discountAllowed;
    }

    /**
     * @return the trail
     */
    public char getTrail() {
        return trail;
    }

    /**
     * @param trail
     *            the trail to set
     */
    public void setTrail(char trail) {
        this.trail = trail;
    }

    /**
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the planStatus
     */
    public String getPlanStatus() {
        return planStatus;
    }

    /**
     * @param planStatus
     *            the planStatus to set
     */
    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    /**
     * @return the addedBy
     */
    public String getAddedBy() {
        return addedBy;
    }

    /**
     * @param addedBy
     *            the addedBy to set
     */
    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    /**
     * @return the addedOn
     */
    public Date getAddedOn() {
        return addedOn;
    }

    /**
     * @param addedOn
     *            the addedOn to set
     */
    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
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
     * @return the taxName
     */
    public String getTaxName() {
        return taxName;
    }

    /**
     * @param taxName
     *            the taxName to set
     */
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    /**
     * @return the taxValue
     */
    public String getTaxValue() {
        return taxValue;
    }

    /**
     * @param taxValue
     *            the taxValue to set
     */
    public void setTaxValue(String taxValue) {
        this.taxValue = taxValue;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount
     *            the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the taxAmount
     */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**
     * @param taxAmount
     *            the taxAmount to set
     */
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    /**
     * @return the graceAmount
     */
    public double getGraceAmount() {
        return graceAmount;
    }

    /**
     * @param graceAmount
     *            the graceAmount to set
     */
    public void setGraceAmount(double graceAmount) {
        this.graceAmount = graceAmount;
    }

    /**
     * @return the planServiceName
     */
    public String getPlanServiceName() {
        return planServiceName;
    }

    /**
     * @param planServiceName
     *            the planServiceName to set
     */
    public void setPlanServiceName(String planServiceName) {
        this.planServiceName = planServiceName;
    }

    /**
     * @return the minInsuranceValue
     */
    public Double getMinInsuranceValue() {
        return minInsuranceValue;
    }

    /**
     * @param minInsuranceValue
     *            the minInsuranceValue to set
     */
    public void setMinInsuranceValue(Double minInsuranceValue) {
        this.minInsuranceValue = minInsuranceValue;
    }

    /**
     * @return the maxInsuranceValue
     */
    public Double getMaxInsuranceValue() {
        return maxInsuranceValue;
    }

    /**
     * @param maxInsuranceValue
     *            the maxInsuranceValue to set
     */
    public void setMaxInsuranceValue(Double maxInsuranceValue) {
        this.maxInsuranceValue = maxInsuranceValue;
    }

    /**
     * @return the products
     */
    public List<String> getProducts() {
        return products;
    }

    /**
     * @param products
     *            the products to set
     */
    public void setProducts(List<String> products) {
        this.products = products;
    }

    /**
     * @return the numberOfDevices
     */
    public long getNumberOfDevices() {
        return numberOfDevices;
    }

    /**
     * @param numberOfDevices
     *            the numberOfDevices to set
     */
    public void setNumberOfDevices(long numberOfDevices) {
        this.numberOfDevices = numberOfDevices;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanMasterDto [planCode=");
		builder.append(planCode);
		builder.append(", planName=");
		builder.append(planName);
		builder.append(", planDesc=");
		builder.append(planDesc);
		builder.append(", productCode=");
		builder.append(productCode);
		builder.append(", frequency=");
		builder.append(frequency);
		builder.append(", annuity=");
		builder.append(annuity);
		builder.append(", backedByInsurance=");
		builder.append(backedByInsurance);
		builder.append(", freeLookin=");
		builder.append(freeLookin);
		builder.append(", graceDays=");
		builder.append(graceDays);
		builder.append(", refundAllowed=");
		builder.append(refundAllowed);
		builder.append(", price=");
		builder.append(price);
		builder.append(", taxcode=");
		builder.append(taxcode);
		builder.append(", downgrade=");
		builder.append(downgrade);
		builder.append(", discountAllowed=");
		builder.append(discountAllowed);
		builder.append(", trail=");
		builder.append(trail);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", planStatus=");
		builder.append(planStatus);
		builder.append(", addedBy=");
		builder.append(addedBy);
		builder.append(", addedOn=");
		builder.append(addedOn);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", authorisedBy=");
		builder.append(authorisedBy);
		builder.append(", authorisedOn=");
		builder.append(authorisedOn);
		builder.append(", taxName=");
		builder.append(taxName);
		builder.append(", taxValue=");
		builder.append(taxValue);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append(", taxAmount=");
		builder.append(taxAmount);
		builder.append(", graceAmount=");
		builder.append(graceAmount);
		builder.append(", planServiceName=");
		builder.append(planServiceName);
		builder.append(", minInsuranceValue=");
		builder.append(minInsuranceValue);
		builder.append(", maxInsuranceValue=");
		builder.append(maxInsuranceValue);
		builder.append(", products=");
		builder.append(products);
		builder.append(", numberOfDevices=");
		builder.append(numberOfDevices);
		builder.append("]");
		return builder.toString();
	}
    
}
