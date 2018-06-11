
package com.spring.data.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;

@Component
@Configuration
public class CustomRestAdapterConfig extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		// TODO Auto-generated method stub
		config.exposeIdsFor(ServiceTaskEntity.class);
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);
		config.useHalAsDefaultJsonMediaType(false);
		super.configureRepositoryRestConfiguration(config);
	}

}
