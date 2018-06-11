package com.oneassist.serviceplatform.webservice.resources;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestStageMstEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqDetailResponse;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestStageMasterService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Service request stage master resource Expose APIs for service request stage
 * master data
 * 
 * @author sanjeev.gupta
 *
 */

@Path("/servicerequeststages")
public class ServiceRequestStageMasterResource {

	private final Logger logger = Logger.getLogger(ServiceRequestStageMasterResource.class);

	@Autowired
	private IServiceRequestStageMasterService serviceRequestStageMasterService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all active service request stages in ascending order of serviceRequestType, stageOrder")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqDetailResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response getAll() throws RestResourceException {

		logger.info("Start getting all service request stages: Resource class");
		ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> response = new ResponseDto<>();

		try {
			response = serviceRequestStageMasterService.getAll();

		} catch (Exception exception) {
			logger.error("Exception", exception);

			throw new RestResourceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED.getErrorCode(),
					exception);
		}

		return Response.status(Status.OK).entity(response).build();
	}

	/**
	 * 
	 * @param serviceRequestTypeIds
	 * @return
	 * @throws RestResourceException
	 */
	@Path("/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all active service request stages in ascending order of serviceRequestType, stageOrder")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqDetailResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response getAllBySearchCriteria(
			@QueryParam("serviceRequestTypeIds") @ApiParam(value = "serviceRequestType", required = true) String serviceRequestTypeIds)
			throws RestResourceException {

		logger.info("Start getting all service request stages for serviceRequestTypeIds: Resource class");
		ResponseDto<Map<String, List<ServiceRequestStageMstEntity>>> response = new ResponseDto<>();

		try {
			response = serviceRequestStageMasterService.getAllBySearchCriteria(serviceRequestTypeIds);

		} catch (Exception exception) {
			logger.error("Exception", exception);

			throw new RestResourceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED.getErrorCode(),
					exception);
		}

		return Response.status(Status.OK).entity(response).build();
	}

}
