package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ErrorCodes;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.PincodeServiceDto;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationRequest;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerDetail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * PincodeService field validate
 * 
 * @author divya.hl
 */

@Component
public class PincodeServiceFieldValidator extends InputValidator {

    private Logger logger = Logger.getLogger(PincodeServiceFieldValidator.class);

    private static final String Y_OR_N_REGEX = ".*Y|N.*";

    public String validatePincodeServiceDto(PincodeServiceDto pincodeServiceDto) {

        StringBuilder errorText = new StringBuilder();
        errorText.append(validatePincode(pincodeServiceDto.getPincode()));
        errorText.append(validateHub(String.valueOf(pincodeServiceDto.getHubId())));
        errorText.append(validateCourtesyAvailability(pincodeServiceDto.getIsCourtesyApplicable()));

        return errorText.toString();
    }

    public String validatePincode(final String pincode) {
        logger.info("validatePincode starts");

        Object cacheObject = validateFromCache(pincode, Constants.PINCODE_MASTER_CACHE);

        if (cacheObject == null) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            return "";
        }
    }

    public String validateHub(String hubId) {
        logger.info("validatePincode starts");

        Object cacheObject = validateFromCache(hubId, Constants.HUB_MASTER_CACHE);

        if (cacheObject == null) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_HUB_ID.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            return "";
        }
    }

    public String validateCourtesyAvailability(String courtesyAvailability) {
        if (Strings.isNullOrEmpty(courtesyAvailability) || !courtesyAvailability.toUpperCase().matches(Y_OR_N_REGEX)) {
            return messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_COURTESY_FLAG.getErrorCode())), new Object[] { "" }, null) + ";";
        } else {
            return "";
        }
    }

    public List<ErrorInfoDto> validateServicePartnerAllocation(List<ServicePartnerAllocationRequest> servicePartnerAllocationRequest) {
        logger.info(">>> Validation for Common Fields :" + servicePartnerAllocationRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(servicePartnerAllocationRequest)) {
            for (ServicePartnerAllocationRequest partnerAllocation : servicePartnerAllocationRequest) {
                try {
                    validateMandatoryField("pincode", partnerAllocation.getPincode(), errorInfoDtoList);
                    Object cacheObject = validateFromCache(String.valueOf(partnerAllocation.getPincode()), Constants.PINCODE_MASTER_CACHE);
                    if (cacheObject == null) {
                        ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null), "pincode");
                        errorInfoDtoList.add(errorInfoDto);
                    }

                    validateMandatoryField("productCode", partnerAllocation.getProductCode(), errorInfoDtoList);
                    cacheObject = validateFromCache(partnerAllocation.getProductCode(), Constants.PRODUCT_MASTER_CACHE);
                    if (cacheObject == null) {
                        ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PRODUCT_CODE.getErrorCode())), new Object[] { "" }, null),
                                "productCode");
                        errorInfoDtoList.add(errorInfoDto);
                    }
                    validateMandatoryField("serviceRequestType", partnerAllocation.getServiceRequestType(), errorInfoDtoList);
                    cacheObject = validateFromCache(partnerAllocation.getServiceRequestType(), Constants.SERVICE_REQUEST_MASTER_CACHE);
                    if (cacheObject == null) {
                        ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_SERVICE_TYPE.getErrorCode())), new Object[] { "" }, null),
                                "serviceType");
                        errorInfoDtoList.add(errorInfoDto);
                    }
                    if (partnerAllocation.getPartner1() != null) {
                        validatePartnerInfo(partnerAllocation.getPartner1(), errorInfoDtoList);
                    }
                    if (partnerAllocation.getPartner2() != null) {
                        validatePartnerInfo(partnerAllocation.getPartner2(), errorInfoDtoList);
                    }
                    if (partnerAllocation.getPartner3() != null) {
                        validatePartnerInfo(partnerAllocation.getPartner3(), errorInfoDtoList);
                    }
                    if (partnerAllocation.getPartner4() != null) {
                        validatePartnerInfo(partnerAllocation.getPartner4(), errorInfoDtoList);
                    }
                } catch (InputValidationException exception) {
                    logger.info("Hub Allocation request validation failed >>", exception);
                }
            }
        } else {
            ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null), "inputObject");
            errorInfoDtoList.add(errorInfoDto);
        }
        logger.info("<< Validation for Common Fields");
        return errorInfoDtoList;
    }

    private void validatePartnerInfo(ServicePartnerDetail partner, List<ErrorInfoDto> errorInfoDtoList) {
        try {
            Object cacheObject = null;
            if (partner.getPartnerCode() != null && Long.valueOf(partner.getPartnerCode()).longValue() != 0l) {
                validateMandatoryField("partnerCode", partner.getPartnerCode(), errorInfoDtoList);
                cacheObject = validateFromCache(String.valueOf(partner.getPartnerCode()), Constants.PARTNER_MASTER_CACHE);
                if (cacheObject == null) {
                    ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PARTNER_CODE.getErrorCode())), new Object[] { "" }, null),
                            "partnerCode");
                    errorInfoDtoList.add(errorInfoDto);
                }
            }
            if (partner.getTat() != null) {
                try {
                    Double.parseDouble(partner.getTat());
                } catch (NumberFormatException w) {
                    ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, "Invalid Partner TAT", "partnerTAT");
                    errorInfoDtoList.add(errorInfoDto);
                }
            }
            if (partner.getPartnerBUCode() != null && Long.valueOf(partner.getPartnerBUCode()).longValue() != 0l) {
                cacheObject = validateFromCache(String.valueOf(partner.getPartnerBUCode()), Constants.PARTNER_BU_CACHE);
                if (cacheObject == null) {
                    ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_SERVICE_TYPE.getErrorCode())), new Object[] { "" }, null),
                            "partnerBUCode");
                    errorInfoDtoList.add(errorInfoDto);
                }
            }
        } catch (InputValidationException exception) {
            logger.info("Hub Allocation request validation failed >>", exception);
        }
    }
}
