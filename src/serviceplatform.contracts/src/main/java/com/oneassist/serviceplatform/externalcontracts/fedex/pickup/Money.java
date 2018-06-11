package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Money")
public class Money {

    @XmlElement(name = "Currency", required = true)
    protected String currency;
    @XmlElement(name = "Amount", required = true)
    protected BigDecimal amount;

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String value) {
        this.currency = value;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    @Override
    public String toString() {
        return "Money [currency=" + this.currency + ", amount=" + this.amount + "]";
    }
}
