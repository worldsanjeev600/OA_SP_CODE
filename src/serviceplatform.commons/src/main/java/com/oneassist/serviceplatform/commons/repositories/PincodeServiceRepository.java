package com.oneassist.serviceplatform.commons.repositories;

import io.swagger.annotations.Api;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "pincodeService")
@Api(tags = { "/pincodeService : Provides CRUD Operations on the Pincode Service Entity" })
public interface PincodeServiceRepository extends CrudRepository<PincodeServiceEntity, Long>, JpaSpecificationExecutor {

    PincodeServiceEntity findByPincode(String pincode);

    PincodeServiceEntity findPincodeServiceByPincodeAndStatus(String pinCode, String status);

    PincodeServiceEntity findByPincodeAndStatus(String pinCode, String status);
}
