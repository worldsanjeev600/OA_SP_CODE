package com.oneassist.serviceplatform.commons.validators;

import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.cache.base.CacheFactory;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.custom.CustomRepository;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.enums.ErrorCodes;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceFulfilmentDto;
import com.oneassist.serviceplatform.externalcontracts.HubMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PartnerMasterDto;
import com.oneassist.serviceplatform.externalcontracts.PincodeMasterDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PincodeServiceFulfilment field validate
 * 
 * @author divya.hl
 */
@Component
public class PincodeServiceFulfilmentFieldValidator extends BaseValidator {

    private Logger logger = Logger.getLogger(PincodeServiceFulfilmentFieldValidator.class);
    public static final String NUMBER_REGEX = "^[0-9]+$";
    public static final String Y_OR_N_REGEX = ".*Y|N.*";

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    CustomRepository customRepositoryImpl;

    /**
     * validate pincode service master dto
     * 
     * @param pincode
     * @return error string
     */
    public String validatePincodeServiceFulfilmentDto(PincodeServiceFulfilmentDto pincodeServiceFulfilmentDto) {
        logger.info("<<<<<<<<<<<In PincodeServiceFulfilmentFieldValidator :validatePincodeServiceFulfilmentDto():starts ");
        StringBuilder errorText = new StringBuilder();
        if (pincodeServiceFulfilmentDto.getPincode() != null)
            errorText.append(validatePincode(pincodeServiceFulfilmentDto.getPincode()));
        if (pincodeServiceFulfilmentDto.getServiceTat() > 0)
            errorText.append(validateVendorTat(String.valueOf(pincodeServiceFulfilmentDto.getServiceTat())));
        if (pincodeServiceFulfilmentDto.getPartnerCode().toString() != null)
            errorText.append(validatePartner(pincodeServiceFulfilmentDto.getPartnerCode().toString()));
        if (pincodeServiceFulfilmentDto.getServiceRequestType() != null)
            errorText.append(validateService(pincodeServiceFulfilmentDto.getServiceRequestType()));
        logger.info("<<<<<<<<<<<In PincodeServiceFulfilmentFieldValidator :validatePincodeServiceFulfilmentDto():ends ");
        return errorText.toString();
    }

    /**
     * @param pincode
     * @return
     */
    public String validatePincode(final String pincode) {
        if (cacheFactory != null) {
            PincodeMasterDto pincodeMasterDto = (PincodeMasterDto) cacheFactory.get(Constants.PINCODE_MASTER_CACHE).get(pincode);
            if (pincodeMasterDto == null) {
                return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null) + ";";
            } else {
                return "";
            }
        } else {
            return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_LOAD_CACHE_FACTORY.getErrorCode())), new Object[] { "" }, null) + ";";
        }
    }

    /**
     * @param hubId
     * @return
     */
    public String validateHub(String hubId) {
        if (cacheFactory != null) {
            HubMasterDto hubMasterDto = (HubMasterDto) cacheFactory.get(Constants.HUB_MASTER_CACHE).get(hubId);

            if (hubMasterDto == null) {
                return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_HUB_ID.getErrorCode())), new Object[] { "" }, null) + ";";
            } else {
                return "";
            }
        } else {
            return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_LOAD_CACHE_FACTORY.getErrorCode())), new Object[] { "" }, null) + ";";
        }
    }

    /**
     * @param courtesyAvailability
     * @return
     */
    public String validateCourtesyAvailability(String courtesyAvailability) {
        if (Strings.isNullOrEmpty(courtesyAvailability) || !courtesyAvailability.toUpperCase().matches(Y_OR_N_REGEX)) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_COURTESY_FLAG.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            return "";
        }
    }

    /**
     * @param partnerCode
     * @return
     */
    public String validatePartner(String partnerCode) {
        if (cacheFactory != null) {
            PartnerMasterDto partnerMasterDto = (PartnerMasterDto) cacheFactory.get(Constants.PARTNER_MASTER_CACHE).get(partnerCode);
            if (partnerMasterDto.getPartnerCode() == null) {
                return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null) + ";";
            } else {
                if (!Strings.isNullOrEmpty(Constants.PARTNER_TYPE_FLAG) && !partnerMasterDto.getPartnerType().equalsIgnoreCase(Constants.PARTNER_TYPE_FLAG)) {
                    return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null) + ";";
                }
                return "";
            }
        } else {
            return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_LOAD_CACHE_FACTORY.getErrorCode())), new Object[] { "" }, null) + ";";
        }
    }

    /**
     * @param serviceName
     * @return
     */
    public String validateService(String serviceType) {
        if (Strings.isNullOrEmpty(serviceType)) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_SERVICE_TYPE.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            List<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntity = customRepositoryImpl.findAllByServiceId();
            if (serviceRequestTypeMstEntity != null && serviceRequestTypeMstEntity.size() > 0) {
                for (ServiceRequestTypeMstEntity serviceRequestTypeMstEntity2 : serviceRequestTypeMstEntity) {
                    if (serviceRequestTypeMstEntity2.getServiceRequestType().equalsIgnoreCase(serviceType))
                        return "";
                }
                return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_SERVICE_TYPE.getErrorCode())), new Object[] { "" }, null) + ";";

            } else {
                return messageSource.getMessage((String.valueOf(ErrorCodes.UNABLE_GET_SERVICE_TYPE.getErrorCode())), new Object[] { "" }, null) + ";";
            }
        }
    }

    /**
     * @param tat
     * @return
     */
    public String validateVendorTat(String tat) {
        if (Strings.isNullOrEmpty(tat)) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            if (!tat.trim().matches(NUMBER_REGEX)) {
                return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null) + ";";
            }
            return "";
        }
    }

}
