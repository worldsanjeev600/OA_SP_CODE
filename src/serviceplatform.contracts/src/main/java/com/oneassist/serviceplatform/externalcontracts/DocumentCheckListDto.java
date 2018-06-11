package com.oneassist.serviceplatform.externalcontracts;

/**
 * 
 * 
 * @author divya.hl
 */
public class DocumentCheckListDto {

    private Long ocdclChkLstId;
    private Long oidclIcPartId;
    private String oidclChkLstName;
    private Long oidSvcId;
    private String oidclDocMandatory;
    private String oidcCheckListDiscription;
    private String oidcClaimType;

    public Long getOcdclChkLstId() {
        return ocdclChkLstId;
    }

    public void setOcdclChkLstId(Long ocdclChkLstId) {
        this.ocdclChkLstId = ocdclChkLstId;
    }

    public Long getOidclIcPartId() {
        return oidclIcPartId;
    }

    public void setOidclIcPartId(Long oidclIcPartId) {
        this.oidclIcPartId = oidclIcPartId;
    }

    public String getOidclChkLstName() {
        return oidclChkLstName;
    }

    public void setOidclChkLstName(String oidclChkLstName) {
        this.oidclChkLstName = oidclChkLstName;
    }

    public Long getOidSvcId() {
        return oidSvcId;
    }

    public void setOidSvcId(Long oidSvcId) {
        this.oidSvcId = oidSvcId;
    }

    public String getOidclDocMandatory() {
        return oidclDocMandatory;
    }

    public void setOidclDocMandatory(String oidclDocMandatory) {
        this.oidclDocMandatory = oidclDocMandatory;
    }

    public String getOidcCheckListDiscription() {
        return oidcCheckListDiscription;
    }

    public void setOidcCheckListDiscription(String oidcCheckListDiscription) {
        this.oidcCheckListDiscription = oidcCheckListDiscription;
    }

    public String getOidcClaimType() {
        return oidcClaimType;
    }

    public void setOidcClaimType(String oidcClaimType) {
        this.oidcClaimType = oidcClaimType;
    }

	@Override
	public String toString() {
		return "DocumentCheckListDto [ocdclChkLstId=" + ocdclChkLstId + ", oidclIcPartId=" + oidclIcPartId
				+ ", oidclChkLstName=" + oidclChkLstName + ", oidSvcId=" + oidSvcId + ", oidclDocMandatory="
				+ oidclDocMandatory + ", oidcCheckListDiscription=" + oidcCheckListDiscription + ", oidcClaimType="
				+ oidcClaimType + "]";
	}
    
}
