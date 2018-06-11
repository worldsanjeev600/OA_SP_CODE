package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class TrackShipmentRequestWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private TrackShipmentRequestBodyWrapper trackShipmentRequestBodyWrapper;

    public TrackShipmentRequestBodyWrapper getTrackShipmentRequestBodyWrapper() {
        return trackShipmentRequestBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setTrackShipmentRequestBodyWrapper(TrackShipmentRequestBodyWrapper trackShipmentRequestBodyWrapper) {
        this.trackShipmentRequestBodyWrapper = trackShipmentRequestBodyWrapper;
    }
}
