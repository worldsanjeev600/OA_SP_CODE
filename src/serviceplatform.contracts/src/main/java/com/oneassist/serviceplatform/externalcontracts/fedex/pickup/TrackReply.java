package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.management.Notification;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TrackReply")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackReply")
public class TrackReply {

    @XmlElement(name = "HighestSeverity", required = true)
    protected NotificationSeverityType highestSeverity;
    @XmlElement(name = "Notifications", required = true)
    protected List<Notification> notifications;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "CompletedTrackDetails")
    protected List<CompletedTrackDetail> completedTrackDetails;

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

    public List<CompletedTrackDetail> getCompletedTrackDetails() {
        if (this.completedTrackDetails == null) {
            this.completedTrackDetails = new ArrayList();
        }
        return this.completedTrackDetails;
    }

    @Override
    public String toString() {
        return

        "TrackReply [highestSeverity=" + this.highestSeverity + ", notifications=" + this.notifications + ", transactionDetail=" + this.transactionDetail + ", version=" + this.version
                + ", completedTrackDetails=" + this.completedTrackDetails + "]";
    }
}
