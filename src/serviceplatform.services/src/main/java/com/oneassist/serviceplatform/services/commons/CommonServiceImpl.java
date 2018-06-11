package com.oneassist.serviceplatform.services.commons;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.cache.ServicePartnerMappingCache;
import com.oneassist.serviceplatform.commons.cache.HubMasterCache;
import com.oneassist.serviceplatform.commons.cache.PartnerBUCache;
import com.oneassist.serviceplatform.commons.cache.PartnerEventMasterCache;
import com.oneassist.serviceplatform.commons.cache.PartnerMasterCache;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.cache.ProductMasterCache;
import com.oneassist.serviceplatform.commons.cache.SystemConfigMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.datanotification.ShipmentDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.MasterData;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentReassignmentRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.PartnerEventDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.ServicePartnerMappingDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceScheduleDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceSlot;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.stage.ServiceReqeustStageDetailDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ServicePartner;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.document.IServiceRequestDocumentService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@SuppressWarnings("unchecked")
@Component("commonService")
public class CommonServiceImpl implements ICommonService {

    private final Logger logger = Logger.getLogger(CommonServiceImpl.class);

    private static final String SERVICE_INSPECTION_SLOT_KEY = "SERVICE_INSPECT_SLOT";

    private static final String SERVICE_PARNTER_MAPPING_CACHE = "servicePartnerMappingCache";

    private static final String INSPECTION_SCHEDULE_CUTOFF_IN_HOURS = "INSPECTION_SCHEDULE_CUTOFF_IN_HOURS";

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    CustomRepository customRepositoryImpl;

    @Autowired
    private SystemConfigMasterCache systemConfigMasterCache;

    @Autowired
    private ServiceRequestValidator serviceRequestValidator;

    @Autowired
    private HubMasterCache hubMasterCache;

    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    @Autowired
    private PartnerEventMasterCache partnerEventMasterCache;

    @Autowired
    private IServiceRequestDocumentService serviceRequestDocumentService;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private LogisticShipmentReassignmentRepository logisticShipmentRepository;

    private static Long SHIPMENT_LABEL_TYPE_ID = 982l;
    private static String SHIPMENT_LABEL_CONTENT_TYPE = "application/json";

    @Autowired
    private PartnerMasterCache partnerMasterCache;

    @Autowired
    @Qualifier("mongoStorageDao")
    private IMongoStorageDao mongoStorageDao;

    @Autowired
    private ServiceRequestStageMasterCache serviceRequestStageMasterCache;

    @Autowired
    private ShipmentDataNotificationManager shipmentDataNotificationManager;

    @Autowired
    private ProductMasterCache productMasterCache;

    @Autowired
    private ServicePartnerMappingCache servicePartnerMappingCache;

    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    @Autowired
    PartnerBUCache partnerBUCache;

    @Override
    public Map<String, Object> getAllData(List<String> dropdowns) throws Exception {
        Map<String, Object> dropDownMap1 = new HashMap<String, Object>();

        if (cacheFactory != null) {
            Map<String, List<SystemConfigDto>> systemConfigMap = null;
            if (cacheFactory.get(Constants.SYSTEM_CONFIG_MASTER_CACHE) != null && cacheFactory.get(Constants.SYSTEM_CONFIG_MASTER_CACHE).getAll().size() > 0) {
                systemConfigMap = cacheFactory.get(Constants.SYSTEM_CONFIG_MASTER_CACHE).getAll();
            }

            for (String dropdown : dropdowns) {
                try {
                    Object masterDataValues = null;
                    MasterData masterData = MasterData.getMasterData(dropdown);
                    switch (masterData) {
                        case PINCODE:
                            masterDataValues = getPincodeMasterData();
                            break;
                        case HUB:
                            masterDataValues = getHubMasterData();
                            break;
                        case SERVICE_MASTER:
                            masterDataValues = getServiceMasterData();
                            break;
                        case PARTNER_MASTER:
                            masterDataValues = getPartnerMasterData();
                            break;
                        case DOC_TYPE:
                            masterDataValues = getDocTypeMasterData();
                            break;

                        case STAGES:
                            masterDataValues = getStagesMasterData(systemConfigMap);
                            break;
                        case STATUS:
                            masterDataValues = getStatusMasterData(systemConfigMap);
                            break;

                        case SERVICE_CENTRE:
                            masterDataValues = getServiceCenterList();
                            break;
                        case SERVICE_PARTNER_BU:
                            masterDataValues = getServiceCenterBUList();
                            break;
                        case DOCUMENT_TYPE:
                            masterDataValues = getDocumentTypeMasterData();
                            break;
                        case WORKFLOW_STAGES:
                            masterDataValues = getWorkflowStages();
                            break;
                        case PRODUCTS:
                            masterDataValues = productMasterCache.getAll();
                            break;
                        case PARTNER_LIST:
                            List<HashMap<String, String>> servicePartners = getServiceCenterList();
                            List<HashMap<String, String>> allPartners = new ArrayList<HashMap<String, String>>();
                            if (!CollectionUtils.isEmpty(servicePartners)) {
                                allPartners.addAll(servicePartners);
                            }
                            HashMap<String, List<HashMap<String, String>>> logisticPartnersMap = getPartnerMasterData();
                            HashMap<String, HashMap<String, String>> partnerMap = new HashMap<String, HashMap<String, String>>();
                            if (logisticPartnersMap != null) {
                                for (Entry<String, List<HashMap<String, String>>> map : logisticPartnersMap.entrySet()) {
                                    if (!CollectionUtils.isEmpty(map.getValue())) {
                                        List<HashMap<String, String>> partners = map.getValue();
                                        for (HashMap<String, String> partner : partners) {
                                            partnerMap.put(partner.get(Constants.PARTNER_NAME), partner);
                                        }
                                    }
                                }
                                allPartners.addAll(partnerMap.values());
                            }
                            masterDataValues = allPartners;
                            break;
                        default:
                            break;
                    }
                    if (masterDataValues != null) {
                        dropDownMap1.put(dropdown, masterDataValues);
                    }
                } catch (Exception e) {
                    logger.error("Exception while getting master data for " + dropdown, e);
                }
            }
        } else {
            throw new Exception("Cache factory is null");
        }

        return dropDownMap1;
    }

