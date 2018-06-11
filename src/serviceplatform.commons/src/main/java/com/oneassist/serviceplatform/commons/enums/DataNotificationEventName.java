package com.oneassist.serviceplatform.commons.enums;

public enum DataNotificationEventName {
    SERVICE_REQUEST_UPDATE("SERVICE_REQUEST_UPDATE"), SHIPMENT_UPDATE("SHIPMENT_UPDATE");

    private final String eventName;

    DataNotificationEventName(String eventName) {

        this.eventName = eventName;
    }

    public String getEventName() {

        return this.eventName;
    }
}
