package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party")
public class Party {

    @XmlElement(name = "AccountNumber")
    protected String accountNumber;
    @XmlElement(name = "Tins")
    protected List<TaxpayerIdentification> tins;
    @XmlElement(name = "Contact")
    protected Contact contact;
    @XmlElement(name = "Address")
    protected Address address;

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    public List<TaxpayerIdentification> getTins() {
        if (this.tins == null) {
            this.tins = new ArrayList();
        }
        return this.tins;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact value) {
        this.contact = value;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address value) {
        this.address = value;
    }

    @Override
    public String toString() {
        return "Party [accountNumber=" + this.accountNumber + ", tins=" + this.tins + ", contact=" + this.contact + ", address=" + this.address + "]";
    }
}
