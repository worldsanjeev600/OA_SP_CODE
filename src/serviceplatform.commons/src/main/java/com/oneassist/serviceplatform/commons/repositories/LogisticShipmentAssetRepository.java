package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ShipmentAssetEntity;

@Transactional(readOnly = true)
public interface LogisticShipmentAssetRepository extends JpaRepository<ShipmentAssetEntity, Long> {

	List<ShipmentAssetEntity> findShipmentAssetEntityByShipmentId(Long shipmentId);
}