package com.oneassist.serviceplatform.commons.validators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardRequestDto;

@Component
public class DashBoardRequestValidator extends InputValidator {

	public List<ErrorInfoDto> doValidate(DashBoardRequestDto dashBoardRequestDto) throws InputValidationException{
		
		List<ErrorInfoDto> errorInfoList = new ArrayList<>();
		validateRequiredField("shipmentStatus", dashBoardRequestDto.getShipmentStatus(),errorInfoList);
		validateRequiredField("toDate", dashBoardRequestDto.getShipmentModifiedDate(),errorInfoList);
		validateDateFormatAndFutureDate("toDate", dashBoardRequestDto.getShipmentModifiedDate(),"dd-MMM-yyyy",errorInfoList);
		return errorInfoList;
	}	
}