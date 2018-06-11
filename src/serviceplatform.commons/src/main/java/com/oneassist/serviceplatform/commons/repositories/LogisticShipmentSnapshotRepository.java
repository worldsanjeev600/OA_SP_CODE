package com.oneassist.serviceplatform.commons.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ShipmentEntitySnapshot;

@Transactional(readOnly = true)
public interface LogisticShipmentSnapshotRepository extends JpaRepository<ShipmentEntitySnapshot, Long> {
	
}