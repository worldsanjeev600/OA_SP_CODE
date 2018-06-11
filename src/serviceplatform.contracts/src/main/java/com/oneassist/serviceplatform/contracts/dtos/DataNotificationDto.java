package com.oneassist.serviceplatform.contracts.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * @author priya.prakash
 *         <p>
 *         Value object for holding notification related details
 *         </p>
 */
public class DataNotificationDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2300944648953913678L;

    private String eventType;
    private String eventName;
    private Date eventPublishedDateTime;
    private Long initiatingSystem;
    private Object messagePayload;
    private String eventMessageId;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventPublishedDateTime() {
        return eventPublishedDateTime;
    }

    public void setEventPublishedDateTime(Date eventPublishedDateTime) {
        this.eventPublishedDateTime = eventPublishedDateTime;
    }

    public Long getInitiatingSystem() {
        return initiatingSystem;
    }

    public void setInitiatingSystem(Long initiatingSystem) {
        this.initiatingSystem = initiatingSystem;
    }

    public Object getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(Object messagePayload) {
        this.messagePayload = messagePayload;
    }

    public String getEventMessageId() {
        return eventMessageId;
    }

    public void setEventMessageId(String eventMessageId) {
        this.eventMessageId = eventMessageId;
    }

    @Override
    public String toString() {
        return "DataNotificationDto [eventType=" + eventType + ", eventName=" + eventName + ", eventPublishedDateTime=" + eventPublishedDateTime + ", initiatingSystem=" + initiatingSystem
                + ", messagePayload=" + messagePayload + ", eventMessageId=" + eventMessageId + "]";
    }
}
