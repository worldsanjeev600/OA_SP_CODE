package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.PartnerUser;

public class PartnerUserDetailDataDto {

    private String partnerCode;
    private PartnerUser partner;

    public String getPartnerCode() {

        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {

        this.partnerCode = partnerCode;
    }

    public PartnerUser getPartner() {

        return partner;
    }

    public void setPartner(PartnerUser partner) {

        this.partner = partner;
    }

	@Override
	public String toString() {
		return "PartnerUserDetailDataDto [partnerCode=" + partnerCode + ", partner=" + partner + "]";
	}
    
}
