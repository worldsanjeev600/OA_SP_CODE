package com.oneassist.serviceplatform.externalcontracts;

import java.util.Date;

/**
 * @author Perumal this pojo will gives the basic requirement to sent the sms
 */
public class SmsVO {

    private long customerId;
    // private long memberShipId;
    private int templateId;

    private boolean isImmediate;
    private String createdBy;
    private String modifiedBy;
    private String toMobileNo;
    private Date custPreferredTime;

    /**
     * This field is used to populate the template name in case a blank template is used. Since subject field in comm dtl is used to identify the SMS type, this field will help to differentiate the
     * SMS' in case of blank templates.
     */
    private String smsName;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public boolean isImmediate() {
        return isImmediate;
    }

    public void setImmediate(boolean isImmediate) {
        this.isImmediate = isImmediate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getToMobileNo() {
        return toMobileNo;
    }

    public void setToMobileNo(String toMobileNo) {
        this.toMobileNo = toMobileNo;
    }

    public Date getCustPreferredTime() {
        return custPreferredTime;
    }

    public void setCustPreferredTime(Date custPreferredTime) {
        this.custPreferredTime = custPreferredTime;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName;
    }

}
