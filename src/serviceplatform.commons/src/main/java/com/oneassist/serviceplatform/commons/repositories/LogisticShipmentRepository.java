package com.oneassist.serviceplatform.commons.repositories;

import java.util.Date;
import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface LogisticShipmentRepository extends JpaRepository<ShipmentEntity, Long> {

    public static final String SHIPMENT_DASHBOARDINFO_QUERY = "SELECT currentStage, COUNT(*) FROM ShipmentEntity WHERE status=:status  AND TRUNC(modifiedOn)<=TRUNC(:modifiedOn) AND currentStage IN :shipmentStages group by currentStage";
    public static final String SHIPMENT_UPDATE_STATUS_QUERY = "UPDATE ShipmentEntity SET status=:status , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId";
    public static final String SHIPMENT_UPDATE_AWB_QUERY = "UPDATE ShipmentEntity SET logisticPartnerRefTrackingNumber=:logisticPartnerRefTrackingNumber , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId AND logisticPartnerCode=:logisticPartnerCode";
    public static final String SHIPMENT_UPDATE_FAILURE_REASON_QUERY = "UPDATE ShipmentEntity SET reasonForFailure=:reasonForFailure , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId";
    public static final String SHIPMENT_UPDATE_CURRENTSTAGE_QUERY = "UPDATE ShipmentEntity SET currentStage=:currentStage , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy WHERE shipmentId=:shipmentId AND logisticPartnerCode=:logisticPartnerCode";
    public static final String SHIPMENT_HISTORY_TRACKING_QUERY = "SELECT hist.origin_addrees_id as col1, hist.destination_addrees_id as col2, hist.logistic_partner_ref_track_no as col3,orig.address_line_1 as col4,orig.address_line_2 as col5, orig.landmark as col6, orig.pincode as col7, orig.district as col8,orig.mobile_no as col9, orig.country_code as col10,dest.address_line_1 as col11,dest.address_line_2 as col12, dest.landmark as col13,dest.pincode as col14,dest.district as col15,dest.mobile_no as col16,dest.country_code as col17,orig.ADDRESSEE_FULL_NAME as col18,dest.ADDRESSEE_FULL_NAME as col19,ship.current_stage as col20, ship.from_date as fromDate, ship.to_date as toDate FROM oa_shipment_hst hist LEFT JOIN oa_service_address_dtl orig ON orig.SERVICE_ADDRESS_ID = hist.origin_addrees_id LEFT JOIN oa_service_address_dtl dest ON dest.SERVICE_ADDRESS_ID = hist.destination_addrees_id INNER JOIN (SELECT current_stage, MIN(modified_on) as from_date, MAX(modified_on) as to_date FROM oa_shipment_hst where shipment_id=:shipmentId GROUP BY current_stage ) ship ON hist.current_stage  = ship.current_stage AND hist.modified_on = ship.to_date  and hist.shipment_id=:shipmentId ORDER BY fromDate ASC ";
    public static final String SHIPMENT_UPDATE_STATUS_UNASSIGNEDSTAGE_QUERY = "UPDATE ShipmentEntity SET status=:status,modifiedOn=:modifiedOn,modifiedBy=:modifiedBy,currentStage=:stage,logisticPartnerCode=:logisticPartnerCode WHERE shipmentId=:shipmentId";

    @Query(SHIPMENT_DASHBOARDINFO_QUERY)
    public List<Object[]> findDashBoardInfoByStatus(@Param("status") String status, @Param("shipmentStages") List<String> shipmentStages, @Param("modifiedOn") Date modifiedOn);

    ShipmentEntity findByShipmentIdAndLogisticPartnerCode(Long shipmentId, String logisticPartnerCode);

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
    Integer updateShipmentCurrentStage(@Param("shipmentId") Long shipmentId, @Param("logisticPartnerCode") String logisticPartnerCode, @Param("currentStage") String currentStage,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    @Query(value = SHIPMENT_HISTORY_TRACKING_QUERY, nativeQuery = true)
    public List<Object[]> findShipmentTrackingHistory(@Param("shipmentId") long shipmentId);

    ShipmentEntity findShipmentEntityByShipmentIdAndStatus(Long shipmentId, String status);

    ShipmentEntity findShipmentEntityByShipmentId(Long shipmentId);

    @Modifying
    @Query(SHIPMENT_UPDATE_STATUS_UNASSIGNEDSTAGE_QUERY)
    Integer updateShipmentStatusAndStage(@Param("shipmentId") Long shipmentId, @Param("logisticPartnerCode") String logisticPartnerCode, @Param("status") String status,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("stage") String stage);
}
