package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "object")
public class TrackingObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5674276410705123239L;

    @XmlPath("field[@name='awb_number']/text()")
    private String awb_number;

    @XmlPath("field[@name='scans']")
    private ScanObject scanObject;

    public String getAwb_number() {
        return awb_number;
    }

    public void setAwb_number(String awb_number) {
        this.awb_number = awb_number;
    }

    public ScanObject getScanObject() {
        return scanObject;
    }

    public void setScanObject(ScanObject scanObject) {
        this.scanObject = scanObject;
    }

}
