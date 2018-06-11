package com.oneassist.serviceplatform.commons.constants;

public class Constants {

    /*
     * Constants guideline 1. If it is a logical set of constants try to define it in enum class. For e.g. set of eventCodes (we have already moved action and now we need to move eventCodes also to
     * new enum.) 2. If we really need a constant then let’s define it in the file where we are using it. In most of the cases, constants are really used in 1-2 files only. 3. If you feel you need to
     * define it in the common constants file then most likely it is a candidate for configuration. For e.g. yyyyMMddHHmmssS, ddMMyyyyhhmmssA, UNAUTHORIZED_ACCESS,
     * COURTESY_SR_CREATION_SMS_EMAIL_TEMPLATE, EW_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT, etc.
     */

    public static final String SERVICE_SCHEDULE_DATETIME_FORMAT = "dd-MMM-yyyy hh:mm a";
    public static final int WORKFLOW_DOC_UPLOADED_STAGE = 2;

    public static final String WORKFLOW_DOC_VERIFICATION_ACTIVITI_VAR = "DocVerification";

    // constants
    public static final String ACTIVE = "A";
    public static final String EXPIRED = "X";

    public static final String SUCCESS = "success";

    public static final String USER_ADMIN = "admin";

    // cache config
    public static final String PINCODE_MASTER_CACHE = "pinCodeMasterCache";
    public static final String HUB_MASTER_CACHE = "hubMasterCache";
    public static final String PARTNER_MASTER_CACHE = "partnerMasterCache";
    public static final String SERVICE_MASTER_CACHE = "serviceMasterCache";
    public static final String SYSTEM_CONFIG_MASTER_CACHE = "systemConfigMasterCache";
    public static final String PARTNER_BU_CACHE = "partnerBUCache";
    public static final String PRODUCT_MASTER_CACHE = "productMasterCache";
    public static final String SERVICE_REQUEST_MASTER_CACHE = "serviceRequestTypeMasterCache";

    public static final String CREATE_ACTION_FLAG = "create";
    public static final String UPDATE_ACTION_FLAG = "update";
    public static final String PARTNER_TYPE_FLAG = "3";
    public static final String PARTNER_TYPE_SERVICE_CENTRE_FLAG = "1";
    public static final String BUSINESS_UNIT_TYPE_SERVICE_CENTRE_FLAG = "SERVICE_CENTER";

    public static final String SHIPMENT_STAGES = "SHIPMENT_STAGE";
    public static final String SHIPMENT_STATUS = "SHIPMENT_STATUS";
    public static final String STAGE_NAME = "stageName";
    public static final String STAGE_VALUE = "stageCode";
    public static final String STATUS_NAME = "statusName";
    public static final String STATUS_VALUE = "statusCode";
    public static final String PARTNER_NAME = "partnerName";
    public static final String PARTNER_VALUE = "partnerCode";
    public static final String SERVICE_NAME = "serviceName";
    public static final String SERVICE_VALUE = "serviceId";
    public static final String HUB_NAME = "hubName";
    public static final String HUB_VALUE = "hubValue";
    public static final String PINCODE_NAME = "pincodeName";
    public static final String PINCODE_VALUE = "pincode";
    public static final String DOCUMENT_TYPE_ID = "docTypeId";
    public static final String DOCUMENT_TYPE_POD = "POD";
    public static final String DOCUMENT_TYPE_LABEL = "LABEL";
    public static final String BUSINESS_UNIT_NAME = "BUName";
    public static final String BUSINESS_UNIT_CODE = "BUCode";

    public static final String SERVICE_REQUEST_ID_LOWER = "serviceRequestId";

    // update Service Request Constants ends here
    public static final String YES_FLAG = "Y";
    public static final String NO_FLAG = "N";

    // Activiti related constants starts here...
    public static final String ACTIVITI_TASK_NAME = "taskName";
    public static final String ACTIVITI_TASK_ID = "taskId";

    public static final String ID = "ID";

    public static final String DEFAULT_CLOSURE_REMARKS_FOR_ACTIVTI_STAGE = "Completed";
    public static final String WORKFLOWDATA = "workflowData";
    public static final String WORKFLOWSTAGE = "workflowStage";
    public static final String SERVICESCHEDULESTARTTIME = "scheduleSlotStartDateTime";
    public static final String SERVICESCHEDULEENDTIME = "scheduleSlotEndDateTime";
    public static final String SPARE_PART_ATTRIBUTE_NAME = "spareParts";

    public static final String REQUEST_SOURCE_BATCH = "BATCH";
    public static final String SPARE_AVAILABLE_DATETIME = "SPARE_AVAILABLE_DATETIME";
    public static final String IS_LAST_STAGE_IC_DECISION = "IS_LAST_STAGE_IC_DECISION";
    public static final String SPAREPART = "SPAREPART";
    public static final String TRANSPORTATION = "TRANSPORTATION";

