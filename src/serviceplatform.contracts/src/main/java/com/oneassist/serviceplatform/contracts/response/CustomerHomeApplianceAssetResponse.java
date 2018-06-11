package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.MembershipDetailsDTO;

public class CustomerHomeApplianceAssetResponse {

    private MembershipDetailsDTO data;

    public MembershipDetailsDTO getData() {
        return data;
    }

    public void setData(MembershipDetailsDTO data) {
        this.data = data;
    }
}
