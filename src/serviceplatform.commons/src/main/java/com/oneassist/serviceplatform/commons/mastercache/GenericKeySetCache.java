package com.oneassist.serviceplatform.commons.mastercache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.cache.base.InMemoryCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.GenericKeySetEntity;
import com.oneassist.serviceplatform.commons.repositories.GenericKeySetRepository;

/**
 * @author alok.singh
 */
@Configuration
@Component("genericKeySetCache")
public class GenericKeySetCache extends InMemoryCache<GenericKeySetEntity> {

	private final Logger logger = Logger.getLogger(GenericKeySetCache.class);

	@Autowired
	private GenericKeySetRepository genericKeySetRepository;

	Map<String, GenericKeySetEntity>  genericKeySetMap = new HashMap<String, GenericKeySetEntity>();

	@Override
	protected Map<String, GenericKeySetEntity> getAllFromDB() {

		List<GenericKeySetEntity> genericKeySetList = genericKeySetRepository.findByStatus(Constants.ACTIVE);
		if ( null != genericKeySetList && !genericKeySetList.isEmpty() )
		{
			for (GenericKeySetEntity genericKeySetEntity: genericKeySetList)
			{
				genericKeySetMap.put(genericKeySetEntity.getKeySetName(), genericKeySetEntity);
			}
		}
		logger.debug("genericKeySetMap >>> "+genericKeySetMap);
		return genericKeySetMap;
	}


	@Override
	protected GenericKeySetEntity getFromDB(String key) {

		GenericKeySetEntity genericKeySetEntity = genericKeySetMap.get(key);
		return genericKeySetEntity;
	}

}