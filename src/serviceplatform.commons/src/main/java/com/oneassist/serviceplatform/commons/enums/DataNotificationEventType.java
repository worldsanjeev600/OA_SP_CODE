package com.oneassist.serviceplatform.commons.enums;

public enum DataNotificationEventType {
    NEW("NEW"), UPDATED("UPDATE"), DELETED("DELETE");

    private final String eventType;

    DataNotificationEventType(String eventType) {

        this.eventType = eventType;
    }

    public String getEventType() {

        return this.eventType;
    }
}
