package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestType {

    // Logistics related requests
    PICKUP("Pickup"),
    DELIVERY("Delivery"),
    COURTESYPICKUP("Courtesy Pickup"),
    COURTESYDELIVERY("Courtesy Delivery"),
    ASCPICKUP("ASC Pickup"),
    ASCDELIVERY("ASC Delivery"),

    // After sale service related requests
    WHC_INSPECTION("WHC_INSPECTION"),
    HA_EW("HA_EW", "SP_HA_EW"),
    HA_BR("HA_BR", "SP_HA_BR"),
    HA_AD("HA_AD", "SP_HA_AD"),
    HA_BD("HA_BD", "SP_HA_BD"),
    HA_FR("HA_FR", "SP_HA_FR"),

    // Personal Electronics related requests
    PE_ADLD("PE_ADLD"),
    PE_THEFT("PE_THEFT"),
    PE_EW("PE_EW");

    private final String requestType;

    private final String requestTypeActivitiKey;

    ServiceRequestType(String requestType) {

        this(requestType, null);
    }

    ServiceRequestType(String requestType, String requestTypeActivitiKey) {

        this.requestType = requestType;
        this.requestTypeActivitiKey = requestTypeActivitiKey;
    }

    public String getRequestType() {

        return this.requestType;
    }

    public String getRequestTypeActivitiKey() {

        return this.requestTypeActivitiKey;
    }

    public static ServiceRequestType getServiceRequestType(String requestType) {
        for (ServiceRequestType enumstatus : ServiceRequestType.values()) {
            if (enumstatus.getRequestType().equals(requestType)) {
                return enumstatus;
            }
        }
        return null;
    }
}
