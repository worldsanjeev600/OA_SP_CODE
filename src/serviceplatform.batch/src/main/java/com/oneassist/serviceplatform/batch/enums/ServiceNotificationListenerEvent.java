package com.oneassist.serviceplatform.batch.enums;

public enum ServiceNotificationListenerEvent {
	ADVICE_UPDATE("ADVICE_UPDATE");

    private final String eventType;

    ServiceNotificationListenerEvent(String eventType) {

        this.eventType = eventType;
    }

    public String getEventType() {

        return this.eventType;
    }

    public static ServiceNotificationListenerEvent getServiceNotificationListenerEvent(String eventType) {
        for (ServiceNotificationListenerEvent enumstatus : ServiceNotificationListenerEvent.values()) {
            if (enumstatus.getEventType().equals(eventType)) {
                return enumstatus;
            }
        }
        return null;
    }
}
