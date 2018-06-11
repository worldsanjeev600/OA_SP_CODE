package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.PartnerBUMasterDataDto;
import com.oneassist.serviceplatform.contracts.response.PartnerBUResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;
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
@Component("partnerBUCache")
public class PartnerBUCache extends InMemoryCache<PartnerBusinessUnit> {

    private final Logger logger = Logger.getLogger(PartnerBUCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${PartnerBUCacheMapping}")
    private String partnerBUMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private Map<String, PartnerBusinessUnit> partnerBUMasterCache = new HashMap<String, PartnerBusinessUnit>();

    /**
	 * 
	 * 
	 */
    @Override
    protected PartnerBusinessUnit getFromDB(String key) {
        Map<String, PartnerBusinessUnit> allItems = getAllFromDB();
        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for ProductMaster
     */
    @Override
    protected Map<String, PartnerBusinessUnit> getAllFromDB() {
        logger.info(">>> Preparing PartnerBusinessUnit Data Cache");
        ResponseEntity<PartnerBUResponseDto> httpResponse;
        PartnerBUResponseDto partnerBUResponseList;

        try {
            HttpEntity<PartnerBUMasterDataDto> entity = new HttpEntity<>(new PartnerBUMasterDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + partnerBUMapping, HttpMethod.GET, entity, PartnerBUResponseDto.class);

            if (httpResponse != null) {
                partnerBUResponseList = httpResponse.getBody();

                if (null != partnerBUResponseList && partnerBUResponseList.getData() != null && partnerBUResponseList.getData().size() > 0) {
                    partnerBUMasterCache = partnerBUResponseList.getData();
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Product BUMaster Cache " + e.getMessage(), e);
        }
        return partnerBUMasterCache;
    }
}
