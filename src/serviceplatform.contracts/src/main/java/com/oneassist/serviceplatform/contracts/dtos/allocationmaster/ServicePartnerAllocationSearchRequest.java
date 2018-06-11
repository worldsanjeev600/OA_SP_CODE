package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.io.Serializable;
import java.util.List;

public class ServicePartnerAllocationSearchRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 985201103858894930L;
    private List<String> pincodes;
    private List<String> states;
    private List<String> cities;
    private String productCode;
    private Long partnerCode;
    private String serviceType;
    private Long serviceTypeId;

    public List<String> getPincodes() {
        return pincodes;
    }

    public void setPincodes(List<String> pincodes) {
        this.pincodes = pincodes;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

}
