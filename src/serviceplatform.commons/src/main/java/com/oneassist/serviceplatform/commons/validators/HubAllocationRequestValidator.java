package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ErrorCodes;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationRequestDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class HubAllocationRequestValidator extends InputValidator {

    private static final Logger logger = Logger.getLogger(HubAllocationRequestValidator.class);

    public List<ErrorInfoDto> doValidateHubAllocationRequest(HubAllocationDto hubAllocationRequestDto) {
        logger.info(">>> Validation for Mandatory Data Hub Allocation Request :" + hubAllocationRequestDto);
        return validateCommonFields(hubAllocationRequestDto);
    }

    private List<ErrorInfoDto> validateCommonFields(HubAllocationDto hubAllocationRequestDto) {
        logger.info(">>> Validation for Common Fields :" + hubAllocationRequestDto);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();

        try {
            validateMandatoryField("pincode", hubAllocationRequestDto.getPincode(), errorInfoDtoList);

            Object cacheObject = validateFromCache(hubAllocationRequestDto.getPincode(), Constants.PINCODE_MASTER_CACHE);

            if (cacheObject == null) {
                ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null), "pincode");
                errorInfoDtoList.add(errorInfoDto);
            }

        } catch (InputValidationException exception) {
            logger.info("Hub Allocation request validation failed >>", exception);
        }

        logger.info("<< Validation for Common Fields");

        return errorInfoDtoList;
    }

    public List<ErrorInfoDto> doValidateHubAllocationRequest(HubAllocationRequestDto hubAllocationRequest) {
        logger.info(">>> Validation for Common Fields :" + hubAllocationRequest);
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();
        if (hubAllocationRequest != null && !CollectionUtils.isEmpty(hubAllocationRequest.getHubAllocations())) {
            for (HubAllocationDto hubAllocation : hubAllocationRequest.getHubAllocations()) {
                try {
                    validateMandatoryField("pincode", hubAllocation.getPincode(), errorInfoDtoList);
                    Object cacheObject = validateFromCache(hubAllocation.getPincode(), Constants.PINCODE_MASTER_CACHE);

                    if (cacheObject == null) {
                        ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_PINCODE.getErrorCode())), new Object[] { "" }, null), "pincode");
                        errorInfoDtoList.add(errorInfoDto);
                    }
                    validateMandatoryField("hubId", hubAllocation.getHubId(), errorInfoDtoList);
                    cacheObject = validateFromCache(String.valueOf(hubAllocation.getHubId()), Constants.HUB_MASTER_CACHE);

                    if (cacheObject == null) {
                        ErrorInfoDto errorInfoDto = new ErrorInfoDto(10, messageSource.getMessage((String.valueOf(ErrorCodes.INVALID_HUB_ID.getErrorCode())), new Object[] { "" }, null), "hubId");
                        errorInfoDtoList.add(errorInfoDto);
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
}
