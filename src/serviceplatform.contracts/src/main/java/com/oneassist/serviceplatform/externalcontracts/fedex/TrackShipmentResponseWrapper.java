package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class TrackShipmentResponseWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private TrackShipmentResponseBodyWrapper trackShipmentResponseBodyWrapper;

    public TrackShipmentResponseBodyWrapper getTrackShipmentResponseBodyWrapper() {
        return trackShipmentResponseBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setTrackShipmentResponseBodyWrapper(TrackShipmentResponseBodyWrapper trackShipmentResponseBodyWrapper) {
        this.trackShipmentResponseBodyWrapper = trackShipmentResponseBodyWrapper;
    }

}
