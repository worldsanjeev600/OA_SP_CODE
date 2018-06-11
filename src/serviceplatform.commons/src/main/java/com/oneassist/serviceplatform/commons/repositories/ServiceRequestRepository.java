package com.oneassist.serviceplatform.commons.repositories;

import io.swagger.annotations.Api;
import java.util.Date;
import java.util.List;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(path = "/serviceRequest")
@Api(tags = { "/serviceRequest : Provides CRUD Operations on the Service Request Entity" })
public interface ServiceRequestRepository extends JpaRepository<ServiceRequestEntity, Long>, JpaSpecificationExecutor {

    public static final String SERVICEREQUEST_UPDATE_STATUS_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_EXTRENAL_REFERENCE_ID_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET externalSRReferenceId=:externalSRReferenceId , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_STATUS_BY_PRIMARY_SECONDARY_REFNO_QUERY = "UPDATE ServiceRequestEntity SET status=:status , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE refPrimaryTrackingNo=:refPrimaryTrackingNo";

    public static final String SERVICEREQUEST_UPDATE_ASIGNEE_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET assignee=:assignee , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy, workflowAlert = DECODE(workflowAlert,'AT', NULL, workflowAlert) "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_SCHEDULE_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET scheduleSlotStartDateTime=:scheduleSlotStartDateTime, scheduleSlotEndDateTime=:scheduleSlotEndDateTime , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_SCHEDULE_AND_STATUS_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET  workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy, workflowAlert = DECODE(workflowAlert,'RNC', NULL, workflowAlert), dueDateTime=:dueDateTime, workflowData=:workflowData "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_WORKFLOWDATA_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET workflowData=:workflowData , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_WORKFLOWALERT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET workflowAlert=:workflowAlert , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_SERVICEREQUEST_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus ,workflowData=:workflowData ,modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_SCHEDULE_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET scheduleSlotStartDateTime=:scheduleSlotStartDateTime, scheduleSlotEndDateTime=:scheduleSlotEndDateTime , status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus ,workflowData=:workflowData ,modifiedOn=:modifiedOn , modifiedBy=:modifiedBy, workflowAlert = null, dueDateTime=:dueDateTime "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_STATUS_AND_WORKFLOWDATA_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , workflowData=:workflowData , modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String UPDATE_WORKFLOW_ALERT_ASSIGNEE_ALLOCATION_BREACH_QUERY = "UPDATE OA_SERVICE_REQUEST_DTL SET WORKFLOW_ALERT=:workflowAlert , MODIFIED_ON=sysdate, MODIFIED_BY=:modifiedBy "
            + "WHERE ASSIGNEE is NULL and (CAST(SCHEDULE_SLOT_START_DATETIME AS DATE) - sysdate) * 24 <=:slaBreachInHours and STATUS='P' and SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST where SERVICE_REQUEST_TYPE=:serviceRequestType) AND SERVICE_PARTNER_BU_CODE is not null ";

    public static final String UPDATE_WORKFLOW_ALERT_ASSIGNEE_VISIT_BREACH_QUERY = "UPDATE OA_SERVICE_REQUEST_DTL SET WORKFLOW_ALERT=:workflowAlert , MODIFIED_ON=sysdate, MODIFIED_BY=:modifiedBy "
            + "WHERE ASSIGNEE is NOT NULL and (sysdate - CAST(SCHEDULE_SLOT_END_DATETIME AS DATE) ) >=0 and STATUS='P' and SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST where SERVICE_REQUEST_TYPE=:serviceRequestType) AND SERVICE_PARTNER_BU_CODE is not null ";

    public static final String UPDATE_WORKFLOW_ALERT_SR_NOTCLOSED_WITHINSLA_QUERY = "UPDATE OA_SERVICE_REQUEST_DTL SET WORKFLOW_ALERT=:workflowAlert , MODIFIED_ON=sysdate, MODIFIED_BY=:modifiedBy WHERE (sysdate - CAST(DUE_DATETIME AS DATE)) >=0 and STATUS NOT IN ('CO','X') and SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST "
            + "where SERVICE_REQUEST_TYPE=:serviceRequestType) AND SERVICE_PARTNER_BU_CODE is not null ";

