package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CreatePickupRequest;

@XmlRootElement(name = "Body")
public class CreatePickupRequestBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private CreatePickupRequest createPickupRequest;

    public CreatePickupRequest getCreatePickupRequest() {
        return createPickupRequest;
    }

    @XmlElement(name = "CreatePickupRequest", namespace = "http://fedex.com/ws/pickup/v13")
    public void setCreatePickupRequest(CreatePickupRequest createPickupRequest) {
        this.createPickupRequest = createPickupRequest;
    }

    @Override
    public String toString() {
        return "CreatePickupRequestWrapper [createPickupRequest=" + createPickupRequest + "]";
    }

}
