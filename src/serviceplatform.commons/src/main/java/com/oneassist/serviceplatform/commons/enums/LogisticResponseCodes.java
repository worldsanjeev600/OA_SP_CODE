package com.oneassist.serviceplatform.commons.enums;

public enum LogisticResponseCodes {
    RAISE_SHIPMENT_REQUEST_FAILED(20001),
    RAISE_SHIPMENT_REQUEST_SUCCESS(20002),
    DASHDOARD_DATA_FAILED(20003),
    DASHDOARD_DATA_SUCCESS(20004),
    UPDATE_SHIPMENT_REQUEST_FAILED(20005),
    UPDATE_SHIPMENT_REQUEST_SUCCESS(20006),
    REASSIGN_SHIPMENT_REQUEST_FAILED(20007),
    REASSIGN_SHIPMENT_REQUEST_SUCCESS(20008),
    SHIPMENT_DOESNOT_EXIST(20009),
    SHIPMENT_STAGE_VALIDATION_FAILED(20010),
    UPLOAD_SHIPMENT_DOCUMENT_SUCCESS(20011),
    UPLOAD_SHIPMENT_DOCUMENT_FAILED(20012),
    SHIPMENT_ID_EMPTY(20013),
    PARTNER_CODE_EMPTY(20014),
    SHIPMENT_CURRENT_STAGE_EMPTY(20015),
    SHIPMENT_CURRENT_STAGES_NOTCONFIGUED(20016),
    INVALID_SHIPMENT_CURRENT_STAGE(20017),
    INVALID_SHIPMENT_MODIFICATION_DATE_FORMAT(20018),
    INVALID_SHIPMENT_MODIFICATION_DATE(20019),
    SHIPMENT_DOESNOT_EXIST_SERVICEREQUEST(20020),
    SERVICEREQUEST_DOESNOT_EXIST(20021),
    SERVICEREQUEST_DATAGET_FAILED(20022),
    SHIPMENT_TRACKINGHISTORY_FAILED(20023),
    SHIPMENT_TRACKINGHISTORY_SUCCESS(20024),
    SHIPMENT_HISTORY_NOTFOUND(20025),
    SERVICEREQUEST_MULTIPLESHIPMENTS_ERROR(20026),
    SERVICEREQUEST_DATAGET_SUCCESS(20027),
    SHIPMENT_SEARCH_SUCCESS(20028),
    SHIPMENT_SEARCH_FAILED(20029),
    DOWNLOAD_SHIPMENT_DOCUMENT_SUCCESS(20030),
    DOWNLOAD_SHIPMENT_DOCUMENT_FAILED(20031),
    ALLOCATION_MASTER_NOT_EXISTS(20032),
    ALLOCATION_MASTER_FAILED(20033),
    SHIPMENT_DOESNOT_MATCH_LOGISTIC_PARTNERCODE(20034),
    SHIPMENT_STATUS_UPDATE_FAIL_INVALIDSTATUS(20035),
    SHIPMENT_STATUS_UPDATE_FAIL_DOESNOT_HAVE_LOGISTIC_PARTNERCODE(20036),
    NULL_RESPONSE_FROM_ECOM(30065),
    FAILED_TO_PLACE_ECOM_REQUEST(30066),
    NULL_AWB_GENERATION_RESPONSE_FROM_ECOM(30067),
    FAILED_TO_GENERATE_LABEL(30068);

    private final int responseCode;

    LogisticResponseCodes(int responseCode) {

        this.responseCode = responseCode;
    }

    public int getErrorCode() {

        return this.responseCode;
    }
}