package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.ServiceMasterDto;

public class ServiceMasterDataDto {

    private String serviceId;

    private ServiceMasterDto serviceMaster;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public ServiceMasterDto getServiceMaster() {
        return serviceMaster;
    }

    public void setServiceMaster(ServiceMasterDto serviceMaster) {
        this.serviceMaster = serviceMaster;
    }

	@Override
	public String toString() {
		return "ServiceMasterDataDto [serviceId=" + serviceId + ", serviceMaster=" + serviceMaster + "]";
	}
    
}
