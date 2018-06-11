package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "object")
@XmlAccessorType(XmlAccessType.FIELD)
public class Object {

    @XmlElement(name = "field")
    private Field[] field;

    private String model;

    private String pk;

    public Field[] getField() {
        return field;
    }

    public void setField(Field[] field) {
        this.field = field;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "ClassPojo [field = " + field + ", model = " + model + ", pk = " + pk + "]";
    }

}
