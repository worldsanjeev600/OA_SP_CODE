package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebAuthenticationDetail")
public class WebAuthenticationDetail {

    @XmlElement(name = "ParentCredential")
    protected WebAuthenticationCredential parentCredential;
    @XmlElement(name = "UserCredential", required = true)
    protected WebAuthenticationCredential userCredential;

    public WebAuthenticationCredential getParentCredential() {
        return this.parentCredential;
    }

    public void setParentCredential(WebAuthenticationCredential value) {
        this.parentCredential = value;
    }

    public WebAuthenticationCredential getUserCredential() {
        return this.userCredential;
    }

    public void setUserCredential(WebAuthenticationCredential value) {
        this.userCredential = value;
    }

    @Override
    public String toString() {
        return "WebAuthenticationDetail [parentCredential=" + this.parentCredential + ", userCredential=" + this.userCredential + "]";
    }
}
