package com.oneassist.serviceplatform.commons.mastercache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTransitionConfigRepository;

/**
 * @author alok.singh
 */
@Configuration
@Component("serviceRequestTransitionConfigCache")
public class ServiceRequestTransitionConfigCache extends InMemoryCache<List<ServiceRequestTransitionConfigEntity>> {

	private final Logger logger = Logger.getLogger(ServiceRequestTransitionConfigCache.class);
	
	@Autowired
	private ServiceRequestTransitionConfigRepository serviceRequestTransitionConfigRepository;

	Map<String, List<ServiceRequestTransitionConfigEntity>>  serviceRequestTransitionConfigMap = new HashMap<String, List<ServiceRequestTransitionConfigEntity>>();
	
	@Override
	protected Map<String, List<ServiceRequestTransitionConfigEntity>> getAllFromDB() {
		
		List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigList = serviceRequestTransitionConfigRepository.findServiceRequestTransitionConfigByStatus(Constants.ACTIVE);
		
		if ( null != serviceRequestTransitionConfigList && !serviceRequestTransitionConfigList.isEmpty() )
		{
			List<ServiceRequestTransitionConfigEntity> serviceReqeustTransitionConfigList = null;
			for (ServiceRequestTransitionConfigEntity serviceRequestTransitionConfigDto: serviceRequestTransitionConfigList)
			{
				if(serviceRequestTransitionConfigMap.get(serviceRequestTransitionConfigDto.getServiceRequestType()) == null){
					serviceReqeustTransitionConfigList = new ArrayList<>();
					serviceReqeustTransitionConfigList.add(serviceRequestTransitionConfigDto);
					serviceRequestTransitionConfigMap.put(serviceRequestTransitionConfigDto.getServiceRequestType(), serviceReqeustTransitionConfigList);
				}else{
					serviceReqeustTransitionConfigList = serviceRequestTransitionConfigMap.get(serviceRequestTransitionConfigDto.getServiceRequestType());
					serviceReqeustTransitionConfigList.add(serviceRequestTransitionConfigDto);
					serviceRequestTransitionConfigMap.put(serviceRequestTransitionConfigDto.getServiceRequestType(), serviceReqeustTransitionConfigList);
				}
			}
		}
		logger.debug("serviceRequestTransitionConfigMap >>> "+serviceRequestTransitionConfigMap);
		return serviceRequestTransitionConfigMap;
	}


	@Override
	protected List<ServiceRequestTransitionConfigEntity> getFromDB(String key) {
	
		List<ServiceRequestTransitionConfigEntity> serviceRequestTransitionConfigEntity = serviceRequestTransitionConfigMap.get(key);
		return serviceRequestTransitionConfigEntity;
	}
}