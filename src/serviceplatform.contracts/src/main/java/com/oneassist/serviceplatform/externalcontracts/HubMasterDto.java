package com.oneassist.serviceplatform.externalcontracts;

public class HubMasterDto {

    private String ochmHubId;
    private String ochmHubName;
    private String ochmHubAddress;
    private String ochmHubCity;
    private String ochmHubState;
    private String ochmHubRegion;
    private String ochmHubEmail;
    private Long ochmHubMobile;

    public String getOchmHubId() {
        return ochmHubId;
    }

    public void setOchmHubId(String ochmHubId) {
        this.ochmHubId = ochmHubId;
    }

    public String getOchmHubName() {
        return ochmHubName;
    }

    public void setOchmHubName(String ochmHubName) {
        this.ochmHubName = ochmHubName;
    }

    public String getOchmHubAddress() {
        return ochmHubAddress;
    }

    public void setOchmHubAddress(String ochmHubAddress) {
        this.ochmHubAddress = ochmHubAddress;
    }

    public String getOchmHubCity() {
        return ochmHubCity;
    }

    public void setOchmHubCity(String ochmHubCity) {
        this.ochmHubCity = ochmHubCity;
    }

    public String getOchmHubState() {
        return ochmHubState;
    }

    public void setOchmHubState(String ochmHubState) {
        this.ochmHubState = ochmHubState;
    }

    public String getOchmHubRegion() {
        return ochmHubRegion;
    }

    public void setOchmHubRegion(String ochmHubRegion) {
        this.ochmHubRegion = ochmHubRegion;
    }

    public String getOchmHubEmail() {
        return ochmHubEmail;
    }

    public void setOchmHubEmail(String ochmHubEmail) {
        this.ochmHubEmail = ochmHubEmail;
    }

    public Long getOchmHubMobile() {
        return ochmHubMobile;
    }

    public void setOchmHubMobile(Long ochmHubMobile) {
        this.ochmHubMobile = ochmHubMobile;
    }

	@Override
	public String toString() {
		return "HubMasterDto [ochmHubId=" + ochmHubId + ", ochmHubName=" + ochmHubName + ", ochmHubAddress="
				+ ochmHubAddress + ", ochmHubCity=" + ochmHubCity + ", ochmHubState=" + ochmHubState
				+ ", ochmHubRegion=" + ochmHubRegion + ", ochmHubEmail=" + ochmHubEmail + ", ochmHubMobile="
				+ ochmHubMobile + "]";
	}
    
}
