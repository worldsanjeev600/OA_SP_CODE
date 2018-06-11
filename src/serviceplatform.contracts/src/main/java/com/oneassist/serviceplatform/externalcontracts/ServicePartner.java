package com.oneassist.serviceplatform.externalcontracts;

/**
 * Service partner enumeration
 */
public enum ServicePartner {

    SPOORS("Spoors"), 
    EDELWEISS("Edelweiss"), 
    FEDEX("Fedex"), 
    BLUEDART("Bluedart"), 
    GOPIGEON("Gopigeon"), 
    ECOM("Ecom"), 
    LOGINEXT("LOGINEXT"),
    DHL("DHL");

    private String partnerName;

    ServicePartner(String partnerName) {
        
    	this.partnerName = partnerName;
    }

    @Override
    public String toString() {
        return partnerName;
    }
}