package com.oneassist.serviceplatform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.entities.ServicePartnerMappingEntity;
import com.oneassist.serviceplatform.commons.repositories.ServicePartnerMappingRepository;
import com.oneassist.serviceplatform.contracts.dtos.ServicePartnerMappingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author
 */
@Configuration
@Component("servicePartnerMappingCache")
public class ServicePartnerMappingCache extends InMemoryCache<ServicePartnerMappingDto> {

    private static String ACTIVE = "A";

    @Autowired
    private ServicePartnerMappingRepository servicePartnerMappingRepository;

    @Override
    protected Map<String, ServicePartnerMappingDto> getAllFromDB() {

        List<ServicePartnerMappingEntity> servicePartners = servicePartnerMappingRepository.findByStatus(ACTIVE);

        Map<String, ServicePartnerMappingDto> servicePartnerMap = new HashMap<String, ServicePartnerMappingDto>();

        if (!CollectionUtils.isEmpty(servicePartners)) {
            for (ServicePartnerMappingEntity servicePartner : servicePartners) {
                ServicePartnerMappingDto mappingDto = new ServicePartnerMappingDto();
                mappingDto.setServicePartnerCode(servicePartner.getPartnerCode());
                mappingDto.setServiceRequestTypeId(servicePartner.getServiceRequestTypeId());
                servicePartnerMap.put(servicePartner.getServiceRequestTypeId() + "_" + servicePartner.getPartnerCode(), mappingDto);
            }
        }

        return servicePartnerMap;
    }

    @Override
    protected ServicePartnerMappingDto getFromDB(String key) {
        return null;
    }
}