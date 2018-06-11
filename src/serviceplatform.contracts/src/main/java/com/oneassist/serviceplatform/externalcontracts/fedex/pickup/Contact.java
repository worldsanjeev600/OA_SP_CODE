package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Contact")
public class Contact {

    @XmlElement(name = "ContactId")
    protected String contactId;
    @XmlElement(name = "PersonName")
    protected String personName;
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "CompanyName")
    protected String companyName;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "PhoneExtension")
    protected String phoneExtension;
    @XmlElement(name = "TollFreePhoneNumber")
    protected String tollFreePhoneNumber;
    @XmlElement(name = "PagerNumber")
    protected String pagerNumber;
    @XmlElement(name = "FaxNumber")
    protected String faxNumber;
    @XmlElement(name = "EMailAddress")
    protected String eMailAddress;

    public String getContactId() {
        return this.contactId;
    }

    public void setContactId(String value) {
        this.contactId = value;
    }

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String value) {
        this.personName = value;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String value) {
        this.companyName = value;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    public String getPhoneExtension() {
        return this.phoneExtension;
    }

    public void setPhoneExtension(String value) {
        this.phoneExtension = value;
    }

    public String getTollFreePhoneNumber() {
        return this.tollFreePhoneNumber;
    }

    public void setTollFreePhoneNumber(String value) {
        this.tollFreePhoneNumber = value;
    }

    public String getPagerNumber() {
        return this.pagerNumber;
    }

    public void setPagerNumber(String value) {
        this.pagerNumber = value;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String value) {
        this.faxNumber = value;
    }

    public String getEMailAddress() {
        return this.eMailAddress;
    }

    public void setEMailAddress(String value) {
        this.eMailAddress = value;
    }

    @Override
    public String toString() {
        return

        "Contact [contactId=" + this.contactId + ", personName=" + this.personName + ", title=" + this.title + ", companyName=" + this.companyName + ", phoneNumber=" + this.phoneNumber
                + ", phoneExtension=" + this.phoneExtension + ", tollFreePhoneNumber=" + this.tollFreePhoneNumber + ", pagerNumber=" + this.pagerNumber + ", faxNumber=" + this.faxNumber
                + ", eMailAddress=" + this.eMailAddress + "]";
    }
}
