package com.oneassist.serviceplatform.contracts.response;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;

public class PartnerBUResponseDto {

    private Map<String, PartnerBusinessUnit> data;

    public Map<String, PartnerBusinessUnit> getData() {
        return data;
    }

    public void setData(Map<String, PartnerBusinessUnit> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "PartnerBUResponseDto [data=" + data + "]";
	}
    
    
}
