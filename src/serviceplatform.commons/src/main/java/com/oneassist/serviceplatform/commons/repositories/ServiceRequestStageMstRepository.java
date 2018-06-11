package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;

@Transactional(readOnly = true)
public interface ServiceRequestStageMstRepository extends JpaRepository<ServiceRequestStageMstEntity, Long> {
	
	List<ServiceRequestStageMstEntity> findServiceRequestStageMstByStatus(String status);
	
	List<ServiceRequestStageMstEntity> findByStatusOrderByServiceRequestTypeIdAscStageOrderAsc(
			String status);
}