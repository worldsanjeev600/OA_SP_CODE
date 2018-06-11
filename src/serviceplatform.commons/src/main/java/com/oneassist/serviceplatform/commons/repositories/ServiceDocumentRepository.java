package com.oneassist.serviceplatform.commons.repositories;

import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ServiceDocumentRepository extends CrudRepository<ServiceDocumentEntity, String> {

	public static final String UPDATE_MONGO_ID_QUERY = "UPDATE ServiceDocumentEntity SET storageRefId=:newMongoId WHERE storageRefId=:oldMongoId";

	List<ServiceDocumentEntity> findByServiceRequestIdAndStatus(Long serviceRequestId, String status);

	// ServiceDocumentEntity createOrReplace(ServiceDocumentEntity
	// serviceDocumentEntity);
	ServiceDocumentEntity findByStorageRefId(String storageRefId);

	@Modifying
	@Query(UPDATE_MONGO_ID_QUERY)
	Integer updateMongoStorageReferenceId(@Param("oldMongoId") String oldMongoId,
			@Param("newMongoId") String newMongoId);

	List<ServiceDocumentEntity> findByServiceRequestIdAndDocumentTypeId(Long serviceRequestId, Long DocumentTypeId);

	List<ServiceDocumentEntity> findByServiceRequestId(Long serviceRequestId);

	List<ServiceDocumentEntity> findByServiceRequestIdAndDocumentTypeIdAndStatus(Long serviceRequestId, Long docTypeId,
			String active);

	List<ServiceDocumentEntity> findByServiceRequestIdAndDocumentTypeIdInAndStatus(Long serviceRequestId,
			List<Long> docTypeIds, String active);

}
