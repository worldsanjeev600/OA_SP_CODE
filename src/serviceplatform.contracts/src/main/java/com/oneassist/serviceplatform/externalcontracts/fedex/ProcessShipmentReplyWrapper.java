package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class ProcessShipmentReplyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 629857700478049354L;
    private ProcessShipmentReplyBodyWrapper processShipmentReplyBodyWrapper;

    public ProcessShipmentReplyBodyWrapper getProcessShipmentReplyBodyWrapper() {
        return processShipmentReplyBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setProcessShipmentReplyBodyWrapper(ProcessShipmentReplyBodyWrapper processShipmentReplyBodyWrapper) {
        this.processShipmentReplyBodyWrapper = processShipmentReplyBodyWrapper;
    }

}
