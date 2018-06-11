package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.CreatePickupReply;

@XmlRootElement(name = "Body")
public class CreatePickupReplyBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4951332521972964798L;
    /**
     * 
     */

    private CreatePickupReply createPickuReply;

    public CreatePickupReply getCreatePickuReply() {
        return createPickuReply;
    }

    @XmlElement(name = "CreatePickupReply", namespace = "http://fedex.com/ws/pickup/v13")
    public void setCreatePickuReply(CreatePickupReply createPickuReply) {
        this.createPickuReply = createPickuReply;
    }

}
