package com.oneassist.serviceplatform.commons.mastercache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.service.IServiceTaskMasterService;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceTaskDto;

/**
 * @author
 */
@Configuration
@Component("serviceTaskMasterCache")
public class ServiceTaskMasterCache extends InMemoryCache<ServiceTaskDto> {

	@Autowired
	private IServiceTaskMasterService serviceTaskMasterService;

	
	@Override
	protected Map<String,ServiceTaskDto> getAllFromDB() {
		
		List<ServiceTaskDto> serviceTaskList = null;
		serviceTaskList = serviceTaskMasterService.getAllServiceTaskConfig();
		
		Map<String, ServiceTaskDto>  serviceTaskMasterMap = new HashMap<String, ServiceTaskDto>();
		
		if ( null != serviceTaskList && !serviceTaskList.isEmpty() )
		{
			for (ServiceTaskDto serviceTaskDto: serviceTaskList)
			{
				serviceTaskMasterMap.put(serviceTaskDto.getServiceTaskId()+"", serviceTaskDto);
			}
		}
				
		return serviceTaskMasterMap;
	}


	@Override
	protected ServiceTaskDto getFromDB(String key) {
	
		ServiceTaskDto  serviceTaskDto = serviceTaskMasterService.getServiceTaskByTaskId(key);
		return serviceTaskDto;
	}
	
}