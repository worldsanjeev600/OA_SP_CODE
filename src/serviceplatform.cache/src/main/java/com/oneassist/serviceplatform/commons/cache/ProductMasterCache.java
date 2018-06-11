package com.oneassist.serviceplatform.commons.cache;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.response.ProductMasterDataDto;
import com.oneassist.serviceplatform.contracts.response.ProductMasterResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ProductMasterDto;
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
@Component("productMasterCache")
public class ProductMasterCache extends InMemoryCache<ProductMasterDto> {

    private final Logger logger = Logger.getLogger(ProductMasterCache.class);

    @Value("${OASYSUrl}")
    private String oasysUrl;

    @Value("${ProductMasterCacheMapping}")
    private String productMasterCacheMapping;

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    private Map<String, ProductMasterDto> productMasterMasterCache = new HashMap<String, ProductMasterDto>();

    /**
	 * 
	 * 
	 */
    @Override
    protected ProductMasterDto getFromDB(String key) {
        Map<String, ProductMasterDto> allItems = getAllFromDB();
        return allItems.get(key);
    }

    /**
     * Consume webservice expose from OASYS to prepare local cache for ProductMaster
     */
    @Override
    protected Map<String, ProductMasterDto> getAllFromDB() {
        logger.info(">>> Preparing Product Master Data Cache");
        ResponseEntity<ProductMasterResponseDto> httpResponse;
        ProductMasterResponseDto productMasterResponseList;

        try {
            HttpEntity<ProductMasterDataDto> entity = new HttpEntity<>(new ProductMasterDataDto());
            httpResponse = restTemplateConfiguration.restTemplate().exchange(oasysUrl + productMasterCacheMapping, HttpMethod.GET, entity, ProductMasterResponseDto.class);

            if (httpResponse != null) {
                productMasterResponseList = httpResponse.getBody();

                if (null != productMasterResponseList && productMasterResponseList.getData() != null && productMasterResponseList.getData().size() > 0) {
                    productMasterMasterCache = productMasterResponseList.getData();
                }
            }

            if (productMasterMasterCache != null && !productMasterMasterCache.isEmpty()) {
                logger.error(">>> Product Master Cache Loaded Successfully >> " + productMasterMasterCache.size());
            } else {
                logger.error(">>> Product Master Cache is Empty !!!>> ");
            }

        } catch (Exception e) {
            logger.error("<<< Exception raised while preparing Product Master Cache " + e.getMessage());
        }
        return productMasterMasterCache;
    }
}
