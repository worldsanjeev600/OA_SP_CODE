package com.oneassist.serviceplatform.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;

public interface DocumentTypeMstRepository extends JpaRepository<DocTypeMstEntity, Long> {

	public DocTypeMstEntity findBydocTypeId(Long docTypeId);

	public DocTypeMstEntity findBydocName(String documentKey);

}
