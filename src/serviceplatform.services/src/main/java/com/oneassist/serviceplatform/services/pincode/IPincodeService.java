package com.oneassist.serviceplatform.services.pincode;

import java.util.List;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.PincodeServicabilityResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;


public interface IPincodeService {

	public PincodeServicabilityResponse checkPincodeServicabilty(String pinCode, String serviceRequestType) throws BusinessServiceException;

	public List<String> getPincodesByRequestType(String serviceRequestType) throws BusinessServiceException;
}
