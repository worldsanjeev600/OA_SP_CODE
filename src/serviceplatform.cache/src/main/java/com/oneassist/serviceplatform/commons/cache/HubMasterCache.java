package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.HubMasterDataDto;
import com.oneassist.serviceplatform.contracts.response.HubMasterResponseDto;
import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;
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
@Component("hubMasterCache")
public class HubMasterCache extends InMemoryCache<HubMasterDto> {

    private final Logger logger = Logger.getLogger(HubMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${HubMasterCacheMapping}")
    private String hubMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private HashMap<String, HubMasterDto> hubMasterCache = new HashMap<String, HubMasterDto>();

    /**
	 * 
	 * 
	 */
    @Override
    protected HubMasterDto getFromDB(String key) {
        Map<String, HubMasterDto> allItems = getAllFromDB();
        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for HubMaster
     */
    @Override
    protected Map<String, HubMasterDto> getAllFromDB() {
        logger.info(">>> Preparing Hub Master Data Cache");
        ResponseEntity<HubMasterResponseDto> httpResponse;
        HubMasterResponseDto hubMasterResponseList;

        try {
            HttpEntity<HubMasterDataDto> entity = new HttpEntity<>(new HubMasterDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + hubMasterCacheMapping, HttpMethod.GET, entity, HubMasterResponseDto.class);

            if (httpResponse != null) {
                hubMasterResponseList = httpResponse.getBody();

                if (null != hubMasterResponseList && hubMasterResponseList.getData() != null && hubMasterResponseList.getData().size() > 0) {
                    hubMasterCache = new HashMap<String, HubMasterDto>();
                    for (int i = 0; i < hubMasterResponseList.getData().size(); i++) {
                        HubMasterDataDto hubMasterResponse = hubMasterResponseList.getData().get(i);
                        hubMasterCache.put(hubMasterResponse.getOchmHubId(), hubMasterResponse.getClaimHubMasterDto());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Hub Master Cache " + e.getMessage());
        }
        return hubMasterCache;
    }
}
