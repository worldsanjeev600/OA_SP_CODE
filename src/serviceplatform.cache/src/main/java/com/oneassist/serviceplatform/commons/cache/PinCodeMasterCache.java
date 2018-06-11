package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeMasterDataDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeMasterResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
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
@Component("pinCodeMasterCache")
public class PinCodeMasterCache extends InMemoryCache<PincodeMasterDto> {

    private final Logger logger = Logger.getLogger(PinCodeMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${PinCodeMasterCacheMapping}")
    private String pinCodeMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private Map<String, PincodeMasterDto> pinCodeMasterCache = new HashMap<String, PincodeMasterDto>();

    @Override
    protected PincodeMasterDto getFromDB(String key) {
        // Get all entities from source in case API doesn't support retrival of single entity.
        Map<String, PincodeMasterDto> allItems = getAllFromDB();

        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for PartnerMaster
     */
    @Override
    protected Map<String, PincodeMasterDto> getAllFromDB() {
        logger.info(">>> Preparing Pincode Master Cache : starts");

        ResponseEntity<PincodeMasterResponseDto> httpResponse;
        PincodeMasterResponseDto pincodeMstList;

        try {
            HttpEntity<PincodeMasterDto> entity = new HttpEntity<>(new PincodeMasterDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + pinCodeMasterCacheMapping, HttpMethod.GET, entity, PincodeMasterResponseDto.class);

            if (httpResponse != null) {
                pincodeMstList = httpResponse.getBody();

                if (null != pincodeMstList && pincodeMstList.getData() != null && pincodeMstList.getData().size() > 0) {
                    pinCodeMasterCache = new HashMap<String, PincodeMasterDto>();
                    for (int i = 0; i < pincodeMstList.getData().size(); i++) {
                        PincodeMasterDataDto pincMasterResponse = pincodeMstList.getData().get(i);
                        pinCodeMasterCache.put(pincMasterResponse.getPincode(), pincMasterResponse.getPincodeMaster());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Pincode Master Cache " + e.getMessage(), e);
        }
        logger.info(">>> Preparing Pincode Master Cache : ends");
        return pinCodeMasterCache;
    }
}
