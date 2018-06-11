package com.oneassist.serviceplatform.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PartnerSubStatusDataMigration {

    public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-utility.xml");
        try {
            ServiceRequestTypeMasterCache serviceRequestTypeMasterCache = ctx.getBean(ServiceRequestTypeMasterCache.class);
            ServiceRequestRepository serviceRequestRepository = ctx.getBean(ServiceRequestRepository.class);

            List<Long> serviceRequestTypeList = new ArrayList<Long>();
            Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMasterList = serviceRequestTypeMasterCache.getAll();
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_AD.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_BD.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_BR.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_FR.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.HA_EW.getRequestType()).getServiceRequestTypeId());
            serviceRequestTypeList.add(serviceRequestTypeMasterList.get(ServiceRequestType.WHC_INSPECTION.getRequestType()).getServiceRequestTypeId());
            for (Long serviceType : serviceRequestTypeList) {
                List<ServiceRequestEntity> srEntities = serviceRequestRepository.findByServiceRequestTypeId(serviceType);
                if (srEntities != null) {
                    Map<String, String> partnerStageStatusMap = new HashMap<>();
                    partnerStageStatusMap.put("RV", "Rescheduled Visit");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("RV", "Rescheduled Visit");
                    partnerStageStatusMap.put("SN", "Spare Needed");
                    partnerStageStatusMap.put("VS", "Visit Scheduled");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("US", "Unsuccessful");
                    partnerStageStatusMap.put("AA", "Awaiting Approval");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("SU", "Success");
                    partnerStageStatusMap.put("US", "Unsuccessful");
                    partnerStageStatusMap.put("US", "Unsuccessful");
                    partnerStageStatusMap.put("PAC", "Pickup Appliance from Customer");
                    partnerStageStatusMap.put("SE", "SLA Extended");
                    partnerStageStatusMap.put("PAC", "Pickup Appliance from Customer");
                    partnerStageStatusMap.put("TRR", "Transport Required");
                    partnerStageStatusMap.put("SPRA", "Spare Available");
                    partnerStageStatusMap.put("AA", "Awaiting Approval");
                    partnerStageStatusMap.put("AA", "Awaiting Approval");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("ARES", "Awaiting Reschedule");
                    partnerStageStatusMap.put("ARES", "Awaiting Reschedule");
                    partnerStageStatusMap.put("TAP", "Technician Allocation Pending");
                    partnerStageStatusMap.put("INS", "Inspection Scheduled");
                    partnerStageStatusMap.put("INI", "Inspection Initiated");
                    partnerStageStatusMap.put("INF", "Inspection failed - No devices are Functional");
                    partnerStageStatusMap.put("CACC", "Cancelled- Customer Cancellation");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("AR", "Awaiting Reschedule");
                    partnerStageStatusMap.put("CAN", "Cancelled");
                    partnerStageStatusMap.put("RSD", "Re-Scheduled");
                    partnerStageStatusMap.put("US", "Unsuccessful");
                    partnerStageStatusMap.put("SCNA", "Service Centre Not Allocated ! No Service Centre configured for the given Pincode");
                    partnerStageStatusMap.put("INF", "Inspection failed - No devices are Functional");
                    partnerStageStatusMap.put("US", "Unsuccessful");
                    partnerStageStatusMap.put("PCD", "Appliance picked up");
                    partnerStageStatusMap.put("US", "Unsuccessful");

                    long numberOfRecordsUpdated = 0;
                    for (ServiceRequestEntity srEntity : srEntities) {
                    		ObjectMapper mapper = new ObjectMapper();
                            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                            WorkflowData workflow = mapper.readValue(srEntity.getWorkflowData(), WorkflowData.class);

                            if (workflow != null && workflow.getPartnerStageStatus() != null && workflow.getPartnerStageStatus().getStatus() != null) {
                                String status = partnerStageStatusMap.get(workflow.getPartnerStageStatus().getStatus());
                                workflow.getPartnerStageStatus().setStatus(status);
                                String workflowString = mapper.writeValueAsString(workflow);
                                srEntity.setWorkflowData(workflowString);
                                serviceRequestRepository.save(srEntity);
                                numberOfRecordsUpdated++;
                                System.out.println("SR Id: " + srEntity.getServiceRequestId());
                            }
                    }
                    System.out.println("Number of records updated: " + numberOfRecordsUpdated);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ctx.close();
            System.exit(0);
        }
    }
}
