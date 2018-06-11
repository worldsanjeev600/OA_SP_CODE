package com.spring.data.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Component
@Configuration
public class CustomRestMVCConfig extends RepositoryRestMvcConfiguration  {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	
		// TODO Auto-generated method stub
		//config.exposeIdsFor(Shipment.class);
		//config.setResourceMappingForRepository(ShipmentRepository.class);
		//config.setBasePath("/api");
	}
	
	@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
	
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");
	 
	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
}
