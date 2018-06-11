package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ProcessShipmentReply")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessShipmentReply", namespace = "http://fedex.com/ws/ship/v19")
public class ProcessShipmentReply {

    @XmlElement(name = "HighestSeverity", required = true)
    protected NotificationSeverityType highestSeverity;
    @XmlElement(name = "Notifications", required = true)
    protected List<Notification> notifications;
    @XmlElement(name = "TransactionDetail")
    protected TransactionDetail transactionDetail;
    @XmlElement(name = "Version", required = true)
    protected VersionId version;
    @XmlElement(name = "JobId")
    protected String jobId;
    @XmlElement(name = "CompletedShipmentDetail")
    protected CompletedShipmentDetail completedShipmentDetail;
    @XmlElement(name = "ErrorLabels")
    protected List<ShippingDocument> errorLabels;

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

    public String getJobId() {
        return this.jobId;
    }

    public void setJobId(String value) {
        this.jobId = value;
    }

    public CompletedShipmentDetail getCompletedShipmentDetail() {
        return this.completedShipmentDetail;
    }

    public void setCompletedShipmentDetail(CompletedShipmentDetail value) {
        this.completedShipmentDetail = value;
    }

    public List<ShippingDocument> getErrorLabels() {
        if (this.errorLabels == null) {
            this.errorLabels = new ArrayList();
        }
        return this.errorLabels;
    }

    @Override
    public String toString() {
        return

        "ProcessShipmentReply [highestSeverity=" + this.highestSeverity + ", notifications=" + this.notifications + ", transactionDetail=" + this.transactionDetail + ", version=" + this.version
                + ", jobId=" + this.jobId + ", completedShipmentDetail=" + this.completedShipmentDetail + ", errorLabels=" + this.errorLabels + "]";
    }
}
