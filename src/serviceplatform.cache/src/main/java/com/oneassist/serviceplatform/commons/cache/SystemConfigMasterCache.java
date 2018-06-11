package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.SystemConfigDataDto;
import com.oneassist.serviceplatform.contracts.response.SystemConfigResponseDto;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
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
@Component("systemConfigMasterCache")
public class SystemConfigMasterCache extends InMemoryCache<List<SystemConfigDto>> {

    private final Logger logger = Logger.getLogger(SystemConfigMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${SystemConfigMasterCacheMapping}")
    private String systemConfigMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private Map<String, List<SystemConfigDto>> systemConfigMasterCache = new HashMap<String, List<SystemConfigDto>>();

    /**
	 * 
	 * 
	 */
    @Override
    protected List<SystemConfigDto> getFromDB(String key) {
        // Get all entities from source in case API doesn't support retrival of single entity.
        Map<String, List<SystemConfigDto>> allItems = getAllFromDB();

        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for SystemConfigMaster
     */
    @Override
    protected Map<String, List<SystemConfigDto>> getAllFromDB() {
        logger.info(">>> Preparing System Config Master Cache");
        ResponseEntity<SystemConfigResponseDto> httpResponse;
        SystemConfigResponseDto systemConfigResponseList;
        try {
            HttpEntity<SystemConfigDto> entity = new HttpEntity<>(new SystemConfigDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + systemConfigMasterCacheMapping, HttpMethod.GET, entity, SystemConfigResponseDto.class);
            
            if (httpResponse != null) {
                systemConfigResponseList = httpResponse.getBody();

                if (null != systemConfigResponseList && systemConfigResponseList.getData() != null && systemConfigResponseList.getData().size() > 0) {
                    systemConfigMasterCache = new HashMap<String, List<SystemConfigDto>>();
                    for (int i = 0; i < systemConfigResponseList.getData().size(); i++) {
                        SystemConfigDataDto systemConfigResponse = systemConfigResponseList.getData().get(i);
                        systemConfigMasterCache.put(systemConfigResponse.getParamCode(), systemConfigResponse.getSystemConfig());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing System Config Master Cache " + e.getMessage(), e);
        }
        return systemConfigMasterCache;
    }
}
