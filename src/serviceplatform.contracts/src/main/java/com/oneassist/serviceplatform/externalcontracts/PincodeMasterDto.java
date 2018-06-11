/**
 * 
 */

package com.oneassist.serviceplatform.externalcontracts;

import java.io.Serializable;
import java.util.Date;

/**
 * @author srikanth
 */
public class PincodeMasterDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 440390812331289696L;

    private String pinCode;

    private String cityCode;
    
    private String	cityName;

	private String stateCode;

	private String	stateName;

    private String status;

    private Date createdOn;

    private String createdBy;

    private Date modifiedOn;

    private String modifiedBy;

    public String getPinCode() {

        return pinCode;
    }

    public void setPinCode(String pinCode) {

        this.pinCode = pinCode;
    }

    public String getCityCode() {

        return cityCode;
    }

    public void setCityCode(String cityCode) {

        this.cityCode = cityCode;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public Date getCreatedOn() {

        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {

        this.createdOn = createdOn;
    }

    public String getCreatedBy() {

        return createdBy;
    }

    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;
    }

    public Date getModifiedOn() {

        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {

        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {

        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {

        this.modifiedBy = modifiedBy;
    }

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public String toString() {
		return "PincodeMasterDto [pinCode=" + pinCode + ", cityCode=" + cityCode + ", cityName=" + cityName
				+ ", stateCode=" + stateCode + ", stateName=" + stateName + ", status=" + status + ", createdOn="
				+ createdOn + ", createdBy=" + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy
				+ "]";
	}

    
}
