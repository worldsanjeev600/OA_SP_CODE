package com.oneassist.serviceplatform.contracts.dtos.allocationmaster;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ServicePartnerDetail {

    private Long partnerCode;
    private String tat;
    private String partnerBUCode;
    private String partnerName;
    private String partnerBUName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "IST")
    private Date modifiedOn;

    public Long getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getTat() {
        return tat;
    }

    public void setTat(String tat) {
        this.tat = tat;
    }

    public String getPartnerBUCode() {
        return partnerBUCode;
    }

    public void setPartnerBUCode(String partnerBUCode) {
        this.partnerBUCode = partnerBUCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerBUName() {
        return partnerBUName;
    }

    public void setPartnerBUName(String partnerBUName) {
        this.partnerBUName = partnerBUName;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

}
