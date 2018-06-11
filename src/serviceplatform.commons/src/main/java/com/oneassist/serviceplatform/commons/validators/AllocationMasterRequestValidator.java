package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;

/**
 * @author Satish.Kumar
 * This class will validate the Check Pincode Courtesy Request
 */
@Component
public class AllocationMasterRequestValidator extends InputValidator {
	private static final Logger logger = Logger.getLogger(AllocationMasterRequestValidator.class);
	
	private AllocationMasterRequestValidator() {
	}

	private List<ErrorInfoDto> validateCommonFields(String pinCode , Long hubId) {
		logger.info(">>> Validation for Common Fields Check Pincode Courtesy Request :" + " pinCode["+pinCode+"]  hubId["+hubId+"]");

		List<ErrorInfoDto>	errorInfoDtoList = new ArrayList<ErrorInfoDto>();

		try {
			validateRequiredField("pinCode",pinCode,errorInfoDtoList);
			validateFromCache("pinCode", pinCode, Constants.PINCODE_MASTER_CACHE,errorInfoDtoList);
		} catch( InputValidationException exception) {
			logger.info("-- CheckPincodeCourtesyRequest request validation failed --");
		}
		
		return errorInfoDtoList;		
	}	
	
	public  List<ErrorInfoDto> doValidateGetPincodeServiceFulfilmentRequest(String pinCode) {
		logger.info(">>> Validation for Mandatory Data Get Pincode service fulfilments request :" + " pinCode["+pinCode+"]");
		return validateCommonFields(pinCode);
	
	}

	private List<ErrorInfoDto> validateCommonFields(String pinCode) {
		logger.info(">>> Validation for Common Fields  Get Pincode service fulfilments request :" + " pinCode["+pinCode+"]");
		List<ErrorInfoDto>	errorInfoDtoList = new ArrayList<ErrorInfoDto>();
		
		try {
			validateRequiredField("pinCode",pinCode,errorInfoDtoList);
			validateFromCache("pinCode", pinCode, Constants.PINCODE_MASTER_CACHE,errorInfoDtoList);
		
		} catch( InputValidationException exception) {
			logger.info("-- Get Pincode service fulfilments request validation failed --");
		}
		
		return errorInfoDtoList;		
	}
}
