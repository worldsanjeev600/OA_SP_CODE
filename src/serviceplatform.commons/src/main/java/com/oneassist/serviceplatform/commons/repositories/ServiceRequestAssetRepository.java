package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;

@Transactional(readOnly = true)
public interface ServiceRequestAssetRepository extends JpaRepository <ServiceRequestAssetEntity, String>{

	List<ServiceRequestAssetEntity> findAll(Specification<ServiceRequestAssetEntity> filterServiceRequests);

	List<ServiceRequestAssetEntity> findByServiceRequestIdAndStatus(Long serviceRequestId,String status);
	
	List<ServiceRequestAssetEntity> findByAssetReferenceNoIn(List<String> assetIds);
	
	List<ServiceRequestAssetEntity> findByServiceRequestId(Long serviceRequestId);
	
}
