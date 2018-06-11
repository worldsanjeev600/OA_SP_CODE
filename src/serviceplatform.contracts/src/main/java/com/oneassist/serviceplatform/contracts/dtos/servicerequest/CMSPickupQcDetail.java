package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class CMSPickupQcDetail {

    private String pickupQc;
    private String verifier;

    public String getPickupQc() {
        return pickupQc;
    }

    public void setPickupQc(String pickupQc) {
        this.pickupQc = pickupQc;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    @Override
    public String toString() {
        return "CMSPickupQcDetail [pickupQc=" + pickupQc + ", verifier=" + verifier + "]";
    }

}
