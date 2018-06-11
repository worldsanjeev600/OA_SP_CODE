package com.oneassist.serviceplatform.contracts.dtos.hub;

import java.io.Serializable;
import java.util.List;

public class HubAllocationSearchRequestDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8828229090652024460L;
    private List<String> pincodes;
    private List<String> states;
    private List<String> cities;
    private List<String> hubIds;

    public List<String> getPincodes() {
        return pincodes;
    }

    public void setPincodes(List<String> pincodes) {
        this.pincodes = pincodes;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getHubIds() {
        return hubIds;
    }

    public void setHubIds(List<String> hubIds) {
        this.hubIds = hubIds;
    }

}
