package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;

/**
 * This is for DocTypeConfigDetailEntity Entity class
 * 
 * @author sanjeev.gupta
 *
 */
@Repository
public interface DocumentTypeConfigRepository
		extends JpaRepository<DocTypeConfigDetailEntity, Long>, JpaSpecificationExecutor {
	
	public List<DocTypeConfigDetailEntity> findByServiceRequestTypeId(String ServiceRequestTypeId);
	
	public List<DocTypeConfigDetailEntity> findByServiceRequestTypeIdAndReferenceCodeAndInsurancePartnerCode(
			String ServiceRequestTypeId, String referenceCode, Long insurancePartnerCode);

}
