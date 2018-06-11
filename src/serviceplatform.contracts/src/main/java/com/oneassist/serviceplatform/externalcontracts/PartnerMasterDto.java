package com.oneassist.serviceplatform.externalcontracts;

public class PartnerMasterDto {

    private String partnerCode;
    private String partnerProviderCode;
    private String partnerType;
    private String partnerName;
    private String companyName;
    private String companyCode;
    private String city;
    private String corporateEmailId;
    private String corporateAlternateEmailId;
    private Long corporateLandlineNumber;
    private Long corporateMobileNumber;
    private Long corporateFaxNumber;
    private String contactPersonName;
    private String partnerStatus;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerProviderCode() {
        return partnerProviderCode;
    }

    public void setPartnerProviderCode(String partnerProviderCode) {
        this.partnerProviderCode = partnerProviderCode;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCorporateEmailId() {
        return corporateEmailId;
    }

    public void setCorporateEmailId(String corporateEmailId) {
        this.corporateEmailId = corporateEmailId;
    }

    public String getCorporateAlternateEmailId() {
        return corporateAlternateEmailId;
    }

    public void setCorporateAlternateEmailId(String corporateAlternateEmailId) {
        this.corporateAlternateEmailId = corporateAlternateEmailId;
    }

    public Long getCorporateLandlineNumber() {
        return corporateLandlineNumber;
    }

    public void setCorporateLandlineNumber(Long corporateLandlineNumber) {
        this.corporateLandlineNumber = corporateLandlineNumber;
    }

    public Long getCorporateMobileNumber() {
        return corporateMobileNumber;
    }

    public void setCorporateMobileNumber(Long corporateMobileNumber) {
        this.corporateMobileNumber = corporateMobileNumber;
    }

    public Long getCorporateFaxNumber() {
        return corporateFaxNumber;
    }

    public void setCorporateFaxNumber(Long corporateFaxNumber) {
        this.corporateFaxNumber = corporateFaxNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getPartnerStatus() {
        return partnerStatus;
    }

    public void setPartnerStatus(String partnerStatus) {
        this.partnerStatus = partnerStatus;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PartnerMasterDto [partnerCode=");
		builder.append(partnerCode);
		builder.append(", partnerProviderCode=");
		builder.append(partnerProviderCode);
		builder.append(", partnerType=");
		builder.append(partnerType);
		builder.append(", partnerName=");
		builder.append(partnerName);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", companyCode=");
		builder.append(companyCode);
		builder.append(", city=");
		builder.append(city);
		builder.append(", corporateEmailId=");
		builder.append(corporateEmailId);
		builder.append(", corporateAlternateEmailId=");
		builder.append(corporateAlternateEmailId);
		builder.append(", corporateLandlineNumber=");
		builder.append(corporateLandlineNumber);
		builder.append(", corporateMobileNumber=");
		builder.append(corporateMobileNumber);
		builder.append(", corporateFaxNumber=");
		builder.append(corporateFaxNumber);
		builder.append(", contactPersonName=");
		builder.append(contactPersonName);
		builder.append(", partnerStatus=");
		builder.append(partnerStatus);
		builder.append("]");
		return builder.toString();
	}
    
}
