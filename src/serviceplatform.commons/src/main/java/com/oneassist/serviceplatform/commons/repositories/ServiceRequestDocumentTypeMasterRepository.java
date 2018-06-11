package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;

@Transactional(readOnly = true)
public interface ServiceRequestDocumentTypeMasterRepository extends CrudRepository<DocTypeMstEntity, String> {

	List<DocTypeMstEntity> findByStatus(String active);

	DocTypeMstEntity findBydocTypeIdAndStatus(String key, String active);
	
	DocTypeMstEntity findByDocName(String documentName);

}
