package com.oneassist.serviceplatform.externalcontracts.fedex.pickup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Notification")
public class Notification {

    @XmlElement(name = "Severity", required = true)
    protected NotificationSeverityType severity;
    @XmlElement(name = "Source", required = true)
    protected String source;
    @XmlElement(name = "Code")
    protected String code;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "LocalizedMessage")
    protected String localizedMessage;

    public NotificationSeverityType getSeverity() {
        return severity;
    }

    public void setSeverity(NotificationSeverityType severity) {
        this.severity = severity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    @Override
    public String toString() {
        return "Notification [severity=" + severity + ", source=" + source + ", code=" + code + ", message=" + message + ", localizedMessage=" + localizedMessage + "]";
    }

}
