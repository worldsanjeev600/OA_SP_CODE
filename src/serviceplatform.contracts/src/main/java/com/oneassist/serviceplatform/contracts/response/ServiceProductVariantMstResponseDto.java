package com.oneassist.serviceplatform.contracts.response;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.ServiceProductVariantMstDto;

public class ServiceProductVariantMstResponseDto {

    private Map<String, ServiceProductVariantMstDto> data;

    public Map<String, ServiceProductVariantMstDto> getData() {
        return data;
    }

    public void setData(Map<String, ServiceProductVariantMstDto> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "ServiceProductVariantMstResponseDto [data=" + data + "]";
	}
    
}
