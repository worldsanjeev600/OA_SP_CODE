package com.oneassist.serviceplatform.services.servicerequest;

import java.util.List;
import java.util.Map;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;


/**
 * Service request stage master service
 * 
 * @author sanjeev.gupta
 *
 */
public interface IServiceRequestStageMasterService {

	public ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> getAll() throws Exception;

	public ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> getAllBySearchCriteria(
			String serviceRequestTypeIds) throws Exception;

}
