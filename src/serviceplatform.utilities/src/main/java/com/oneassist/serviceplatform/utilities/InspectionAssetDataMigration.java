package com.oneassist.serviceplatform.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceDocumentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntityDocumentEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestEntityDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ImageStorageReference;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Inspection;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;
import com.oneassist.serviceplatform.services.asset.ServiceRequestAssetServiceImpl;
import org.drools.core.util.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

public class InspectionAssetDataMigration {

    public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-utility.xml");
        try {
            ServiceRequestRepository serviceRequestRepository = ctx.getBean(ServiceRequestRepository.class);
            ServiceRequestTypeMasterCache serviceRequestTypeMasterCache = ctx.getBean(ServiceRequestTypeMasterCache.class);
            ServiceRequestAssetServiceImpl serviceRequestAssetServiceImpl = ctx.getBean(ServiceRequestAssetServiceImpl.class);
            ServiceRequestEntityDocumentRepository serviceRequestAssetDocumentRepository = ctx.getBean(ServiceRequestEntityDocumentRepository.class);
            List<ServiceRequestEntity> srEntities = serviceRequestRepository.findByServiceRequestTypeId(serviceRequestTypeMasterCache.get(ServiceRequestType.WHC_INSPECTION.getRequestType())
                    .getServiceRequestTypeId());
            ServiceDocumentRepository serviceDocumentRepository = ctx.getBean(ServiceDocumentRepository.class);
            if (srEntities != null) {
                for (ServiceRequestEntity srEntity : srEntities) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    WorkflowData workflow = mapper.readValue(srEntity.getWorkflowData(), WorkflowData.class);
                    if (workflow.getInspectionAssessment() != null && !CollectionUtils.isEmpty(workflow.getInspectionAssessment().getAssets())) {
                        for (Inspection inspection : workflow.getInspectionAssessment().getAssets()) {
                            ServiceRequestAssetRequestDto asset = new ServiceRequestAssetRequestDto();
                            asset.setAssetAge(inspection.getProductAge());
                            asset.setAssetInspectionStatus(inspection.getProdInspectionStatus());
                            asset.setAssetSize(inspection.getProductSize());
                            asset.setAssetTechnology(inspection.getProductTechnology());
                            asset.setAssetUnit(inspection.getProductUnit());
                            asset.setCreatedBy("SCRIPT");
                            asset.setCreatedOn(new Date());
                            asset.setIsAccidentalDamage(inspection.getIsAccidentalDamage());
                            asset.setIsFunctional(inspection.getIsFunctional());
                            asset.setIsInformationCorrect(inspection.getIsInformationCorrect());
                            asset.setMake(inspection.getBrand());
                            asset.setModelNo(inspection.getModelNo());
                            asset.setProductCode(inspection.getProductCode());
                            asset.setSerialNo(inspection.getSerialNo());
                            asset.setAssetId(UUID.randomUUID().toString().replaceAll("-", ""));
                            asset.setServiceRequestId(srEntity.getServiceRequestId());
                            ServiceRequestAssetResponseDto response = serviceRequestAssetServiceImpl.createServiceRequestAsset(asset);
                            if (response != null && inspection.getImageStorageRef() != null) {
                                List<ServiceRequestEntityDocumentEntity> assetDocEntities = new ArrayList<ServiceRequestEntityDocumentEntity>();
                                for (ImageStorageReference imgStorage : inspection.getImageStorageRef()) {
                                    String docId = null;
                                    if (!StringUtils.isEmpty(imgStorage.getDocumentId())) {
                                        docId = imgStorage.getDocumentId();
                                    } else if (!StringUtils.isEmpty(imgStorage.getImageMongoRefId())) {
                                        ServiceDocumentEntity docEntity = serviceDocumentRepository.findByStorageRefId(imgStorage.getImageMongoRefId());
                                        if (docEntity != null) {
                                            docId = docEntity.getDocumentId();
                                        }
                                    }
                                    if (docId != null) {
                                        ServiceRequestEntityDocumentEntity assetDocEntity = new ServiceRequestEntityDocumentEntity();
                                        assetDocEntity.setCreatedBy("SCRIPT");
                                        assetDocEntity.setCreatedOn(new Date());
                                        assetDocEntity.setDocumentId(docId);
                                        assetDocEntity.setEntityName("asset");
                                        assetDocEntity.setEntityId(response.getAssetId());
                                        assetDocEntity.setStatus(Constants.ACTIVE);
                                        assetDocEntities.add(assetDocEntity);
                                    }
                                }
                                if (!CollectionUtils.isEmpty(assetDocEntities)) {
                                    serviceRequestAssetDocumentRepository.save(assetDocEntities);
                                }
                            }
                        }/*
                          * 
                          * workflow.getInspectionAssessment().setAssets(null); srEntity.setWorkflowData(mapper.writeValueAsString(workflow)); serviceRequestRepository.save(srEntity);
                          */

                    }
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
