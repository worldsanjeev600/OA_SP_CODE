package com.oneassist.serviceplatform.commons.cache.base;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class RestTemplateConfiguration {
	private final Logger logger = Logger.getLogger(RestTemplateConfiguration.class);
	
	@Value("${RESTTEMPLATE_READ_TIMEOUT}")
	private String readTimeout;
	
	@Value("${RESTTEMPLATE_CONNECTION_TIMEOUT}")
	private String connectionTimeout;
	
    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
    	
    	SSLContext context = null;
		
    	try {
			context = SSLContext.getInstance("TLSv1.2");
			context.init(null, null, null);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			logger.error("Not able to get SSLContext:: ", e);
		}
    	
		CloseableHttpClient httpClient = HttpClientBuilder.create().setSslcontext(context).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        factory.setReadTimeout(Integer.valueOf(readTimeout));
        factory.setConnectTimeout(Integer.valueOf(connectionTimeout));

        
        return factory;
    }
}
