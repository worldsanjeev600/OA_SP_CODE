package com.oneassist.serviceplatform.contracts.dtos;

import java.io.Serializable;

public class ServicePartnerMappingDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6274940743288212480L;

    private Long serviceRequestTypeId;
    
    private String serviceRequestType;
    
    private String servicePartnerName;
    
    private Long servicePartnerCode;

    public Long getServiceRequestTypeId() {
        return serviceRequestTypeId;
    }

    public void setServiceRequestTypeId(Long serviceRequestTypeId) {
        this.serviceRequestTypeId = serviceRequestTypeId;
    }

    public String getServiceRequestType() {
        return serviceRequestType;
    }

    public void setServiceRequestType(String serviceRequestType) {
        this.serviceRequestType = serviceRequestType;
    }

    public String getServicePartnerName() {
        return servicePartnerName;
    }

    public void setServicePartnerName(String servicePartnerName) {
        this.servicePartnerName = servicePartnerName;
    }

    public Long getServicePartnerCode() {
        return servicePartnerCode;
    }

    public void setServicePartnerCode(Long servicePartnerCode) {
        this.servicePartnerCode = servicePartnerCode;
    }

	@Override
	public String toString() {
		return "ServicePartnerMappingDto [serviceRequestTypeId=" + serviceRequestTypeId + ", serviceRequestType="
				+ serviceRequestType + ", servicePartnerName=" + servicePartnerName + ", servicePartnerCode="
				+ servicePartnerCode + "]";
	}
    
}
