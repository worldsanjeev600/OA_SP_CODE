package com.oneassist.serviceplatform.commons.repositories;

import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticShipmentAddressRepository extends JpaRepository<ServiceAddressEntity, Long> {
}
