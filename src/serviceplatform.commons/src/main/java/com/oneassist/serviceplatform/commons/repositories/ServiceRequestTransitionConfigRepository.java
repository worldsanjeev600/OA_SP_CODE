package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestTransitionConfigEntity;

@Transactional(readOnly = true)
public interface ServiceRequestTransitionConfigRepository extends JpaRepository<ServiceRequestTransitionConfigEntity, Long> {
	
	List<ServiceRequestTransitionConfigEntity> findServiceRequestTransitionConfigByStatus(String status);
}