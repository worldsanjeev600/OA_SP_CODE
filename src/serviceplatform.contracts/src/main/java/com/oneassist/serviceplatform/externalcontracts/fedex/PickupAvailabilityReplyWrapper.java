package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class PickupAvailabilityReplyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private PickupAvailabilityReplyBodyWrapper pickupAvailabilityReplyBodyWrapper;

    public PickupAvailabilityReplyBodyWrapper getPickupAvailabilityReplyBodyWrapper() {
        return pickupAvailabilityReplyBodyWrapper;
    }

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    public void setPickupAvailabilityReplyBodyWrapper(PickupAvailabilityReplyBodyWrapper pickupAvailabilityReplyBodyWrapper) {
        this.pickupAvailabilityReplyBodyWrapper = pickupAvailabilityReplyBodyWrapper;
    }

}
