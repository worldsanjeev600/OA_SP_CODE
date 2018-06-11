package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.response.PartnerEventDetailDataDto;
import com.oneassist.serviceplatform.contracts.response.PartnerEventMasterResponseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Configuration
@Component("partnerEventMasterCache")
public class PartnerEventMasterCache extends InMemoryCache<PartnerEventDetailDto> {

    private final Logger logger = Logger.getLogger(PartnerEventMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${PartnerEventMasterCacheMapping}")
    private String partnerEventMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    /**
	 * 
	 * 
	 */
    @Override
    protected PartnerEventDetailDto getFromDB(String key) {
        Map<String, PartnerEventDetailDto> allItems = getAllFromDB();
        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for HubMaster
     */
    @Override
    protected Map<String, PartnerEventDetailDto> getAllFromDB() {
        HashMap<String, PartnerEventDetailDto> partnerEventMasterCache = new HashMap<String, PartnerEventDetailDto>();

        logger.info(">>> Preparing Partner Event Master Cache");
        ResponseEntity<PartnerEventMasterResponseDto> httpResponse;
        PartnerEventMasterResponseDto partnerEventMasterResponseList;

        try {
            HttpEntity<PartnerEventDetailDataDto> entity = new HttpEntity<>(new PartnerEventDetailDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + partnerEventMasterCacheMapping, HttpMethod.GET, entity, PartnerEventMasterResponseDto.class);

            if (httpResponse != null) {
                partnerEventMasterResponseList = httpResponse.getBody();

                if (null != partnerEventMasterResponseList && partnerEventMasterResponseList.getData() != null && partnerEventMasterResponseList.getData().size() > 0) {
                    partnerEventMasterCache = new HashMap<String, PartnerEventDetailDto>();
                    logger.info("size..." + partnerEventMasterCache.size());
                    for (int i = 0; i < partnerEventMasterResponseList.getData().size(); i++) {
                        PartnerEventDetailDataDto partnerEventDetailResponse = partnerEventMasterResponseList.getData().get(i);
                        partnerEventMasterCache.put(partnerEventDetailResponse.getEventName(), partnerEventDetailResponse.getPartnerEvent());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Partner event Master Cache " + e.getMessage(), e);
        }

        return partnerEventMasterCache;
    }
}
