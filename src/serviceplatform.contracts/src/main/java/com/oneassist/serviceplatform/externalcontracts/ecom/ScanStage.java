package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlAccessorType(XmlAccessType.FIELD)
public class ScanStage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3475162877032657204L;

    @XmlPath("field[@name='updated_on']/text()")
    private String updated_on;

    @XmlPath("field[@name='status']/text()")
    private String status;

    @XmlPath("field[@name='reason_code']/text()")
    private String reason_code;

    @XmlPath("field[@name='reason_code_number']/text()")
    private String reason_code_number;

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public String getReason_code_number() {
        return reason_code_number;
    }

    public void setReason_code_number(String reason_code_number) {
        this.reason_code_number = reason_code_number;
    }

}
