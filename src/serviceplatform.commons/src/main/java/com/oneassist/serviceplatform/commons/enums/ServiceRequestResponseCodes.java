package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestResponseCodes {
    CREATE_SERVICE_REQUEST_FAILED(30001),
    CREATE_SERVICE_REQUEST_SUCCESS(30002),
    UPDATE_SERVICE_REQUEST_FAILED(30003),
    UPDATE_SERVICE_REQUEST_SUCCESS(30004),
    SERVICE_REQUEST_NOT_ASSOCIATED_WITH_SERVICETYPE_ERROR(30005),
    INVALID_SERVICE_REQUEST_TYPE(30006),
    UPLOAD_SERVICE_DOCUMENT_FAILED(30007),
    CREATE_SERVICE_DOCUMENT_SUCCESS(30008),
    SERVICE_START_TIME_INVALID_ERROR(30009),
    GET_SERVICE_REQUEST_FAILED(30010),
    GET_SERVICE_REQUEST_SUCCESS(30011),
    AUTHORIZATION_CODE_VALIDATION_FAILED(30012),
    AUTHORIZATION_CODE_VALIDATION_SUCCESS(30013),
    AUTHORIZATION_CODE_FETCH_SUCCESS(30014),
    AUTHORIZATION_CODE_FETCH_FAILED(30015),
    CALCULATE_SERVICECOST_CUSTOMER_FAILED(30016),
    CALCULATE_SERVICECOST_CUSTOMER_SUCCESS(30017),
    CALCULATE_SERVICECOST_ASSESSMENT_ID_REQUIRED_ERROR(30018),
    SERVICE_REQUEST_ASSIGNED_SEVICECENTRE(30019),
    SERVICE_REQUEST_NON_SERVICEABLE_PINCODE(30020),
    SERVICE_TECHNICIAN_PARTNER_CODE_NOTFOUND(30021),
    SERVICE_TECHNICIAN_PARTNER_BU_CODE_NOTFOUND(30022),
    CMS_DASHBOARD_SUCCESS(30023),
    CMS_DASHBOARD_FAILURE(30024),
    UPDATE_SERVICE_DOCUMENT_FAILED(30025),
    CREATE_REPAIR_REQUEST_SR_ALREADY_EXISTS(30026),
    CREATE_REPAIR_REQUEST_SR_NOTCLOSED_MEMBERSHIPID(30027),
    UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE(30036),
    UPDATE_SERVICE_REQUEST_INVALID_SERVICE_REQUEST_ID(30029),
    UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE_SPARE_REQ_RAISED(30030),
    UPDATE_SERVICE_REQUEST_NON_SERVICEABLE(30031),
    UPDATE_SERVICE_REQUEST_INVALID_TECHINICIAN(30032),
    UPDATE_SERVICE_REQUEST_INVALID_STATUS(30033),
    UPDATE_SERVICE_REQUEST_GET_TECHINICIAN_DETAILS_FAILED(30034),
    UPDATE_SERVICE_REQUEST_GET_STATUS_DETAILS_FAILED(30035),
    SERVICE_REQUEST_ID_NOT_EXISTS(30028),
    UPDATE_SERVICE_REQUEST_VISIT_NOT_SCHEDULED(30037),
    UPDATE_SERVICE_REQUEST_DUPLICATE_TECHNICIAN_ASSIGN(30038),
    UPDATE_SERVICE_REQUEST_DUPLICATE_STATUS_UPDATE(30039),
    UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE_TRANSPORT_RAISED(30040),
    PINCODE_SERVICABLE(30041),
    PINCODE_NOT_SERVICABLE(30042),
    PINCODE_SEARCH_INVALID_SERVICE_TYPE(30063),
    PINCODE_SEARCH_SUCCESS(30064),
    PINCODE_SEARCH_FAILED(30065),
    UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE_SERVICETIME_ELAPSED(30043),
    UPDATE_SERVICE_REQUEST_INVALID_EVENT_CODE_PAYMENT_ALREADY_DONE(30044),
    INSPECTION_REQUEST_ASSIGNED_SEVICECENTRE(30045),
    INSPECTION_REQUEST_NON_SERVICEABLE_PINCODE(30046),
    PRODUCTS_NOT_AVAILABLE(30047),
    PRODUCTS_REQUEST_FAILED(30048),
    CREATE_SERVICE_REQUEST_FAILED_DUE_TO_NONSERVICEABLE_PINCODE(30049),
    CREATE_REPAIR_REQUEST_SR_NOTCLOSED_MEMBERSHIPID_AND_PRODUCT(30050),
    CREATE_INSPECTION_REQUEST_ORDERID_ALREADY_EXISTS(30052),
    UPDATE_SERVICE_REQUEST_INVALID_STATUS_FOR_RESCHEDULE(30053),
    REQUEST_CANNOT_BE_PROCESSED_FOR_PINCODE(30054),
    REQUEST_DOESNTEXIST_FOR_ORDERID(30055),
    SERVICE_SLOT_SCHEDULE_ERROR(30056),
    INSPECTION_REQUEST_COMPLETED_SUCCESSFULLY(30057),
    SERVICE_DOCUMENT_DELETE_SUCCESS(30058),
    SERVICE_DOCUMENT_DELETE_FAILED(30059),
    SERVICE_REQUEST_CREATED(30060),
    INVALID_ASSIGNEE(30061),
    GET_SERVICE_REQUEST_INVALID_DATE_FROMAT_FAILED(30062),
    INVALID_DATE_FORMAT(19),
    INVALID_DOCUMENT_TYPE(30063),
    INVALID_SERVICE_REQUEST_ID(30064),
    INVALID_STAGE_FOR_RESCHEDULE_SERVICE(30069),
    SERVICE_REQUEST_ALREADY_COMPLETE(30075),
    SERVICE_REQUEST_CLAIM_NOT_ELIGIBLE(30076),
    FAILED_CLAIM_RETRIEVAL_STATUS(30077),
    SERVICE_REQUEST_CREATE_ASSET_FAILED(30078),
    SERVICE_REQUEST_CLAIM_ELIGIBLE(30079),
    NO_ASSET_DETAILS_FOUND(30080),
    NUMBER_OF_ASSET_IS_GREATER_THAN_MAX_QTY(30081),
    MISSING_DOCUMENT(30082),
    MAXIMUM_ASSETS_HAS_BEEN_ADDED(30083),
    COMPLETE_INSPECTION_VALIDATION_FAILED(30084),
    DOCUMENT_UPLOAD_COMPLETION_VALIDATION_FAILED(30085),
    MANDATORY_DOCUMENT_MISSING(30086),
    EDIT_CLAIM_RESTRICTED(30087),
    RAISE_CLAIM_ELIGIBILITY_FAILED(30088);


    private final int responseCode;

    ServiceRequestResponseCodes(int responseCode) {

        this.responseCode = responseCode;
    }

    public int getErrorCode() {

        return this.responseCode;
    }
}
