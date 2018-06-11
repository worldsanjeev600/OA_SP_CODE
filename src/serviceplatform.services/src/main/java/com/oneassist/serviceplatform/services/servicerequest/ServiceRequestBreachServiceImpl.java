package com.oneassist.serviceplatform.services.servicerequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestSLABreachParams;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceRequestBreachServiceImpl implements IServiceRequestBreachService {

    private final Logger logger = Logger.getLogger(ServiceRequestBreachServiceImpl.class);

    private static final int EW_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT = 4;

    private static final int AD_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT = 4;

    private static final int BD_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT = 4;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    ServiceRequestHelper serviceRequestHelper;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @Transactional
    public ResponseDto processSLABreach() {

        ResponseDto response = new ResponseDto();

        List<ErrorInfoDto> errorInfoList = new ArrayList<>();
        processSRSLABreachForHAExtendedWarranty(errorInfoList);
        processInspectionRequestSLABreach(errorInfoList);
        processSRSLABreachForHAAccidentalDamage(errorInfoList);
        processSRSLABreachForHABreakdown(errorInfoList);

        if (errorInfoList != null && !errorInfoList.isEmpty()) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage("SLA Breach Update Processed with Errors");
            response.setInvalidData(errorInfoList);
        } else {
            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage("SLA Breach Update Processed Successfully");
            response.setInvalidData(null);
        }

        return response;
    }

    private void processSRSLABreachForHAAccidentalDamage(List<ErrorInfoDto> errorInfoList) {
        processTechnicianNotAssignedBreach(ServiceRequestSLABreachParams.ALLOCATE_TECHNICIAN_BREACH.getParamName(), Constants.REQUEST_SOURCE_BATCH, AD_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT,
                ServiceRequestType.HA_AD.getRequestType(), errorInfoList);
        processTechnicianVisitBreachForAccidentalDamage(ServiceRequestSLABreachParams.VISIT_NOT_STARTED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.HA_AD.getRequestType(),
                errorInfoList);
        processSRNotCompletedBreachForAccidentalDamage(ServiceRequestSLABreachParams.HA_AD_REPAIR_NOT_COMPLETED.getParamName(), Constants.REQUEST_SOURCE_BATCH,
                ServiceRequestType.HA_AD.getRequestType(), errorInfoList);
    }

    private void processTechnicianVisitBreachForAccidentalDamage(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processTechnicianVisitBreachForAccidentalDamage for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's for which Technician has not Visited before the schedule time -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForAssigneeVisitBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of Technician Not Visited SRs for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of Technician Not Visited SRs for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processSRNotCompletedBreachForAccidentalDamage(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processSRNotCompletedBreachForAccidentalDamage for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's that are not completed before the due date -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForSRInCompleteBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of  SRs Not Completed for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of  SRs Not Completed for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processSRSLABreachForHABreakdown(List<ErrorInfoDto> errorInfoList) {

        processTechnicianNotAssignedBreach(ServiceRequestSLABreachParams.ALLOCATE_TECHNICIAN_BREACH.getParamName(), Constants.REQUEST_SOURCE_BATCH, BD_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT,
                ServiceRequestType.HA_BD.getRequestType(), errorInfoList);
        processTechnicianVisitBreachForBreakdown(ServiceRequestSLABreachParams.VISIT_NOT_STARTED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.HA_BD.getRequestType(),
                errorInfoList);
        processSRNotCompletedBreachForBreakdown(ServiceRequestSLABreachParams.HA_BD_REPAIR_NOT_COMPLETED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.HA_BD.getRequestType(),
                errorInfoList);
    }

    private void processTechnicianVisitBreachForBreakdown(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processTechnicianVisitBreachForBreakdown for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's for which Technician has not Visited before the schedule time -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForAssigneeVisitBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of Technician Not Visited SRs for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of Technician Not Visited SRs for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processSRNotCompletedBreachForBreakdown(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processSRNotCompletedBreachForBreakdown for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's that are not completed before the due date -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForSRInCompleteBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of  SRs Not Completed for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of  SRs Not Completed for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processSRSLABreachForHAExtendedWarranty(List<ErrorInfoDto> errorInfoList) {

        processTechnicianNotAssignedBreach(ServiceRequestSLABreachParams.ALLOCATE_TECHNICIAN_BREACH.getParamName(), Constants.REQUEST_SOURCE_BATCH, EW_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT,
                ServiceRequestType.HA_EW.getRequestType(), errorInfoList);
        processTechnicianVisitBreach(ServiceRequestSLABreachParams.VISIT_NOT_STARTED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.HA_EW.getRequestType(), errorInfoList);
        processSRNotCompletedBreach(ServiceRequestSLABreachParams.REPAIR_NOT_COMPLETED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.HA_EW.getRequestType(), errorInfoList);
    }

    private void processInspectionRequestSLABreach(List<ErrorInfoDto> errorInfoList) {

        processTechnicianNotAssignedBreach(ServiceRequestSLABreachParams.ALLOCATE_TECHNICIAN_BREACH.getParamName(), Constants.REQUEST_SOURCE_BATCH, EW_SLA_TECHNICIAN_ALLOCATION_BREACH_TIMEOUT,
                ServiceRequestType.WHC_INSPECTION.getRequestType(), errorInfoList);
        processInspectionVisitBreach(ServiceRequestSLABreachParams.VISIT_NOT_STARTED.getParamName(), Constants.REQUEST_SOURCE_BATCH, ServiceRequestType.WHC_INSPECTION.getRequestType(), errorInfoList);
        processInspectionSRExpiredBreach(ServiceRequestSLABreachParams.INSPECTION_CANCELLED_DUE_TO_SR_EXPIRATION.getParamName(), Constants.REQUEST_SOURCE_BATCH,
                ServiceRequestType.WHC_INSPECTION.getRequestType(), errorInfoList);
    }

    private void processInspectionSRExpiredBreach(String workFlowAlert, String modifiedBy, String requestType, List<ErrorInfoDto> errorInfoList) {

        try {
            List<ServiceRequestEntity> expiredInspectionSrs = serviceRequestRepository.getExpiredInspectionSRs(ServiceRequestType.WHC_INSPECTION.getRequestType());
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            int isUpdated;

            for (ServiceRequestEntity expiredSREntity : expiredInspectionSrs) {
                ServiceRequestDto serviceRequestUpdateDto = new ServiceRequestDto();
                serviceRequestUpdateDto.setWorkflowJsonString(expiredSREntity.getWorkflowData());
                serviceRequestUpdateDto.setServiceRequestId(expiredSREntity.getServiceRequestId());
                serviceRequestUpdateDto.setServiceRequestType(ServiceRequestType.WHC_INSPECTION.getRequestType());

                try {
                    serviceRequestHelper.populateAllStatusesBasedOnEventCode(serviceRequestUpdateDto, ServiceRequestEventCode.INSPECTION_SR_EXPIRED, null);
                    isUpdated = serviceRequestRepository.updateServiceRequestStatusAndWorkflowAlert(serviceRequestUpdateDto.getStatus(), serviceRequestUpdateDto.getWorkflowStage(),
                            serviceRequestUpdateDto.getWorkflowStageStatus(), serviceRequestUpdateDto.getWorkflowJsonString(), workFlowAlert, currentTimestamp,
                            serviceRequestUpdateDto.getModifiedBy(), serviceRequestUpdateDto.getServiceRequestId());

                    if (isUpdated == 0) {
                        logger.error("Error Updating Status Of Expired Inspection SR :: " + serviceRequestUpdateDto.getServiceRequestId());
                    } else {
                        // Communication Notification to be sent if needed
                        // serviceRequestHelper.sendCommunication(serviceRequestUpdateDto,
                        // CommunicationGatewayEventCode.WHC_TECHNICIAN_CANCELLED_FOR_INSPECTION, null);
                    }
                } catch (Exception e) {
                    logger.error("Error Updating Status Of Expired Inspection SR :: " + serviceRequestUpdateDto.getServiceRequestId() + e);
                    ErrorInfoDto errorInfoDto = new ErrorInfoDto();
                    errorInfoDto.setErrorMessage("Error Updating Status Of Expired Inspection SR with service Request ID" + serviceRequestUpdateDto.getServiceRequestId());
                    errorInfoList.add(errorInfoDto);
                }
            }
        } catch (Exception e) {
            logger.error("Error Getting Expired Inspection SRs >>", e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Getting Expired Inspection SRs " + requestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processInspectionVisitBreach(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processTechnicianVisitBreach for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's for which Technician has not Visited before the schedule time -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForInspectionVisitBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of Inspection Visit Breached SRs >> ", e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of Inspection Visit Breached SRss for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processTechnicianNotAssignedBreach(String workflowAlert, String modifiedBy, int slaBreachInHours, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        ServiceRequestEntity serviceRequestEntity = null;
        List<Long> srList = new ArrayList<>();

        try {
            logger.debug("In processTechnicianNotAssignedBreach for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType + " and slaBreachInHours>> "
                    + slaBreachInHours);

            List<ServiceRequestEntity> unassignedSRs = serviceRequestRepository.getUnAssisgnedSRs(serviceRequestType);

            Iterator<ServiceRequestEntity> srIterator = unassignedSRs.iterator();

            while (srIterator.hasNext()) {
                serviceRequestEntity = srIterator.next();
                checkForTechnicianAllocationBreach(serviceRequestEntity, slaBreachInHours, srList);
            }

            if (!srList.isEmpty()) {

                List<List<Long>> subSRLists = subLists(srList, 1000);
                Iterator<List<Long>> itr = subSRLists.iterator();

                while (itr.hasNext()) {
                    serviceRequestRepository.updateWorkFlowAlertForTechnicianAllocationBreach(workflowAlert, modifiedBy, itr.next());
                }
            }

            logger.debug("Number of " + serviceRequestType + " SRs for which Technician is not Assigned -->>>>" + unassignedSRs.size());
        } catch (Exception e) {
            logger.error("Error Updating Status Of " + serviceRequestType + " Not Assigned SRs >>", e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of " + serviceRequestType + " SR:" + serviceRequestEntity.getServiceRequestId());
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processTechnicianVisitBreach(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processTechnicianVisitBreach for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's for which Technician has not Visited before the schedule time -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForAssigneeVisitBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of Technician Not Visited SRs for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of Technician Not Visited SRs for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private void processSRNotCompletedBreach(String workflowAlert, String modifiedBy, String serviceRequestType, List<ErrorInfoDto> errorInfoList) {

        try {
            logger.debug("In processSRNotCompletedBreach for workflowAlert >> " + workflowAlert + "  and serviceRequestType>> " + serviceRequestType);
            logger.debug("Number of SR's that are not completed before the due date -->>>>"
                    + serviceRequestRepository.updateWorkFlowAlertForSRInCompleteBreach(workflowAlert, modifiedBy, serviceRequestType));
        } catch (Exception e) {
            logger.error("Error Updating Status Of  SRs Not Completed for Request Type :" + serviceRequestType, e);
            ErrorInfoDto errorInfoDto = new ErrorInfoDto();
            errorInfoDto.setErrorMessage("Error Updating Status Of  SRs Not Completed for Request Type " + serviceRequestType);
            errorInfoList.add(errorInfoDto);
        }
    }

    private List<List<Long>> subLists(List<Long> srList, final int length) {
        List<List<Long>> subSRLists = new ArrayList<>();
        int size = srList.size();
        for (int i = 0; i < size; i += length) {
            srList.subList(i, Math.min(size, i + length));
            subSRLists.add(new ArrayList<Long>(srList.subList(i, Math.min(size, i + length))));
        }
        return subSRLists;
    }

    private void checkForTechnicianAllocationBreach(ServiceRequestEntity serviceRequestEntity, int slaBreachInHours, List<Long> srList) {

        Date currentDate = new Date();
        Date serviceScheduleStartTime = serviceRequestEntity.getScheduleSlotStartDateTime();

        if (serviceScheduleStartTime.getTime() - currentDate.getTime() <= slaBreachInHours * 60 * 60 * 1000) {
            srList.add(serviceRequestEntity.getServiceRequestId());

        } else {

            Date nextBusinessDate = com.oneassist.serviceplatform.commons.utils.DateUtils.getNextBusinessDateTime(currentDate);
            Date businessEndTimeToday = com.oneassist.serviceplatform.commons.utils.DateUtils.getCurrentBusinessEndDateTime(currentDate);

            Calendar cutOffTime = Calendar.getInstance();
            cutOffTime.setTimeInMillis(serviceRequestEntity.getScheduleSlotStartDateTime().getTime() - nextBusinessDate.getTime());

            Calendar timeGap = Calendar.getInstance();
            timeGap.setTimeInMillis(slaBreachInHours * 60 * 60 * 1000 - cutOffTime.getTimeInMillis());

            Calendar alertTriggerTimeToday = Calendar.getInstance();
            alertTriggerTimeToday.setTimeInMillis(businessEndTimeToday.getTime() - timeGap.getTimeInMillis());

            if (cutOffTime.getTimeInMillis() <= slaBreachInHours * 60 * 60 * 1000 && currentDate.after(alertTriggerTimeToday.getTime())) {

                srList.add(serviceRequestEntity.getServiceRequestId());

            }

        }
    }
}