    private Map<String, List<ServiceReqeustStageDetailDto>> getWorkflowStages() {
        Map<String, List<ServiceRequestStageMstEntity>> serviceTypeStageMap = serviceRequestStageMasterCache.getAll();
        if (!CollectionUtils.isEmpty(serviceTypeStageMap)) {
            Map<String, List<ServiceReqeustStageDetailDto>> mapData = new HashMap<>();
            for (String serviceRequestType : serviceTypeStageMap.keySet()) {
                List<ServiceRequestStageMstEntity> stageEntityList = serviceTypeStageMap.get(serviceRequestType);
                List<ServiceReqeustStageDetailDto> serviceRequestStageDtoList = populateServiceRequestStageList(stageEntityList);
                mapData.put(serviceRequestType, serviceRequestStageDtoList);
            }
            return mapData;
        }
        return null;
    }

    private List<ServiceReqeustStageDetailDto> populateServiceRequestStageList(List<ServiceRequestStageMstEntity> stageEntityList) {
        List<ServiceReqeustStageDetailDto> stageListData = new ArrayList<ServiceReqeustStageDetailDto>();
        for (ServiceRequestStageMstEntity serviceRequestStageMstEntity : stageEntityList) {
            ServiceReqeustStageDetailDto serviceReqeustStageDetailDto = new ServiceReqeustStageDetailDto();
            serviceReqeustStageDetailDto.setId(serviceRequestStageMstEntity.getId());
            serviceReqeustStageDetailDto.setServiceReqeustType(serviceRequestStageMstEntity.getServiceRequestTypeId());
            serviceReqeustStageDetailDto.setStageCode(serviceRequestStageMstEntity.getStageCode());
            serviceReqeustStageDetailDto.setStageName(serviceRequestStageMstEntity.getStageName());
            serviceReqeustStageDetailDto.setStageOrder(serviceRequestStageMstEntity.getStageOrder());
            serviceReqeustStageDetailDto.setCreatedBy(serviceRequestStageMstEntity.getCreatedBy());
            serviceReqeustStageDetailDto.setCreatedOn(serviceRequestStageMstEntity.getCreatedOn());
            serviceReqeustStageDetailDto.setModifiedBy(serviceRequestStageMstEntity.getModifiedBy());
            serviceReqeustStageDetailDto.setModifiedOn(serviceRequestStageMstEntity.getModifiedOn());
            serviceReqeustStageDetailDto.setStatus(serviceRequestStageMstEntity.getStatus());
            stageListData.add(serviceReqeustStageDetailDto);
        }
        return stageListData;
    }

