package com.oneassist.serviceplatform.commons.workflowmanager;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;

@Configuration
@Component("workflowManager")
public interface IWorkflowManager {

	public Map<String, String> startActivitiProcess(String activitiKey, Map<String, Object> activitiMap) throws BusinessServiceException;
	public void completeActivitiTask(Long activitiProcId, String remarks, String userId, WorkflowStage workflowStage);
	public Map<String, List<String>> getAllTaskNameForAllDeployments();
	public void setVariable(String executionId, String variableName, String value);
	public String getVariable(String executionId, String variableName);
}
