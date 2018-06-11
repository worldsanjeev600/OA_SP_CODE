package com.oneassist.serviceplatform.commons.workflowmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.WorkflowStage;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;

@Component
public class ActivitiWorkflowManager implements IWorkflowManager {

	private static final Logger logger = Logger.getLogger(ActivitiWorkflowManager.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;

	@Override
	public void completeActivitiTask(Long activitiProcId, String remarks, String userId, WorkflowStage workflowStage) {
		logger.info("Inside completeActivitiTask()..." +activitiProcId + workflowStage);
		Task task = storeRemarks(activitiProcId.toString(), remarks);
		if (task != null) {
			if (!com.google.common.base.Strings.isNullOrEmpty(userId)) {
				// taskService.claim(task.getId(), userId);
				task.setAssignee(userId);
				taskService.saveTask(task);
			}
			// check for task name completeActivitiTask
			if (workflowStage != null && (task.getName().equals(workflowStage.getWorkflowTaskName()) || 
					workflowStage.equals(WorkflowStage.CLOSE_SERVICE_REQEUST))) {
				taskService.complete(task.getId());
			}
		}
	}

	private Task storeRemarks(String procInstanceId, String remarks) {
		Task task = taskService.createTaskQuery().processInstanceId(procInstanceId).singleResult();
		if (task != null) {
			taskService.addComment(task.getId(), procInstanceId, remarks);
		}
		return task;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	@Override
	public Map<String, List<String>> getAllTaskNameForAllDeployments() {

		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (ProcessDefinition processDefinition : processDefinitionList) {
			if (processDefinition.getId().startsWith("SP_HA")) {
				ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
				List<String> taskName = new ArrayList<String>();

				for (ActivityImpl activityImpl : processDefinitionEntity.getActivities()) {
					String type = (String) activityImpl.getProperty("type");
					String name = (String) activityImpl.getProperty("name");
					if (type.equalsIgnoreCase("UserTask")) {
						taskName.add(name);
					}
				}
				map.put(processDefinition.getId(), taskName);
			}
		}

		return map;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Override
	public void setVariable(String executionId, String variableName, String value){
		runtimeService.setVariable(executionId, variableName, value);
	}
	@Override
	public String getVariable(String executionId, String variableName){
		return (String)runtimeService.getVariable(executionId, variableName);
	}

	@Override
	public Map<String, String> startActivitiProcess(String activitiKey, Map<String, Object> activitiMap) throws BusinessServiceException{
		// TODO Auto-generated method stub
		//RVW: Remove try/catch pair
		ProcessInstance processInstance = null;
		Map<String, String> responseMap = null;
		try {
			logger.info("Inside ActivitiUtils startActivitiProcess() start" + activitiKey);
			if (activitiKey != null) {
				processInstance = runtimeService.startProcessInstanceByKey(activitiKey, activitiMap);
				if (processInstance != null) {
					responseMap = new HashMap<String, String>();
					responseMap.put(Constants.WORKFLOW_PROCESS_ID, processInstance.getId());
					responseMap.put(Constants.WORKFLOW_PROC_DEF_KEY, processInstance.getProcessDefinitionId());
				} else {
					throw new Exception("processInstance = null");
				}
			} else {
				throw new Exception("activitiKey = " + activitiKey);
			}
		} catch (Exception e) {
			logger.error("Inside ActivitiUtils startActivitiProcess(): ", e);
			throw new BusinessServiceException("Error in starting activiti: ", e);
		}
		logger.info("Inside ActivitiUtils startActivitiProcess() ends");
		return responseMap;
	}	
}
