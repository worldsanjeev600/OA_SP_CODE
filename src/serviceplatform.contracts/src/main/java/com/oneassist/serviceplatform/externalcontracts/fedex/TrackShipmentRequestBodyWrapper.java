package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackRequest;

@XmlRootElement(name = "Body")
public class TrackShipmentRequestBodyWrapper implements Serializable {

    private static final long serialVersionUID = -8438850944405422368L;
    private TrackRequest trackRequest;

    public TrackRequest getTrackRequest() {
        return trackRequest;
    }

    @XmlElement(name = "TrackRequest", namespace = "http://fedex.com/ws/track/v12")
    public void setTrackRequest(TrackRequest trackRequest) {
        this.trackRequest = trackRequest;
    }

}
