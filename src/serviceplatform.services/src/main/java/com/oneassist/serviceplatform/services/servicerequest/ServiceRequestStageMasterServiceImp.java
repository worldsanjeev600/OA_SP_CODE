package com.oneassist.serviceplatform.services.servicerequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestStageMasterCache;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;

/**
 * Service request stage master service
 * 
 * @author sanjeev.gupta
 *
 */
@Service
public class ServiceRequestStageMasterServiceImp implements IServiceRequestStageMasterService {

	private final Logger logger = Logger.getLogger(ServiceRequestStageMasterServiceImp.class);

	@Autowired
	protected ServiceRequestStageMasterCache serviceRequestStageMasterCache;

	/**
	 * Get all service request stage master data
	 */
	@Override
	public ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> getAll() throws Exception {

		logger.info("Start : service class");
		ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> response = null;

		Map<String, List<ServiceRequestStageMstEntity>> responseData = serviceRequestStageMasterCache.getAll();
		if (null != responseData && !responseData.isEmpty()) {

			response = new ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>>();
			response.setStatus(ResponseConstant.SUCCESS);
			response.setData(responseData);
			response.setMessage("Total #" + responseData.size() + " service task(s) fetched successfully");

			/*
			 * if list of entities are null or empty then set response data null
			 */
		} else {
			response = new ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>>();
			response.setStatus(ResponseConstant.FAILED);
			response.setMessage("No data found");
		}

		logger.info("End : service class");
		return response;
	}

	/**
	 * Get all using search criteria
	 */
	@Override
	public ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> getAllBySearchCriteria(
			String serviceRequestTypeIds) throws Exception {

		logger.info("Start : service class");
		logger.info("start getting all service request stages for service request type IDs: " + serviceRequestTypeIds);

		Map<String, List<ServiceRequestStageMstEntity>> servicRequestStageMstEntities = new HashMap<String, List<ServiceRequestStageMstEntity>>();
		;

		ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> response = null;

		/* Add service request types IDs in List of String */
		List<String> serviceRequestTypeIdList = Arrays.asList(serviceRequestTypeIds.split(","));

		for (String serviceRequestTypeId : serviceRequestTypeIdList) {
			if (serviceRequestStageMasterCache.get(serviceRequestTypeId) != null)
				servicRequestStageMstEntities.put(serviceRequestTypeId,
						serviceRequestStageMasterCache.get(serviceRequestTypeId));
		}

		/*
		 * Check if list of entities are not null then mapped entities data in
		 * response dto
		 */
		if (null != servicRequestStageMstEntities && !servicRequestStageMstEntities.isEmpty()) {

			response = new ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>>();
			response.setStatus(ResponseConstant.SUCCESS);
			response.setData(servicRequestStageMstEntities);
			response.setMessage(
					"Total #" + servicRequestStageMstEntities.size() + " service task(s) fetched successfully");

			/*
			 * if list of entities are null or empty then set response data null
			 */
		} else {
			response = new ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>>();
			response.setStatus(ResponseConstant.FAILED);
			response.setMessage("No data found");
		}

		logger.info("End : service class");
		return response;

	}

}
