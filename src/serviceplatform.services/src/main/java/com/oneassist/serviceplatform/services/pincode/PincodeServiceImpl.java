package com.oneassist.serviceplatform.services.pincode;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.cache.PinCodeMasterCache;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceFulfilmentRepository;
import com.oneassist.serviceplatform.commons.repositories.PincodeServiceRepository;
import com.oneassist.serviceplatform.commons.validators.ServiceRequestValidator;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.PincodeServicabilityResponse;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;

@Service
public class PincodeServiceImpl implements IPincodeService {

    private final Logger logger = Logger.getLogger(PincodeServiceImpl.class);

    @Autowired
    private ServiceRequestValidator serviceRequestValidator;

    @Autowired
    private PincodeServiceFulfilmentRepository pincodeServiceFulfilmentRepository;

    @Autowired
    private PincodeServiceRepository pincodeServiceRepository;

    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;
    
    @Autowired
    private PinCodeMasterCache pinCodeMasterCache;

    @Override
    public PincodeServicabilityResponse checkPincodeServicabilty(String pinCode, String serviceRequestType) throws BusinessServiceException {

        PincodeServicabilityResponse pincodeServicabilityResponse = new PincodeServicabilityResponse();

        List<ErrorInfoDto> errorInfoList = serviceRequestValidator.doValidatePincodeServicebleRequest(pinCode, serviceRequestType);

        if (null != errorInfoList && errorInfoList.size() > 0) {

            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {
            List<String> supportedRequestTypes = new ArrayList<>();

            if (!Strings.isNullOrEmpty(serviceRequestType)) {
                ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMasterCache.getAll().get(serviceRequestType);
                if (serviceRequestTypeMstEntity != null) {
                    long serviceRequestTypeId = serviceRequestTypeMstEntity.getServiceRequestTypeId();
                    supportedRequestTypes = pincodeServiceFulfilmentRepository.getDistinctServiceRequestTypesByPincodeAndServiceRequestTypeId(pinCode, serviceRequestTypeId, Constants.ACTIVE);
                } else {
                    throw new BusinessServiceException(ServiceRequestResponseCodes.PINCODE_SEARCH_INVALID_SERVICE_TYPE.getErrorCode(), new Object[] { serviceRequestType });
                }
            } else {
                supportedRequestTypes = pincodeServiceFulfilmentRepository.getDistinctServiceRequestTypesByPincode(pinCode, Constants.ACTIVE);
            }

            PincodeMasterDto pincodeMasterDto = pinCodeMasterCache.get(pinCode);
            if (pincodeMasterDto != null) {
            	pincodeServicabilityResponse.setCityName(pincodeMasterDto.getCityName());
            	pincodeServicabilityResponse.setStateName(pincodeMasterDto.getStateName());
            	pincodeServicabilityResponse.setCityCode(pincodeMasterDto.getCityCode());
            	pincodeServicabilityResponse.setStateCode(pincodeMasterDto.getStateCode());
            }
            
            pincodeServicabilityResponse.setPincode(pinCode);
            pincodeServicabilityResponse.setSupportedRequestTypes(supportedRequestTypes);

            PincodeServiceEntity pincodeServiceEntity = pincodeServiceRepository.findPincodeServiceByPincodeAndStatus(pinCode, Constants.ACTIVE);
            pincodeServicabilityResponse.setPincodeCategory(pincodeServiceEntity.getPincodeCategory());
            if (null != pincodeServiceEntity) {
                pincodeServicabilityResponse.setHubId(pincodeServiceEntity.getHubId());
                pincodeServicabilityResponse.setIsCourtesyApplicable(pincodeServiceEntity.getIsCourtesyApplicable());
            }

            if (null != supportedRequestTypes && !supportedRequestTypes.isEmpty()) {
                pincodeServicabilityResponse.setIsPincodeServicable(Constants.YES_FLAG);
            } else {
                pincodeServicabilityResponse.setIsPincodeServicable(Constants.NO_FLAG);
            }
        }

        return pincodeServicabilityResponse;
    }

    @Override
    public List<String> getPincodesByRequestType(String serviceRequestType) throws BusinessServiceException {

        List<String> pincodes = new ArrayList<>();
        List<ErrorInfoDto> errorInfoList = serviceRequestValidator.doValidateGetPincodesRequest(serviceRequestType);

        if (errorInfoList != null && !errorInfoList.isEmpty()) {

            throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoList, new InputValidationException());
        } else {

            ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = serviceRequestTypeMasterCache.getAll().get(serviceRequestType);

            if (serviceRequestTypeMstEntity == null) {
                throw new BusinessServiceException(ServiceRequestResponseCodes.PINCODE_SEARCH_INVALID_SERVICE_TYPE.getErrorCode(), new Object[] { serviceRequestType });
            }

            logger.debug("Pincodes to be fetched for ServiceRequestType ::" + serviceRequestType + " with ServiceRequestTypeID :: " + serviceRequestTypeMstEntity.getServiceRequestTypeId());

            pincodes = pincodeServiceFulfilmentRepository.getDistinctPincodesByRequestType(serviceRequestTypeMstEntity.getServiceRequestTypeId(), Constants.ACTIVE);
        }

        return pincodes;
    }
}
