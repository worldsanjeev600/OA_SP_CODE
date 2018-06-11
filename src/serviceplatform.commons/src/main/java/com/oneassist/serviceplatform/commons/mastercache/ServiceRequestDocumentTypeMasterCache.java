package com.oneassist.serviceplatform.commons.mastercache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.service.IServiceRequestDocumentTypeMasterService;

/**
 * @author
 */
@Configuration
@Component("serviceRequestDocumentTypeMasterCache")
public class ServiceRequestDocumentTypeMasterCache extends InMemoryCache<DocTypeMstEntity> {

	@Autowired
	private IServiceRequestDocumentTypeMasterService serviceRequestDocumentTypeMasterService;

	
	@Override
	protected Map<String,DocTypeMstEntity> getAllFromDB() {
		
		List<DocTypeMstEntity> serviceDocTypeList = serviceRequestDocumentTypeMasterService.getAllServiceTaskConfig();
		
		Map<String, DocTypeMstEntity>  serviceDocTypeMasterMap = new HashMap<String, DocTypeMstEntity>();
		
		if ( null != serviceDocTypeList && !serviceDocTypeList.isEmpty() )
		{
			for (DocTypeMstEntity docTypeMstEntity: serviceDocTypeList)
			{
				serviceDocTypeMasterMap.put(docTypeMstEntity.getDocTypeId()+"", docTypeMstEntity);
			}
		}
				
		return serviceDocTypeMasterMap;
	}


	@Override
	protected DocTypeMstEntity getFromDB(String key) {
	
		DocTypeMstEntity  docTypeMstEntity = serviceRequestDocumentTypeMasterService.getDocumentTypeByDocumentTypeId(key);
		return docTypeMstEntity;
	}
	
}
