package com.oneassist.serviceplatform.services.shipment;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.cache.PartnerEventMasterCache;
import com.oneassist.serviceplatform.commons.cache.PartnerMasterCache;
import com.oneassist.serviceplatform.commons.cache.SystemConfigMasterCache;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.CommunicationTemplatesConfig;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.datanotification.ShipmentDataNotificationManager;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentReassignEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentSearchResultEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestCurrentStage;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestType;
import com.oneassist.serviceplatform.commons.enums.ShipmentStatus;
import com.oneassist.serviceplatform.commons.enums.ShipmentType;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.proxies.CommunicationGatewayProxy;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentReassignmentRepository;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentSnapshotRepository;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceFulfilmentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceAddressRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestTypeMstRepository;
import com.oneassist.serviceplatform.commons.utils.DateFormatDeserializer;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ShipmentRequestValidator;
import com.oneassist.serviceplatform.commons.validators.ShipmentStatusUpdateValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.ServiceRequestEventDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ReassignDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentAssetDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignFailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchResultDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchViewResultDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentStatusDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimLifecycleEvent;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ServicePartner;
import com.oneassist.serviceplatform.externalcontracts.SystemConfigDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.logisticpartner.services.ILogisticPartnerService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@SuppressWarnings("unchecked")
public class ShipmentServiceImpl implements IShipmentService {

    private final Logger logger = Logger.getLogger(ShipmentServiceImpl.class);

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private LogisticShipmentRepository logisticShipmentRepository;

    @Autowired
    private LogisticShipmentSnapshotRepository logisticShipmentServiceRequestRepository;

    @Autowired
    private ShipmentRequestValidator shipmentRequestValidator;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestTypeMstRepository mstRepository;

    @Autowired
    private PincodeServiceFulfilmentRepository fulfilmentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SystemConfigMasterCache systemConfigMasterCache;

    @Autowired
    private ShipmentStatusUpdateValidator shipmentStatusUpdateValidator;

    @Autowired
    CustomRepository customRepositoryImpl;

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private ServiceDocumentRepository shipmentDocumentRepository;

    @Autowired
    ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private ShipmentDataNotificationManager shipmentDataNotificationManager;

    @Autowired
    private LogisticShipmentReassignmentRepository logisticShipmentReassignmentRepository;

    @Autowired
    private PartnerMasterCache partnerMasterCache;

    @Autowired
    private PartnerEventMasterCache partnerEventMasterCache;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    private ServiceAddressRepository serviceAddressRepository;

    @Autowired
    private CommunicationGatewayProxy communicationGatewayProxy;

    @Autowired
    @Qualifier("logisticProviders")
    private HashMap<String, ILogisticPartnerService> logisticProviders;

    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ShipmentRequestDto> createShipment(ShipmentRequestDto shipmentRequestDto) throws IOException {
        logger.info(">>> In LogisticServiceImpl: createShipment");
        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateRaiseShipmentRequest(shipmentRequestDto);
        ResponseDto<ShipmentRequestDto> response = new ResponseDto<ShipmentRequestDto>();

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
            response = createFailedShipment(shipmentRequestDto, errorInfoList, response);
        } else {
            String pinCode = "";
            if (!Strings.isNullOrEmpty(shipmentRequestDto.getCustomerPincode())) {
                pinCode = shipmentRequestDto.getCustomerPincode();
            } else {
                pinCode = shipmentRequestDto.getOriginAddressDetails().getPincode();
            }
            setCreatedDate(shipmentRequestDto);
            ServiceRequestEntity ServiceRequestEntity = null;
            ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = mstRepository.findServiceRequestTypeMstByServiceRequestTypeAndStatus(shipmentRequestDto.getServiceRequestDetails()
                    .getServiceRequestType(), Constants.ACTIVE);

            if (serviceRequestTypeMstEntity != null) {
                if (null != shipmentRequestDto.getServiceRequestDetails().getServiceRequestId()) {
                    ServiceRequestEntity = serviceRequestRepository.findOne(shipmentRequestDto.getServiceRequestDetails().getServiceRequestId());
                    ServiceRequestEntity.setModifiedBy(shipmentRequestDto.getServiceRequestDetails().getModifiedBy());
                } else {
                    ServiceRequestEntity = modelMapper.map(shipmentRequestDto.getServiceRequestDetails(), new TypeToken<ServiceRequestEntity>() {
                    }.getType());
                    ServiceRequestEntity.setCreatedOn(new Date());
                    ServiceRequestEntity.setServiceRequestTypeId(serviceRequestTypeMstEntity.getServiceRequestTypeId());
                }
                ServiceRequestEntity.setModifiedOn(new Date());
                ServiceRequestEntity.setStatus(ServiceRequestStatus.CLOSED.getStatus());
                ShipmentEntity shipmentEntity = new ShipmentEntity();
                shipmentEntity = modelMapper.map(shipmentRequestDto, new TypeToken<ShipmentEntity>() {
                }.getType());

                if (ServiceRequestType.COURTESYDELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.DELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.ASCDELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())) {
                    shipmentEntity.setShipmentType(ShipmentType.REVERSE_PICKUP.getShipmentType());
                } else if (ServiceRequestType.COURTESYPICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.PICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.ASCPICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())) {
                    shipmentEntity.setShipmentType(ShipmentType.PICKUP.getShipmentType());
                }

                shipmentEntity.setServiceRequestDetails(ServiceRequestEntity);
                shipmentEntity.setStatus(ShipmentStatus.PENDING.getStatus()); // set shipment status pending
                shipmentEntity.setCurrentStage(ServiceRequestCurrentStage.ASSIGNED.getCurrentstage());
                PincodeServiceFulfilmentEntity fulfilmentEntity = getLogisticPartnerForPincodeByServiceRequestType(pinCode, serviceRequestTypeMstEntity.getServiceRequestTypeId(), Constants.ACTIVE,
                        shipmentRequestDto.getSubCategoryCode());
                shipmentRequestDto.setShipmentId(shipmentEntity.getShipmentId());

