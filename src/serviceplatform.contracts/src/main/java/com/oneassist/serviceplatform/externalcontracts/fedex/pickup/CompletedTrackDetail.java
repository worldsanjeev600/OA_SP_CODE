package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.management.Notification;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompletedTrackDetail")
public class CompletedTrackDetail {

    @XmlElement(name = "HighestSeverity")
    protected NotificationSeverityType highestSeverity;
    @XmlElement(name = "Notifications")
    protected List<Notification> notifications;
    @XmlElement(name = "DuplicateWaybill")
    protected Boolean duplicateWaybill;
    @XmlElement(name = "MoreData")
    protected Boolean moreData;
    @XmlElement(name = "PagingToken")
    protected String pagingToken;
    @XmlElement(name = "TrackDetailsCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger trackDetailsCount;
    @XmlElement(name = "TrackDetails")
    protected List<TrackDetail> trackDetails;

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

    public Boolean isDuplicateWaybill() {
        return this.duplicateWaybill;
    }

    public void setDuplicateWaybill(Boolean value) {
        this.duplicateWaybill = value;
    }

    public Boolean isMoreData() {
        return this.moreData;
    }

    public void setMoreData(Boolean value) {
        this.moreData = value;
    }

    public String getPagingToken() {
        return this.pagingToken;
    }

    public void setPagingToken(String value) {
        this.pagingToken = value;
    }

    public BigInteger getTrackDetailsCount() {
        return this.trackDetailsCount;
    }

    public void setTrackDetailsCount(BigInteger value) {
        this.trackDetailsCount = value;
    }

    public List<TrackDetail> getTrackDetails() {
        return trackDetails;
    }

    public void setTrackDetails(List<TrackDetail> trackDetails) {
        this.trackDetails = trackDetails;
    }

    public Boolean getDuplicateWaybill() {
        return duplicateWaybill;
    }

    public Boolean getMoreData() {
        return moreData;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "CompletedTrackDetail [highestSeverity=" + highestSeverity + ", notifications=" + notifications + ", duplicateWaybill=" + duplicateWaybill + ", moreData=" + moreData + ", pagingToken="
                + pagingToken + ", trackDetailsCount=" + trackDetailsCount + "]";
    }

}
