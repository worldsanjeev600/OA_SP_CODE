package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ScanObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3441045397359439663L;

    @XmlElement(name = "object")
    private List<ScanStage> scan_stages;

    public List<ScanStage> getScan_stages() {
        return scan_stages;
    }

    public void setScan_stages(List<ScanStage> scan_stages) {
        this.scan_stages = scan_stages;
    }

}
