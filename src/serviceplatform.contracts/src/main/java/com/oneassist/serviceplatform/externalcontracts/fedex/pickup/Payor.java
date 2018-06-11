package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payor")
public class Payor {

    @XmlElement(name = "ResponsibleParty")
    protected Party responsibleParty;

    public Party getResponsibleParty() {
        return this.responsibleParty;
    }

    public void setResponsibleParty(Party value) {
        this.responsibleParty = value;
    }

    @Override
    public String toString() {
        return "Payor [responsibleParty=" + this.responsibleParty + "]";
    }
}
