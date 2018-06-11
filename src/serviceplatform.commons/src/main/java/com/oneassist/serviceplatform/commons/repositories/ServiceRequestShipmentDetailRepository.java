package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestShipmentDetailViewEntity;

@Transactional(readOnly = true)
public interface ServiceRequestShipmentDetailRepository extends JpaRepository<ServiceRequestShipmentDetailViewEntity, Long> {
	
	List<ServiceRequestShipmentDetailViewEntity> findByPrimaryTrackingNumber(String primaryTrackingNumber);
}