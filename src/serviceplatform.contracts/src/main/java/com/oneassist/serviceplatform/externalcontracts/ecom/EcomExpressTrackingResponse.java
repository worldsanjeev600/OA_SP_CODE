package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name = "ecomexpress-objects")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("ecomexpress-objects")
public class EcomExpressTrackingResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6449592494397940122L;

    @XmlElement(name = "object")
    @JsonProperty("object")
    private List<TrackingObject> trackingObjects;

    public List<TrackingObject> getTrackingObjects() {
        return trackingObjects;
    }

    public void setTrackingObjecta(List<TrackingObject> trackingObjects) {
        this.trackingObjects = trackingObjects;
    }

}
