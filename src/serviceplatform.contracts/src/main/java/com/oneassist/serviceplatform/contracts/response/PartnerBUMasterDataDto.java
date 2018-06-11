package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;

public class PartnerBUMasterDataDto {

    private String partnerBusinessUnitCode;
    private PartnerBusinessUnit partnerBusinessUnit;

    public String getPartnerBusinessUnitCode() {
        return partnerBusinessUnitCode;
    }

    public void setPartnerBusinessUnitCode(String partnerBusinessUnitCode) {
        this.partnerBusinessUnitCode = partnerBusinessUnitCode;
    }

    public PartnerBusinessUnit getPartnerBusinessUnit() {
        return partnerBusinessUnit;
    }

    public void setPartnerBusinessUnit(PartnerBusinessUnit partnerBusinessUnit) {
        this.partnerBusinessUnit = partnerBusinessUnit;
    }

	@Override
	public String toString() {
		return "PartnerBUMasterDataDto [partnerBusinessUnitCode=" + partnerBusinessUnitCode + ", partnerBusinessUnit="
				+ partnerBusinessUnit + "]";
	}
    
}
