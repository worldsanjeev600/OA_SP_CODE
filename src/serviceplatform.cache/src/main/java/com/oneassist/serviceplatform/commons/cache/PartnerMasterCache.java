package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.PartnerMasterDataDto;
import com.oneassist.serviceplatform.contracts.response.PartnerMasterResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
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
@Component("partnerMasterCache")
public class PartnerMasterCache extends InMemoryCache<PartnerMasterDto> {

    private final Logger logger = Logger.getLogger(PartnerMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${PartnerMasterCacheMapping}")
    private String partnerMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private HashMap<String, PartnerMasterDto> partnerMasterCache = new HashMap<String, PartnerMasterDto>();

    @Override
    protected PartnerMasterDto getFromDB(String key) {
        // Get all entities from source in case API doesn't support retrival of single entity.
        Map<String, PartnerMasterDto> allItems = getAllFromDB();

        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for PartnerMaster
     */
    @Override
    protected Map<String, PartnerMasterDto> getAllFromDB() {
        logger.info(">>> Preparing Partner Master Data ");
        ResponseEntity<PartnerMasterResponseDto> httpResponse;
        PartnerMasterResponseDto partnerDetailResponseList;

        try {
            HttpEntity<PartnerMasterDataDto> entity = new HttpEntity<>(new PartnerMasterDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + partnerMasterCacheMapping, HttpMethod.GET, entity, PartnerMasterResponseDto.class);

            if (httpResponse != null) {
                partnerDetailResponseList = httpResponse.getBody();

                if (null != partnerDetailResponseList && partnerDetailResponseList.getData() != null && partnerDetailResponseList.getData().size() > 0) {
                    for (int i = 0; i < partnerDetailResponseList.getData().size(); i++) {
                        PartnerMasterDataDto partnerDetailResponse = partnerDetailResponseList.getData().get(i);
                        partnerMasterCache.put(partnerDetailResponse.getPartnerCode(), partnerDetailResponse.getPartner());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Partner Master Cache " + e.getMessage());
        }

        return partnerMasterCache;
    }
}
