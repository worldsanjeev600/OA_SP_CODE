package com.oneassist.serviceplatform.batch.enums;

public enum ServiceRequestListenerEvent {
    CREATE_SERVICE_REQUEST("CreateServiceRequest"),
    RAISE_SHIPMENT("RaiseShipment"),
    FEDEX_PROCESS_SHIPMENT("FedexProcessShipment"),
    CLOSE_CLAIM("CloseClaim"),
    ECOM_PINCODE("EcomPincode"),
    LOGINEXT_SHIPMENT("LoginextShipment"),
    DHL_SHIPMENT("DHLShipment"),
    SERVICE_REQUEST_UPDATE("ServiceRequestUpdate"),
    CREATE_EXTERNAL_SERVICE_REQUEST("CreateExternalServiceRequest"),
    UPDATE_EXTERNAL_SERVICE_REQUEST("UpdateExternalServiceRequest");

    private final String eventType;

    ServiceRequestListenerEvent(String eventType) {

        this.eventType = eventType;
    }

    public String getEventType() {

        return this.eventType;
    }

    public static ServiceRequestListenerEvent getServiceRequestListenerEvent(String eventType) {
        for (ServiceRequestListenerEvent enumstatus : ServiceRequestListenerEvent.values()) {
            if (enumstatus.getEventType().equals(eventType)) {
                return enumstatus;
            }
        }
        return null;
    }
}
