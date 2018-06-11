package com.oneassist.serviceplatform.commons.service;

import java.util.List;

import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;

public interface IServiceRequestDocumentTypeMasterService {

	List<DocTypeMstEntity> getAllServiceTaskConfig();

	DocTypeMstEntity getDocumentTypeByDocumentTypeId(String key);
	
}
