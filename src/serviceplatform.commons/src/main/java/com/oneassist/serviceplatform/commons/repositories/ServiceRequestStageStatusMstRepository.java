package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageStatusMstEntity;

@Transactional(readOnly = true)
public interface ServiceRequestStageStatusMstRepository extends JpaRepository<ServiceRequestStageStatusMstEntity, Long> {
	
	List<ServiceRequestStageStatusMstEntity> findServiceRequestStageStatusMstByStatus(String status);
}