    public static final String SERVICEREQUEST_UPDATE_STATUS_AND_WORKFLOWALERT_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus,workflowData=:workflowData, workflowAlert=:workflowAlert, modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_ASSIGNEE_SERVICEREQUEST_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET assignee=:assignee ,status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus ,workflowData=:workflowData ,modifiedOn=:modifiedOn , modifiedBy=:modifiedBy ,workflowAlert = null "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String INSPECTIONREQUEST_UPDATE_SCHEDULE_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET scheduleSlotStartDateTime=:scheduleSlotStartDateTime, scheduleSlotEndDateTime=:scheduleSlotEndDateTime , status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus ,workflowData=:workflowData ,modifiedOn=:modifiedOn , modifiedBy=:modifiedBy, workflowAlert = null "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String INSPECTION_REQUEST_GET_SLA_EXPIRED_SRS = "SELECT * FROM OA_SERVICE_REQUEST_DTL WHERE (sysdate- CAST(DUE_DATETIME AS DATE)) >=0 AND STATUS NOT IN ('CO','X','CAN') AND SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST  "
            + "WHERE SERVICE_REQUEST_TYPE=:serviceRequestType)";

    public static final String UPDATE_WORKFLOWALERT_TECHNICIAN_ALLOCATION_BREACH_QUERY = "UPDATE OA_SERVICE_REQUEST_DTL SET WORKFLOW_ALERT=:workflowAlert , MODIFIED_ON=sysdate, MODIFIED_BY=:modifiedBy WHERE SERVICE_REQUEST_ID in :serviceRequestIds";

    public static final String SELECT_TECHNICIAN_ALLOCATION_BREACH_QUERY = "SELECT * FROM OA_SERVICE_REQUEST_DTL WHERE ASSIGNEE is NULL and STATUS='P' and SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST "
            + "where SERVICE_REQUEST_TYPE=:serviceRequestType) AND SERVICE_PARTNER_BU_CODE is not null";

    public static final String UPDATE_WORKFLOW_ALERT_INSPECTION_VISIT_BREACH_QUERY = "UPDATE OA_SERVICE_REQUEST_DTL SET WORKFLOW_ALERT=:workflowAlert , MODIFIED_ON=sysdate, MODIFIED_BY=:modifiedBy WHERE ASSIGNEE is NOT NULL and (sysdate - CAST(SCHEDULE_SLOT_END_DATETIME AS DATE) ) >=0 and STATUS='P' and SERVICE_REQUEST_TYPE_ID=(SELECT SERVICE_REQUEST_TYPE_ID FROM OA_SERVICE_REQUEST_TYPE_MST "
            + "where SERVICE_REQUEST_TYPE=:serviceRequestType)";

    public static final String SERVICEREQUEST_UPDATE_STATUS_WORKFLOWALERT_AND_ACTUALSTARTDATE_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus,workflowData=:workflowData, workflowAlert=:workflowAlert, actualStartDateTime=:actualStartDateTime, modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_STATUS_WORKFLOWALERT_AND_ACTUALENDDATE_ON_EVENT_BY_SERVICEREQUESTID_QUERY = "UPDATE ServiceRequestEntity SET status=:status , workflowStage=:workflowStage ,workflowStageStatus=:workflowStageStatus,workflowData=:workflowData, workflowAlert=:workflowAlert, actualEndDateTime=:actualEndDateTime, modifiedOn=:modifiedOn , modifiedBy=:modifiedBy "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_SUBMIT_FEEDBACK_QUERY = "UPDATE ServiceRequestEntity SET " + "feedbackRating=:feedbackRating, " + "feedbackCode=:feedbackCode, "
            + "feedbackComments=:feedbackComments," + "modifiedOn=:modifiedOn, " + "modifiedBy=:modifiedBy " + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_CLOSED_EXPIRED_ARCHIVAL_QUERY = "SELECT req.SERVICE_REQUEST_ID,listagg(doc.STORAGE_REF_ID, '#') within GROUP (ORDER BY req.SERVICE_REQUEST_ID) FROM oa_service_doc_dtl doc JOIN oa_service_request_dtl req ON req.SERVICE_REQUEST_ID=doc.SERVICE_REQUEST_ID WHERE req.STATUS IN ('CO','X') AND req.MODIFIED_ON + :archivalCutOffInDays <= SYSDATE  AND CLOUD_STORAGE_ARCHIVE_ID is NULL GROUP BY req.SERVICE_REQUEST_ID";

