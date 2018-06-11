package com.oneassist.serviceplatform.commons.repositories;

import java.util.Date;
import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface LogisticShipmentReassignmentRepository extends JpaRepository<ShipmentReassignEntity, Long> {

    public static final String SHIPMENT_UPDATE_STATUS_QUERY = "UPDATE ShipmentReassignEntity SET status=:status , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId";
    public static final String SHIPMENT_UPDATE_AWB_QUERY = "UPDATE ShipmentReassignEntity SET logisticPartnerRefTrackingNumber=:logisticPartnerRefTrackingNumber , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId AND logisticPartnerCode=:logisticPartnerCode";
    public static final String SHIPMENT_UPDATE_FAILURE_REASON_QUERY = "UPDATE ShipmentReassignEntity SET reasonForFailure=:reasonForFailure , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId";
    public static final String SHIPMENT_UPDATE_CURRENTSTAGE_QUERY = "UPDATE ShipmentReassignEntity SET currentStage=:currentStage , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId AND currentStage != :currentStage";
    public static final String SHIPMENTS_FOR_TRACKING_BY_PARTNER_CODE = "SELECT shipmentId,logisticPartnerRefTrackingNumber,shipmentType FROM ShipmentReassignEntity WHERE logisticPartnerCode=:logisticPartnerCode AND currentStage != 'DEL' AND logisticPartnerRefTrackingNumber IS NOT NULL AND status = 'P'";
    public static final String SHIPMENT_UPDATE_CURRENTSTAGE_AND_AWB_QUERY = "UPDATE ShipmentReassignEntity SET currentStage=:currentStage ,logisticPartnerRefTrackingNumber=:logisticPartnerRefTrackingNumber, modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId AND currentStage != :currentStage";

    @Modifying
    @Query(SHIPMENT_UPDATE_STATUS_QUERY)
    Integer updateShipmentStatus(@Param("shipmentId") Long shipmentId, @Param("status") String status, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    @Modifying
    @Query(SHIPMENT_UPDATE_AWB_QUERY)
    Integer updateShipmentAWB(@Param("shipmentId") Long shipmentId, @Param("logisticPartnerCode") String logisticPartnerCode,
            @Param("logisticPartnerRefTrackingNumber") String logisticPartnerRefTrackingNumber, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    @Modifying
    @Query(SHIPMENT_UPDATE_FAILURE_REASON_QUERY)
    Integer updateFailureReason(@Param("shipmentId") Long shipmentId, @Param("reasonForFailure") String reasonForFailure, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    @Modifying
    @Query(SHIPMENT_UPDATE_CURRENTSTAGE_QUERY)
    Integer updateShipmentCurrentStage(@Param("shipmentId") Long shipmentId, @Param("currentStage") String currentStage, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    List<ShipmentReassignEntity> findByServiceRequestId(Long serviceRequestEntity);

    @Query(SHIPMENTS_FOR_TRACKING_BY_PARTNER_CODE)
    List<Object[]> findShipmentsForTracking(@Param("logisticPartnerCode") String partnerCode);

    List<ShipmentReassignEntity> findByLogisticPartnerRefTrackingNumber(String awbNumber);

    @Modifying
    @Query(SHIPMENT_UPDATE_CURRENTSTAGE_AND_AWB_QUERY)
    Integer updateShipmentCurrentStageAndAWB(@Param("shipmentId") Long shipmentId, @Param("currentStage") String currentStage, @Param("logisticPartnerRefTrackingNumber") String awbNumber,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);
}