    public static final String ACTIVITI_VAR_IS_CUSOTMER_OPT_SELFREPAIR = "isCustomerOptForSelfRepair";
    public static final String ACTIVITI_VAR_IS_CLAUSE_PASSED = "ICClausePassed";
    public static final String ACTIVITI_VAR_IS_REPAIR_COMPLETED = "isRepairCompleted";
    public static final String ACTIVITI_VAR_CUSTOMER_ASKS_REFUND = "customerAsksRefund";
    public static final String ACTIVITI_VAR_IC_APPROVAL = "ICApproval";
    public static final String ACTIVITI_VAR_IS_ESTIMATEDINVOICE_VERIFIED = "isEstimatedInvoiceVerified";
    public static final String ACTIVITI_VAR_CLOSE_SERVICE_REQUEST = "shortCircuit";
    public static final String PARTNER = "PARTNER";
    public static final String PRODUCT = "PRODUCT";
    public static final String PARTNER_PRODUCT = "PARTNER_PRODUCT";

    public static final String INPROGRESS = "Inprogress";
    public static final String WORKFLOW_PROC_DEF_KEY = "WORKFLOW_PROC_DEF_KEY";
    public static final String WORKFLOW_PROCESS_ID = "WORKFLOW_PROCESS_ID";
    public static final String WORKFLOW_ALERT = "WORKFLOW_ALERT";
    public static final String STAGE_ORDER = "STAGE_ORDER";
    public static final String NOT_STARTED = "Not started";

    public static final String ACTIVITI_VAR_IS_ACCIDENTAL_DAMAGE = "isAccidentalDamage";
    public static final String SERVICE_TASK_INSPECTION_CHARGE = "INSPECTIONCHARGE";

    public static final String DELIM_HIFEN = "-";
    public static final String DELIM_SPACE = " ";
    public static final String COMMA = ",";

    public static final String MODIFIED_BY_CALLBACK_API = "CALLBACK";
    public static final String MODIFIED_BY_BATCH = "BATCH";
    public static final String SERVICE_TYPE = "serviceType";

    public static final Object MAX_ASSET_COUNT_KEY = "plan.planQtyCustom";

    public static final String DEVICE_BREAKDOWN_PARAM_NAME = "deviceBreakdownDetail";
    public static final String MOBILE_LOSS_PARAM_NAME = "mobileLossDetails";
    public static final String MOBILE_DAMAGE_PARAM_NAME = "mobileDamageDetails";

    public static final String CLAIM_COUNT_PARAM_NAME = "claimCount";
    public static final String DEFAULT_HUB_PARAM_NAME = "defaultHub";
    public static final String MODEL_VERSION_PARAM_NAME = "modelVersion";
    public static final String MU_PARAM_NAME = "mu";
    public static final String ASSET_INVOICE_PARAM_NAME = "assetInvoiceNo";
    public static final String MARKET_VALUE_PARAM_NAME = "icMarketValue";
    public static final String COURTESY_REQUIRED_PARAM_NAME = "courtesyRequired";

    public static final String DEVICE_WARRANTY_PARAM_NAME = "deviceWarranty";
    public static final String DOCS_NEVER_UPLOADED_PARAM_NAME = "docsNeverUploaded";
    public static final String DOCS_RECEIVED_VIA_EMAIL = "docsRecievedViaEmail";
    public static final String EXPECTED_DELIVERY_DATE_FORM = "expectedDeliveryDateForm";
    public static final String HUB_ID_PARAM_NAME = "hubId";
    public static final String PARTNER_BU_CODE_PARAM_NAME = "partnerBUCode";
    public static final String PICKUP_FROM_ASC_STATUS_PARAM_NAME = "pickupFromAscStatus";
    public static final String PLAN_CODE_PARAM_NAME = "planCode";
    public static final String REQUIREMENT_TRIGGERED_PARAM_NAME = "requirementTriggered";
    public static final String SERVICE_ID_PARAM_NAME = "serviceId";
    public static final String SIGMA_PARAM_NAME = "sigma";
    public static final String SUM_ASSURED_PARAM_NAME = "sumAssured";
    public static final String TAT_PARAM_NAME = "tat";
    public static final String TAT_VALUE_PARAM_NAME = "tatValue";
    public static final String PARTNER_ATTRIBUTE_DETAILS_PARAM_NAME = "claimPartnerAttributesdetails";
    public static final String START_DATE_PARAM_NAME = "startDate";

    public static final String DESC = "desc";
    public static final String ASC = "asc";

    public static final String SOURCE_QUEUE = "QUEUE";
    public static final String SP_AND_CMS_STAGE_MAPPING = "SP_AND_CMS_STAGE_MAPPING";
    public static final String SP_AND_CMS_STATUS_MAPPING = "SP_AND_CMS_STATUS_MAPPING";

    public static final String COMPLETE = "Complete";

    public static final String CMS_COMPLETE_STATUS_CODE = "C";
    public static final String CUSTOMER_ID = "customerId";

}
