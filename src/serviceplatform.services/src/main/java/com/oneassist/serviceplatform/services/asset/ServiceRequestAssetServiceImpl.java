package com.oneassist.serviceplatform.services.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntityDocumentEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestAssetResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestAssetRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestEntityDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.specifications.ServiceRequestAssetSpecifications;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestAssetValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestEntityDocumentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestEntityDocumentResponseDto;
import com.oneassist.serviceplatform.services.document.IServiceRequestDocumentService;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class ServiceRequestAssetServiceImpl implements IServiceRequestAssetService {

    @Autowired
    private ServiceRequestAssetValidator serviceRequestAssetValidator;

    @Autowired
    private ServiceRequestAssetRepository serviceRequestAssetRepository;

    @Autowired
    private ServiceRequestEntityDocumentRepository serviceRequestEntityDocumentRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private IServiceRequestDocumentService serviceRequestDocumentService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    private final Logger logger = Logger.getLogger(ServiceRequestAssetServiceImpl.class);

    @Override
    public List<ServiceRequestAssetResponseDto> getServiceRequestAssetDetails(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException {

        List<ErrorInfoDto> errorInfoList = serviceRequestAssetValidator.doValidateServiceAssetRequest(serviceRequestAssetRequestDto);

        if (null != errorInfoList && errorInfoList.size() > 0) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {
            List<ServiceRequestAssetEntity> serviceRequestAssetEntities = serviceRequestAssetRepository.findAll(ServiceRequestAssetSpecifications.filterServiceRequests(serviceRequestAssetRequestDto));

            List<ServiceRequestAssetResponseDto> listData = modelMapper.map(serviceRequestAssetEntities, new TypeToken<List<ServiceRequestAssetResponseDto>>() {
            }.getType());
            if (listData != null && !listData.isEmpty()) {
                for (ServiceRequestAssetResponseDto serviceRequestAssetResponseDto : listData) {
                    List<ServiceRequestEntityDocumentResponseDto> serviceRequestAssetDocuments = serviceRequestAssetResponseDto.getServiceRequestAssetDocuments();
                    if (serviceRequestAssetDocuments != null && !serviceRequestAssetDocuments.isEmpty()) {
                        Iterator<ServiceRequestEntityDocumentResponseDto> responseIterator = serviceRequestAssetDocuments.iterator();
                        while (responseIterator.hasNext()) {
                            ServiceRequestEntityDocumentResponseDto serviceRequestEntityDocumentResponseDto = responseIterator.next();
                            if (serviceRequestEntityDocumentResponseDto.getStatus() != null && !serviceRequestEntityDocumentResponseDto.getStatus().equals(Constants.ACTIVE)) {
                                responseIterator.remove();
                            }
                        }
                    }
                }
            }
            return listData;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ServiceRequestAssetResponseDto createServiceRequestAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException, JsonProcessingException {
        ServiceRequestAssetResponseDto serviceRequestAssetResponseDto = null;

        List<ErrorInfoDto> errorInfoList = serviceRequestAssetValidator.doValidateCreateServiceAssetRequest(serviceRequestAssetRequestDto);
        if (null != errorInfoList && errorInfoList.size() > 0) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {
            try {
                ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestAssetRequestDto.getServiceRequestId());
                if (serviceRequestEntity != null) {
                    List<ServiceRequestAssetEntity> serviceRequestAssetEntities = serviceRequestAssetRepository.findByServiceRequestIdAndStatus(serviceRequestAssetRequestDto.getServiceRequestId(),
                            Constants.ACTIVE);

                    if (!CollectionUtils.isEmpty(serviceRequestAssetEntities)) {
                        ServiceRequestDto serviceRequestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);

                        if (!CollectionUtils.isEmpty(serviceRequestDto.getThirdPartyProperties())
                                && (serviceRequestDto.getThirdPartyProperties().get(Constants.MAX_ASSET_COUNT_KEY) != null && serviceRequestAssetEntities.size() >= Integer.parseInt(serviceRequestDto
                                        .getThirdPartyProperties().get(Constants.MAX_ASSET_COUNT_KEY).toString()))) {
                            throw new BusinessServiceException(ServiceRequestResponseCodes.MAXIMUM_ASSETS_HAS_BEEN_ADDED.getErrorCode(), errorInfoList, new InputValidationException());
                        }

                        for (ServiceRequestAssetEntity asset : serviceRequestAssetEntities) {
                            if (asset.getAssetId().equalsIgnoreCase(serviceRequestAssetRequestDto.getAssetId())) {
                                throw new BusinessServiceException(GenericResponseCodes.INVALID_REQUEST.getErrorCode(), errorInfoList, new InputValidationException());
                            }
                        }
                    }
                    ServiceRequestAssetEntity serviceRequestAssetEntity = modelMapper.map(serviceRequestAssetRequestDto, new TypeToken<ServiceRequestAssetEntity>() {}.getType());
                    serviceRequestAssetEntity.setStatus(Constants.ACTIVE);
                    serviceRequestAssetEntity.setCreatedOn(new Date());
                    String assetId = StringUtils.isEmpty(serviceRequestAssetEntity.getAssetId()) ? UUID.randomUUID().toString().replaceAll("-", "") : serviceRequestAssetEntity.getAssetId();
                    serviceRequestAssetEntity.setAssetId(assetId);
                    
                    serviceRequestAssetRepository.save(serviceRequestAssetEntity);

                    List<ServiceRequestEntityDocumentEntity> serviceRequestEntityDocumentEntities = new ArrayList<>();
                    List<ServiceRequestEntityDocumentRequestDto> documentIds = serviceRequestAssetRequestDto.getServiceRequestAssetDocuments();
                    if (documentIds != null && !documentIds.isEmpty()) {
                        for (ServiceRequestEntityDocumentRequestDto serviceRequestEntityDocumentRequestDto : documentIds) {
                            ServiceRequestEntityDocumentEntity serviceRequestEntityDocumentEntity = new ServiceRequestEntityDocumentEntity();
                            serviceRequestEntityDocumentEntity.setEntityId(assetId);
                            serviceRequestEntityDocumentEntity.setDocumentId(serviceRequestEntityDocumentRequestDto.getDocumentId());
                            serviceRequestEntityDocumentEntity.setStatus(Constants.ACTIVE);
                            serviceRequestEntityDocumentEntity.setCreatedOn(new Date());
                            serviceRequestEntityDocumentEntity.setCreatedBy(serviceRequestAssetEntity.getCreatedBy());
                            serviceRequestEntityDocumentEntities.add(serviceRequestEntityDocumentEntity);
                        }
                        try {
                            serviceRequestEntityDocumentRepository.save(serviceRequestEntityDocumentEntities);
                        } catch (DataIntegrityViolationException | ConstraintViolationException ex) {
                            logger.error("Exception while saving asset entity mapping:", ex);
                        }
                        serviceRequestAssetEntity.setServiceRequestAssetDocuments(serviceRequestEntityDocumentEntities);
                    }
                    serviceRequestAssetResponseDto = modelMapper.map(serviceRequestAssetEntity, new TypeToken<ServiceRequestAssetResponseDto>() {
                    }.getType());
                } else {
                    throw new BusinessServiceException(GenericResponseCodes.INVALID_REQUEST.getErrorCode(), errorInfoList, new InputValidationException());
                }

            } catch (BusinessServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new BusinessServiceException(ServiceRequestAssetResponseCodes.INVALID_SR_OR_DUPLICATE_SERVICE_REQUEST_ASSET.getErrorCode(), errorInfoList, new InputValidationException());
            }
        }
        return serviceRequestAssetResponseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public ServiceRequestAssetResponseDto updateServiceRequestAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException {
        ServiceRequestAssetResponseDto serviceRequestAssetResponseDto = null;
        List<ErrorInfoDto> errorInfoList = serviceRequestAssetValidator.doValidateUpdateServiceAssetRequest(serviceRequestAssetRequestDto);
        if (null != errorInfoList && errorInfoList.size() > 0) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {
            ServiceRequestAssetEntity serviceRequestAssetEntity = serviceRequestAssetRepository.findOne(serviceRequestAssetRequestDto.getAssetId());
            if (serviceRequestAssetEntity != null) {

                populateServiceRequestAssetRequest(serviceRequestAssetEntity, serviceRequestAssetRequestDto);
                serviceRequestAssetRepository.save(serviceRequestAssetEntity);
                serviceRequestAssetResponseDto = modelMapper.map(serviceRequestAssetEntity, new TypeToken<ServiceRequestAssetResponseDto>() {
                }.getType());
            } else {
                throw new BusinessServiceException(ServiceRequestAssetResponseCodes.INVALID_SERVICE_REQUEST_ASSET.getErrorCode(), errorInfoList, new InputValidationException());
            }
        }
        return serviceRequestAssetResponseDto;
    }

    private void populateServiceRequestAssetRequest(ServiceRequestAssetEntity serviceRequestAssetEntity, ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
        serviceRequestAssetEntity.setAssetAge(serviceRequestAssetRequestDto.getAssetAge());
        serviceRequestAssetEntity.setAssetInspectionStatus(serviceRequestAssetRequestDto.getAssetInspectionStatus());
        serviceRequestAssetEntity.setAssetSize(serviceRequestAssetRequestDto.getAssetSize());
        serviceRequestAssetEntity.setAssetTechnology(serviceRequestAssetRequestDto.getAssetTechnology());
        serviceRequestAssetEntity.setAssetUnit(serviceRequestAssetRequestDto.getAssetUnit());
        serviceRequestAssetEntity.setIsAccidentalDamage(serviceRequestAssetRequestDto.getIsAccidentalDamage());
        serviceRequestAssetEntity.setIsFunctional(serviceRequestAssetRequestDto.getIsFunctional());
        serviceRequestAssetEntity.setIsInformationCorrect(serviceRequestAssetRequestDto.getIsInformationCorrect());
        serviceRequestAssetEntity.setMake(serviceRequestAssetRequestDto.getMake());
        serviceRequestAssetEntity.setModelNo(serviceRequestAssetRequestDto.getModelNo());
        serviceRequestAssetEntity.setModifiedBy(serviceRequestAssetRequestDto.getModifiedBy());
        serviceRequestAssetEntity.setSerialNo(serviceRequestAssetRequestDto.getSerialNo());
        serviceRequestAssetEntity.setModifiedOn(new Date());

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteServiceRequestAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException, ParseException {

        ServiceRequestAssetEntity serviceRequestAssetEntity = serviceRequestAssetRepository.findOne(serviceRequestAssetRequestDto.getAssetId());
        if (serviceRequestAssetEntity != null) {
            if (serviceRequestAssetEntity.getServiceRequestAssetDocuments() != null && !serviceRequestAssetEntity.getServiceRequestAssetDocuments().isEmpty()) {
                int i = 0;
                String documentIds = "";
                for (ServiceRequestEntityDocumentEntity docEntity : serviceRequestAssetEntity.getServiceRequestAssetDocuments()) {
                    if (i != 0)
                        documentIds += ",";
                    documentIds += docEntity.getDocumentId();
                    i++;
                }
                serviceRequestDocumentService.deleteServiceRequestDocument(documentIds);
            }
            serviceRequestAssetEntity.setStatus(Constants.EXPIRED);
            serviceRequestAssetEntity.setModifiedBy(serviceRequestAssetRequestDto.getModifiedBy());
            serviceRequestAssetEntity.setModifiedOn(new Date());
            serviceRequestAssetRepository.save(serviceRequestAssetEntity);
        } else {
            throw new BusinessServiceException(ServiceRequestAssetResponseCodes.INVALID_SERVICE_REQUEST_ASSET.getErrorCode(), new ArrayList<ErrorInfoDto>(), new InputValidationException());
        }
    }
}
