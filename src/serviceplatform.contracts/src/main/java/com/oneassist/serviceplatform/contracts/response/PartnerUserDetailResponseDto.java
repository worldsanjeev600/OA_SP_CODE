package com.oneassist.serviceplatform.contracts.response;

import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.externalcontracts.PartnerUser;

public class PartnerUserDetailResponseDto {

    private Map<String, List<PartnerUser>> data;

    public Map<String, List<PartnerUser>> getData() {
        return data;
    }

    public void setData(Map<String, List<PartnerUser>> data) {
        this.data = data;
    }

	@Override
	public String toString() {
		return "PartnerUserDetailResponseDto [data=" + data + "]";
	}
    
}
