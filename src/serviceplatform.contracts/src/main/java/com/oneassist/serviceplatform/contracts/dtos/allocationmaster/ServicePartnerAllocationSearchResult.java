package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ServicePartnerAllocationSearchResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5610010201541949086L;
    private String pincode;
    private String city;
    private String state;
    private String product;
    private String productCode;
    private String serviceName;
    private String serviceTypeCode;
    private ServicePartnerDetail partner1;
    private ServicePartnerDetail partner2;
    private ServicePartnerDetail partner3;
    private ServicePartnerDetail partner4;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
    private Date modifiedOn;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
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

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

}
