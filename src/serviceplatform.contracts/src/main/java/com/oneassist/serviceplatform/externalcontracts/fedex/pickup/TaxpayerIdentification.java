package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxpayerIdentification")
public class TaxpayerIdentification {

    @XmlElement(name = "TinType", required = true)
    protected String tinType;
    @XmlElement(name = "Number", required = true)
    protected String number;
    @XmlElement(name = "Usage")
    protected String usage;
    @XmlElement(name = "EffectiveDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar effectiveDate;
    @XmlElement(name = "ExpirationDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expirationDate;

    public String getTinType() {
        return this.tinType;
    }

    public void setTinType(String value) {
        this.tinType = value;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String value) {
        this.number = value;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setUsage(String value) {
        this.usage = value;
    }

    public XMLGregorianCalendar getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(XMLGregorianCalendar value) {
        this.effectiveDate = value;
    }

    public XMLGregorianCalendar getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(XMLGregorianCalendar value) {
        this.expirationDate = value;
    }

    @Override
    public String toString() {
        return

        "TaxpayerIdentification [tinType=" + this.tinType + ", number=" + this.number + ", usage=" + this.usage + ", effectiveDate=" + this.effectiveDate + ", expirationDate=" + this.expirationDate
                + "]";
    }
}
