package com.oneassist.serviceplatform.externalcontracts;

/**
 * 
 * Event enumeration.
 *
 */
public enum ClaimLifecycleEvent {

    CLAIM_INTIMATION("ClaimIntimation"),
    DOCUMENT_UPLOAD("DocumentUpload"),
    DOCUMENT_UPLOAD_TEMPLATE("DocumentUploadTemplate"),
    RAISE_SHIPMENT("RaiseShipment"),
    FEDEX_PROCESS_SHIPMENT("FedexProcessShipment"),
    FEDEX_PICKUP("FedexPickup"),
    FEDEX_TRACK("FedexTrack"),
    CLOSE_CLAIM("CloseClaim"),
    ECOM_PINCODE("EcomPincode"),
    ECOM_AWB("EcomAWB"),
    ECOM_REVERSE_PICKUP("EcomReversePickup"),
    ECOM_FORWARD_SHIPMENT("EcomForwadShipment"),
    ECOM_TRACK("EcomTrack"),
    CLOSE_REPAIR_SR("CloseRepairSR"),
    WHC_COMPLETE_INSPECTION_SR("WHCInspectionSR"),
    LOGINEXT_SHIPMENT("LoginextShipment"),
    LOGINEXT_SHIPMENT_CANCELLATION("LoginextCancelShipment"),
    DHL_PICKUP("DHLPickup"),
    DHL_SHIPMENT("DHLShipment"),
    CREATE_EXTERNAL_SERVICE_REQUEST("CreateExternalServiceRequest"),
    UPDATE_EXTERNAL_SERVICE_REQUEST("UpdateExternalServiceRequest");

    private String description;

    ClaimLifecycleEvent(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
