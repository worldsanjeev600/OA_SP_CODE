package com.oneassist.serviceplatform.mappers;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfiguration{

	@Autowired
	private Set<? extends PropertyMap> customMappers;
	
	private ModelMapper modelMapper;

	private ModelMapperConfiguration() {
			this.modelMapper = new ModelMapper();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return this.modelMapper;
	}
	
	@PostConstruct
	private void addCustomMappings(){
		Set<?> customMappers = this.customMappers;
		if(customMappers.size()>0){
			Iterator itr = customMappers.iterator();
			while(itr.hasNext()){
				PropertyMap propertyMap = (PropertyMap)itr.next();
				modelMapper.addMappings(propertyMap);
			}
		}
				
	}

	public Set<? extends PropertyMap> getCustomMappers() {
		return customMappers;
	}

	public void setCustomMappers(Set<? extends PropertyMap> customMappers) {
		this.customMappers = customMappers;
	}

	public ModelMapper getModelMapper() {
		return modelMapper;
	}

	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
}