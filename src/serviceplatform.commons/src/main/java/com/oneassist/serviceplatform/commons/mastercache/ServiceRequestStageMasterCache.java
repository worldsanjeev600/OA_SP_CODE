package com.oneassist.serviceplatform.commons.mastercache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestStageMstRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author alok.singh
 */
@Configuration
@Component("serviceRequestStageMasterCache")
public class ServiceRequestStageMasterCache extends InMemoryCache<List<ServiceRequestStageMstEntity>> {

    private final Logger logger = Logger.getLogger(ServiceRequestStageMasterCache.class);

    @Autowired
    private ServiceRequestStageMstRepository serviceRequestStageMstRepository;

    Map<String, List<ServiceRequestStageMstEntity>> serviceRequestStageMasterMap = null;

    @Override
    protected Map<String, List<ServiceRequestStageMstEntity>> getAllFromDB() {

        List<ServiceRequestStageMstEntity> serviceRequestStageMasterList = serviceRequestStageMstRepository.findByStatusOrderByServiceRequestTypeIdAscStageOrderAsc(Constants.ACTIVE);

        if (null != serviceRequestStageMasterList && !serviceRequestStageMasterList.isEmpty()) {
            serviceRequestStageMasterMap = new HashMap<String, List<ServiceRequestStageMstEntity>>();
            List<ServiceRequestStageMstEntity> stageMasterList = null;
            for (ServiceRequestStageMstEntity serviceRequestStageMasterDto : serviceRequestStageMasterList) {
                if (serviceRequestStageMasterMap.get(serviceRequestStageMasterDto.getServiceRequestTypeId()) == null) {
                    stageMasterList = new ArrayList<>();
                    stageMasterList.add(serviceRequestStageMasterDto);
                    serviceRequestStageMasterMap.put(serviceRequestStageMasterDto.getServiceRequestTypeId(), stageMasterList);
                } else {
                    stageMasterList = serviceRequestStageMasterMap.get(serviceRequestStageMasterDto.getServiceRequestTypeId());
                    stageMasterList.add(serviceRequestStageMasterDto);
                    serviceRequestStageMasterMap.put(serviceRequestStageMasterDto.getServiceRequestTypeId(), stageMasterList);
                }
            }
        }
        logger.debug("serviceRequestStageMasterMap >>> " + serviceRequestStageMasterMap);
        return serviceRequestStageMasterMap;
    }

    @Override
    protected List<ServiceRequestStageMstEntity> getFromDB(String key) {

    	Map<String, List<ServiceRequestStageMstEntity>> serviceRequestStageMstEntity = getAllFromDB();
        return serviceRequestStageMstEntity.get(key);
    }

}
