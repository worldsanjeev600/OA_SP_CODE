package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ServicePartnerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ServicePartnerMappingRepository extends JpaRepository<ServicePartnerMappingEntity, Long> {

    List<ServicePartnerMappingEntity> findByStatus(String status);

}
