package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class CMSRepairQcDetail {

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

    @Override
    public String toString() {
        return "CMSRepairQcDetail [touchScreenStatus=" + touchScreenStatus + ", cameraStatus=" + cameraStatus + ", buttonsStatus=" + buttonsStatus + ", speakerStatus=" + speakerStatus
                + ", microphoneStatus=" + microphoneStatus + ", chargePinStatus=" + chargePinStatus + ", headPhoneJackStatus=" + headPhoneJackStatus + ", wifiStatus=" + wifiStatus
                + ", dispalyStatus=" + dispalyStatus + ", verifier=" + verifier + "]";
    }

}
