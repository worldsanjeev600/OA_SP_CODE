package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntityDocumentEntity;

@Transactional(readOnly = true)
public interface ServiceRequestEntityDocumentRepository extends JpaRepository <ServiceRequestEntityDocumentEntity, Long>{

	List<ServiceRequestEntityDocumentEntity> findByDocumentIdIn(List<String> asList);
	
}
