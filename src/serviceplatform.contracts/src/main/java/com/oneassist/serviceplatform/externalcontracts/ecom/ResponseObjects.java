/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author Savita.kodli
 *
 */
@XmlRootElement(name = "RESPONSE-OBJECTS")
@JsonRootName(value = "RESPONSE-OBJECTS")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseObjects {

    @XmlElement(name = "AIRWAYBILL-OBJECTS")
    private AirwaybillObjects AirwaybillObjects;
    @XmlElement(name = "RESPONSE-COMMENT")
    private String ResponseComment;

    @JsonProperty("AIRWAYBILL-OBJECTS")
    public AirwaybillObjects getAirwaybillObjects() {
        return AirwaybillObjects;
    }

    @JsonProperty("AIRWAYBILL-OBJECTS")
    public void setAirwaybillObjects(AirwaybillObjects AirwaybillObjects) {
        this.AirwaybillObjects = AirwaybillObjects;
    }

    @JsonProperty("RESPONSE-COMMENT")
    public String getResponseComment() {
        return ResponseComment;
    }

    @JsonProperty("RESPONSE-COMMENT")
    public void setResponseComment(String ResponseComment) {
        this.ResponseComment = ResponseComment;
    }

    @Override
    public String toString() {
        return "ClassPojo [AirwaybillObjects = " + AirwaybillObjects + ", ResponseComment = " + ResponseComment + "]";
    }

}
