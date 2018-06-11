package com.oneassist.serviceplatform.services.generickeyset;

import java.util.List;

import org.springframework.context.NoSuchMessageException;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

public interface IGenericKeySetService {
	
	public List<GenericKeySetDetailDto> getGenericKeySetDetail(GenericKeySetRequestDto genericKeySetRequestDto) 
			throws BusinessServiceException;	
}
