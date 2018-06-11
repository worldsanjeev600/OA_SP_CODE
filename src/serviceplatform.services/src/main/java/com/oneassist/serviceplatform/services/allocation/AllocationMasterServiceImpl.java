package com.oneassist.serviceplatform.services.allocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.oneassist.serviceplatform.commons.cache.PartnerBUCache;
import com.oneassist.serviceplatform.commons.cache.PartnerMasterCache;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.cache.ProductMasterCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentAssetEntity;
import com.oneassist.serviceplatform.commons.enums.AllocationMasterResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ErrorCodes;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.PartnerPriority;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentAssetRepository;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceFulfilmentRepository;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceRepository;
import com.oneassist.serviceplatform.commons.specifications.ServiceFulfilmentSpecification;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.commons.validators.AllocationMasterRequestValidator;
import com.oneassist.serviceplatform.commons.validators.PincodeServiceFieldValidator;
import com.oneassist.serviceplatform.commons.validators.PincodeServiceFulfilmentFieldValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentResponse;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchResult;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerDetail;
import com.oneassist.serviceplatform.contracts.response.base.PageResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerBusinessUnit;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import com.oneassist.serviceplatform.externalcontracts.ProductMasterDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Branch field validate
 * 
 * @author divya.hl
 */
@Service
public class AllocationMasterServiceImpl implements IAllocationMasterService {

