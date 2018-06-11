package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetRequestDto;

@Component
public class GenericKeySetRequestValidator extends InputValidator {
	private static final Logger logger = Logger.getLogger(GenericKeySetRequestValidator.class);
	
	public  List<ErrorInfoDto> doValidateGenericKeySetRequest(GenericKeySetRequestDto genericKeySetRequestDto) {
		logger.info(">>> Validation for Mandatory Data GenericKeySet Request :" + genericKeySetRequestDto);
		return validateCommonFields(genericKeySetRequestDto);	
	}
	
	private List<ErrorInfoDto> validateCommonFields(GenericKeySetRequestDto genericKeySetRequestDto) {
		logger.info(">>> Validation for Common Fields :" + genericKeySetRequestDto);
		List<ErrorInfoDto>	errorInfoDtoList	= new ArrayList<>();
		
		try {
			
			validateMandatoryField("keySetName", genericKeySetRequestDto.getKeySetName(), errorInfoDtoList);
		} catch( InputValidationException exception) {
			logger.info("-- GenericKeySet request validation failed --", exception);
		}
		
		logger.info("<< Validation for Common Fields");
		
		return errorInfoDtoList;
	}
}
