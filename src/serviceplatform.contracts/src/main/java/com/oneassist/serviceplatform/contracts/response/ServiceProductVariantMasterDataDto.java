package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.ServiceProductVariantMstDto;

public class ServiceProductVariantMasterDataDto {

    private String productVariantId;
    private ServiceProductVariantMstDto serviceProductVariantMstDto;

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public ServiceProductVariantMstDto getServiceProductVariantMstDto() {
        return serviceProductVariantMstDto;
    }

    public void setServiceProductVariantMstDto(ServiceProductVariantMstDto serviceProductVariantMstDto) {
        this.serviceProductVariantMstDto = serviceProductVariantMstDto;
    }
}
