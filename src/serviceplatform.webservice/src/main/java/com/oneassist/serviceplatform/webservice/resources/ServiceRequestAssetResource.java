package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestAssetResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceRequestAssetListResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceRequestAssetResponse;
import com.oneassist.serviceplatform.services.asset.IServiceRequestAssetService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service Request Asset Resource
 * 
 * @author alok.singh
 */
@Path("/servicerequests")
@Component
@Api(tags = { "/assets : All About Managing Service Request Asset Information" })
public class ServiceRequestAssetResource extends BaseResource {

    @Autowired
    private IServiceRequestAssetService serviceRequestAssetService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/assets/{assetId}")
    @ApiOperation(value = "Provides Asset Information for the assetId")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceRequestAssetResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getServiceRequestAssetDetails(@PathParam("assetId") @ApiParam(value = "Asset Id", required = true) String assetId) throws BusinessServiceException {
        ResponseDto<List<ServiceRequestAssetResponseDto>> entityResponse = new ResponseDto<>();
        ServiceRequestAssetRequestDto serviceRequestAssetRequestDto = new ServiceRequestAssetRequestDto();
        try {
            serviceRequestAssetRequestDto.setAssetId(assetId);

            entityResponse.setData(serviceRequestAssetService.getServiceRequestAssetDetails(serviceRequestAssetRequestDto));

            entityResponse.setStatus(ResponseConstant.SUCCESS);
            entityResponse.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestAssetResponseCodes.DELETE_SERVICE_REQUEST_ASSET_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(entityResponse).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/assets")
    @ApiOperation(value = "Provides Asset Information for Service Request")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceRequestAssetListResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response searchServiceRequestAssetDetails(@QueryParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId) throws BusinessServiceException {
        ResponseDto<List<ServiceRequestAssetResponseDto>> entityResponse = new ResponseDto<>();
        try {
            ServiceRequestAssetRequestDto serviceRequestAssetRequestDto = new ServiceRequestAssetRequestDto();
            serviceRequestAssetRequestDto.setServiceRequestId(serviceRequestId);

            entityResponse.setData(serviceRequestAssetService.getServiceRequestAssetDetails(serviceRequestAssetRequestDto));

            entityResponse.setStatus(ResponseConstant.SUCCESS);
            entityResponse.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestAssetResponseCodes.DELETE_SERVICE_REQUEST_ASSET_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(entityResponse).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{serviceRequestId}/assets")
    @ApiOperation(value = "Used to add Asset Information")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceRequestAssetResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response createServiceRequestAsset(@PathParam("serviceRequestId") @ApiParam(value = "Service Request Id", required = true) Long serviceRequestId,
            ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) throws BusinessServiceException {
        ResponseDto<ServiceRequestAssetResponseDto> response = new ResponseDto<>();

        try {
            serviceRequestAssetRequestDto.setServiceRequestId(serviceRequestId);
            ServiceRequestAssetResponseDto data = serviceRequestAssetService.createServiceRequestAsset(serviceRequestAssetRequestDto);
            response.setData(data);
            this.setSuccessStatus(response, ServiceRequestAssetResponseCodes.CREATE_SERVICE_REQUEST_ASSET_SUCCESS.getErrorCode(), new Object[] { data.getAssetId().toString() });
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestAssetResponseCodes.CREATE_SERVICE_REQUEST_ASSET_FAILED.getErrorCode(), ex);
        }
        return Response.status(Status.OK).entity(response).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/assets/{assetId}")
    @ApiOperation(value = "Used to update Asset Information based on Asset Id")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceRequestAssetResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response updateServiceRequestAsset(@PathParam("assetId") @ApiParam(value = "Asset Id", required = true) String assetId, ServiceRequestAssetRequestDto serviceRequestAssetRequestDto)
            throws BusinessServiceException {
        ResponseDto<ServiceRequestAssetResponseDto> response = new ResponseDto<>();

        try {
            serviceRequestAssetRequestDto.setAssetId(assetId);
            ServiceRequestAssetResponseDto data = serviceRequestAssetService.updateServiceRequestAsset(serviceRequestAssetRequestDto);
            response.setData(data);
            this.setSuccessStatus(response, ServiceRequestAssetResponseCodes.UPDATE_SERVICE_REQUEST_ASSET_SUCCESS.getErrorCode(), new Object[] { data.getAssetId().toString() });
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestAssetResponseCodes.UPDATE_SERVICE_REQUEST_ASSET_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/assets/{assetId}")
    @ApiOperation(value = "Used to delete Asset Information based on Asset Id")
    @ApiResponses({ @ApiResponse(code = 200, response = BaseResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response deleteServiceRequest(@PathParam("assetId") @ApiParam(value = "Asset Id", required = true) String assetId) throws BusinessServiceException {
        ResponseDto<ServiceRequestAssetResponseDto> response = new ResponseDto<>();

        try {
            ServiceRequestAssetRequestDto serviceRequestAssetRequestDto = new ServiceRequestAssetRequestDto();
            serviceRequestAssetRequestDto.setAssetId(assetId);
            serviceRequestAssetService.deleteServiceRequestAsset(serviceRequestAssetRequestDto);

            this.setSuccessStatus(response, ServiceRequestAssetResponseCodes.DELETE_SERVICE_REQUEST_ASSET_SUCCESS.getErrorCode(), new Object[] { String.valueOf(assetId) });
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestAssetResponseCodes.DELETE_SERVICE_REQUEST_ASSET_FAILED.getErrorCode(), ex);
        }
        return Response.status(Status.OK).entity(response).build();
    }
}