                if (fulfilmentEntity != null) {
                    shipmentEntity.setLogisticPartnerCode(fulfilmentEntity.getPartnerCode().toString());
                    String partnerName = null;
                    HashMap<String, PartnerMasterDto> partnerMastersCache = (HashMap<String, PartnerMasterDto>) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).getAll();

                    if (fulfilmentEntity.getPartnerCode() != null) {
                        if (partnerMastersCache.containsKey(String.valueOf(fulfilmentEntity.getPartnerCode()))) {
                            PartnerMasterDto partnerMasterDto = partnerMastersCache.get(String.valueOf(fulfilmentEntity.getPartnerCode()));
                            partnerName = partnerMasterDto.getPartnerName();
                        }
                    }

                    sendCommunication(shipmentRequestDto, partnerName, null);
                } else {

                    shipmentEntity.setStatus(ShipmentStatus.UNASSIGNED.getStatus()); // set shipment status pending
                    shipmentEntity.setCurrentStage(ServiceRequestCurrentStage.UNASSIGNED.getCurrentstage());
                }

                if (null != shipmentRequestDto.getServiceRequestDetails().getServiceRequestId()) {
                    ServiceAddressEntity originAddress = serviceAddressRepository.save(shipmentEntity.getOriginAddressDetails());
                    ServiceAddressEntity destinationAddress = serviceAddressRepository.save(shipmentEntity.getDestinationAddressDetails());
                    ShipmentReassignEntity reassignEntity = getObject(shipmentEntity, ShipmentReassignEntity.class);
                    reassignEntity.setOrginAddressId(originAddress.getServiceAddressId());
                    reassignEntity.setDestinationAddressId(destinationAddress.getServiceAddressId());
                    reassignEntity.setServiceRequestId(shipmentRequestDto.getServiceRequestDetails().getServiceRequestId());
                    reassignEntity.setShipmentAssetDetails(shipmentEntity.getShipmentAssetDetails());
                    reassignEntity = logisticShipmentReassignmentRepository.save(reassignEntity);
                    shipmentEntity.setOriginAddressDetails(originAddress);
                    shipmentEntity.setDestinationAddressDetails(destinationAddress);
                    shipmentEntity.setShipmentAssetDetails(reassignEntity.getShipmentAssetDetails());
                    shipmentEntity.setShipmentId(reassignEntity.getShipmentId());
                } else {
                    shipmentEntity = logisticShipmentRepository.save(shipmentEntity);
                }

                try {
                    publishShipmentMessage(shipmentEntity);
                } catch (Exception e) {
                    logger.error("Exception while publishing the message to queue::", e);
                }

                shipmentRequestDto = modelMapper.map(shipmentEntity, new TypeToken<ShipmentRequestDto>() {
                }.getType());

                response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()), new Object[] { shipmentEntity.getShipmentId() }, null));
                response.setData(shipmentRequestDto);
                response.setStatus(ResponseConstant.SUCCESS);
                response.setStatusCode(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()));
            } else {
                response.setStatus(ResponseConstant.FAILED);
                response.setStatusCode(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_FAILED.getErrorCode()));
                response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_FAILED.getErrorCode()), new Object[] { "" }, null));
                logger.error("Create shipment failed. No service type master found for request " + shipmentRequestDto);
            }
        }

        return response;
    }

    private void sendCommunication(ShipmentRequestDto shipmentRequestDto, String partnerName, String templateId) {
        communicationGatewayProxy.sendCommunication(shipmentRequestDto, partnerName, templateId);
    }

    private void setCreatedDate(ShipmentRequestDto shipmentRequestDto) {
        shipmentRequestDto.setCreatedOn(new Date());
        shipmentRequestDto.setModifiedOn(new Date());

        if (null != shipmentRequestDto.getServiceRequestDetails()) {
            shipmentRequestDto.getServiceRequestDetails().setCreatedOn(new Date());
        }

        if (null != shipmentRequestDto.getShipmentAssetDetails() && shipmentRequestDto.getShipmentAssetDetails().size() > 0) {
            for (ShipmentAssetDetailsDto shipmentAssetDetails : shipmentRequestDto.getShipmentAssetDetails()) {
                shipmentAssetDetails.setCreatedOn(new Date());
            }
        }

        if (null != shipmentRequestDto.getOriginAddressDetails()) {
            shipmentRequestDto.getOriginAddressDetails().setCreatedOn(new Date());
        }

        if (null != shipmentRequestDto.getDestinationAddressDetails()) {
            shipmentRequestDto.getDestinationAddressDetails().setCreatedOn(new Date());
        }
    }

    private ResponseDto<ShipmentRequestDto> createFailedShipment(ShipmentRequestDto shipmentRequestDto, List<ErrorInfoDto> errorInfoList, ResponseDto<ShipmentRequestDto> response) {
        logger.error("inside createFailedShipment ::shipmentRequestDto:" + shipmentRequestDto + "::errorInfoList:" + errorInfoList);

        try {
            if (shipmentRequestDto != null && shipmentRequestDto.getServiceRequestDetails() != null && !StringUtils.isEmpty(shipmentRequestDto.getServiceRequestDetails().getRefPrimaryTrackingNo())) {
                ServiceRequestEntity serviceRequestEntity = null;

                if (shipmentRequestDto.getServiceRequestDetails().getServiceRequestId() != null) {
                    serviceRequestEntity = serviceRequestRepository.findOne(shipmentRequestDto.getServiceRequestDetails().getServiceRequestId());
                    serviceRequestEntity.setModifiedOn(new Date());
                } else {
                    serviceRequestEntity = modelMapper.map(shipmentRequestDto.getServiceRequestDetails(), new TypeToken<ServiceRequestEntity>() {
                    }.getType());
                    ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = mstRepository.findServiceRequestTypeMstByServiceRequestTypeAndStatus(shipmentRequestDto.getServiceRequestDetails()
                            .getServiceRequestType(), Constants.ACTIVE);
                    serviceRequestEntity.setServiceRequestTypeId(serviceRequestTypeMstEntity.getServiceRequestTypeId());
                    serviceRequestEntity.setCreatedOn(new Date());
                    serviceRequestEntity.setModifiedOn(new Date());
                }

                serviceRequestEntity.setStatus(ServiceRequestStatus.FAILED.getStatus());
                ShipmentEntity shipmentEntity = modelMapper.map(shipmentRequestDto, new TypeToken<ShipmentEntity>() {
                }.getType());

                if (ServiceRequestType.COURTESYDELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.DELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.ASCDELIVERY.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())) {
                    shipmentEntity.setShipmentType(ShipmentType.REVERSE_PICKUP.getShipmentType());
                } else if (ServiceRequestType.COURTESYPICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.PICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())
                        || ServiceRequestType.ASCPICKUP.getRequestType().equalsIgnoreCase(shipmentRequestDto.getServiceRequestDetails().getServiceRequestType())) {
                    shipmentEntity.setShipmentType(ShipmentType.PICKUP.getShipmentType());
                }

                shipmentEntity.setServiceRequestDetails(serviceRequestEntity);
                shipmentEntity.setStatus(ShipmentStatus.FAILED.getStatus());
                shipmentEntity.setCurrentStage(ServiceRequestCurrentStage.UNASSIGNED.getCurrentstage());
                StringBuilder failureReason = new StringBuilder();
                failureReason.append("Reason for shipment not created:");
                int errorCount = 1;
                for (ErrorInfoDto errorDto : errorInfoList) {
                    failureReason.append((errorCount++) + ". " + errorDto.getErrorField() + " is " + errorDto.getErrorMessage());
                }

                if (shipmentEntity.getOriginAddressDetails() != null) {
                    shipmentEntity.getOriginAddressDetails().setCreatedOn(new Date());
                } else {
                    shipmentEntity.setOriginAddressDetails(new ServiceAddressEntity());
                }

                if (shipmentEntity.getDestinationAddressDetails() != null) {
                    shipmentEntity.getDestinationAddressDetails().setCreatedOn(new Date());
                } else {
                    shipmentEntity.setDestinationAddressDetails(new ServiceAddressEntity());
                }

                if (!CollectionUtils.isEmpty(shipmentEntity.getShipmentAssetDetails())) {
                    for (ShipmentAssetEntity asset : shipmentEntity.getShipmentAssetDetails()) {
                        asset.setCreatedOn(new Date());
                    }
                } else {
                    List<ShipmentAssetEntity> assets = new ArrayList<ShipmentAssetEntity>();
                    assets.add(new ShipmentAssetEntity());
                    shipmentEntity.setShipmentAssetDetails(assets);
                }

                shipmentEntity.setCreatedOn(new Date());
                shipmentEntity.setModifiedOn(new Date());
                String fullReason = failureReason.toString();
                if (!StringUtils.isEmpty(fullReason) && fullReason.length() > 200) {
                    fullReason = fullReason.substring(0, 200);
                }

                shipmentEntity.setReasonForFailure(fullReason);

                logisticShipmentRepository.save(shipmentEntity);

                shipmentRequestDto.getServiceRequestDetails().setServiceRequestId(shipmentEntity.getServiceRequestDetails().getServiceRequestId());
                response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()), new Object[] { shipmentEntity.getShipmentId() }, null));
                response.setData(shipmentRequestDto);
                response.setStatus(ResponseConstant.SUCCESS);
                response.setStatusCode(String.valueOf(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()));
                shipmentDataNotificationManager.notify(DataNotificationEventType.NEW, shipmentRequestDto);
            } else {
                logger.error("Service request details/ primary tracking number is empty, hence not creating failed request");
            }
        } catch (Exception e) {
            logger.error("Exception while creating failed shipment for " + shipmentRequestDto, e);
        }
        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ReassignDto> reassignShipment(Long shipmentId, ShipmentReassignRequestDto shipmentReassignRequestDto) {
        logger.info(">>> In LogisticServiceImpl: reassignShipment");

        List<ShipmentReassignFailDto> errList = new ArrayList<ShipmentReassignFailDto>();
        ShipmentReassignFailDto failedObj = new ShipmentReassignFailDto();
        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateRasieShipmentRequest(shipmentReassignRequestDto);
        ReassignDto reassignFailDto = new ReassignDto();
        ResponseDto<ReassignDto> response = new ResponseDto<>();

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
        } else {
            try {
                ShipmentEntity entity = logisticShipmentRepository.findShipmentEntityByShipmentId(shipmentId);

                if (entity != null) {
                    ShipmentReassignEntity reassignEntity = getObject(entity, ShipmentReassignEntity.class);

                    if (null != entity && entity.getStatus() != null && !entity.getStatus().equalsIgnoreCase(ShipmentStatus.UNASSIGNED.getStatus())
                            && !entity.getStatus().equalsIgnoreCase(ShipmentStatus.FAILED.getStatus())) {
                        reassignEntity.setReasonForFailure(null);
                        reassignEntity.setStatus(ShipmentStatus.PENDING.getStatus());
                        reassignEntity.setCurrentStage(ServiceRequestCurrentStage.ASSIGNED.getCurrentstage());
                        reassignEntity.setShipmentId(null);
                        reassignEntity.setCreatedBy(shipmentReassignRequestDto.getModifiedBy());
                        reassignEntity.setLogisticPartnerCode(shipmentReassignRequestDto.getAssignlogisticPartnerCode());
                        reassignEntity.setCreatedOn(new Date());
                        reassignEntity.setServiceRequestId(entity.getServiceRequestDetails().getServiceRequestId());
                        reassignEntity.setOrginAddressId(entity.getOriginAddressDetails().getServiceAddressId());
                        reassignEntity.setDestinationAddressId(entity.getDestinationAddressDetails().getServiceAddressId());

                        if (!CollectionUtils.isEmpty(reassignEntity.getShipmentAssetDetails())) {
                            for (ShipmentAssetEntity asset : reassignEntity.getShipmentAssetDetails()) {
                                asset.setShipmentAssetId(null);
                                asset.setCreatedOn(new Date());
                                asset.setCreatedBy(shipmentReassignRequestDto.getModifiedBy());
                            }
                        }

                        reassignEntity.setLogisticPartnerRefTrackingNumber(null);
                        logisticShipmentReassignmentRepository.save(reassignEntity);
                        logger.info("-- shipment reassign successfully -- ");

                        try {
                            publishShipmentMessage(logisticShipmentRepository.findShipmentEntityByShipmentId(reassignEntity.getShipmentId()));
                        } catch (Exception exception) {
                            logger.error("Exception while publishing reassignment message for shipment id " + shipmentId + " to queue::", exception);
                            failedObj.setFailedReason("Exception occured in publish shipment msgs");
                        }

                        String templateId = CommunicationTemplatesConfig.SHIPMENT_REASSIGNMENT_SMS_EMAIL_TEMPLATE;
                        String partnerName = getPartnerName(shipmentReassignRequestDto.getAssignlogisticPartnerCode());
                        Timestamp currentTimestamp = new java.sql.Timestamp(new Date().getTime());
                        String reassignedStatus = ShipmentStatus.REASSIGNED.getStatus();
                        logisticShipmentRepository.updateShipmentStatus(shipmentId, reassignedStatus, currentTimestamp, shipmentReassignRequestDto.getModifiedBy());
                        logger.info("-- cancel shipment REASSIGNED  successfully --");

                        sendCommunication((ShipmentRequestDto) modelMapper.map(entity, new TypeToken<ShipmentRequestDto>() {
                        }.getType()), partnerName, templateId);
                        response.setStatus(ResponseConstant.SUCCESS);
                        response.setStatusCode(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_SUCCESS.getErrorCode()));
                        response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_SUCCESS.getErrorCode()), new Object[] { "" }, null));

                    } else if (null != entity && entity.getStatus() != null && entity.getStatus().equalsIgnoreCase(ShipmentStatus.UNASSIGNED.getStatus())) {
                        entity.setReasonForFailure(null);
                        Calendar calendar = Calendar.getInstance();
                        java.util.Date now = calendar.getTime();
                        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
                        String reassignedStatus = ShipmentStatus.PENDING.getStatus();
                        String reassignedStage = ServiceRequestCurrentStage.ASSIGNED.getCurrentstage();
                        logisticShipmentRepository.updateShipmentStatusAndStage(shipmentId, shipmentReassignRequestDto.getAssignlogisticPartnerCode(), reassignedStatus, currentTimestamp,
                                shipmentReassignRequestDto.getModifiedBy(), reassignedStage);
                        logger.info("-- cancel shipment ASSIGNED  successfully --");
                        response.setStatus(ResponseConstant.SUCCESS);
                        response.setStatusCode(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_SUCCESS.getErrorCode()));
                        response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_SUCCESS.getErrorCode()), new Object[] { "" }, null));

                        try {
                            entity.setLogisticPartnerCode(shipmentReassignRequestDto.getAssignlogisticPartnerCode());
                            publishShipmentMessage(entity);
                        } catch (Exception exception) {
                            logger.error("Exception while publishing reassignment message for shipment id " + shipmentId + " to queue::", exception);
                            failedObj.setFailedReason("Exception occured in publish shipment msgs");
                        }

                        String partnerName = getPartnerName(shipmentReassignRequestDto.getAssignlogisticPartnerCode());
                        sendCommunication((ShipmentRequestDto) modelMapper.map(entity, new TypeToken<ShipmentRequestDto>() {
                        }.getType()), partnerName, null);
                    } else {
                        failedObj.getFailedReason();

                        if (failedObj.getFailedReason() == null) {
                            failedObj.setFailedReason("failed to fetch the record");
                        }

                        failedObj.setShipmentid(shipmentId);
                        errList.add(failedObj);
                        reassignFailDto.setFailedList(errList);
                        reassignFailDto.setRecordFailed(1);

                        response.setStatus(ResponseConstant.FAILED);
                        response.setStatusCode(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_FAILED.getErrorCode()));
                        response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_FAILED.getErrorCode()), new Object[] { "" }, null));
                        logger.info("-- shipment can not be canceled because it is not in pending state --");
                    }
                } else {
                    failedObj.setFailedReason("Shipment id doesn't exist");
                    throw new Exception("Shipment id doesn't exist");
                }
            } catch (Exception e) {
                if (failedObj.getFailedReason() == null) {
                    failedObj.setFailedReason("Exception occurred while updating records");
                }
                failedObj.setShipmentid(shipmentId);
                errList.add(failedObj);
                reassignFailDto.setFailedList(errList);
                reassignFailDto.setRecordFailed(1);

                logger.error("Exception occurred while updating records :", e);
            }
        }

        logger.info("<<< In LogisticServiceImpl: createShipment");

        response.setData(reassignFailDto);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseDto<ShipmentUpdateRequestDto> updateShipment(Long shipmentId, String fieldtoupdate, ShipmentUpdateRequestDto shipmentUpdateRequestDto) throws Exception {
        logger.info(">>> In LogisticServiceImpl: Update Shipment");
        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateUpdateShipmentRequest(shipmentId, fieldtoupdate, shipmentUpdateRequestDto);
        ResponseDto<ShipmentUpdateRequestDto> response = new ResponseDto<ShipmentUpdateRequestDto>();

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
        } else {
            Integer isUpdated = 0;
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

            ShipmentEntity entity = logisticShipmentRepository.findOne(shipmentId);
            if (entity != null) {
                if (fieldtoupdate.equals("status") && null != shipmentUpdateRequestDto.getStatus()) {
                    entity.setStatus(shipmentUpdateRequestDto.getStatus());
                    isUpdated = logisticShipmentReassignmentRepository.updateShipmentStatus(shipmentId, shipmentUpdateRequestDto.getStatus(), currentTimestamp,
                            shipmentUpdateRequestDto.getModifiedBy());
                    if (isUpdated > 0) {
                        entity.setCurrentStage(ServiceRequestCurrentStage.DELIVERED.getCurrentstage());
                        logisticShipmentReassignmentRepository.updateShipmentCurrentStage(shipmentId, ServiceRequestCurrentStage.DELIVERED.getCurrentstage(), currentTimestamp,
                                shipmentUpdateRequestDto.getModifiedBy());
                        if (shipmentUpdateRequestDto.getStatus().equalsIgnoreCase(ShipmentStatus.CANCELLED.getStatus())) {
                            cancelLogisticPartnerOrder(entity);
                        }
                    }

                } else if (fieldtoupdate.equals("currentstage") && null != shipmentUpdateRequestDto.getCurrentStage()) {
                    entity.setCurrentStage(shipmentUpdateRequestDto.getCurrentStage());
                    isUpdated = logisticShipmentReassignmentRepository.updateShipmentCurrentStage(shipmentId, shipmentUpdateRequestDto.getCurrentStage(), currentTimestamp,
                            shipmentUpdateRequestDto.getModifiedBy());
                }
            }

            if (isUpdated == 1) {
                ShipmentRequestDto shipmentRequestDto = serviceRequestHelper.convertObject(entity, ShipmentRequestDto.class);
                Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();
                ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
                String serviceRequestType = null;
                for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
                    serviceRequestTypeMstEntity = entry.getValue();
                    if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == entity.getServiceRequestDetails().getServiceRequestTypeId().longValue()) {
                        serviceRequestType = entry.getKey();
                    }
                }
                shipmentRequestDto.getServiceRequestDetails().setServiceRequestType(serviceRequestType);
                shipmentRequestDto.getServiceRequestDetails().setServiceRequestTypeName(serviceRequestType);
                shipmentDataNotificationManager.notify(DataNotificationEventType.UPDATED, shipmentRequestDto, shipmentId);
                response.setStatus(ResponseConstant.SUCCESS);
                response.setStatusCode(String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()));
                response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_SUCCESS.getErrorCode()), new Object[] { "" }, null));
            } else {
                response.setStatus(ResponseConstant.FAILED);
                response.setStatusCode(String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_FAILED.getErrorCode()));
                response.setMessage(messageSource.getMessage(String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_FAILED.getErrorCode()), new Object[] { "" }, null));
            }
        }

        logger.info("<<< In LogisticServiceImpl: Update Shipment");

        return response;
    }

    @Override
    @Transactional
    public ResponseDto<List<ShipmentStatusDto>> addBulkShipmentStatus(List<ShipmentStatusDto> shipmentStatusDtos) {

        logger.info("<<<<<<<<<<<In LogisticServiceImpl :addBulkShipmentStatus():starts ");
        ResponseDto<List<ShipmentStatusDto>> addBulkShipmentStatusResponse = new ResponseDto<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy h:m a");

        if (shipmentStatusDtos != null && !shipmentStatusDtos.isEmpty()) {
            for (ShipmentStatusDto shipmentStatusDto : shipmentStatusDtos) {

                try {

                    String errorInfo = shipmentStatusUpdateValidator.validateShipmentStatusUpdateDto(shipmentStatusDto);

                    if (!Strings.isNullOrEmpty(errorInfo)) {
                        throw new BusinessServiceException(errorInfo);
                    } else {

                        ShipmentEntity shipmentEntity = logisticShipmentRepository.findOne(shipmentStatusDto.getShipmentId());

                        if (shipmentEntity == null) {
                            throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_DOESNOT_EXIST.getErrorCode());
                        } else {
                            String oldLogisticPartnerRefTrackingNumber = shipmentEntity.getLogisticPartnerRefTrackingNumber();
                            if (shipmentEntity.getLogisticPartnerCode() == null) {
                                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_STATUS_UPDATE_FAIL_DOESNOT_HAVE_LOGISTIC_PARTNERCODE.getErrorCode());
                            }

                            if (!shipmentStatusDto.getPartnerCode().equals(Long.valueOf(shipmentEntity.getLogisticPartnerCode()))) {
                                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_DOESNOT_MATCH_LOGISTIC_PARTNERCODE.getErrorCode());
                            }

                            if (shipmentEntity.getStatus() != null
                                    && !(shipmentEntity.getStatus().equals(ShipmentStatus.PENDING.getStatus()) || shipmentEntity.getStatus().equals(ShipmentStatus.UNASSIGNED.getStatus()))) {
                                throw new BusinessServiceException(LogisticResponseCodes.SHIPMENT_STATUS_UPDATE_FAIL_INVALIDSTATUS.getErrorCode());
                            }

                            errorInfo = shipmentStatusUpdateValidator.validateShipmentModificationDate(shipmentEntity.getModifiedOn(), shipmentStatusDto);

                            if (!Strings.isNullOrEmpty(errorInfo)) {
                                throw new BusinessServiceException(errorInfo);

                            } else {

                                if (!Strings.isNullOrEmpty(shipmentStatusDto.getLogisticPartnerRefTrackingNumber())) {
                                    shipmentEntity.setLogisticPartnerRefTrackingNumber(shipmentStatusDto.getLogisticPartnerRefTrackingNumber());
                                }

                                shipmentEntity.setCurrentStage(shipmentStatusDto.getCurrentStage());
                                shipmentEntity.setModifiedBy(Constants.USER_ADMIN);
                                Date shipmentModificationDt = dateFormat.parse(shipmentStatusDto.getStatusUpdateDate() + " " + shipmentStatusDto.getStatusUpdateTime());
                                shipmentEntity.setModifiedOn(shipmentModificationDt);
                                logisticShipmentRepository.save(shipmentEntity);
                                shipmentStatusDto.setSuccess(true);
                                shipmentStatusDto.setErrorMessage(Constants.SUCCESS);

                                if (Strings.isNullOrEmpty(oldLogisticPartnerRefTrackingNumber) && !Strings.isNullOrEmpty(shipmentStatusDto.getLogisticPartnerRefTrackingNumber())) {
                                    ShipmentRequestDto shipmentDto = modelMapper.map(shipmentEntity, new TypeToken<ShipmentRequestDto>() {
                                    }.getType());
                                    PartnerMasterDto partnerMasterDto = (PartnerMasterDto) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).get(String.valueOf(shipmentStatusDto.getPartnerCode()));
                                    ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = mstRepository.findOne(shipmentEntity.getServiceRequestDetails().getServiceRequestTypeId());
                                    shipmentDto.getServiceRequestDetails().setServiceRequestType(serviceRequestTypeMstEntity.getServiceRequestType());
                                    sendCommunication(shipmentDto, partnerMasterDto.getPartnerName(), null);
                                }
                            }
                        }
                    }
                } catch (DataAccessException e) {
                    shipmentStatusDto.setSuccess(false);
                    shipmentStatusDto.setErrorMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_FAILED.getErrorCode())), new Object[] { "" }, null));
                    logger.error("DataAccessException occurred while updating Shipment status", e);
                } catch (Exception e) {
                    shipmentStatusDto.setSuccess(false);
                    shipmentStatusDto.setErrorMessage(e.getMessage());
                    logger.error("Exception occurred while updating Shipment status", e);
                }
            }
        }

        addBulkShipmentStatusResponse.setData(shipmentStatusDtos);
        addBulkShipmentStatusResponse.setStatus(ResponseConstant.SUCCESS);
        addBulkShipmentStatusResponse.setMessage(messageSource.getMessage((String.valueOf(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_SUCCESS.getErrorCode())), new Object[] { "" }, null));
        logger.info(">>>>>>>>>In LogisticServiceImpl :addBulkShipmentStatus():ends ");

        return addBulkShipmentStatusResponse;
    }

    @Override
    public ResponseDto<List<ShipmentSearchViewResultDto>> searchShipmentInfoByNativeQuery(ShipmentSearchRequestDto shipmentSearchRequestDto) {
        logger.info("<<<In LogisticServiceImpl: searchShipmentInfo start ");
        ResponseDto<List<ShipmentSearchResultDto>> response = new ResponseDto<List<ShipmentSearchResultDto>>();
        List<ErrorInfoDto> errorInfoList = shipmentRequestValidator.doValidateShipmentSearchRequest(shipmentSearchRequestDto);
        List<ShipmentSearchViewResultDto> copyOfShipmentSearchResultDto = new ArrayList<ShipmentSearchViewResultDto>();
        ResponseDto<List<ShipmentSearchViewResultDto>> copyResponse = new ResponseDto<List<ShipmentSearchViewResultDto>>();

        if (errorInfoList == null) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
        } else {
            List<ShipmentSearchResultEntity> shipmentSearchInfo = customRepositoryImpl.findAllByNativeQuery(shipmentSearchRequestDto);
            if (shipmentSearchInfo != null && shipmentSearchInfo.size() > 0) {
                for (ShipmentSearchResultEntity shipmentEntity : shipmentSearchInfo) {
                    ShipmentSearchViewResultDto shipmentSearchResultDto = modelMapper.map(shipmentEntity, new TypeToken<ShipmentSearchViewResultDto>() {
                    }.getType());
                    ShipmentSearchViewResultDto updatedShipmentSearchResultDto = populateNativeQueryDetails(shipmentSearchResultDto);
                    copyOfShipmentSearchResultDto.add(updatedShipmentSearchResultDto);
                }
            }
            copyResponse.setData(copyOfShipmentSearchResultDto);
            copyResponse.setStatus(ResponseConstant.SUCCESS);
            copyResponse.setMessage(ResponseConstant.SUCCESS);
        }

        logger.info("<<< In LogisticServiceImpl: searchShipmentInfo ends");
        return copyResponse;
    }

    private ShipmentSearchViewResultDto populateNativeQueryDetails(ShipmentSearchViewResultDto shipmentSearchResultDto) {
        logger.info("In LogisticServiceImpl: populateDetails starts>>>");
        HashMap<String, PartnerMasterDto> partnerMasterCache = (HashMap<String, PartnerMasterDto>) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).getAll();
        List<SystemConfigDto> shipmentStagesConfigList = serviceRequestHelper.getShipmentStage(shipmentSearchResultDto.getLogisticPartnerCode(), shipmentSearchResultDto.getShipmentType());
        List<SystemConfigDto> shipmentStatusConfigList = serviceRequestHelper.getShipmentStatus(shipmentSearchResultDto.getLogisticPartnerCode());

        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();

        for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
            ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = entry.getValue();
            if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == shipmentSearchResultDto.getServiceRequestTypeId().longValue()) {
                shipmentSearchResultDto.setServiceName(serviceRequestTypeMstEntity.getServiceRequestTypeName());
                break;
            }
        }
        if (shipmentSearchResultDto.getLogisticPartnerCode() != null) {
            if (partnerMasterCache.containsKey(shipmentSearchResultDto.getLogisticPartnerCode())) {
                PartnerMasterDto partnerMasterDto = partnerMasterCache.get(shipmentSearchResultDto.getLogisticPartnerCode());
                shipmentSearchResultDto.setPartnerName(partnerMasterDto.getPartnerName());
            }
        }
        if (shipmentSearchResultDto.getShipmentStatus() != null) {
            for (SystemConfigDto systemConfigDto : shipmentStatusConfigList) {
                if (systemConfigDto.getParamName().equalsIgnoreCase(shipmentSearchResultDto.getShipmentStatus().toString())) {
                    shipmentSearchResultDto.setShipmentStatus(systemConfigDto.getParamValue());
                    shipmentSearchResultDto.setShipmentStatusCode(systemConfigDto.getParamName());
                    break;
                }
            }
        }
        Map<String, String> stages = new HashMap<String, String>();
        if (shipmentSearchResultDto.getCurrentStage() != null) {
            for (SystemConfigDto systemConfigDto : shipmentStagesConfigList) {

                if (systemConfigDto.getParamName().equalsIgnoreCase(shipmentSearchResultDto.getCurrentStage().toString())) {
                    shipmentSearchResultDto.setShipmentStage(systemConfigDto.getParamValue());
                    shipmentSearchResultDto.setCurrentStage(systemConfigDto.getParamValue());
                    shipmentSearchResultDto.setShipmentStageCode(systemConfigDto.getParamName());
                } else {
                    stages.put(systemConfigDto.getParamName(), systemConfigDto.getParamValue());
                }
            }
        } else {
            for (SystemConfigDto systemConfigDto : shipmentStagesConfigList) {
                stages.put(systemConfigDto.getParamName(), systemConfigDto.getParamValue());
            }
        }
        shipmentSearchResultDto.setStages(stages);
        logger.info("In LogisticServiceImpl: populateDetails ends>>>");
        return shipmentSearchResultDto;
    }

    public String publishShipmentMessage(ShipmentEntity entity) throws Exception {
        logger.error("<<< In LogisticServiceImpl: publishShipmentMessage:starts");
        ServiceRequestEventDto shipmentEventDto = new ServiceRequestEventDto();
        String partnerCode = null;
        String status = null;
        if (entity != null) {
            shipmentEventDto.setShipmentId(entity.getShipmentId());
            shipmentEventDto.setPartnerCode(Long.valueOf(entity.getLogisticPartnerCode()));

            if (entity.getServiceRequestDetails() != null) {
                shipmentEventDto.setClaimSrNo(entity.getServiceRequestDetails().getRefPrimaryTrackingNo() != null ? Long.valueOf(entity.getServiceRequestDetails().getRefPrimaryTrackingNo()) : null);
            }
            partnerCode = entity.getLogisticPartnerCode();
            logger.info("--------- entity.getShipmentId( ------------------" + entity.getShipmentId());
        }
        logger.info("--------- partnerCode ------------------" + partnerCode);

        if (partnerCode != null) {
            PartnerMasterDto partnerMasterDto = partnerMasterCache.get(partnerCode);
            if (partnerMasterDto != null) {
                if (partnerMasterDto.getPartnerName().equalsIgnoreCase(ServicePartner.FEDEX.toString())) {
                    logger.info("--------- fedex ------------------");
                    shipmentEventDto.setEventName(ClaimLifecycleEvent.FEDEX_PROCESS_SHIPMENT);
                    shipmentEventDto.setPartnerName(ServicePartner.FEDEX);
                    shipmentEventDto.setShipmentDetails(getObject(entity, ShipmentSearchResultDto.class));
                    serviceRequestHelper.publishMessageInSPSRProcessingQueue(shipmentEventDto);
                } else if (partnerMasterDto.getPartnerName().equalsIgnoreCase(ServicePartner.ECOM.toString())) {
                    shipmentEventDto.setPartnerName(ServicePartner.ECOM);
                    shipmentEventDto.setEventName(ClaimLifecycleEvent.ECOM_PINCODE);
                    shipmentEventDto.setShipmentDetails(getObject(entity, ShipmentSearchResultDto.class));
                    serviceRequestHelper.publishMessageInSPSRProcessingQueue(shipmentEventDto);
                } else if (partnerMasterDto.getPartnerName().equalsIgnoreCase(ServicePartner.LOGINEXT.toString())) {
                    shipmentEventDto.setPartnerName(ServicePartner.LOGINEXT);
                    shipmentEventDto.setEventName(ClaimLifecycleEvent.LOGINEXT_SHIPMENT);
                    shipmentEventDto.setShipmentDetails(getObject(entity, ShipmentSearchResultDto.class));
                    serviceRequestHelper.publishMessageInSPSRProcessingQueue(shipmentEventDto);
                } else if (partnerMasterDto.getPartnerName().equalsIgnoreCase(ServicePartner.DHL.toString())) {
                    shipmentEventDto.setPartnerName(ServicePartner.DHL);
                    shipmentEventDto.setEventName(ClaimLifecycleEvent.DHL_SHIPMENT);
                    shipmentEventDto.setShipmentDetails(getObject(entity, ShipmentSearchResultDto.class));
                    serviceRequestHelper.publishMessageInSPSRProcessingQueue(shipmentEventDto);
                } else {
                    logger.error("Partner name " + partnerMasterDto.getPartnerName() + " is not configured for publish event");
                }
            } else {
                logger.error("Partner code " + partnerCode + " doesn't exist in partner cache");
            }
        }
        logger.error("<<< In LogisticServiceImpl: publishShipmentMessage:ends");
        return status;

    }

    PincodeServiceFulfilmentEntity getLogisticPartnerForPincodeByServiceRequestType(String pinCode, Long serviceRequestTypeId, String status, String subCategorycode) {
        List<PincodeServiceFulfilmentEntity> fulfilmentEntities = fulfilmentRepository.findShipmentPincodeFulfilment(pinCode, serviceRequestTypeId, status, subCategorycode);
        PincodeServiceFulfilmentEntity fulfilmentEntity = null;

        if (null != fulfilmentEntities && fulfilmentEntities.size() > 0) {
            fulfilmentEntity = fulfilmentEntities.get(0);
        }

        return fulfilmentEntity;
    }

    @Override
    public List<ShipmentDetailsDto> getShipmentsByServiceRequestId(Long serviceRequestId) {
        List<ShipmentDetailsDto> shipmentDetailsDtos = null;
        try {
            List<ShipmentReassignEntity> shipmentEntities = logisticShipmentReassignmentRepository.findByServiceRequestId(serviceRequestId);
            if (!CollectionUtils.isEmpty(shipmentEntities)) {
                sortByShipmentId(shipmentEntities);
                shipmentDetailsDtos = modelMapper.map(shipmentEntities, new TypeToken<List<ShipmentDetailsDto>>() {
                }.getType());
                if (shipmentDetailsDtos != null) {
                    for (ShipmentDetailsDto shipmentDetail : shipmentDetailsDtos) {
                        List<SystemConfigDto> shipmentStatusConfigList = serviceRequestHelper.getShipmentStatus(shipmentDetail.getLogisticPartnerCode());
                        List<SystemConfigDto> shipmentStagesConfigList = serviceRequestHelper.getShipmentStage(shipmentDetail.getLogisticPartnerCode(), shipmentDetail.getShipmentType());
                        if (!CollectionUtils.isEmpty(shipmentStatusConfigList)) {
                            for (SystemConfigDto status : shipmentStatusConfigList) {
                                if (status.getParamName().equalsIgnoreCase(shipmentDetail.getStatus())) {
                                    shipmentDetail.setStatusName(status.getParamValue());
                                    break;
                                }
                            }
                        }
                        if (!CollectionUtils.isEmpty(shipmentStagesConfigList)) {
                            for (SystemConfigDto status : shipmentStagesConfigList) {
                                if (status.getParamName().equalsIgnoreCase(shipmentDetail.getCurrentStage())) {
                                    shipmentDetail.setCurrentStageName(status.getParamValue());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception while getShipmentsByServiceRequestId", e);
        }
        return shipmentDetailsDtos;
    }

    private void sortByShipmentId(List<ShipmentReassignEntity> shipmentEntities) {
        try {
            Collections.sort(shipmentEntities, new Comparator<ShipmentReassignEntity>() {

                @Override
                public int compare(ShipmentReassignEntity e1, ShipmentReassignEntity e2) {
                    int equality = 0;
                    if (e1.getShipmentId() > e2.getShipmentId()) {
                        equality = -1;
                    } else if (e2.getShipmentId() > (e1.getShipmentId())) {
                        equality = 1;
                    }
                    return equality;
                }
            });
        } catch (Exception e) {
            logger.error("Exception while sorting stages by time", e);
        }
    }

    private void cancelLogisticPartnerOrder(ShipmentEntity shipmentEntity) throws Exception {
        if (!StringUtils.isEmpty(shipmentEntity.getLogisticPartnerCode()) && !StringUtils.isEmpty(shipmentEntity.getLogisticPartnerRefTrackingNumber())) {
            PartnerMasterDto partnerMasterDto = partnerMasterCache.get(String.valueOf(shipmentEntity.getLogisticPartnerCode()));
            ServicePartner servicePartner = ServicePartner.valueOf(partnerMasterDto.getPartnerName());
            switch (servicePartner) {
                case LOGINEXT:
                    logisticProviders.get(partnerMasterDto.getPartnerName()).cancelShipment(shipmentEntity.getLogisticPartnerRefTrackingNumber());
                    break;
                default:
                    logger.error("Cancel event is not configured for the partner ::" + partnerMasterDto.getPartnerName());
                    break;
            }
        }
    }

    @Override
    public ShipmentRequestDto getByShipmentId(Long shipmentId) {
        ShipmentRequestDto shpipmentDetails = null;
        ShipmentEntity shipmentEntity = logisticShipmentRepository.findOne(shipmentId);
        if (shipmentEntity != null) {
            shpipmentDetails = modelMapper.map(shipmentEntity, new TypeToken<ShipmentRequestDto>() {
            }.getType());
        }
        return shpipmentDetails;
    }

    private <T> T getObject(Object payload, Class<T> classType) throws IOException {
        T responseClass = null;
        if (payload != null) {
            String payloadString = null;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Date.class, new DateFormatDeserializer());
            objectMapper.registerModule(module);
            if (!(payload instanceof String)) {
                payloadString = objectMapper.writeValueAsString(payload);
            } else {
                payloadString = payload.toString();
            }
            responseClass = objectMapper.readValue(payloadString, classType);
        }
        return responseClass;
    }

    private String getPartnerName(String partnerCode) {

        String partnerName = null;

        if (partnerCode != null) {
            HashMap<String, PartnerMasterDto> partnerCache = (HashMap<String, PartnerMasterDto>) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).getAll();

            if (partnerCache != null && partnerCache.containsKey(partnerCode)) {

                PartnerMasterDto partnerMasterDto = partnerCache.get(partnerCode);
                partnerName = partnerMasterDto.getPartnerName();
            }
        }

        return partnerName;
    }
}
