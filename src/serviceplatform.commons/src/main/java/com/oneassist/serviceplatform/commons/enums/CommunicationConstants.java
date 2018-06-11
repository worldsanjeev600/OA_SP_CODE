package com.oneassist.serviceplatform.commons.enums;

/**
 * 
 */
public enum CommunicationConstants {

    COMM_PARTNER_BU_CODE("COMM_PARTNER_BU_CODE"),
    COMM_TECHNICIAN_ASSIGNEE_ID("COMM_TECHNICIAN_ASSIGNEE_ID"),
    COMM_TECHNICIAN_PROFILE_DATA("COMM_TECHNICIAN_PROFILE_DATA");

    private final String value;

    private CommunicationConstants(String value) {

        this.value = value;
    }

    public String getValue() {

        return this.value;
    }
}