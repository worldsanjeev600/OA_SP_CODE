package com.oneassist.serviceplatform.commons.enums;

public enum MasterData {

    PINCODE("pincodeDropdown"),
    HUB("hubDropdown"),
    SERVICE_MASTER("movementTypes"),
    PARTNER_MASTER("partners"),
    STAGES("shipmentStages"),
    STATUS("shipmentStatus"),
    DOC_TYPE("docTypes"),
    SERVICE_CENTRE("servicePartners"),
    SERVICE_PARTNER_BU("servicePartnerBU"),
    PRODUCTS("products"),
    DOCUMENT_TYPE("documentTypes"),
    SERVICE_CONFIG("serviceConfigs"),
    WORKFLOW_STAGES("workflowStages"),
    WORKFLOW_ALERTS("workflowAlerts"),
    SERVICES("services"),
    PARTNER_BU("partnerBU"),
    PARTNER_LIST("allPartners");

    private final String masterData;

    MasterData(String masterData) {

        this.masterData = masterData;
    }

    public String getMasterData() {

        return this.masterData;
    }

    public static MasterData getMasterData(String masterdata) {
        for (MasterData masterData : MasterData.values()) {
            if (masterData.getMasterData().equals(masterdata)) {
                return masterData;
            }
        }
        return null;
    }
}
