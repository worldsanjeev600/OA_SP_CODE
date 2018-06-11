package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebAuthenticationCredential")
public class WebAuthenticationCredential {

    @XmlElement(name = "Key", required = true)
    protected String key;
    @XmlElement(name = "Password", required = true)
    protected String password;

    public String getKey() {
        return this.key;
    }

    public void setKey(String value) {
        this.key = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    @Override
    public String toString() {
        return "WebAuthenticationCredential [key=" + this.key + ", password=" + this.password + "]";
    }
}
