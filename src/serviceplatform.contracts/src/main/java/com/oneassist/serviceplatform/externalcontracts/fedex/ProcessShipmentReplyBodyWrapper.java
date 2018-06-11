package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ProcessShipmentReply;

@XmlRootElement(name = "Body")
public class ProcessShipmentReplyBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private ProcessShipmentReply processShipmentReply;

    public ProcessShipmentReply getProcessShipmentReply() {
        return processShipmentReply;
    }

    @XmlElement(name = "ProcessShipmentReply", namespace = "http://fedex.com/ws/ship/v19")
    public void setProcessShipmentReply(ProcessShipmentReply processShipmentReply) {
        this.processShipmentReply = processShipmentReply;
    }
}
