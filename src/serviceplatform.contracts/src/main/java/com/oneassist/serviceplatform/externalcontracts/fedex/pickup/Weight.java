package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Weight")
public class Weight {

    @XmlElement(name = "Units")
    protected String units;
    @XmlElement(name = "Value")
    protected BigDecimal value;

    public String getUnits() {
        return this.units;
    }

    public void setUnits(String value) {
        this.units = value;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Weight [units=" + this.units + ", value=" + this.value + "]";
    }
}
