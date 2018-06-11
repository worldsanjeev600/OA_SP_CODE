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

@XmlRootElement(name = "PickupAvailabilityReply")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PickupAvailabilityReply", namespace = "http://fedex.com/ws/pickup/v13")
public class PickupAvailabilityReply {

    @XmlElement(name = "HighestSeverity", required = true)
    protected NotificationSeverityType highestSeverity;
    @XmlElement(name = "Notifications", required = true)
    protected List<Notification> notifications;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "RequestTimestamp")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestTimestamp;
    @XmlElement(name = "CloseTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar closeTime;
    @XmlElement(name = "LocalTime")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar localTime;
    @XmlElement(name = "Options")
    protected List<PickupScheduleOption> options;

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

    public XMLGregorianCalendar getRequestTimestamp() {
        return this.requestTimestamp;
    }

    public void setRequestTimestamp(XMLGregorianCalendar value) {
        this.requestTimestamp = value;
    }

    public XMLGregorianCalendar getCloseTime() {
        return this.closeTime;
    }

    public void setCloseTime(XMLGregorianCalendar value) {
        this.closeTime = value;
    }

    public XMLGregorianCalendar getLocalTime() {
        return this.localTime;
    }

    public void setLocalTime(XMLGregorianCalendar value) {
        this.localTime = value;
    }

    public List<PickupScheduleOption> getOptions() {
        return options;
    }

    public void setOptions(List<PickupScheduleOption> options) {
        this.options = options;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "PickupAvailabilityReply [highestSeverity=" + highestSeverity + ", notifications=" + notifications + ", transactionDetail=" + transactionDetail + ", version=" + version
                + ", requestTimestamp=" + requestTimestamp + ", closeTime=" + closeTime + ", localTime=" + localTime + ", options=" + options + "]";
    }

}
