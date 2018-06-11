package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;

public class PartnerMasterDataDto {

    private String partnerCode;
    private PartnerMasterDto partner;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public PartnerMasterDto getPartner() {
        return partner;
    }

    public void setPartner(PartnerMasterDto partner) {
        this.partner = partner;
    }

	@Override
	public String toString() {
		return "PartnerMasterDataDto [partnerCode=" + partnerCode + ", partner=" + partner + "]";
	}

	    
}
