package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestUpdateAction {

    ASSIGN("assign"),
    RESCHEDULE_SERVICE("schedule"),
    WF_DATA("updateWorkflowData"),
    SERVICE_REQUEST_STATUS("updateStatus"),
    SUBMIT_SERVICE_REQUEST_FEEDBACK("updateFeedback"),
    UPDATE_SERVICE_REQUEST_ON_EVENT("onEvent"),
    UPDATE_WF_DATA_ON_EVENT("updateWorkflowOnEvent"),
    CANCEL_SERVICE("cancelService"),
    CLOSE_SERVICE_REQUEST("closeServiceRequest");

    private final String requestUpdateAction;

    private ServiceRequestUpdateAction(String requestUpdateAction) {
        this.requestUpdateAction = requestUpdateAction;
    }

    public String getServiceRequestUpdateAction() {
        return this.requestUpdateAction;
    }

    public static ServiceRequestUpdateAction getServiceRequestUpdateAction(String requestUpdateAction) {
        for (ServiceRequestUpdateAction enumstatus : ServiceRequestUpdateAction.values()) {

            if (enumstatus.getServiceRequestUpdateAction().equals(requestUpdateAction)) {
                return enumstatus;
            }
        }

        return null;
    }
}