    private final Logger logger = Logger.getLogger(AllocationMasterServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PincodeServiceRepository pincodeServiceRepository;

    @Autowired
    private PincodeServiceFulfilmentRepository pincodeServiceFulfilmentRepository;

    @Autowired
    private PincodeServiceFieldValidator pincodeServiceFieldValidator;

    @Autowired
    private PincodeServiceFulfilmentFieldValidator pincodeServiceFulfilmentFieldValidator;

    @Autowired
    private AllocationMasterRequestValidator allocationMasterRequestValidator;

    @Autowired
    private CustomRepository customRepositoryImpl;

    @Autowired
    private LogisticShipmentAssetRepository logisticShipmentAssetRepository;

    @Autowired
    private LogisticShipmentRepository logisticShipmentRepository;

    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    @Autowired
    private ProductMasterCache productMasterCache;

    @Autowired
    private PartnerBUCache partnerBUCache;

    @Autowired
    private PartnerMasterCache partnerMasterCache;

    @Override
    @Transactional
    public ResponseDto<List<PincodeServiceDto>> addPincodeServiceMaster(List<PincodeServiceDto> pincodeServiceDtos) throws BusinessServiceException {
        List<PincodeServiceDto> responsePincodesToAdd = new ArrayList<PincodeServiceDto>();
        ResponseDto<List<PincodeServiceDto>> response = new ResponseDto<List<PincodeServiceDto>>();

        try {
            for (PincodeServiceDto pincodeServiceDto : pincodeServiceDtos) {
                logger.info("In AllocationMasterServiceImpl : validate pincodeservice - pincode, hub id");
                String errorInfo = pincodeServiceFieldValidator.validatePincodeServiceDto(pincodeServiceDto);

                if (!Strings.isNullOrEmpty(errorInfo)) {
                    pincodeServiceDto.setSuccess(false);
                    pincodeServiceDto.setErrorMessage(errorInfo);
                    responsePincodesToAdd.add(pincodeServiceDto);
                } else {
                    try {
                        PincodeServiceEntity pincodeEntity = pincodeServiceRepository.findByPincode(pincodeServiceDto.getPincode());
                        PincodeServiceEntity pincodeServiceEntity = null;

                        if (pincodeEntity == null) {
                            pincodeServiceEntity = populatePincodeServiceEntity(pincodeServiceDto, Constants.CREATE_ACTION_FLAG);
                            pincodeServiceEntity = pincodeServiceRepository.save(pincodeServiceEntity);
                        } else {
                            pincodeServiceEntity = populatePincodeServiceEntity(pincodeServiceDto, Constants.UPDATE_ACTION_FLAG);
                            pincodeServiceEntity.setStatus(pincodeEntity.getStatus());
                            pincodeServiceEntity.setCreatedBy(pincodeEntity.getCreatedBy());
                            pincodeServiceEntity.setCreatedOn(pincodeEntity.getCreatedOn());
                            pincodeServiceEntity.setPincodeServiceId(pincodeEntity.getPincodeServiceId());
                            pincodeServiceEntity.setPincodeCategory(pincodeEntity.getPincodeCategory());
                            pincodeServiceRepository.save(pincodeServiceEntity);
                        }

                        pincodeServiceDto.setStatus(pincodeServiceEntity.getStatus());
                        pincodeServiceDto.setPincodeServiceId(pincodeServiceEntity.getPincodeServiceId());
                        pincodeServiceDto.setSuccess(true);
                        pincodeServiceDto.setErrorMessage(Constants.SUCCESS);
                        responsePincodesToAdd.add(pincodeServiceDto);
                    } catch (DataAccessException dAccessException) {
                        pincodeServiceDto.setSuccess(false);
                        pincodeServiceDto.setErrorMessage(messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_SAVE_PINCODE_SERVICE)), new Object[] { "" }, null));
                        responsePincodesToAdd.add(pincodeServiceDto);
                    }
                }
            }
        } catch (Exception exception) {
            response.setStatus(ResponseConstant.FAILED);
            response.setMessage(messageSource.getMessage((String.valueOf(ErrorCodes.PINCODESERVICE_EXCEPTION.getErrorCode())), new Object[] { "" }, null));
            throw new BusinessServiceException("<<<<<<<<<<<Exception occurred : In AllocationMasterServiceImpl: ", exception);
        }

        response.setData(responsePincodesToAdd);
        return response;
    }

    @Override
    @Transactional
    public ResponseDto<List<PincodeServiceFulfilmentDto>> addPincodeServiceFulfilmentMaster(List<PincodeServiceFulfilmentDto> pincodeServiceFulfilmentDtos) throws BusinessServiceException {

        List<PincodeServiceFulfilmentDto> responsePincodeFulfilmentsToAdd = new ArrayList<PincodeServiceFulfilmentDto>();
        ResponseDto<List<PincodeServiceFulfilmentDto>> response = new ResponseDto<List<PincodeServiceFulfilmentDto>>();

        try {
            List<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntity = customRepositoryImpl.findAllByServiceId();

            for (PincodeServiceFulfilmentDto pincodeServiceFulfilmentDto : pincodeServiceFulfilmentDtos) {
                logger.info("validate pincodeServiceFulfilmentDto - pincode, partner code");
                String errorInfo = pincodeServiceFulfilmentFieldValidator.validatePincodeServiceFulfilmentDto(pincodeServiceFulfilmentDto);

                if (!Strings.isNullOrEmpty(errorInfo)) {
                    pincodeServiceFulfilmentDto.setSuccess(false);
                    pincodeServiceFulfilmentDto.setErrorMessage(errorInfo);

                    responsePincodeFulfilmentsToAdd.add(pincodeServiceFulfilmentDto);
                } else {
                    try {
                        PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = null;
                        Long serviceRequestTypeId = populateServiceType(serviceRequestTypeMstEntity, pincodeServiceFulfilmentDto.getServiceRequestType());
                        pincodeServiceFulfilmentDto.setServiceRequestTypeId(serviceRequestTypeId);

                        PincodeServiceFulfilmentEntity pincodeFulfilmentEntity = pincodeServiceFulfilmentRepository.findByPincodeAndPartnerPriorityAndSubCategoryCodeAndServiceRequestTypeId(
                                pincodeServiceFulfilmentDto.getPincode(), pincodeServiceFulfilmentDto.getPartnerPriority(), pincodeServiceFulfilmentDto.getSubCategoryCode(), serviceRequestTypeId);

                        if (pincodeFulfilmentEntity == null) {
                            pincodeServiceFulfilmentEntity = populatePincodeServiceFulfilmentEntity(pincodeServiceFulfilmentDto, Constants.CREATE_ACTION_FLAG);
                            pincodeServiceFulfilmentEntity = pincodeServiceFulfilmentRepository.save(pincodeServiceFulfilmentEntity);
                        } else {
                            pincodeServiceFulfilmentEntity = populatePincodeServiceFulfilmentEntity(pincodeServiceFulfilmentDto, Constants.UPDATE_ACTION_FLAG);
                            pincodeServiceFulfilmentEntity.setStatus(pincodeFulfilmentEntity.getStatus());
                            pincodeServiceFulfilmentEntity.setCreatedBy(pincodeFulfilmentEntity.getCreatedBy());
                            pincodeServiceFulfilmentEntity.setCreatedOn(pincodeFulfilmentEntity.getCreatedOn());
                            pincodeServiceFulfilmentEntity.setFulfilmentId(pincodeFulfilmentEntity.getFulfilmentId());
                            pincodeServiceFulfilmentRepository.save(pincodeServiceFulfilmentEntity);
                        }
                        pincodeServiceFulfilmentDto.setStatus(pincodeServiceFulfilmentEntity.getStatus());
                        pincodeServiceFulfilmentDto.setFulfilmentId(pincodeServiceFulfilmentEntity.getFulfilmentId());
                        pincodeServiceFulfilmentDto.setSuccess(true);
                        pincodeServiceFulfilmentDto.setErrorMessage(Constants.SUCCESS);
                        responsePincodeFulfilmentsToAdd.add(pincodeServiceFulfilmentDto);
                    } catch (DataAccessException dAccessException) {
                        pincodeServiceFulfilmentDto.setSuccess(false);
                        pincodeServiceFulfilmentDto.setErrorMessage(messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_SAVE_PINCODE_SERVICE_FULFILMENT.getErrorCode())), new Object[] { "" },
                                null));
                        responsePincodeFulfilmentsToAdd.add(pincodeServiceFulfilmentDto);
                    }
                }
            }
        } catch (Exception ex) {

            throw new BusinessServiceException(ErrorCodes.PINCODESERVICEFULFILMENT_EXCEPTION.getErrorCode(), ex);
        }

        response.setData(responsePincodeFulfilmentsToAdd);

        return response;
    }

    private Long populateServiceType(List<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntity, String serviceRequestType) {
        Long servcieRequestTypeId = null;

        for (ServiceRequestTypeMstEntity serviceRequestTypeMstEntity2 : serviceRequestTypeMstEntity) {
            if (serviceRequestTypeMstEntity2.getServiceRequestType().equals(serviceRequestType)) {
                return serviceRequestTypeMstEntity2.getServiceRequestTypeId();
            }
        }

        return servcieRequestTypeId;
    }

    private PincodeServiceEntity populatePincodeServiceEntity(PincodeServiceDto pincodeServiceDto, String flag) {
        ModelMapper modelMapper = new ModelMapper();
        PincodeServiceEntity pincodeServiceEntity = modelMapper.map(pincodeServiceDto, PincodeServiceEntity.class);

        if (flag.equalsIgnoreCase(Constants.CREATE_ACTION_FLAG)) {
            pincodeServiceEntity.setStatus(Constants.ACTIVE);
            pincodeServiceEntity.setCreatedBy(Constants.USER_ADMIN);
            pincodeServiceEntity.setCreatedOn(new Date());
            pincodeServiceEntity.setModifiedBy(Constants.USER_ADMIN);
            pincodeServiceEntity.setModifiedOn(new Date());
        } else {
            pincodeServiceEntity.setModifiedBy(Constants.USER_ADMIN);
            pincodeServiceEntity.setModifiedOn(new Date());
        }

        return pincodeServiceEntity;
    }

    private PincodeServiceFulfilmentEntity populatePincodeServiceFulfilmentEntity(PincodeServiceFulfilmentDto pincodeServiceFulfilmentDto, String flag) {
        ModelMapper modelMapper = new ModelMapper();
        PincodeServiceFulfilmentEntity pincodeServiceFulfilmentEntity = modelMapper.map(pincodeServiceFulfilmentDto, PincodeServiceFulfilmentEntity.class);
        if (flag.equalsIgnoreCase(Constants.CREATE_ACTION_FLAG)) {
            pincodeServiceFulfilmentEntity.setStatus(Constants.ACTIVE);
            pincodeServiceFulfilmentEntity.setCreatedBy(Constants.USER_ADMIN);
            pincodeServiceFulfilmentEntity.setCreatedOn(new Date());
            pincodeServiceFulfilmentEntity.setModifiedBy(Constants.USER_ADMIN);
            pincodeServiceFulfilmentEntity.setModifiedOn(new Date());
        } else {
            pincodeServiceFulfilmentEntity.setModifiedBy(Constants.USER_ADMIN);
            pincodeServiceFulfilmentEntity.setModifiedOn(new Date());
        }
        return pincodeServiceFulfilmentEntity;
    }

    @Override
    public ResponseDto<List<PincodeServiceFulfilmentResponse>> getPincodeFulfilments(String pinCode, String shipmentId, String serviceId) {
        List<ErrorInfoDto> errorInfoList = allocationMasterRequestValidator.doValidateGetPincodeServiceFulfilmentRequest(pinCode);
        ShipmentAssetEntity shipmentAssetEntity = null;
        ResponseDto<List<PincodeServiceFulfilmentResponse>> response = new ResponseDto<List<PincodeServiceFulfilmentResponse>>();

        if (null != errorInfoList && errorInfoList.size() > 0) {
            response.setInvalidData(errorInfoList);
            response.setStatus(ResponseConstant.FAILED);
            response.setStatusCode(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(GenericResponseCodes.VALIDATION_FAIL.getErrorCode()), new Object[] { "" }, null));
        } else {
            String status = Constants.ACTIVE;

            List<ShipmentAssetEntity> shipmentAssetList = logisticShipmentAssetRepository.findShipmentAssetEntityByShipmentId(Long.valueOf(shipmentId));
            if (shipmentAssetList.size() > 0) {
                shipmentAssetEntity = shipmentAssetList.get(0);
            }

            if (shipmentAssetEntity != null && shipmentAssetEntity.getAssetCategoryName() != null) {

                List<PincodeServiceFulfilmentEntity> pincodeServiceEntity = pincodeServiceFulfilmentRepository.findByPincodeAndSubCategoryCodeAndServiceRequestTypeIdAndStatus(pinCode,
                        shipmentAssetEntity.getAssetCategoryName(), Long.valueOf(serviceId), status);
                if (null != pincodeServiceEntity && pincodeServiceEntity.size() > 0) {
                    ModelMapper modelMapper = new ModelMapper();
                    List<PincodeServiceFulfilmentResponse> fulfilmentResponses = modelMapper.map(pincodeServiceEntity, new TypeToken<List<PincodeServiceFulfilmentResponse>>() {
                    }.getType());
                    response.setData(fulfilmentResponses);
                    response.setStatus(ResponseConstant.SUCCESS);
                    response.setStatusCode(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_SUCCESS.getErrorCode()));
                    response.setMessage(messageSource.getMessage(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_SUCCESS.getErrorCode()), new Object[] { "" }, null));
                } else {
                    response.setStatus(ResponseConstant.FAILED);
                    response.setStatusCode(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_FAILS.getErrorCode()));
                    response.setMessage(messageSource.getMessage(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_FAILS.getErrorCode()), new Object[] { "" }, null));
                }
            } else {
                response.setStatus(ResponseConstant.FAILED);
                response.setStatusCode(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_FAILS.getErrorCode()));
                response.setMessage(messageSource.getMessage(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_FAILS.getErrorCode()), new Object[] { "" }, null));
            }
        }
        return response;
    }

    @Override
    public ResponseDto<Map<String, List<Object>>> loadAllocationMasterData() {

        ResponseDto<Map<String, List<Object>>> response = new ResponseDto<Map<String, List<Object>>>();
        Map<String, List<Object>> entities = new HashMap<String, List<Object>>();
        Iterable<PincodeServiceEntity> pincodeServiceEntity = pincodeServiceRepository.findAll();
        List<PincodeServiceEntity> pincodeServiceEntities = Lists.newArrayList(pincodeServiceEntity);
        List<Object> objectList = new ArrayList<Object>(pincodeServiceEntities);

        if (null != pincodeServiceEntities && pincodeServiceEntities.size() > 0) {
            entities.put("PincodeServiceData", objectList);

            response.setData(entities);
            response.setStatus(ResponseConstant.SUCCESS);
            response.setStatusCode(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_SUCCESS.getErrorCode()));
            response.setMessage(messageSource.getMessage(String.valueOf(AllocationMasterResponseCodes.PINCODE_FULLFILMENTS_SUCCESS.getErrorCode()), new Object[] { "" }, null));
        }

        return response;
    }

    @Override
    public void modifyServicePartnerAllocation(List<ServicePartnerAllocationRequest> servicePartnerAllocationRequest) {
        List<ErrorInfoDto> errorInfoList = pincodeServiceFieldValidator.validateServicePartnerAllocation(servicePartnerAllocationRequest);
        if (!CollectionUtils.isEmpty(errorInfoList)) {
            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        }
        List<PincodeServiceFulfilmentEntity> pincodeServiceEntities = new ArrayList<PincodeServiceFulfilmentEntity>();
        for (ServicePartnerAllocationRequest partnerAllocation : servicePartnerAllocationRequest) {
            if (partnerAllocation.getPartner1() != null) {
                populatePartnerDetails(partnerAllocation, partnerAllocation.getPartner1(), pincodeServiceEntities, PartnerPriority.ONE);
            }
            if (partnerAllocation.getPartner2() != null) {
                populatePartnerDetails(partnerAllocation, partnerAllocation.getPartner2(), pincodeServiceEntities, PartnerPriority.TWO);
            }
            if (partnerAllocation.getPartner3() != null) {
                populatePartnerDetails(partnerAllocation, partnerAllocation.getPartner3(), pincodeServiceEntities, PartnerPriority.THREE);
            }
            if (partnerAllocation.getPartner4() != null) {
                populatePartnerDetails(partnerAllocation, partnerAllocation.getPartner4(), pincodeServiceEntities, PartnerPriority.FOUR);
            }
        }
        pincodeServiceFulfilmentRepository.save(pincodeServiceEntities);
    }

    private void populatePartnerDetails(ServicePartnerAllocationRequest partnerAllocation, ServicePartnerDetail servicePartnerDtl, List<PincodeServiceFulfilmentEntity> pincodeServiceEntities,
            PartnerPriority priority) {
        Long serviceTypeId = serviceRequestTypeMasterCache.get(partnerAllocation.getServiceRequestType()).getServiceRequestTypeId();
        List<PincodeServiceFulfilmentEntity> serviceEntities = pincodeServiceFulfilmentRepository.findByPincodeAndSubCategoryCodeAndServiceRequestTypeIdAndStatusAndPartnerPriority(
                String.valueOf(partnerAllocation.getPincode()), partnerAllocation.getProductCode(), serviceTypeId, Constants.ACTIVE, priority.getPriority());
        if (!CollectionUtils.isEmpty(serviceEntities)) {
            for (PincodeServiceFulfilmentEntity serviceEntity : serviceEntities) {
                if (servicePartnerDtl.getPartnerBUCode() != null) {
                    serviceEntity.setPartnerBUCode(String.valueOf(servicePartnerDtl.getPartnerBUCode()));
                }
                if (servicePartnerDtl.getPartnerCode() != null) {
                    serviceEntity.setPartnerCode(servicePartnerDtl.getPartnerCode());
                }
                if (servicePartnerDtl.getTat() != null) {
                    serviceEntity.setServiceTat(Integer.valueOf(String.valueOf(servicePartnerDtl.getTat())));
                }
                serviceEntity.setModifiedOn(new Date());
                serviceEntity.setModifiedBy(partnerAllocation.getModifiedBy());
                pincodeServiceEntities.add(serviceEntity);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageResponseDto<Collection<ServicePartnerAllocationSearchResult>> getServicePartnerAllocation(ServicePartnerAllocationSearchRequest servicePartnerAllocationSearchRequest,
            PageRequest pageRequest) {
        PageResponseDto<Collection<ServicePartnerAllocationSearchResult>> searchResult = new PageResponseDto<Collection<ServicePartnerAllocationSearchResult>>();
        List<PincodeServiceFulfilmentEntity> pincodeServiceEntities = null;
        List<String> pincodes = serviceRequestHelper.filterPincodes(servicePartnerAllocationSearchRequest.getPincodes(), servicePartnerAllocationSearchRequest.getStates(),
                servicePartnerAllocationSearchRequest.getCities());
        servicePartnerAllocationSearchRequest.setPincodes(pincodes);
        if (!StringUtils.isEmpty(servicePartnerAllocationSearchRequest.getServiceType())) {
            servicePartnerAllocationSearchRequest.setServiceTypeId(serviceRequestTypeMasterCache.get(servicePartnerAllocationSearchRequest.getServiceType()).getServiceRequestTypeId());
        }

        Page<PincodeServiceFulfilmentEntity> page = pincodeServiceFulfilmentRepository.findAll(ServiceFulfilmentSpecification.filterServiceHubMaster(servicePartnerAllocationSearchRequest),
                pageRequest);
        if (page != null) {
            pincodeServiceEntities = page.getContent();
            searchResult.setTotalElements(page.getTotalElements());
            searchResult.setTotalPages(page.getTotalPages());
            if (!CollectionUtils.isEmpty(pincodeServiceEntities)) {
                searchResult.setData(populateServicePartnerAllocationSearchResult(pincodeServiceEntities));
            } else {
                searchResult.setData(new ArrayList<ServicePartnerAllocationSearchResult>());
            }
        }
        return searchResult;
    }

    private Collection<ServicePartnerAllocationSearchResult> populateServicePartnerAllocationSearchResult(List<PincodeServiceFulfilmentEntity> pincodeServiceEntities) {
        Map<String, ServicePartnerAllocationSearchResult> servicePartnerMap = new HashMap<String, ServicePartnerAllocationSearchResult>();
        for (PincodeServiceFulfilmentEntity pincodeServiceEntity : pincodeServiceEntities) {
            PincodeMasterDto pincodeMst = pinCodeMasterCache.get(pincodeServiceEntity.getPincode());
            if (pincodeMst != null) {
                String key = pincodeServiceEntity.getPincode() + "_" + pincodeServiceEntity.getServiceRequestTypeId() + "_" + pincodeServiceEntity.getSubCategoryCode();
                ServicePartnerAllocationSearchResult servicePartnerDto = servicePartnerMap.get(key);
                if (servicePartnerDto == null) {
                    servicePartnerDto = getServicePartnerDetail(pincodeServiceEntity, pincodeMst);
                }
                ServicePartnerDetail partner = new ServicePartnerDetail();
                partner.setTat(String.valueOf(pincodeServiceEntity.getServiceTat()));
                if (pincodeServiceEntity.getPartnerCode() != null) {
                    PartnerMasterDto partnerMst = partnerMasterCache.get(String.valueOf(pincodeServiceEntity.getPartnerCode()));
                    if (partnerMst != null) {
                        partner.setPartnerCode(pincodeServiceEntity.getPartnerCode());
                        partner.setPartnerName(partnerMst.getPartnerName());
                    }
                }
                if (pincodeServiceEntity.getPartnerBUCode() != null) {
                    PartnerBusinessUnit partnerBuMst = partnerBUCache.get(pincodeServiceEntity.getPartnerBUCode());
                    partner.setPartnerBUCode(pincodeServiceEntity.getPartnerBUCode());
                    partner.setPartnerBUName(partnerBuMst.getBusinessUnitName());
                }
                partner.setModifiedOn(pincodeServiceEntity.getModifiedOn());
                if (pincodeServiceEntity.getPartnerPriority() != null && pincodeServiceEntity.getPartnerPriority() != 0) {
                    PartnerPriority priority = PartnerPriority.getPriority(pincodeServiceEntity.getPartnerPriority());
                    if (priority != null) {
                        switch (priority) {
                            case FOUR:
                                servicePartnerDto.setPartner4(partner);
                                break;
                            case ONE:
                                servicePartnerDto.setPartner1(partner);
                                break;
                            case THREE:
                                servicePartnerDto.setPartner3(partner);
                                break;
                            case TWO:
                                servicePartnerDto.setPartner2(partner);
                                break;
                            default:
                                break;
                        }
                    }
                }
                servicePartnerMap.put(key, servicePartnerDto);
            }
        }
        return servicePartnerMap.values();
    }

    private ServicePartnerAllocationSearchResult getServicePartnerDetail(PincodeServiceFulfilmentEntity pincodeServiceEntity, PincodeMasterDto pincodeMst) {
        ServicePartnerAllocationSearchResult servicePartnerDto = new ServicePartnerAllocationSearchResult();
        servicePartnerDto.setPincode(pincodeMst.getPinCode());
        servicePartnerDto.setCity(pincodeMst.getCityName());
        servicePartnerDto.setState(pincodeMst.getStateName());
        ProductMasterDto productMst = productMasterCache.get(pincodeServiceEntity.getSubCategoryCode());
        if (productMst != null) {
            servicePartnerDto.setProduct(productMst.getProductName());
        }
        servicePartnerDto.setProductCode(pincodeServiceEntity.getSubCategoryCode());
        Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();
        ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
        for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
            if (entry.getValue().getServiceRequestTypeId().longValue() == pincodeServiceEntity.getServiceRequestTypeId().longValue()) {
                serviceRequestTypeMstEntity = entry.getValue();
                break;
            }
        }
        if (serviceRequestTypeMstEntity != null) {
            servicePartnerDto.setServiceTypeCode(serviceRequestTypeMstEntity.getServiceRequestType());
            servicePartnerDto.setServiceName(serviceRequestTypeMstEntity.getServiceRequestTypeName());
        }
        servicePartnerDto.setModifiedOn(pincodeServiceEntity.getModifiedOn());
        return servicePartnerDto;
    }
}
