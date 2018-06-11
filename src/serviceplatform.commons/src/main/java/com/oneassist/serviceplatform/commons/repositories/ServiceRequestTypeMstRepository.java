package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;

@Transactional(readOnly = true)
public interface ServiceRequestTypeMstRepository extends JpaRepository<ServiceRequestTypeMstEntity, Long> {
	
	ServiceRequestTypeMstEntity findServiceRequestTypeMstByServiceRequestTypeAndStatus(String	serviceRequestType , String status);
	
	ServiceRequestTypeMstEntity findServiceRequestTypeMstByServiceRequestTypeId(long serviceRequestTypeId);
	
	List<ServiceRequestTypeMstEntity> findServiceRequestTypeMstByStatus(String status);
}