package com.oneassist.serviceplatform.commons.enums;

public enum WorkflowStageStatus {

    SUCCESSFUL("SU"),
    REFUND("RF"),
    BER("BER"),
    BER_APPROVED("BERA", "BER-Approved"),
    APPROVED("AP", "Approved"),
    REJECTED("RJ", "Rejected"),
    DOC_UPLOADED("uploaded"),
    DOC_VERIFIED("verified"),
    VERIFICATION_UNSUCCESSFUL("VU", "Unsuccessful"),
    UNRESOLVED("UNRESOLVED", "Closed UnResolved"),
    IN_PROGRESS("IP", "In Progress"),
    PENDING("P", "Pending"),
    COMPLETED("CO", "Completed"),
    BER_REJECTED("BERR", "BER-Rejected"),
    VERIFICATION_UNSUCCESSFU("BERR", "BER-Rejected"),
    AWAITING_APPROVAL("AA", "Awaiting Approval"),
    PAYMENT_PENDING("CPP", "Payment Pending"),
    VERIFICATION_PENDING("VP", "Verification Pending"),
    DOCUMENT_UPLOAD_PENDING("DUP", "Document Upload Pending"),
    APPROVED_STANDBY("AS", "Approved Standby"),
    APPROVED_COMPLETE("AC", "Approved Complete"),
    BER_APPROVED_STANDBY("BERAS", "BER-Approved Standby"),
    BER_APPROVED_COMPLETE("BERAC", "BER-Approved Complete");

    private final String workflowStageStatus;
    private final String workflowStageStatusCode;

    WorkflowStageStatus(String workflowStageStatus) {

        this(workflowStageStatus, workflowStageStatus);
    }

    WorkflowStageStatus(String workflowStageStatusCode, String workflowStageStatus) {

        this.workflowStageStatus = workflowStageStatus;
        this.workflowStageStatusCode = workflowStageStatusCode;
    }

    public String getWorkflowStageStatus() {

        return this.workflowStageStatus;
    }

    public static WorkflowStageStatus getWorkflowStageStatus(String workflowStageStatus) {
        for (WorkflowStageStatus workflowStage : WorkflowStageStatus.values()) {
            if (workflowStage.getWorkflowStageStatus().equals(workflowStageStatus)) {
                return workflowStage;
            }
        }

        return null;
    }

    public String getWorkflowStageStatusCode() {

        return this.workflowStageStatusCode;
    }

    public static WorkflowStageStatus getWorkflowStageStatusCode(String workflowStageStatusCode) {
        for (WorkflowStageStatus workflowStage : WorkflowStageStatus.values()) {
            if (workflowStage.getWorkflowStageStatusCode().equals(workflowStageStatusCode)) {
                return workflowStage;
            }
        }
        return null;
    }
}
