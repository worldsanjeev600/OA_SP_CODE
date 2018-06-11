package com.oneassist.serviceplatform.externalcontracts.ecom;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "QC")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipmentQC implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6866358024578114907L;

    @XmlElement(name = "QCCHECKCODE")
    private String qcCheckCode;

    @XmlElement(name = "VALUE")
    private String value;

    @XmlElement(name = "DESC")
    private String desc;

    @JsonProperty("QCCHECKCODE")
    public String getQcCheckCode() {
        return qcCheckCode;
    }

    @JsonProperty("QCCHECKCODE")
    public void setQcCheckCode(String qcCheckCode) {
        this.qcCheckCode = qcCheckCode;
    }

    @JsonProperty("VALUE")
    public String getValue() {
        return value;
    }

    @JsonProperty("VALUE")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("DESC")
    public String getDesc() {
        return desc;
    }

    @JsonProperty("DESC")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ShipmentQC [qcCheckCode=" + qcCheckCode + ", value=" + value + ", desc=" + desc + "]";
    }
}
