package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.PickupAvailabilityRequest;

@XmlRootElement(name = "Body")
public class PickupAvailabilityRequestBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8438850944405422368L;
    private PickupAvailabilityRequest pickupAvailabilityRequest;

    public PickupAvailabilityRequest getPickupAvailabilityRequest() {
        return pickupAvailabilityRequest;
    }

    @XmlElement(name = "PickupAvailabilityRequest", namespace = "http://fedex.com/ws/pickup/v13")
    public void setPickupAvailabilityRequest(PickupAvailabilityRequest pickupAvailabilityRequest) {
        this.pickupAvailabilityRequest = pickupAvailabilityRequest;
    }

    @Override
    public String toString() {
        return "FedexPickupAvailabilityRequestWrapper [pickupAvailabilityRequest=" + pickupAvailabilityRequest + "]";
    }
}
