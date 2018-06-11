package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repair {

    private String serviceEndCode;

    private String resolutionCode;

    private String touchScreenStatus;
    private String cameraStatus;
    private String buttonsStatus;
    private String speakerStatus;
    private String microphoneStatus;
    private String chargePinStatus;
    private String headPhoneJackStatus;
    private String wifiStatus;
    private String dispalyStatus;
    private String verifier;
    private String repairDate;
    private String repairDueDate;
    private String repairable;
    private String status;
    private String statusCode;
    private String description;

    public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getServiceEndCode() {
        return serviceEndCode;
    }

    public void setServiceEndCode(String serviceEndCode) {
        this.serviceEndCode = serviceEndCode;
    }

    public String getResolutionCode() {
        return resolutionCode;
    }

    public void setResolutionCode(String resolutionCode) {
        this.resolutionCode = resolutionCode;
    }

    public String getTouchScreenStatus() {
        return touchScreenStatus;
    }

    public void setTouchScreenStatus(String touchScreenStatus) {
        this.touchScreenStatus = touchScreenStatus;
    }

    public String getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(String cameraStatus) {
        this.cameraStatus = cameraStatus;
    }

    public String getButtonsStatus() {
        return buttonsStatus;
    }

    public void setButtonsStatus(String buttonsStatus) {
        this.buttonsStatus = buttonsStatus;
    }

    public String getSpeakerStatus() {
        return speakerStatus;
    }

    public void setSpeakerStatus(String speakerStatus) {
        this.speakerStatus = speakerStatus;
    }

    public String getMicrophoneStatus() {
        return microphoneStatus;
    }

    public void setMicrophoneStatus(String microphoneStatus) {
        this.microphoneStatus = microphoneStatus;
    }

    public String getChargePinStatus() {
        return chargePinStatus;
    }

    public void setChargePinStatus(String chargePinStatus) {
        this.chargePinStatus = chargePinStatus;
    }

    public String getHeadPhoneJackStatus() {
        return headPhoneJackStatus;
    }

    public void setHeadPhoneJackStatus(String headPhoneJackStatus) {
        this.headPhoneJackStatus = headPhoneJackStatus;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(String wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public String getDispalyStatus() {
        return dispalyStatus;
    }

    public void setDispalyStatus(String dispalyStatus) {
        this.dispalyStatus = dispalyStatus;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(String repairDate) {
        this.repairDate = repairDate;
    }

    public String getRepairDueDate() {
        return repairDueDate;
    }

    public void setRepairDueDate(String repairDueDate) {
        this.repairDueDate = repairDueDate;
    }

    public String getRepairable() {
        return repairable;
    }

    public void setRepairable(String repairable) {
        this.repairable = repairable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Repair [serviceEndCode=" + serviceEndCode + ", resolutionCode=" + resolutionCode
				+ ", touchScreenStatus=" + touchScreenStatus + ", cameraStatus=" + cameraStatus + ", buttonsStatus="
				+ buttonsStatus + ", speakerStatus=" + speakerStatus + ", microphoneStatus=" + microphoneStatus
				+ ", chargePinStatus=" + chargePinStatus + ", headPhoneJackStatus=" + headPhoneJackStatus
				+ ", wifiStatus=" + wifiStatus + ", dispalyStatus=" + dispalyStatus + ", verifier=" + verifier
				+ ", repairDate=" + repairDate + ", repairDueDate=" + repairDueDate + ", repairable=" + repairable
				+ ", status=" + status + ", statusCode=" + statusCode + ", description=" + description + "]";
	}

}
