package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactAndAddress")
public class ContactAndAddress {

    @XmlElement(name = "Contact")
    protected Contact contact;
    @XmlElement(name = "Address")
    protected Address address;

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
        return "ContactAndAddress [contact=" + this.contact + ", address=" + this.address + "]";
    }
}