    public static final String SERVICEREQUEST_UPDATE_GLACIER_ARCHIVE_ID_QUERY = "UPDATE ServiceRequestEntity SET cloudStorageArchiveId=:cloudStorageArchiveId, modifiedOn=:modifiedOn "
            + "WHERE serviceRequestId=:serviceRequestId";
    public static final String SERVICEREQUEST_UPDATE_CLOUD_JOB_STATUS_QUERY = "UPDATE ServiceRequestEntity SET cloudJobStatus=:cloudJobStatus, modifiedOn=:modifiedOn "
            + "WHERE serviceRequestId=:serviceRequestId";

    public static final String SERVICEREQUEST_UPDATE_ADVICE_ID = "UPDATE ServiceRequestEntity SET adviceId=:adviceId WHERE serviceRequestId=:serviceRequestId";

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestStatus(@Param("status") String status, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_BY_PRIMARY_SECONDARY_REFNO_QUERY)
    Integer updateServiceRequestStatusByRefNum(@Param("status") String status, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("refPrimaryTrackingNo") String refPrimaryTrackingNo);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_ASIGNEE_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestAssignee(@Param("assignee") Long assignee, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_SCHEDULE_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestSchedule(@Param("scheduleSlotStartDateTime") Date scheduleSlotStartDateTime, @Param("scheduleSlotEndDateTime") Date scheduleSlotEndDateTime,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_WORKFLOWDATA_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestWorkflowData(@Param("workflowData") String workflowData, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_WORKFLOWALERT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestWorkflowAlert(@Param("workflowAlert") String workflowAlert, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_SERVICEREQUEST_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestOnEvent(@Param("status") String status, @Param("workflowStage") String workflowStage, @Param("workflowStageStatus") String workflowStageStatus,
            @Param("workflowData") String workflowData, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_AND_WORKFLOWALERT_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestStatusAndWorkflowAlert(@Param("status") String status, @Param("workflowStage") String workflowStage, @Param("workflowStageStatus") String workflowStageStatus,
            @Param("workflowData") String workflowData, @Param("workflowAlert") String workflowAlert, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_SCHEDULE_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestScheduleAndStatus(@Param("scheduleSlotStartDateTime") Date scheduleSlotStartDateTime, @Param("scheduleSlotEndDateTime") Date scheduleSlotEndDateTime,
            @Param("status") String status, @Param("workflowStage") String workflowStage, @Param("workflowStageStatus") String workflowStageStatus, @Param("workflowData") String workflowData,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId, @Param("dueDateTime") Date dueDateTime);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_SCHEDULE_AND_STATUS_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestScheduleAndCMSStatus(@Param("workflowStage") String workflowStage, @Param("workflowStageStatus") String workflowStageStatus, @Param("modifiedOn") Date modifiedOn,
            @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId, @Param("dueDateTime") Date dueDateTime, @Param("workflowData") String workflowData);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_WORKFLOWALERT_AND_ACTUALSTARTDATE_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestStatusWorkflowAlertAndActualStartDate(@Param("status") String status, @Param("workflowStage") String workflowStage,
            @Param("workflowStageStatus") String workflowStageStatus, @Param("workflowData") String workflowData, @Param("workflowAlert") String workflowAlert,
            @Param("actualStartDateTime") Date actualStartDateTime, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_WORKFLOWALERT_AND_ACTUALENDDATE_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestStatusWorkflowAlertAndActualEndDate(@Param("status") String status, @Param("workflowStage") String workflowStage,
            @Param("workflowStageStatus") String workflowStageStatus, @Param("workflowData") String workflowData, @Param("workflowAlert") String workflowAlert,
            @Param("actualEndDateTime") Date actualEndDateTime, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_STATUS_AND_WORKFLOWDATA_BY_SERVICEREQUESTID_QUERY)
    Integer updateServiceRequestStatusAndWorkflowData(@Param("status") String status, @Param("workflowData") String workflowData, @Param("modifiedOn") Date modifiedOn,
            @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    List<ServiceRequestEntity> findByRefPrimaryTrackingNo(String refPrimaryTrackingNo);

    ServiceRequestEntity findServiceRequestEntityByServiceRequestId(Long serviceRequestId);

    @Modifying
    @Query(value = UPDATE_WORKFLOW_ALERT_ASSIGNEE_VISIT_BREACH_QUERY, nativeQuery = true)
    Integer updateWorkFlowAlertForAssigneeVisitBreach(@Param("workflowAlert") String workflowAlert, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestType") String serviceRequestType);

    @Modifying
    @Query(value = UPDATE_WORKFLOW_ALERT_SR_NOTCLOSED_WITHINSLA_QUERY, nativeQuery = true)
    Integer updateWorkFlowAlertForSRInCompleteBreach(@Param("workflowAlert") String workflowAlert, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestType") String serviceRequestType);

    List<ServiceRequestEntity> findServiceRequestEntityByServiceRequestIdAndRefPrimaryTrackingNo(Long serviceRequestId, String refPrimaryTrackingNo);

    List<ServiceRequestEntity> findByRefPrimaryTrackingNoAndServiceRequestTypeIdIn(String refPrimaryTrackingNo, List<Long> serviceRequestTypeId);

    List<ServiceRequestEntity> findByReferenceNoAndServiceRequestTypeIdAndStatusNotIn(String referenceNo, Long serviceRequestTypeId, List<String> statusList);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_ASSIGNEE_SERVICEREQUEST_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateAssigneeAndServiceRequestOnEvent(@Param("assignee") Long assignee, @Param("status") String status, @Param("workflowStage") String workflowStage,
            @Param("workflowStageStatus") String workflowStageStatus, @Param("workflowData") String workflowData, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(INSPECTIONREQUEST_UPDATE_SCHEDULE_ON_EVENT_BY_SERVICEREQUESTID_QUERY)
    Integer updateInspectionRequestScheduleAndStatus(@Param("scheduleSlotStartDateTime") Date scheduleSlotStartDateTime, @Param("scheduleSlotEndDateTime") Date scheduleSlotEndDateTime,
            @Param("status") String status, @Param("workflowStage") String workflowStage, @Param("workflowStageStatus") String workflowStageStatus, @Param("workflowData") String workflowData,
            @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(value = INSPECTION_REQUEST_GET_SLA_EXPIRED_SRS, nativeQuery = true)
    List<ServiceRequestEntity> getExpiredInspectionSRs(@Param("serviceRequestType") String serviceRequestType);

    @Modifying
    @Query(value = UPDATE_WORKFLOWALERT_TECHNICIAN_ALLOCATION_BREACH_QUERY, nativeQuery = true)
    Integer updateWorkFlowAlertForTechnicianAllocationBreach(@Param("workflowAlert") String workflowAlert, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestIds") List<Long> serviceRequestIds);

    @Modifying
    @Query(value = SELECT_TECHNICIAN_ALLOCATION_BREACH_QUERY, nativeQuery = true)
    List<ServiceRequestEntity> getUnAssisgnedSRs(@Param("serviceRequestType") String serviceRequestType);

    @Modifying
    @Query(value = UPDATE_WORKFLOW_ALERT_INSPECTION_VISIT_BREACH_QUERY, nativeQuery = true)
    Integer updateWorkFlowAlertForInspectionVisitBreach(@Param("workflowAlert") String workflowAlert, @Param("modifiedBy") String modifiedBy, @Param("serviceRequestType") String serviceRequestType);

    List<ServiceRequestEntity> findByReferenceNoAndWorkflowStageAndWorkflowStageStatusInAndServiceRequestTypeIdIn(String referenceNo, String status, List<String> workflowStageStatusList,
            List serviceRequestTypeList);

    List<ServiceRequestEntity> findByRefPrimaryTrackingNoAndServiceRequestTypeId(String orderId, Long serviceRequestTypeId);

    @Modifying
    @Query(SERVICEREQUEST_SUBMIT_FEEDBACK_QUERY)
    Integer updateServiceRequestFeedbackDetails(@Param("serviceRequestId") Long serviceRequestId, @Param("feedbackRating") String feedbackRating, @Param("feedbackCode") String feedbackCode,
            @Param("feedbackComments") String feedbackComments, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy);

    List<ServiceRequestEntity> findByReferenceNoAndRefSecondaryTrackingNoAndServiceRequestTypeIdAndStatusNotIn(String membershipId, String assetId, Long serviceRequestTypeId, List<String> statusList);

    List<ServiceRequestEntity> findByReferenceNoAndRefSecondaryTrackingNoInAndServiceRequestTypeIdInAndStatusNotIn(String membershipId, List<String> assetId, List<Long> serviceRequestTypeList,
            List<String> statusList);

    List<ServiceRequestEntity> findByServiceRequestTypeId(Long serviceRequestTypeId);

    @Query(value = SERVICEREQUEST_CLOSED_EXPIRED_ARCHIVAL_QUERY, nativeQuery = true)
    public List<Object[]> getArchivableSRs(@Param("archivalCutOffInDays") int archivalCutOffInDays);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_GLACIER_ARCHIVE_ID_QUERY)
    int updateAwsArchiveId(@Param("cloudStorageArchiveId") String cloudStorageArchiveId, @Param("modifiedOn") Date modifiedOn, @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_CLOUD_JOB_STATUS_QUERY)
    int updateCloudJobStatus(@Param("cloudJobStatus") String cloudJobStatus, @Param("modifiedOn") Date modifiedOn, @Param("serviceRequestId") long serviceRequestId);

    List<ServiceRequestEntity> findByServiceRequestIdInAndReferenceNo(List<Long> serviceRequestIds, String membershipId);

    List<ServiceRequestEntity> findByReferenceNoAndCreatedOnAfter(String referenceNo, Date createdOn);

    ServiceRequestEntity findByExternalSRReferenceId(String externalServiceId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_EXTRENAL_REFERENCE_ID_BY_SERVICEREQUESTID_QUERY)
    Integer updateExternalSRReferenceId(@Param("externalSRReferenceId") String externalSRReferenceId, @Param("modifiedOn") Date modifiedOn, @Param("modifiedBy") String modifiedBy,
            @Param("serviceRequestId") Long serviceRequestId);

    @Modifying
    @Query(SERVICEREQUEST_UPDATE_ADVICE_ID)
    Integer updateAdviceId(@Param("adviceId") String adviceId, @Param("serviceRequestId") Long serviceRequestId);

    public ServiceRequestEntity findByServiceRequestId(Long serviceRequestId);

    ServiceRequestEntity findByAdviceId(String adviceId);

    List<ServiceRequestEntity> findByRefSecondaryTrackingNoAndReferenceNoAndServiceRequestTypeIdAndStatus(String refSecondaryTrackingNo, String referenceNo, Long serviceRequestTypeId, String status);
}
