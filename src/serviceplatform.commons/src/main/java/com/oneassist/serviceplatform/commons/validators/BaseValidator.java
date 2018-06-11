
package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;

@Component
public class BaseValidator {

	@Autowired
	protected MessageSource			messageSource;

	protected List<ErrorInfoDto>	errorInfoDtoList	= new ArrayList<ErrorInfoDto>();

	protected ErrorInfoDto			errorInfoDto;

}