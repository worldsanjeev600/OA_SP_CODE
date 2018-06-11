package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class CreatePickupRequestWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private CreatePickupRequestBodyWrapper createPickupRequestBodyWrapper;

    public CreatePickupRequestBodyWrapper getCreatePickupRequestBodyWrapper() {
        return createPickupRequestBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setCreatePickupRequestBodyWrapper(CreatePickupRequestBodyWrapper createPickupRequestBodyWrapper) {
        this.createPickupRequestBodyWrapper = createPickupRequestBodyWrapper;
    }
}
