package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;

public class EditClaimDetailDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2941180944012192116L;

    private Long ocdClaimIdTmp;

    private Long ocdClaimId;

    private String ocdIncidentDescription;

    private String ocdDamageLossDateTime;

    private String ocdCreatedDate;

    public Long getOcdClaimIdTmp() {
        return ocdClaimIdTmp;
    }

    public void setOcdClaimIdTmp(Long ocdClaimIdTmp) {
        this.ocdClaimIdTmp = ocdClaimIdTmp;
    }

    public Long getOcdClaimId() {
        return ocdClaimId;
    }

    public void setOcdClaimId(Long ocdClaimId) {
        this.ocdClaimId = ocdClaimId;
    }

    public String getOcdIncidentDescription() {
        return ocdIncidentDescription;
    }

    public void setOcdIncidentDescription(String ocdIncidentDescription) {
        this.ocdIncidentDescription = ocdIncidentDescription;
    }

    public String getOcdDamageLossDateTime() {
        return ocdDamageLossDateTime;
    }

    public void setOcdDamageLossDateTime(String ocdDamageLossDateTime) {
        this.ocdDamageLossDateTime = ocdDamageLossDateTime;
    }

    public String getOcdCreatedDate() {
        return ocdCreatedDate;
    }

    public void setOcdCreatedDate(String ocdCreatedDate) {
        this.ocdCreatedDate = ocdCreatedDate;
    }

    @Override
    public String toString() {
        return "EditClaimDetailDto [ocdClaimIdTmp=" + ocdClaimIdTmp + ", ocdClaimId=" + ocdClaimId + ", ocdIncidentDescription=" + ocdIncidentDescription + ", ocdDamageLossDateTime="
                + ocdDamageLossDateTime + ", ocdCreatedDate=" + ocdCreatedDate + "]";
    }

}
