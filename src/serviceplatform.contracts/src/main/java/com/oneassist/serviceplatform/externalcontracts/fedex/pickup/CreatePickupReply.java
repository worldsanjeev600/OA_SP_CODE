package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement(name = "CreatePickupReply")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreatePickupReply")
public class CreatePickupReply {

    @XmlElement(name = "HighestSeverity", required = true)
    protected NotificationSeverityType highestSeverity;
    @XmlElement(name = "Notifications", required = true)
    protected List<Notification> notifications;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "PickupConfirmationNumber")
    protected String pickupConfirmationNumber;
    @XmlElement(name = "Location")
    protected String location;
    @XmlElement(name = "MessageCode")
    protected String messageCode;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "PRPControlNumber")
    protected String prpControlNumber;
    @XmlElement(name = "LastAccessTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar lastAccessTime;

    public NotificationSeverityType getHighestSeverity() {
        return this.highestSeverity;
    }

    public void setHighestSeverity(NotificationSeverityType value) {
        this.highestSeverity = value;
    }

    public List<Notification> getNotifications() {
        if (this.notifications == null) {
            this.notifications = new ArrayList();
        }
        return this.notifications;
    }

    public TransactionDetail getTransactionDetail() {
        return this.transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail value) {
        this.transactionDetail = value;
    }

    public VersionId getVersion() {
        return this.version;
    }

    public void setVersion(VersionId value) {
        this.version = value;
    }

    public String getPickupConfirmationNumber() {
        return this.pickupConfirmationNumber;
    }

    public void setPickupConfirmationNumber(String value) {
        this.pickupConfirmationNumber = value;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String value) {
        this.location = value;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public void setMessageCode(String value) {
        this.messageCode = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public String getPRPControlNumber() {
        return this.prpControlNumber;
    }

    public void setPRPControlNumber(String value) {
        this.prpControlNumber = value;
    }

    public XMLGregorianCalendar getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(XMLGregorianCalendar value) {
        this.lastAccessTime = value;
    }

    @Override
    public String toString() {
        return

        "CreatePickupReply [highestSeverity=" + this.highestSeverity + ", notifications=" + this.notifications + ", transactionDetail=" + this.transactionDetail + ", version=" + this.version
                + ", pickupConfirmationNumber=" + this.pickupConfirmationNumber + ", location=" + this.location + ", messageCode=" + this.messageCode + ", message=" + this.message
                + ", prpControlNumber=" + this.prpControlNumber + ", lastAccessTime=" + this.lastAccessTime + "]";
    }
}
