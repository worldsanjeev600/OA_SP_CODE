package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackDetail")
public class TrackDetail {

    @XmlElement(name = "StatusDetail")
    protected TrackStatusDetail statusDetail;

    public TrackStatusDetail getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(TrackStatusDetail statusDetail) {
        this.statusDetail = statusDetail;
    }

    @Override
    public String toString() {
        return "statusDetail =" + statusDetail;
    }

}
