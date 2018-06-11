package com.oneassist.serviceplatform.contracts.dtos.hub;

import java.util.List;

public class HubAllocationRequestDto {

    private List<HubAllocationDto> hubAllocations;
    private String modifiedBy;

    public List<HubAllocationDto> getHubAllocations() {
        return hubAllocations;
    }

    public void setHubAllocations(List<HubAllocationDto> hubAllocations) {
        this.hubAllocations = hubAllocations;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
