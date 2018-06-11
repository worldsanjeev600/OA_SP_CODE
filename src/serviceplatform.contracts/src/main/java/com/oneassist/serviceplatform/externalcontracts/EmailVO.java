package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;
import java.util.List;

public class EmailVO {

    private int templateId;

    private long customerId;
    private boolean sendImmediate;
    private boolean logEmail;

    private String toAddress;
    private String ccAddress;
    private String bccAddress;

    private String subject;
    private String emailBody;
    private String deliveryStatus;

    private String failureMessage;

    private int initiatingSystem;
    private String createdBy;
    private String userType;
    private List attachfilePaths;
    private Long senderProvider;
    private Long templateProvider;
    private Date custPreferredTime;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public boolean isSendImmediate() {
        return sendImmediate;
    }

    public void setSendImmediate(boolean sendImmediate) {
        this.sendImmediate = sendImmediate;
    }

    public boolean isLogEmail() {
        return logEmail;
    }

    public void setLogEmail(boolean logEmail) {
        this.logEmail = logEmail;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getInitiatingSystem() {
        return initiatingSystem;
    }

    public void setInitiatingSystem(int initiatingSystem) {
        this.initiatingSystem = initiatingSystem;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public List getAttachfilePaths() {
        return attachfilePaths;
    }

    public void setAttachfilePaths(List attachfilePaths) {
        this.attachfilePaths = attachfilePaths;
    }

    public Long getSenderProvider() {
        return senderProvider;
    }

    public void setSenderProvider(Long senderProvider) {
        this.senderProvider = senderProvider;
    }

    public Long getTemplateProvider() {
        return templateProvider;
    }

    public void setTemplateProvider(Long templateProvider) {
        this.templateProvider = templateProvider;
    }

    public Date getCustPreferredTime() {
        return custPreferredTime;
    }

    public void setCustPreferredTime(Date custPreferredTime) {
        this.custPreferredTime = custPreferredTime;
    }
}
