package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class PickupAvailabilityRequestWrapper implements Serializable {

    public PickupAvailabilityRequestBodyWrapper getPickupAvailabilityRequestBodyWrapper() {
        return pickupAvailabilityRequestBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setPickupAvailabilityRequestBodyWrapper(PickupAvailabilityRequestBodyWrapper pickupAvailabilityRequestBodyWrapper) {
        this.pickupAvailabilityRequestBodyWrapper = pickupAvailabilityRequestBodyWrapper;
    }

    private static final long serialVersionUID = -8438850944405422368L;
    private PickupAvailabilityRequestBodyWrapper pickupAvailabilityRequestBodyWrapper;

}
