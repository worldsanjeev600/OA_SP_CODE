package com.oneassist.serviceplatform.contracts.response;

import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;

public class HubMasterDataDto {

    private String ochmHubId;
    private HubMasterDto claimHubMasterDto;

    public String getOchmHubId() {
        return ochmHubId;
    }

    public void setOchmHubId(String ochmHubId) {
        this.ochmHubId = ochmHubId;
    }

    public HubMasterDto getClaimHubMasterDto() {
        return claimHubMasterDto;
    }

    public void setClaimHubMasterDto(HubMasterDto claimHubMasterDto) {
        this.claimHubMasterDto = claimHubMasterDto;
    }

	@Override
	public String toString() {
		return "HubMasterDataDto [ochmHubId=" + ochmHubId + ", claimHubMasterDto=" + claimHubMasterDto + "]";
	}
    
}
