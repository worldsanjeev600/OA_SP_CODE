package com.oneassist.serviceplatform.contracts.response;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.ProductMasterDto;

public class ProductMasterResponseDto {

    private Map<String, ProductMasterDto> data;

    public Map<String, ProductMasterDto> getData() {
        return data;
    }

    public void setData(Map<String, ProductMasterDto> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "ProductMasterResponseDto [data=" + data + "]";
	}
    
}
