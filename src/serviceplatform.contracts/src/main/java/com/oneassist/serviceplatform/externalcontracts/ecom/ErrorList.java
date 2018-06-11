/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Savita.kodli
 *
 */
@XmlRootElement(name = "error_list")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorList {

    @XmlElement(name = "reason_comment")
    private String reasonComment;

    @XmlElement(name = "awb_used")
    private String awbUsed;

    @JsonProperty("reason_comment")
    public String getReasonComment() {
        return reasonComment;
    }

    @JsonProperty("reason_comment")
    public void setReasonComment(String reasonComment) {
        this.reasonComment = reasonComment;
    }

    @JsonProperty("awb_used")
    public String getAwbUsed() {
        return awbUsed;
    }

    @JsonProperty("awb_used")
    public void setAwbUsed(String awbUsed) {
        this.awbUsed = awbUsed;
    }

}
