package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.ServiceMasterDataDto;
import com.oneassist.serviceplatform.contracts.response.ServiceMasterResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ServiceMasterDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Configuration
@Component("serviceMasterCache")
public class ServiceMasterCache extends InMemoryCache<ServiceMasterDto> {

    private final Logger logger = Logger.getLogger(ServiceMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${ServiceMasterCacheMapping}")
    private String serviceMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private HashMap<String, ServiceMasterDto> serviceMasterCache = new HashMap<String, ServiceMasterDto>();

    /**
	 * 
	 * 
	 */
    @Override
    protected ServiceMasterDto getFromDB(String key) {

        // Get all entities from source in case API doesn't support retrival of single entity.
        Map<String, ServiceMasterDto> allItems = getAllFromDB();

        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for ServiceMaster
     */
    @Override
    protected Map<String, ServiceMasterDto> getAllFromDB() {
        logger.info(">>> Preparing Service Master Data Cache : starts");
        ResponseEntity<ServiceMasterResponseDto> httpResponse;
        ServiceMasterResponseDto serviceMasterResponseList;

        try {
            HttpEntity<ServiceMasterDataDto> entity = new HttpEntity<>(new ServiceMasterDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + serviceMasterCacheMapping, HttpMethod.GET, entity, ServiceMasterResponseDto.class);

            if (httpResponse != null) {
                serviceMasterResponseList = httpResponse.getBody();

                if (null != serviceMasterResponseList && serviceMasterResponseList.getData() != null && serviceMasterResponseList.getData().size() > 0) {
                    serviceMasterCache = new HashMap<String, ServiceMasterDto>();
                    for (int i = 0; i < serviceMasterResponseList.getData().size(); i++) {
                        ServiceMasterDataDto serviceMasterResponse = serviceMasterResponseList.getData().get(i);
                        serviceMasterCache.put(serviceMasterResponse.getServiceId(), serviceMasterResponse.getServiceMaster());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Service Master Cache " + e.getMessage());
        }
        logger.info(">>> Preparing Service Master Data Cache : ends");
        return serviceMasterCache;
    }
}
