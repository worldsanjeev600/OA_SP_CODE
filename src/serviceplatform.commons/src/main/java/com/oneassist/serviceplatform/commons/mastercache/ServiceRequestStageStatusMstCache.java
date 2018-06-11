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
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestStageStatusMstRepository;

/**
 * @author alok.singh
 */
@Configuration
@Component("serviceRequestStageStatusMstCache")
public class ServiceRequestStageStatusMstCache extends InMemoryCache<List<ServiceRequestStageStatusMstEntity>> {

	private final Logger logger = Logger.getLogger(ServiceRequestStageStatusMstCache.class);
	
	@Autowired
	private ServiceRequestStageStatusMstRepository serviceRequestStageStatusMstRepository;

	Map<String, List<ServiceRequestStageStatusMstEntity>>  serviceRequestStageStatusMap = new HashMap<String, List<ServiceRequestStageStatusMstEntity>>();
	
	@Override
	protected Map<String, List<ServiceRequestStageStatusMstEntity>> getAllFromDB() {
		
		List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusList = serviceRequestStageStatusMstRepository.findServiceRequestStageStatusMstByStatus(Constants.ACTIVE);
		
		if ( null != serviceRequestStageStatusList && !serviceRequestStageStatusList.isEmpty() )
		{
			List<ServiceRequestStageStatusMstEntity> stageStatusMasterList = null;
			for (ServiceRequestStageStatusMstEntity serviceRequestStageStatusDto: serviceRequestStageStatusList)
			{
				if(serviceRequestStageStatusMap.get(serviceRequestStageStatusDto.getServiceRequestType()) == null){
					stageStatusMasterList = new ArrayList<>();
					stageStatusMasterList.add(serviceRequestStageStatusDto);
					serviceRequestStageStatusMap.put(serviceRequestStageStatusDto.getServiceRequestType(), stageStatusMasterList);
				}else{
					stageStatusMasterList = serviceRequestStageStatusMap.get(serviceRequestStageStatusDto.getServiceRequestType());
					stageStatusMasterList.add(serviceRequestStageStatusDto);
					serviceRequestStageStatusMap.put(serviceRequestStageStatusDto.getServiceRequestType(), stageStatusMasterList);
				}
			}
		}
		logger.debug("serviceRequestStageStatusMap >>> "+serviceRequestStageStatusMap);
		return serviceRequestStageStatusMap;
	}


	@Override
	protected List<ServiceRequestStageStatusMstEntity> getFromDB(String key) {
	
		List<ServiceRequestStageStatusMstEntity> serviceRequestStageStatusMstEntity = serviceRequestStageStatusMap.get(key);
		return serviceRequestStageStatusMstEntity;
	}
	
}