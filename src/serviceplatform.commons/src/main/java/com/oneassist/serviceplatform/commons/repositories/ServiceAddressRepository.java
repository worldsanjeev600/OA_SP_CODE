package com.oneassist.serviceplatform.commons.repositories;

import io.swagger.annotations.Api;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;

@Repository
@RepositoryRestResource(path="serviceAddressService")
@Api(tags = {"/serviceAddressService : Provides CRUD Operations on the Servicee address Entity"})

public interface ServiceAddressRepository extends CrudRepository<ServiceAddressEntity, Long>,JpaSpecificationExecutor<ServiceAddressEntity> {
	ServiceAddressEntity findByServiceAddressId(Long serviceAddressId);
	
	List<ServiceAddressEntity> findByServiceAddressIdIn(List<Long> addressIds);
}