package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.PlanMasterDto;

public class PlanMasterDataDto {

    private String planCode;
    private PlanMasterDto planMasterDto;

    public String getProductCode() {
        return planCode;
    }

    public void setProductCode(String productCode) {
        this.planCode = productCode;
    }

    public PlanMasterDto getProductMasterDto() {
        return planMasterDto;
    }

    public void setProductMasterDto(PlanMasterDto planMasterDto) {
        this.planMasterDto = planMasterDto;
    }

	@Override
	public String toString() {
		return "PlanMasterDataDto [planCode=" + planCode + ", planMasterDto=" + planMasterDto + "]";
	}
    
}
