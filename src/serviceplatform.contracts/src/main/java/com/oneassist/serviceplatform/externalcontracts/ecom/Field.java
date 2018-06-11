package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "field")
public class Field {

    // private String content;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "type")
    private String type;

    /*
     * public String getContent () { return content; }
     * 
     * public void setContent (String content) { this.content = content; }
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /*
     * @Override public String toString() { return "ClassPojo [content = "+content+", name = "+name+", type = "+type+"]"; }
     */

    @Override
    public String toString() {
        return "ClassPojo [name = " + name + ", type = " + type + "]";
    }

}
