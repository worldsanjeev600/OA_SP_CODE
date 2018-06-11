package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupAvailabilityReply;

@XmlRootElement(name = "Body")
public class PickupAvailabilityReplyBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private PickupAvailabilityReply pickupAvailabilityReply;

    public PickupAvailabilityReply getPickupAvailabilityReply() {
        return pickupAvailabilityReply;
    }

    @XmlElement(name = "PickupAvailabilityReply", namespace = "http://fedex.com/ws/pickup/v13")
    public void setPickupAvailabilityReply(PickupAvailabilityReply pickupAvailabilityReply) {
        this.pickupAvailabilityReply = pickupAvailabilityReply;
    }

    @Override
    public String toString() {
        return "PickupAvailabilityReplyWrapper [pickupAvailabilityReply=" + pickupAvailabilityReply + "]";
    }
}