    private List<HashMap<String, String>> getDocumentTypeMasterData() {
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();
        List<DocTypeMstEntity> docTypeMstEntities = customRepositoryImpl.findAllByDocType();

        for (DocTypeMstEntity docTypeMstEntity : docTypeMstEntities) {
            HashMap<String, String> sysConfigMap = new HashMap<String, String>();
            sysConfigMap.put(docTypeMstEntity.getDocName(), docTypeMstEntity.getDocName());
            sysConfigMap.put(Constants.DOCUMENT_TYPE_ID, docTypeMstEntity.getDocTypeId().toString());
            sysConfigMap.put(docTypeMstEntity.getDocName(), docTypeMstEntity.toString());
            sysConfigMap2.add(sysConfigMap);
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getServiceCenterBUList() {
        Map<String, PartnerBusinessUnit> partnerBUCache = cacheFactory.get(Constants.PARTNER_BU_CACHE).getAll();
        List<HashMap<String, String>> serviceCentreBUList = new ArrayList<>();

        if (partnerBUCache != null && !partnerBUCache.isEmpty()) {
            for (Entry<String, PartnerBusinessUnit> parBUEntry : partnerBUCache.entrySet()) {
                PartnerBusinessUnit partnerBusinessUnitDto = parBUEntry.getValue();
                if (partnerBusinessUnitDto != null && Constants.BUSINESS_UNIT_TYPE_SERVICE_CENTRE_FLAG.equalsIgnoreCase(partnerBusinessUnitDto.getBusinessUnitType())) {
                    HashMap<String, String> serviceCentreBusinessUnitMap = new HashMap<String, String>();
                    serviceCentreBusinessUnitMap.put(Constants.BUSINESS_UNIT_NAME, partnerBusinessUnitDto.getBusinessUnitName());
                    serviceCentreBusinessUnitMap.put(Constants.BUSINESS_UNIT_CODE, parBUEntry.getKey());
                    serviceCentreBUList.add(serviceCentreBusinessUnitMap);
                }
            }
        }

        return serviceCentreBUList;
    }

    private List<HashMap<String, String>> getServiceCenterList() {
        HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).getAll();
        List<HashMap<String, String>> serviceCentreList = new ArrayList<>();

        if (partnerMasterCache != null && partnerMasterCache.size() > 0) {
            for (Entry<String, PartnerMasterDto> parEntry : partnerMasterCache.entrySet()) {
                PartnerMasterDto partnerMasterDto = parEntry.getValue();
                if (partnerMasterDto != null && Constants.PARTNER_TYPE_SERVICE_CENTRE_FLAG.equals(partnerMasterDto.getPartnerType())) {
                    HashMap<String, String> serviceCentreMap = new HashMap<String, String>();
                    serviceCentreMap.put(Constants.PARTNER_NAME, parEntry.getValue().getPartnerName());
                    serviceCentreMap.put(Constants.PARTNER_VALUE, parEntry.getKey());
                    serviceCentreList.add(serviceCentreMap);
                }
            }
        }

        return serviceCentreList;
    }

    private List<HashMap<String, String>> getStatusMasterData(Map<String, List<SystemConfigDto>> systemConfigMap) {
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();

        if (systemConfigMap != null && !systemConfigMap.isEmpty()) {
            List<SystemConfigDto> configMap = systemConfigMap.get(Constants.SHIPMENT_STATUS);

            for (SystemConfigDto systemConfigDto : configMap) {
                HashMap<String, String> sysConfigMap = new HashMap<String, String>();
                sysConfigMap.put(Constants.STATUS_NAME, systemConfigDto.getParamValue());
                sysConfigMap.put(Constants.STATUS_VALUE, systemConfigDto.getParamName());
                sysConfigMap2.add(sysConfigMap);
            }
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getStagesMasterData(Map<String, List<SystemConfigDto>> systemConfigMap) {
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();
        List<String> uniqueStages = new ArrayList<String>();

        if (systemConfigMap != null && !systemConfigMap.isEmpty()) {

            List<SystemConfigDto> configMap = systemConfigMap.get(Constants.SHIPMENT_STAGES);

            for (SystemConfigDto systemConfigDto : configMap) {
                if (!uniqueStages.contains(systemConfigDto.getParamName())) {
                    HashMap<String, String> sysConfigMap = new HashMap<String, String>();
                    sysConfigMap.put(Constants.STAGE_NAME, systemConfigDto.getParamValue());
                    sysConfigMap.put(Constants.STAGE_VALUE, systemConfigDto.getParamName());
                    sysConfigMap2.add(sysConfigMap);
                }

                uniqueStages.add(systemConfigDto.getParamName());
            }
        }

        return sysConfigMap2;
    }

    private HashMap<String, List<HashMap<String, String>>> getPartnerMasterData() {
        HashMap<String, ServicePartnerMappingDto> servicePartnerMappings = (HashMap<String, ServicePartnerMappingDto>) cacheFactory.get(SERVICE_PARNTER_MAPPING_CACHE).getAll();
        HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).getAll();
        HashMap<String, List<HashMap<String, String>>> sysConfigMap2 = new HashMap<String, List<HashMap<String, String>>>();

        if (!CollectionUtils.isEmpty(servicePartnerMappings)) {

            for (Map.Entry<String, ServicePartnerMappingDto> servicePartnerMapping : servicePartnerMappings.entrySet()) {
                ServicePartnerMappingDto mappingDto = servicePartnerMapping.getValue();
                List<HashMap<String, String>> parnterMapping = sysConfigMap2.get(String.valueOf(mappingDto.getServiceRequestTypeId()));
                if (parnterMapping == null) {
                    parnterMapping = new ArrayList<HashMap<String, String>>();
                }

                HashMap<String, String> sysConfigMap = new HashMap<String, String>();
                PartnerMasterDto partnerDto = partnerMasterCache.get(String.valueOf(mappingDto.getServicePartnerCode()));

                if (partnerDto != null) {
                    sysConfigMap.put(Constants.PARTNER_NAME, partnerDto.getPartnerName());
                    sysConfigMap.put(Constants.PARTNER_VALUE, partnerDto.getPartnerCode());
                    parnterMapping.add(sysConfigMap);
                }

                sysConfigMap2.put(String.valueOf(mappingDto.getServiceRequestTypeId()), parnterMapping);
            }
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getDocTypeMasterData() {
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();
        String data = null;
        List<DocTypeMstEntity> docTypeMstEntities = customRepositoryImpl.findAllByDocType();

        for (DocTypeMstEntity docTypeMstEntity : docTypeMstEntities) {
            if (docTypeMstEntity.getDocName().toLowerCase().contains("label")) {
                data = Constants.DOCUMENT_TYPE_LABEL;
            } else {
                data = Constants.DOCUMENT_TYPE_POD;

            }

            HashMap<String, String> sysConfigMap = new HashMap<String, String>();
            sysConfigMap.put("type", data);
            sysConfigMap.put(data, docTypeMstEntity.getDocName());
            sysConfigMap.put(Constants.DOCUMENT_TYPE_ID, docTypeMstEntity.getDocTypeId().toString());
            sysConfigMap2.add(sysConfigMap);
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getServiceMasterData() {
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();
        List<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntity = customRepositoryImpl.findAllByServiceId();

        for (ServiceRequestTypeMstEntity serviceRequestTypeMstEntity2 : serviceRequestTypeMstEntity) {
            serviceRequestTypeMstEntity2.getServiceRequestType();
            HashMap<String, String> sysConfigMap = new HashMap<String, String>();
            sysConfigMap.put(Constants.SERVICE_VALUE, serviceRequestTypeMstEntity2.getServiceRequestTypeId().toString());
            sysConfigMap.put(Constants.SERVICE_NAME, serviceRequestTypeMstEntity2.getServiceRequestTypeName());
            sysConfigMap.put(Constants.SERVICE_TYPE, serviceRequestTypeMstEntity2.getServiceRequestType());
            sysConfigMap2.add(sysConfigMap);
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getHubMasterData() {
        HashMap<String, HubMasterDto> hubMasterCache = (HashMap<String, HubMasterDto>) cacheFactory.get(Constants.HUB_MASTER_CACHE).getAll();
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();

        if (hubMasterCache != null && hubMasterCache.size() > 0) {
            for (Entry<String, HubMasterDto> parEntry : hubMasterCache.entrySet()) {
                HashMap<String, String> sysConfigMap = new HashMap<String, String>();
                sysConfigMap.put(Constants.HUB_VALUE, parEntry.getKey());
                sysConfigMap.put(Constants.HUB_NAME, parEntry.getValue().getOchmHubName());
                sysConfigMap2.add(sysConfigMap);
            }
        }

        return sysConfigMap2;
    }

    private List<HashMap<String, String>> getPincodeMasterData() {
        HashMap<String, PincodeMasterDto> pincodeMasterCache = (HashMap<String, PincodeMasterDto>) cacheFactory.get(Constants.PINCODE_MASTER_CACHE).getAll();
        List<HashMap<String, String>> sysConfigMap2 = new ArrayList<HashMap<String, String>>();

        if (pincodeMasterCache != null && pincodeMasterCache.size() > 0) {
            for (Entry<String, PincodeMasterDto> parEntry : pincodeMasterCache.entrySet()) {
                HashMap<String, String> sysConfigMap = new HashMap<String, String>();
                sysConfigMap.put(Constants.PINCODE_NAME, parEntry.getKey());
                sysConfigMap.put(Constants.PINCODE_VALUE, parEntry.getValue().getPinCode());
                sysConfigMap2.add(sysConfigMap);
            }
        }

        return sysConfigMap2;
    }

    @Override
    public ResponseDto<ServiceScheduleDto> getAvailableScheduleSlotsForInspection(ServiceScheduleDto serviceScheduleDto) throws BusinessServiceException {

        Date currentDate = new Date();
        Calendar targetSlot = Calendar.getInstance();
        List<ServiceSlot> availableSlots = new ArrayList<>();
        Map<String, String> configuredSlotTimes = new TreeMap<>();
        ResponseDto<ServiceScheduleDto> response = new ResponseDto<>();
        response.setData(serviceScheduleDto);
        serviceScheduleDto.setServiceSlots(availableSlots);
        String inspectionScheduleCutOffInHrs = messageSource.getMessage(INSPECTION_SCHEDULE_CUTOFF_IN_HOURS, new Object[] { "" }, null);

        List<ErrorInfoDto> errorInfoList = serviceRequestValidator.doValidateServiceScheduleRequest(serviceScheduleDto);

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
        } else {

            ServiceRequestType requestType = ServiceRequestType.getServiceRequestType(serviceScheduleDto.getServiceRequestType());
            String serviceSlotKey = null;

            if (requestType == null) {
                throw new BusinessServiceException("Invalid Service Request Type: " + serviceScheduleDto.getServiceRequestType());
            }

            switch (requestType) {
                case WHC_INSPECTION:
                    serviceSlotKey = SERVICE_INSPECTION_SLOT_KEY;
                    break;
                default:
                    break;
            }

            // Getting slots configured in OA_SYSTEM_CONFIG
            List<SystemConfigDto> serviceSlotConfig = systemConfigMasterCache.get(serviceSlotKey);

            if (serviceSlotConfig == null || serviceSlotConfig.isEmpty()) {
                throw new BusinessServiceException("Service Schedule Slots not configured for the given Request Type: " + serviceScheduleDto.getServiceRequestType() + " with Param Code: "
                        + serviceSlotKey);
            }

            for (int i = 0; i < serviceSlotConfig.size(); i++) {
                configuredSlotTimes.put(serviceSlotConfig.get(i).getParamName(), serviceSlotConfig.get(i).getParamValue());
            }

            logger.info(">>> Configured Slots >> " + configuredSlotTimes);

            if (serviceScheduleDto.getServiceRequestDate() == null) {
                throw new BusinessServiceException("Error! Service Request Date could'nt be Empty");
            } else {

                if (DateUtils.isSameDay(currentDate, serviceScheduleDto.getServiceRequestDate())) {
                    logger.info("Inspection Request Raised for the Same Day");
                    targetSlot.setTimeInMillis(currentDate.getTime() + Integer.valueOf(inspectionScheduleCutOffInHrs) * 60 * 60 * 1000);
                    calculateTimeSlots(configuredSlotTimes, availableSlots, currentDate, targetSlot);
                } else if (currentDate.after(serviceScheduleDto.getServiceRequestDate())) {

                    logger.info("Inspection Request Raised for the Previous Date");
                    throw new BusinessServiceException("Error! Inspection Request Raised for the Previous Date");
                } else {
                    logger.info("Inspection Request Raised for the Future Date");

                    Date nextBusinessStartDate = com.oneassist.serviceplatform.commons.utils.DateUtils.getNextBusinessDateTime(currentDate);

                    if (serviceScheduleDto.getServiceRequestDate().after(nextBusinessStartDate)) {
                        logger.info("Inspection Request Raised for Days after tomorrow");
                        targetSlot.setTimeInMillis(currentDate.getTime() + Integer.valueOf(inspectionScheduleCutOffInHrs) * 60 * 60 * 1000);
                        calculateTimeSlots(configuredSlotTimes, availableSlots, nextBusinessStartDate, targetSlot);

                    }

                    else {
                        logger.info("Inspection Request Raised for tomorrow");

                        Date businessEndSlotToday = com.oneassist.serviceplatform.commons.utils.DateUtils.getCurrentBusinessEndDateTime(currentDate);

                        if (currentDate.before(businessEndSlotToday)) {
                            targetSlot.setTimeInMillis(nextBusinessStartDate.getTime() + Integer.valueOf(inspectionScheduleCutOffInHrs) * 60 * 60 * 1000
                                    - (businessEndSlotToday.getTime() - currentDate.getTime()));
                        } else {
                            targetSlot.setTimeInMillis(nextBusinessStartDate.getTime() + Integer.valueOf(inspectionScheduleCutOffInHrs) * 60 * 60 * 1000);
                        }

                        logger.info("Target Slot >> " + targetSlot.getTime());
                        calculateTimeSlots(configuredSlotTimes, availableSlots, nextBusinessStartDate, targetSlot);
                    }
                }
            }
        }

        response.setStatus(ResponseConstant.SUCCESS);
        response.setMessage(availableSlots.size() + " slots found for the given date");

        return response;
    }

    @Override
    public HubMasterDto getHubDetails(String hubId) throws Exception {
        HubMasterDto hubMasterDto = null;
        if (hubMasterCache != null) {
            hubMasterDto = hubMasterCache.get(hubId);
        } else {
            throw new Exception("Hub master cache is null");
        }
        return hubMasterDto;
    }

    @Override
    public PincodeMasterDto getPincodeMaster(String pincode) throws Exception {
        PincodeMasterDto pincodeMasterDto = null;
        if (pinCodeMasterCache != null) {
            pincodeMasterDto = pinCodeMasterCache.get(pincode);
        } else {
            throw new Exception("pincode master cache is null");
        }
        return pincodeMasterDto;
    }

    @Override
    public String getStateCode(String stateCode) throws Exception {
        List<SystemConfigDto> stateCodes = systemConfigMasterCache.get("STATECODE");
        if (!CollectionUtils.isEmpty(stateCodes)) {
            for (SystemConfigDto systemConfig : stateCodes) {
                if (stateCode.equalsIgnoreCase(systemConfig.getParamName())) {
                    stateCode = systemConfig.getParamValue();
                    break;
                }
            }
        } else {
            throw new Exception(" no configs found for stateCodes");
        }
        return stateCode;
    }

    @Override
    public PartnerEventDetailDto getPartnerEventMst(String eventCode) throws Exception {
        PartnerEventDetailDto partnerEventDetailDto = null;
        if (partnerEventMasterCache != null) {
            partnerEventDetailDto = partnerEventMasterCache.get(eventCode);
        } else {
            throw new Exception("Partner event master cache is null");
        }
        return partnerEventDetailDto;
    }

    @Override
    public void storeThirdPartyInteractionDetailsInMongo(Long srNo, ClaimLifecycleEvent event, Object requestJsonPayloadString, Object responseJsonPayloadString, String error) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("srNo", srNo);
            jsonObj.put("event", "\"" + event + "\"");
            jsonObj.put("requestPayload", (requestJsonPayloadString != null && !(requestJsonPayloadString instanceof String)) ? new ObjectMapper().writeValueAsString(requestJsonPayloadString)
                    : requestJsonPayloadString);
            jsonObj.put("responsePayload", (responseJsonPayloadString != null && !(responseJsonPayloadString instanceof String)) ? new ObjectMapper().writeValueAsString(responseJsonPayloadString)
                    : responseJsonPayloadString);
            jsonObj.put("errorMessage", (error != null && !(error instanceof String)) ? new ObjectMapper().writeValueAsString(error) : error);
            jsonObj.put("createdDate", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));
            mongoStorageDao.insertCollection(jsonObj.toJSONString(), THIRD_PARTY_INTEGRATION_COLLECTION);
        } catch (Exception e) {
            logger.error("Exception while storing the request details in mongo", e);
        }
    }

    @Override
    public boolean storeMongoRefInDoc(Long serviceRequestId, String trackingNumber, File label) throws Exception {
        boolean status = true;
        try {
            serviceRequestDocumentService.uploadMultipleServiceDocuments(serviceRequestId, label, SHIPMENT_LABEL_TYPE_ID, SHIPMENT_LABEL_CONTENT_TYPE);
        } catch (Exception e) {
            logger.error("Exception while storing doc in mongo", e);
            throw e;
        }

        return status;
    }

    @Override
    public void updateShipmentFailReason(Long shipmentId, String failedReason, Exception exception, String modifiedBy) {
        logger.info("Inside updateShipmentFailReason() - start ");
        try {
            String fullReason = failedReason;
            if (failedReason != null) {
                if (exception != null) {
                    fullReason += "." + exception.getMessage();
                }
                if (!StringUtils.isEmpty(fullReason) && fullReason.length() > 200) {
                    fullReason = fullReason.substring(0, 200);
                }
            }
            ShipmentUpdateRequestDto shipmentUpdateRequestDto = new ShipmentUpdateRequestDto();
            shipmentUpdateRequestDto.setReasonForFailuren(fullReason);
            shipmentUpdateRequestDto.setShipmentId(String.valueOf(shipmentId));
            logisticShipmentRepository.updateFailureReason(shipmentId, fullReason, new Date(), modifiedBy);
        } catch (Exception e) {
            logger.error("Exception while updating the failed reason for " + shipmentId + " failedReason:" + failedReason, e);
        }
        logger.info("Inside updateShipmentFailReason() - end ");
    }

    @Override
    public void updateShipmentAWB(Long shipmentId, String awb, String partnerCode, String modifiedBy) throws Exception {
        logger.info("Inside updateShipmentFailReason() - start ");
        try {

            ShipmentUpdateRequestDto shipmentUpdateRequestDto = new ShipmentUpdateRequestDto();
            shipmentUpdateRequestDto.setLogisticPartnerRefTrackingNumber(awb);
            shipmentUpdateRequestDto.setShipmentId(String.valueOf(shipmentId));
            int updated = logisticShipmentRepository.updateShipmentAWB(shipmentId, partnerCode, awb, new Date(), modifiedBy);
            if (updated != 1) {
                throw new Exception("Failed to update AWB number for shipment :" + shipmentId);
            } else {
                try {
                    shipmentDataNotificationManager.notify(DataNotificationEventType.UPDATED, null, shipmentId);
                    serviceRequestHelper.updateShipmentDescriptionForAWB(shipmentId);
                } catch (Exception e) {
                    logger.error("Exception while publising data to queue", e);
                }
            }
        } catch (Exception e) {
            logger.error("Exception while updating the awb number " + shipmentId + " awb:" + awb, e);
            throw e;
        }
        logger.info("Inside updateShipmentFailReason() - end ");
    }

    @Override
    public void updateShipmentStage(ShipmentReassignEntity shipment, String notificationType, String awbNumber) throws Exception {
        int updated = 0;
        List<SystemConfigDto> partnerStatusList = serviceRequestHelper.getPartnerStatus(shipment.getLogisticPartnerCode(), shipment.getShipmentType());
        List<SystemConfigDto> shipmentStagesConfigList = serviceRequestHelper.getShipmentStage(shipment.getLogisticPartnerCode(), shipment.getShipmentType());
        try {
            if (!CollectionUtils.isEmpty(partnerStatusList) && !CollectionUtils.isEmpty(shipmentStagesConfigList)) {
                for (SystemConfigDto statusConfig : partnerStatusList) {
                    if (statusConfig.getParamName().equalsIgnoreCase(notificationType)) {
                        if (!statusConfig.getParamValue().equalsIgnoreCase(shipment.getCurrentStage())) {
                            if (shipment.getLogisticPartnerRefTrackingNumber() == null && !StringUtils.isEmpty(awbNumber)) {
                                updated = logisticShipmentRepository.updateShipmentCurrentStageAndAWB(shipment.getShipmentId(), statusConfig.getParamValue(), awbNumber, new Date(),
                                        Constants.MODIFIED_BY_CALLBACK_API);
                            } else {
                                updated = logisticShipmentRepository.updateShipmentCurrentStage(shipment.getShipmentId(), statusConfig.getParamValue(), new Date(), Constants.MODIFIED_BY_CALLBACK_API);
                            }
                            if (updated == 1) {
                                try {
                                    shipmentDataNotificationManager.notify(DataNotificationEventType.UPDATED, serviceRequestHelper.convertObject(shipment, ShipmentRequestDto.class));
                                } catch (Exception e) {
                                    logger.error("Exception while publising data to queue", e);
                                }
                            }
                            logger.error("Updated tracking status of " + shipment.getShipmentId() + " to " + statusConfig.getParamValue());
                        } else {
                            logger.error("not updating tracking status of " + shipment.getShipmentId() + " to " + statusConfig.getParamValue());
                        }
                        break;
                    }
                }
            } else {
                throw new Exception("No stages has been configured for " + shipment.getLogisticPartnerCode());
            }
        } finally {
            if (updated == 0 && !StringUtils.isEmpty(awbNumber)) {
                logisticShipmentRepository.updateShipmentAWB(shipment.getShipmentId(), shipment.getLogisticPartnerCode(), awbNumber, new Date(), Constants.MODIFIED_BY_CALLBACK_API);
            }
        }
    }

    public static final String THIRD_PARTY_INTEGRATION_COLLECTION = "ThirdpartyPayloadCollection";

    @Override
    public Long getPartnerCodeByName(ServicePartner servicePartner) {
        Long partnerCode = null;
        if (servicePartner != null) {
            Map<String, PartnerMasterDto> partners = partnerMasterCache.getAll();
            for (Map.Entry<String, PartnerMasterDto> partner : partners.entrySet()) {
                if (servicePartner.toString().equalsIgnoreCase(partner.getValue().getPartnerName())) {
                    partnerCode = Long.parseLong(partner.getValue().getPartnerCode());
                }
            }
        }
        return partnerCode;
    }

    @Override
    public List<ShipmentDetailsDto> getShipmentsForTracking(Long partnerCode) {
        List<ShipmentDetailsDto> shipmentsForTracking = new ArrayList<ShipmentDetailsDto>();
        List<Object[]> shipmentTrackings = logisticShipmentRepository.findShipmentsForTracking(String.valueOf(partnerCode));
        if (!CollectionUtils.isEmpty(shipmentTrackings)) {
            for (Object[] tracking : shipmentTrackings) {
                ShipmentDetailsDto shipment = new ShipmentDetailsDto();
                shipment.setShipmentId((Long) tracking[0]);
                shipment.setShipmentType((Integer) tracking[2]);
                shipment.setLogisticPartnerRefTrackingNumber((String) tracking[1]);
                shipment.setLogisticPartnerCode(String.valueOf(partnerCode));
                shipmentsForTracking.add(shipment);
            }
        }
        return shipmentsForTracking;
    }

    @Override
    public String getOneassistShipmentStage(String statusCode, String partnerCode, Integer shipmentType) {
        String oneassistStage = null;
        List<SystemConfigDto> partnerStatusList = serviceRequestHelper.getPartnerStatus(partnerCode, shipmentType);
        List<SystemConfigDto> shipmentStagesConfigList = serviceRequestHelper.getShipmentStage(partnerCode, shipmentType);
        if (!CollectionUtils.isEmpty(partnerStatusList) && !CollectionUtils.isEmpty(shipmentStagesConfigList)) {
            for (SystemConfigDto statusConfig : partnerStatusList) {
                if (statusConfig.getParamName().equalsIgnoreCase(statusCode)) {
                    oneassistStage = statusConfig.getParamValue();
                    break;
                }
            }
        }
        return oneassistStage;
    }

    @Override
    public void updateShipmentStage(Long shipmentId, String stageName, Date updatedOn, String modifiedBy) throws Exception {
        int updated = logisticShipmentRepository.updateShipmentCurrentStage(shipmentId, stageName, updatedOn, modifiedBy);
        if (updated != 1) {
            throw new Exception("Failed to update shipment stage ::" + shipmentId);
        }
    }

    @Override
    public ShipmentReassignEntity getShipmentEntityByAWBNumber(String awbNumber) {
        ShipmentReassignEntity shipmentEntity = null;
        List<ShipmentReassignEntity> shipmentEntities = logisticShipmentRepository.findByLogisticPartnerRefTrackingNumber(awbNumber);
        if (!CollectionUtils.isEmpty(shipmentEntities)) {
            shipmentEntity = shipmentEntities.get(0);
        }
        return shipmentEntity;
    }

    @Override
    public PincodeMasterDto getStateAndCity(String pincode) throws Exception {
        return pinCodeMasterCache.get(pincode);
    }

    private void calculateTimeSlots(Map<String, String> configuredSlotTimes, List<ServiceSlot> availableSlots, Date targetDate, Calendar targetSlot) throws BusinessServiceException {

        try {

            DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

            String todayDate = com.oneassist.serviceplatform.commons.utils.DateUtils.toShortFormattedString(targetDate);

            for (Map.Entry<String, String> slotEntry : configuredSlotTimes.entrySet()) {
                String timeSlot = slotEntry.getValue();
                String[] slotTimes = timeSlot.split(Constants.DELIM_HIFEN);
                String startDateTime = todayDate + Constants.DELIM_SPACE + slotTimes[0];
                String endDateTime = todayDate + Constants.DELIM_SPACE + slotTimes[1];

                Date startSlot = com.oneassist.serviceplatform.commons.utils.DateUtils.fromString(startDateTime, "dd-MMM-yyyy HH:mm");
                Date endSlot = com.oneassist.serviceplatform.commons.utils.DateUtils.fromString(endDateTime, "dd-MMM-yyyy HH:mm");

                if (startSlot.after(targetSlot.getTime())) {
                    ServiceSlot slot = new ServiceSlot();
                    slot.setStartTime(timeFormatter.format(startSlot));
                    slot.setEndTime(timeFormatter.format(endSlot));
                    availableSlots.add(slot);
                }
            }
        } catch (Exception e) {
            throw new BusinessServiceException("Error calculateTimeSlots ");
        }
    }

    @Override
    public List<Map<String, Object>> filterMasterData(MasterData masterData, String partnerCodes) {
        List<Map<String, Object>> masterDataValues = new ArrayList<Map<String, Object>>();
        List<String> partners = new ArrayList<String>();
        if (!StringUtils.isEmpty(partnerCodes)) {
            partners.addAll(Arrays.asList(partnerCodes.split(",")));
        }
        switch (masterData) {
            case PARTNER_BU:
                masterDataValues = getPartnerBU(partners);
                break;
            case SERVICES:
                masterDataValues = getServices(partners);
                break;
            default:
                break;
        }
        return masterDataValues;
    }

    private List<Map<String, Object>> getServices(List<String> partnerCodes) {

        List<Map<String, Object>> services = new ArrayList<Map<String, Object>>();
        Collection<ServicePartnerMappingDto> servicePartnerMappings = servicePartnerMappingCache.getAll().values();
        if (!CollectionUtils.isEmpty(servicePartnerMappings)) {
            for (ServicePartnerMappingDto mappingDto : servicePartnerMappings) {
                if (partnerCodes.contains(String.valueOf(mappingDto.getServicePartnerCode()))) {
                    Long serviceTypeId = mappingDto.getServiceRequestTypeId();
                    Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();
                    ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
                    for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
                        if (entry.getValue().getServiceRequestTypeId().longValue() == serviceTypeId.longValue()) {
                            serviceRequestTypeMstEntity = entry.getValue();
                            break;
                        }
                    }
                    if (serviceRequestTypeMstEntity != null) {
                        Map<String, Object> service = new HashMap<String, Object>();
                        service.put("serviceId", serviceTypeId);
                        service.put("serviceName", serviceRequestTypeMstEntity.getServiceRequestTypeName());
                        service.put("serviceType", serviceRequestTypeMstEntity.getServiceRequestType());
                        services.add(service);
                    }
                }
            }
        }
        return services;
    }

    private List<Map<String, Object>> getPartnerBU(List<String> partnerCodes) {

        List<Map<String, Object>> bus = new ArrayList<Map<String, Object>>();
        Collection<PartnerBusinessUnit> partnerBUs = partnerBUCache.getAll().values();
        if (!CollectionUtils.isEmpty(partnerBUs)) {
            for (PartnerBusinessUnit partnerBU : partnerBUs) {
                if (CollectionUtils.isEmpty(partnerCodes) || partnerCodes.contains(String.valueOf(partnerBU.getPartnerCode()))) {
                    Map<String, Object> bu = new HashMap<String, Object>();
                    bu.put("buCode", partnerBU.getPartnerBusinessUnitCode());
                    bu.put("buName", partnerBU.getBusinessUnitName());
                    bus.add(bu);
                }
            }
        }
        return bus;
    }

    @Override
    public String getInvoiceValue(Double shipmentDeclareValue) {
        String invoiceValue = String.valueOf(shipmentDeclareValue);
        if (shipmentDeclareValue >= 50000) {
            Random random = new Random();
            invoiceValue = String.valueOf(random.nextInt(9000) + 41000);
        }
        return invoiceValue;
    }

    @Override
    public void clearCache(String cacheName) {
        if (!StringUtils.isEmpty(cacheName)) {
            cacheFactory.clearCache(cacheName);
        }
    }
}
