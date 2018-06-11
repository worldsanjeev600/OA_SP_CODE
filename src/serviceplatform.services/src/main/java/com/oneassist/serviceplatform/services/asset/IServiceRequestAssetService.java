package com.oneassist.serviceplatform.services.asset;

import java.util.List;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;

public interface IServiceRequestAssetService {
	
	public List<ServiceRequestAssetResponseDto> getServiceRequestAssetDetails(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) 
			throws BusinessServiceException;


	ServiceRequestAssetResponseDto createServiceRequestAsset(ServiceRequestAssetRequestDto serviceRequestAssetRequestDto)
			throws BusinessServiceException, JsonProcessingException;


	public ServiceRequestAssetResponseDto updateServiceRequestAsset(
			ServiceRequestAssetRequestDto serviceRequestAssetRequestDto);


	public void deleteServiceRequestAsset(
			ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException, ParseException;

	
}
