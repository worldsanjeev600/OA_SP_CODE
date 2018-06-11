package com.oneassist.serviceplatform.commons.repositories;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;

@Transactional(readOnly = true)
@RepositoryRestResource(path="/servicetasks")
@Api(tags = {"/servicetasks : Service Task Master Request Entity"})
public interface ServiceTaskRepository extends JpaRepository<ServiceTaskEntity, Long>,JpaSpecificationExecutor<ServiceTaskEntity>  {
	
	List<ServiceTaskEntity> findByReferenceCodeAndTaskTypeAndStatus(
			@ApiParam(value="Reference Code") @Param("referenceCode") String referenceCode,
			@ApiParam(value="Task Type") @Param("taskType") String taskType ,
			@ApiParam(value="Status") @Param("status") String status );
	
	List<ServiceTaskEntity> findByProductVariantIdAndStatus(
			@ApiParam(value="Product Variant Id") @Param("productVariantId") Long productVariantId,
			@ApiParam(value="Status") @Param("status") String status );
	
	ServiceTaskEntity findServiceTaskEntityByServiceTaskId(Long serviceTaskId);
	
	List<ServiceTaskEntity> findByStatus(String status); 
	
	List<ServiceTaskEntity> findByReferenceCodeAndStatus(
			@ApiParam(value="Reference Code") @Param("referenceCode") String referenceCode,
            @ApiParam(value="Status") @Param("status") String status );

	List<ServiceTaskEntity> findByTaskTypeAndStatus(String serviceTaskType,String status);

}