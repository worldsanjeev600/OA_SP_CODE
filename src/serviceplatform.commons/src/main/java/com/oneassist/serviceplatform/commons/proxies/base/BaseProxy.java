package com.oneassist.serviceplatform.commons.proxies.base;

import com.oneassist.serviceplatform.commons.cache.PartnerEventMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.RestTemplateConfiguration;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseProxy {

    @Autowired
    private RestTemplateConfiguration restTemplateConfiguration;

    protected String baseUrl;

    @Autowired
    private PartnerEventMasterCache partnerEventMasterCache;

    public <T> ResponseEntity<T> callUrl(Class<T> type, String url, HttpMethod httpMethod) {
        HttpEntity<T> httpEntity = new HttpEntity<T>(this.getHeaders());

        ResponseEntity<T> httpResponse = restTemplateConfiguration.restTemplate().exchange(url, httpMethod, httpEntity, type);

        return httpResponse;
    }

    public <T> ResponseEntity<T> callUrl(Class<T> type, String url, HttpMethod httpMethod, HttpEntity httpEntity) {

        ResponseEntity<T> httpResponse = restTemplateConfiguration.restTemplate().exchange(url, httpMethod, httpEntity, type);

        return httpResponse;
    }

    protected abstract HttpHeaders getHeaders();

    protected abstract String getBaseUrl();

    public PartnerEventDetailDto getPartnerEventMst(String eventCode) throws Exception {
        PartnerEventDetailDto partnerEventDetailDto = null;
        
        if (partnerEventMasterCache != null) {
            partnerEventDetailDto = partnerEventMasterCache.get(eventCode);
        } else {
            throw new Exception("Partner event master cache is null");
        }
        return partnerEventDetailDto;
    }
}
