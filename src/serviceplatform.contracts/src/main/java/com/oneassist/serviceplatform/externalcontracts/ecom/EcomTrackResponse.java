/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Savita.kodli
 *
 */
@XmlRootElement(name = "ecomexpress-objects")
@XmlAccessorType(XmlAccessType.FIELD)
public class EcomTrackResponse {

    @XmlElement(name = "object")
    private Object object;

    private String version;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ClassPojo [object = " + object + ", version = " + version + "]";
    }
}
