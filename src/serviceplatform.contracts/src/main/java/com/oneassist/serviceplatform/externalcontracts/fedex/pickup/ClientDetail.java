package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientDetail")
public class ClientDetail {

    @XmlElement(name = "AccountNumber", required = true)
    protected String accountNumber;
    @XmlElement(name = "MeterNumber", required = true)
    protected String meterNumber;
    @XmlElement(name = "MeterInstance")
    protected String meterInstance;
    @XmlElement(name = "IntegratorId")
    protected String integratorId;

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    public String getMeterNumber() {
        return this.meterNumber;
    }

    public void setMeterNumber(String value) {
        this.meterNumber = value;
    }

    public String getMeterInstance() {
        return this.meterInstance;
    }

    public void setMeterInstance(String value) {
        this.meterInstance = value;
    }

    public String getIntegratorId() {
        return this.integratorId;
    }

    public void setIntegratorId(String value) {
        this.integratorId = value;
    }

    @Override
    public String toString() {
        return

        "ClientDetail [accountNumber=" + this.accountNumber + ", meterNumber=" + this.meterNumber + ", meterInstance=" + this.meterInstance + ", integratorId=" + this.integratorId + "]";
    }
}
