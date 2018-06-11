package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;

public class PickupDetail implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4475097406200771627L;

    private String ascHub;
    private String pickupQc;
    private String verifier;
    private Long fulfilmentHubId;
    private String pickupDate;
    private String status;
    private String statusCode;
    private String description;
    private String pincodeCategory;

    public String getAscHub() {
        return ascHub;
    }

    public void setAscHub(String ascHub) {
        this.ascHub = ascHub;
    }

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

    public Long getFulfilmentHubId() {
        return fulfilmentHubId;
    }

    public void setFulfilmentHubId(Long fulfilmentHubId) {
        this.fulfilmentHubId = fulfilmentHubId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPincodeCategory() {
		return pincodeCategory;
	}

	public void setPincodeCategory(String pincodeCategory) {
		this.pincodeCategory = pincodeCategory;
	}

	@Override
	public String toString() {
		return "PickupDetail [ascHub=" + ascHub + ", pickupQc=" + pickupQc + ", verifier=" + verifier
				+ ", fulfilmentHubId=" + fulfilmentHubId + ", pickupDate=" + pickupDate + ", status=" + status
				+ ", statusCode=" + statusCode + ", description=" + description + ", pincodeCategory=" + pincodeCategory
				+ "]";
	}

}
