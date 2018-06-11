package com.oneassist.serviceplatform.commons.mastercache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTypeMstRepository;

/**
 * @author
 */
@Configuration
@Component("serviceRequestTypeMasterCache")
public class ServiceRequestTypeMasterCache extends InMemoryCache<ServiceRequestTypeMstEntity> {

	private final Logger logger = Logger.getLogger(ServiceRequestTypeMasterCache.class);
	
	@Autowired
	private ServiceRequestTypeMstRepository serviceRequestTypeMstRepository;

	Map<String, ServiceRequestTypeMstEntity>  serviceRequestTypeMasterMap = new HashMap<String, ServiceRequestTypeMstEntity>();
	
	@Override
	protected Map<String,ServiceRequestTypeMstEntity> getAllFromDB() {
		
		List<ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMstRepository.findServiceRequestTypeMstByStatus(Constants.ACTIVE);
		
		if ( null != serviceRequestTypeMasterList && !serviceRequestTypeMasterList.isEmpty() )
		{
			for (ServiceRequestTypeMstEntity serviceRequestTypeMasterDto: serviceRequestTypeMasterList)
			{
				serviceRequestTypeMasterMap.put(serviceRequestTypeMasterDto.getServiceRequestType(), serviceRequestTypeMasterDto);
			}
		}
		logger.debug("serviceRequestTypeMasterMap >>> "+serviceRequestTypeMasterMap);
		return serviceRequestTypeMasterMap;
	}


	@Override
	protected ServiceRequestTypeMstEntity getFromDB(String key) {
	
		ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMstRepository.findServiceRequestTypeMstByServiceRequestTypeAndStatus(key, Constants.ACTIVE);
		return serviceRequestTypeMstEntity;
	}
	
}