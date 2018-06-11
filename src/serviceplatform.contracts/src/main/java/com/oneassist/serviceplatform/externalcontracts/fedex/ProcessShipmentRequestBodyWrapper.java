package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.oneassist.serviceplatform.externalcontracts.fedex.pickup.ProcessShipmentRequest;

@XmlRootElement(name = "Body")
public class ProcessShipmentRequestBodyWrapper implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8438850944405422368L;
    private ProcessShipmentRequest processShipmentRequest;

    public ProcessShipmentRequest getProcessShipmentRequest() {
        return processShipmentRequest;
    }

    @XmlElement(name = "ProcessShipmentRequest", namespace = "http://fedex.com/ws/ship/v19")
    public void setProcessShipmentRequest(ProcessShipmentRequest processShipmentRequest) {
        this.processShipmentRequest = processShipmentRequest;
    }
}
