package com.oneassist.serviceplatform.contracts.response;

import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.PlanMasterDto;

public class PlanMasterResponseDto {

    private Map<String, PlanMasterDto> data;

    public Map<String, PlanMasterDto> getData() {
        return data;
    }

    public void setData(Map<String, PlanMasterDto> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "PlanMasterResponseDto [data=" + data + "]";
	}
    
}
