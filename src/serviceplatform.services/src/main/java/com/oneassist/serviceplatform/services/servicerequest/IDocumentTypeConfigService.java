package com.oneassist.serviceplatform.services.servicerequest;

import java.util.List;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentTypeConfigDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.DocumentTypeConfigResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

public interface IDocumentTypeConfigService {

	public ResponseDto<List<DocumentTypeConfigResponse>> getAllBySearchCriteria(
			DocumentTypeConfigDto documentTypeConfigDto) throws BusinessServiceException;

}
