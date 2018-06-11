package com.oneassist.serviceplatform.commons.enums;

public enum WorkflowStage {

    VISIT("Visit", "VE"),
    DOCUMENT_UPLOAD("documentUpload", "Document Upload", "DU"),
    VERIFICATION("Verification", "VR"),
    REPAIR_ASSESSMENT("repairAssessment", "Repair Assessment", "RA"),
    REPAIR("repair", "Repair", "RE"),
    CLAIM_SETTLEMENT("claimSettlement", "Claim Settlement", "CS"),
    SOFT_APPROVAL("SoftApproval"),
    INSPECTION_ASSESSMENT("inspectionAssessment"),
    COMPLETED("completed", "Completed", "CO"),
    IC_DECISION("insuranceDecision", "IC Decision", "ID"),
    IC_DOC("icDoc", "IC Doc", "ICD"),
    PARTNER_STAGE_STATUS("partnerStageStatus"),
    INSPECTION_STARTED("inspectionStarted"),
    CANCEL_INSPECTION("CANCELINSPECTION"),
    CLOSE_SERVICE_REQEUST("Close Service Request"),
    PICKUP("pickup", "Pick Up", "PU"),
    DELIVERY("delivery", "Delivery", "DE"),
    IC_DOC_SETTLEMENT("icDoc", "IC Doc Settlment", "DS");

    private final String workflowStageName;

    private final String workflowTaskName;

    private final String workflowStageCode;

    WorkflowStage(String workflowStageName) {

        this(workflowStageName, workflowStageName, workflowStageName);
    }

    WorkflowStage(String workflowStageName, String workflowStageCode) {

        this(workflowStageName, workflowStageName, workflowStageCode);
    }

    WorkflowStage(String workflowStageName, String workflowTaskName, String workflowStageCode) {

        this.workflowStageName = workflowStageName;
        this.workflowTaskName = workflowTaskName;
        this.workflowStageCode = workflowStageCode;
    }

    public String getWorkflowStageName() {

        return this.workflowStageName;
    }

    public String getWorkflowTaskName() {

        return this.workflowTaskName;
    }

    public String getWorkflowStageCode() {
        return this.workflowStageCode;
    }

    public static WorkflowStage getWorkflowStage(String workflowStageName) {
        for (WorkflowStage workflowStage : WorkflowStage.values()) {
            if (workflowStage.getWorkflowStageName().equals(workflowStageName)) {
                return workflowStage;
            }
        }

        return null;
    }

    public static WorkflowStage getWorkflowStageByCode(String workflowStageCode) {
        for (WorkflowStage workflowStage : WorkflowStage.values()) {
            if (workflowStage.getWorkflowStageCode().equals(workflowStageCode)) {
                return workflowStage;
            }
        }
        return null;
    }
}
