package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ShipmentDocumentRepository extends CrudRepository<ServiceDocumentEntity, Long> {

    ServiceDocumentEntity findByStorageRefId(String storageRefId);

    ServiceDocumentEntity findByServiceRequestIdAndDocumentTypeId(Long serviceRequestId, Long docTypeId);

    List<ServiceDocumentEntity> findByServiceRequestId(Long shipmentId);

    ServiceDocumentEntity findByServiceRequestIdAndDocumentTypeIdAndStatus(Long serviceRequestId, Long docTypeId, String status);
}
