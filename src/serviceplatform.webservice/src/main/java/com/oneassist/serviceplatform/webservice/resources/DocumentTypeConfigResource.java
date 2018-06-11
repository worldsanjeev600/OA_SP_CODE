package com.oneassist.serviceplatform.webservice.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentTypeConfigDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.DocumentTypeConfigResponse;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqDetailResponse;
import com.oneassist.serviceplatform.services.servicerequest.IDocumentTypeConfigService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Document Type Config Resource Class
 * 
 * @author sanjeev.gupta
 *
 */

@Path("/servicedocumenttypes")
public class DocumentTypeConfigResource {

	private final Logger logger = Logger.getLogger(ServiceRequestStageMasterResource.class);

	@Autowired
	private IDocumentTypeConfigService documentTypeConfigService;

	@Path("/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get all document type config master data ")
	@ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqDetailResponse.class, message = "Success"),
			@ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
	public Response getAllBySearchCriteria(@QueryParam("serviceRequestTypeId") String serviceRequestTypeId,
			@QueryParam("referenceCode") String referenceCode,
			@QueryParam("insurancePartnerCode") Long insurancePartnerCode,
			@QueryParam("isMandatory") String isMandatory) throws RestResourceException {

		logger.info("Start getting all service request stages: Resource class");
		ResponseDto<List<DocumentTypeConfigResponse>> response = new ResponseDto<>();

		try {
			DocumentTypeConfigDto documentTypeConfigDto = new DocumentTypeConfigDto();
			documentTypeConfigDto.setServiceRequestTypeId(serviceRequestTypeId);
			documentTypeConfigDto.setReferenceCode(referenceCode);
			documentTypeConfigDto.setInsurancePartnerCode(insurancePartnerCode);
			documentTypeConfigDto.setIsMandatory(isMandatory);

			response = documentTypeConfigService.getAllBySearchCriteria(documentTypeConfigDto);

		} catch (Exception exception) {
			logger.error("Exception", exception);

			throw new RestResourceException(ServiceRequestResponseCodes.CREATE_SERVICE_REQUEST_FAILED.getErrorCode(),
					exception);
		}

		return Response.status(Status.OK).entity(response).build();
	}

}
