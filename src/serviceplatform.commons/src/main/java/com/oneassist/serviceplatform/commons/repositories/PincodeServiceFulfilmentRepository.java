package com.oneassist.serviceplatform.commons.repositories;

import io.swagger.annotations.Api;
import java.util.List;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "pincodeServiceFulfilment")
@Api(tags = { "/pincodeServiceFulfilment : Provides CRUD Operations on the Pincode Service Fulfilment Entity" })
public interface PincodeServiceFulfilmentRepository extends CrudRepository<PincodeServiceFulfilmentEntity, Long>, JpaSpecificationExecutor {

    public static final String SHIPMENT_PINCODE_FULFILMENT_QUERY = "SELECT * FROM OA_PINCODE_SERV_FULFILMENT_MST o WHERE (o.PINCODE=:pincode OR o.PINCODE = '*') AND o.SERVICE_REQUEST_TYPE_ID=:serviceRequestTypeId AND o.status =:status and (o.SUBCATEGORY_CODE =:subCategoryCode OR o.SUBCATEGORY_CODE = '*') and ROWNUM = 1 order by o.PARTNER_PRIORITY asc";
    public static final String DISTINCT_PINCODES_QUERY = "SELECT DISTINCT(pincode) from OA_PINCODE_SERV_FULFILMENT_MST where SERVICE_REQUEST_TYPE_ID=:serviceRequestTypeId AND status =:status ";
    public static final String DISTINCT_PINCODES_REQUEST_TYPE_ID_QUERY = "SELECT SERVICE_REQUEST_TYPE from OA_SERVICE_REQUEST_TYPE_MST where SERVICE_REQUEST_TYPE_ID IN (SELECT  distinct SERVICE_REQUEST_TYPE_ID from OA_PINCODE_SERV_FULFILMENT_MST where pincode=:pincode AND SERVICE_REQUEST_TYPE_ID=:serviceRequestTypeId AND status =:status) ";
    public static final String DISTINCT_PINCODES_REQUEST_TYPE_QUERY = "SELECT SERVICE_REQUEST_TYPE from OA_SERVICE_REQUEST_TYPE_MST where SERVICE_REQUEST_TYPE_ID IN (SELECT  distinct SERVICE_REQUEST_TYPE_ID from OA_PINCODE_SERV_FULFILMENT_MST where pincode=:pincode AND status =:status) ";
    public static final String FIND_BY_PINCODE_PARTNERCODE_SERVICETYPE_AND_PRODUCTCODE = "SELECT * FROM OA_PINCODE_SERV_FULFILMENT_MST WHERE PINCODE IN (:pincodes) AND SERVICE_REQUEST_TYPE_ID =:serviceRequestTypeId AND PARTNER_CODE=:partnerCode AND SUBCATEGORY_CODE=:subCategoryCode AND STATUS = 'A'";
    public static final String FIND_BY_PINCODES = "SELECT * FROM OA_PINCODE_SERV_FULFILMENT_MST WHERE PINCODE IN (:pincodes) AND STATUS = 'A'";

    PincodeServiceFulfilmentEntity findByPincodeAndPartnerPriorityAndSubCategoryCodeAndServiceRequestTypeId(String pincode, int partnerPriority, String subCategoryCode, Long serviceRequestTypeId);

    List<PincodeServiceFulfilmentEntity> findPincodeServiceByPincodeAndStatus(String pinCode, String status);

    List<PincodeServiceFulfilmentEntity> findPincodeServiceFulfilmentByPincodeAndServiceRequestTypeIdAndStatus(String pinCode, Long serviceRequestTypeId, String status);

    @Query(value = SHIPMENT_PINCODE_FULFILMENT_QUERY, nativeQuery = true)
    public List<PincodeServiceFulfilmentEntity> findShipmentPincodeFulfilment(@Param("pincode") String pincode, @Param("serviceRequestTypeId") Long serviceRequestTypeId,
            @Param("status") String status, @Param("subCategoryCode") String subCategoryCode);

    List<PincodeServiceFulfilmentEntity> findByPincodeAndSubCategoryCodeAndServiceRequestTypeIdAndStatus(String pinCode, String subCategoryCode, Long serviceRequestTypeId, String status);

    List<PincodeServiceFulfilmentEntity> findServiceCentreByPincodeAndServiceRequestTypeIdAndStatus(@Param("pincode") String pinCode, @Param("serviceRequestTypeId") Long serviceRequestTypeId,
            @Param("status") String status);

    @Query(value = DISTINCT_PINCODES_QUERY, nativeQuery = true)
    List<String> getDistinctPincodesByRequestType(@Param("serviceRequestTypeId") Long serviceRequestTypeId, @Param("status") String status);

    @Query(value = DISTINCT_PINCODES_REQUEST_TYPE_QUERY, nativeQuery = true)
    List<String> getDistinctServiceRequestTypesByPincode(@Param("pincode") String pinCode, @Param("status") String status);

    @Query(value = DISTINCT_PINCODES_REQUEST_TYPE_ID_QUERY, nativeQuery = true)
    List<String> getDistinctServiceRequestTypesByPincodeAndServiceRequestTypeId(@Param("pincode") String pinCode, @Param("serviceRequestTypeId") Long serviceRequestTypeId,
            @Param("status") String status);

    List<PincodeServiceFulfilmentEntity> findByPincodeAndSubCategoryCodeAndServiceRequestTypeIdAndStatusAndPartnerPriority(String pinCode, String subCategoryCode, Long serviceRequestTypeId,
            String status, Integer partnerPriority);
}
