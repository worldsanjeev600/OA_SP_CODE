package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestStatus {
    CREATED("C", "Created"),
    CLOSED("X", "Closed"),
    ASSIGNED("A", "Assigned"),
    PENDING("P", "Pending"),
    COMPLETED("CO", "Completed"),
    ONHOLD("OH", "Onhold"),
    INPROGRESS("IP", "Inprogress"),
    FAILED("F", "Failed"),
    CLOSEDUNRESOLVED("CUNR", "Closed Unresolved"),
    CLOSEDRESOLVED("CRES", "Closed Resolved"),
    CLOSEDREJECTED("CREJ", "Closed Rejected");

    private final String status;

    private final String value;

    ServiceRequestStatus(String status, String value) {

        this.status = status;
        this.value = value;
    }

    public String getStatus() {

        return this.status;
    }

    public String getValue() {

        return value;
    }

    public static ServiceRequestStatus getServiceRequestStatus(String status) {

        for (ServiceRequestStatus enumstatus : ServiceRequestStatus.values()) {
            if (enumstatus.getStatus().equals(status)) {
                return enumstatus;
            }
        }
        return null;
    }
}
