package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.enums.GenericResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;

@Component
public class ServiceRequestAssetValidator extends InputValidator {
	private static final Logger logger = Logger.getLogger(ServiceRequestAssetValidator.class);

	public  List<ErrorInfoDto> doValidateServiceAssetRequest(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for Mandatory Data Service Asset Request :" + serviceRequestAssetRequestDto);
		return validateCommonFields(serviceRequestAssetRequestDto);	
	}

	private List<ErrorInfoDto> validateCommonFields(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for Common Fields :" + serviceRequestAssetRequestDto);
		List<ErrorInfoDto>	errorInfoDtoList	= new ArrayList<>();

		if (serviceRequestAssetRequestDto.getServiceRequestId() == null && serviceRequestAssetRequestDto.getAssetId() == null){
			validateSearchFilterCriteria(errorInfoDtoList);
		}
		return errorInfoDtoList;
	}

	public  List<ErrorInfoDto> doValidateCreateServiceAssetRequest(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for Mandatory Data of Create Service Asset Request :" + serviceRequestAssetRequestDto);
		return validateCommonFieldsForCreateServiceAsset(serviceRequestAssetRequestDto);	
	}

	private List<ErrorInfoDto> validateCommonFieldsForCreateServiceAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for create Common Fields :" + serviceRequestAssetRequestDto);
		List<ErrorInfoDto>	errorInfoDtoList	= new ArrayList<>();

		try {
			validateMandatoryField("serviceRequestId", serviceRequestAssetRequestDto.getServiceRequestId(), errorInfoDtoList);
			validateMandatoryField("productCode", serviceRequestAssetRequestDto.getProductCode(), errorInfoDtoList);
			validateMandatoryField("createdBy", serviceRequestAssetRequestDto.getCreatedBy(), errorInfoDtoList);

			validateRequiredField("make", serviceRequestAssetRequestDto.getMake(), errorInfoDtoList);
			validateRequiredField("serialNo", serviceRequestAssetRequestDto.getSerialNo(), errorInfoDtoList);
			validateRequiredField("modelNo", serviceRequestAssetRequestDto.getModelNo(), errorInfoDtoList);
			validateRequiredField("assetAge", serviceRequestAssetRequestDto.getAssetAge(), errorInfoDtoList);
		} catch( InputValidationException exception) {
			logger.info("-- Create Service Request Asset request validation failed --", exception);
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, exception);
		}

		return errorInfoDtoList;
	}

	public  List<ErrorInfoDto> doValidateUpdateServiceAssetRequest(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for Mandatory Data of Create Service Asset Request :" + serviceRequestAssetRequestDto);
		return validateCommonFieldsForUpdateServiceAsset(serviceRequestAssetRequestDto);	
	}

	private List<ErrorInfoDto> validateCommonFieldsForUpdateServiceAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		logger.info(">>> Validation for update Common Fields :" + serviceRequestAssetRequestDto);
		List<ErrorInfoDto>	errorInfoDtoList	= new ArrayList<>();

		try {
			validateMandatoryField("assetId", serviceRequestAssetRequestDto.getAssetId(), errorInfoDtoList);
			validateMandatoryField("modifiedBy", serviceRequestAssetRequestDto.getModifiedBy(), errorInfoDtoList);
			validateRequiredField("make", serviceRequestAssetRequestDto.getMake(), errorInfoDtoList);
			validateRequiredField("serialNo", serviceRequestAssetRequestDto.getSerialNo(), errorInfoDtoList);
			validateRequiredField("modelNo", serviceRequestAssetRequestDto.getModelNo(), errorInfoDtoList);
			validateRequiredField("assetAge", serviceRequestAssetRequestDto.getAssetAge(), errorInfoDtoList);
		} catch( InputValidationException exception) {
			logger.info("-- Update Service Request Asset request validation failed --", exception);
			throw new BusinessServiceException(GenericResponseCodes.VALIDATION_FAIL.getErrorCode(), errorInfoDtoList, exception);
		}

		return errorInfoDtoList;
	}

}
