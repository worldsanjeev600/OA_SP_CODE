package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

public class ServicePartnerAllocationRequest {

    private Long pincode;
    private String serviceRequestType;
    private String modifiedBy;
    private String productCode;
    private ServicePartnerDetail partner1;
    private ServicePartnerDetail partner2;
    private ServicePartnerDetail partner3;
    private ServicePartnerDetail partner4;

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(String serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public ServicePartnerDetail getPartner1() {
        return partner1;
    }

    public void setPartner1(ServicePartnerDetail partner1) {
        this.partner1 = partner1;
    }

    public ServicePartnerDetail getPartner2() {
        return partner2;
    }

    public void setPartner2(ServicePartnerDetail partner2) {
        this.partner2 = partner2;
    }

    public ServicePartnerDetail getPartner3() {
        return partner3;
    }

    public void setPartner3(ServicePartnerDetail partner3) {
        this.partner3 = partner3;
    }

    public ServicePartnerDetail getPartner4() {
        return partner4;
    }

    public void setPartner4(ServicePartnerDetail partner4) {
        this.partner4 = partner4;
    }

}
