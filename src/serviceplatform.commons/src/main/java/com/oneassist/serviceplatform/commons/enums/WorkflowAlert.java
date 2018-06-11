package com.oneassist.serviceplatform.commons.enums;

public enum WorkflowAlert {

    PROCURE_SPARE("SR","PROCURE SPARE"),
    RETURN_MACHINE_TO_CUSTOMER("MTC", "RETURN MACHINE TO CUSTOMER");
	
	private final String workflowAlertCode;
	
	private final String workflowAlertName;
	
	WorkflowAlert(String workflowAlertCode, String workflowAlertName) {
		this.workflowAlertCode = workflowAlertCode;
		this.workflowAlertName = workflowAlertName;
	}
	
	
	public String getWorkflowAlertCode() {
		return workflowAlertCode;
	}

	public String getWorkflowAlertName() {
		return workflowAlertName;
	}

	public static WorkflowAlert getWorkflowAlert(String workflowStageCode) {
        for (WorkflowAlert workflowAlert : WorkflowAlert.values()) {
            if (workflowAlert.getWorkflowAlertCode().equals(workflowStageCode)) {
                return workflowAlert;
            }
        }
        return null;
	}
}