package com.oneassist.serviceplatform.commons.service;

import java.util.List;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;

public interface IServiceTaskMasterService {
	public List<ServiceTaskDto > getAllServiceTaskConfig(); 

	public ServiceTaskDto getServiceTaskByTaskId(String serviceTaskId);
	
	List<ServiceTaskDto> getServiceTaskByTaskType(String serviceTaskType);
}
