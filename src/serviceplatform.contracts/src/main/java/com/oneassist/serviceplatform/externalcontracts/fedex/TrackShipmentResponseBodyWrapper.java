package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.TrackReply;

@XmlRootElement(name = "Body")
public class TrackShipmentResponseBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8438850944405422368L;
    private TrackReply trackReply;

    public TrackReply getTrackReply() {
        return trackReply;
    }

    @XmlElement(name = "TrackReply")
    public void setTrackReply(TrackReply trackReply) {
        this.trackReply = trackReply;
    }
}
