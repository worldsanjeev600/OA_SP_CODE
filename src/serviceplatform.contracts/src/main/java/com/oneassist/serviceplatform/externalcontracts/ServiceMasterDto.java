package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;

public class ServiceMasterDto {

    private String serviceId;
    private String planServiceName;
    private long partnerCode;
    private String resource1;
    private String resource2;
    private String resource3;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String serviceStatus;
    private String authorisedBy;
    private Date authorisedOn;
    private Character needPartnerRef;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPlanServiceName() {
        return planServiceName;
    }

    public void setPlanServiceName(String planServiceName) {
        this.planServiceName = planServiceName;
    }

    public long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getResource1() {
        return resource1;
    }

    public void setResource1(String resource1) {
        this.resource1 = resource1;
    }

    public String getResource2() {
        return resource2;
    }

    public void setResource2(String resource2) {
        this.resource2 = resource2;
    }

    public String getResource3() {
        return resource3;
    }

    public void setResource3(String resource3) {
        this.resource3 = resource3;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public Date getAuthorisedOn() {
        return authorisedOn;
    }

    public void setAuthorisedOn(Date authorisedOn) {
        this.authorisedOn = authorisedOn;
    }

    public Character getNeedPartnerRef() {
        return needPartnerRef;
    }

    public void setNeedPartnerRef(Character needPartnerRef) {
        this.needPartnerRef = needPartnerRef;
    }

	@Override
	public String toString() {
		return "ServiceMasterDto [serviceId=" + serviceId + ", planServiceName=" + planServiceName + ", partnerCode="
				+ partnerCode + ", resource1=" + resource1 + ", resource2=" + resource2 + ", resource3=" + resource3
				+ ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy
				+ ", modifiedOn=" + modifiedOn + ", serviceStatus=" + serviceStatus + ", authorisedBy=" + authorisedBy
				+ ", authorisedOn=" + authorisedOn + ", needPartnerRef=" + needPartnerRef + "]";
	}
    
}
