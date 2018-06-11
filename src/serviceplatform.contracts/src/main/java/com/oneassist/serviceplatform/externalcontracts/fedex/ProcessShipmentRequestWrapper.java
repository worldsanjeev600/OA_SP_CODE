package com.oneassist.serviceplatform.externalcontracts.fedex;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope")
public class ProcessShipmentRequestWrapper implements Serializable {

    private static final long serialVersionUID = -8438850944405422368L;
    private ProcessShipmentRequestBodyWrapper processShipmentRequestBodyWrapper;

    public ProcessShipmentRequestBodyWrapper getProcessShipmentRequestBodyWrapper() {
        return processShipmentRequestBodyWrapper;
    }

    @XmlElement(name = "Body")
    public void setProcessShipmentRequestBodyWrapper(ProcessShipmentRequestBodyWrapper processShipmentRequestBodyWrapper) {
        this.processShipmentRequestBodyWrapper = processShipmentRequestBodyWrapper;
    }

}
