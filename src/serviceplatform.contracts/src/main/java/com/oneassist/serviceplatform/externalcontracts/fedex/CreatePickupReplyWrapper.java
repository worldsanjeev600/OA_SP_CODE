package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class CreatePickupReplyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4951332521972964798L;
    /**
     * 
     */

    private CreatePickupReplyBodyWrapper createPickupReplyBodyWrapper;

    public CreatePickupReplyBodyWrapper getCreatePickupReplyBodyWrapper() {
        return createPickupReplyBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setCreatePickupReplyBodyWrapper(CreatePickupReplyBodyWrapper createPickupReplyBodyWrapper) {
        this.createPickupReplyBodyWrapper = createPickupReplyBodyWrapper;
    }

}
