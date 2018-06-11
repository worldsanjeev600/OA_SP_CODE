package com.oneassist.serviceplatform.services.servicerequest.actioncommands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commands.BaseActionCommand;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

@Component
public class ServiceRequestActionCommandFactory<TInput, TResult> {
	
    @Autowired
    private SubmitFeedbackCommand submitFeedbackCommand;
    
    @Autowired
    private AssignCommand assignCommand;
    
    @Autowired
    private ScheduleCommand scheduleCommand;
    
    @Autowired
    private CancelCommand cancelCommand;
    
    @Autowired
    private CloseCommand closeCommand;
    
    @Autowired
    private UpdateStatusCommand updateStatusCommand;
    
    @Autowired
    private UpdateSRonEventCommand updateSRonEventCommand;
    
    @Autowired
    private UpdateWorkFlowDataCommand updateWorkFlowDataCommand;
    
    @Autowired
    private UpdateWorkflowDataOnEventCommand updateWorkflowDataOnEventCommand;
   
	public BaseActionCommand<ServiceRequestDto, ServiceResponseDto> getServiceRequestActionCommand(ServiceRequestUpdateAction serviceRequestUpdateAction) throws Exception {	
		BaseActionCommand<ServiceRequestDto, ServiceResponseDto> actionCommand = null;
		
		switch(serviceRequestUpdateAction) {
		case SUBMIT_SERVICE_REQUEST_FEEDBACK : {
			actionCommand = this.submitFeedbackCommand;
			break;
		}
		case ASSIGN : {
            actionCommand = this.assignCommand;
            break;
        }
		case RESCHEDULE_SERVICE : {
            actionCommand = this.scheduleCommand;
            break;
        }
		case CANCEL_SERVICE : {
		    actionCommand = this.cancelCommand;
            break;
		}
		case SERVICE_REQUEST_STATUS : {
		    actionCommand = this.updateStatusCommand;
            break;
		}
		case UPDATE_SERVICE_REQUEST_ON_EVENT : {
		    actionCommand = this.updateSRonEventCommand;
            break;
		}
		case WF_DATA : {
            actionCommand = this.updateWorkFlowDataCommand;
            break;
        }
		case UPDATE_WF_DATA_ON_EVENT : {
            actionCommand = this.updateWorkflowDataOnEventCommand;
            break;
        }
		case CLOSE_SERVICE_REQUEST : {
            actionCommand = this.closeCommand;
            break;
        }
		default:
			throw new Exception("No command found for the given action: " + serviceRequestUpdateAction.toString());
		}
		
		return actionCommand;
	}
}