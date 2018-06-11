package com.oneassist.serviceplatform.commons.mastercache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.commons.repositories.DocumentTypeConfigRepository;

/**
 * Cache for OA_DOC_TYPE_MST table
 * 
 * @author sanjeev.gupta
 *
 */
@Configuration
@Component("docTypeConfigDetailCache")
public class DocTypeConfigDetailCache extends InMemoryCache<List<DocTypeConfigDetailEntity>> {

	private final Logger logger = Logger.getLogger(DocTypeConfigDetailCache.class);

	@Autowired
	private DocumentTypeConfigRepository documentTypeConfigRepository;

	Map<String, List<DocTypeConfigDetailEntity>> documentTypeConfigDetailMasterMap = null;

	@Override
	protected List<DocTypeConfigDetailEntity> getFromDB(String key) {
		
		Map<String, List<DocTypeConfigDetailEntity>> map = getAllFromDB();
		return map.get(key);
	}

	@Override
	protected Map<String, List<DocTypeConfigDetailEntity>> getAllFromDB() {

		List<DocTypeConfigDetailEntity> finalList;
		List<DocTypeConfigDetailEntity> documentTypeConfigDetailEntityList = documentTypeConfigRepository.findAll();
		documentTypeConfigDetailMasterMap = new HashMap<String, List<DocTypeConfigDetailEntity>>();
		if (null != documentTypeConfigDetailEntityList && !documentTypeConfigDetailEntityList.isEmpty()) {

			for (DocTypeConfigDetailEntity docTypeConfigDetailEntity : documentTypeConfigDetailEntityList)

			{
				if (documentTypeConfigDetailMasterMap
						.get(docTypeConfigDetailEntity.getServiceRequestTypeId()) == null) {
					finalList = new ArrayList<>();
					finalList.add(docTypeConfigDetailEntity);
					documentTypeConfigDetailMasterMap.put(docTypeConfigDetailEntity.getServiceRequestTypeId(),
							finalList);

				} else {
					finalList = documentTypeConfigDetailMasterMap
							.get(docTypeConfigDetailEntity.getServiceRequestTypeId());
					finalList.add(docTypeConfigDetailEntity);
					documentTypeConfigDetailMasterMap.put(docTypeConfigDetailEntity.getServiceRequestTypeId(),
							finalList);
				}
			}

		}

		logger.debug("Docuemnt Type " + documentTypeConfigDetailMasterMap);
		return documentTypeConfigDetailMasterMap;
	}

}
