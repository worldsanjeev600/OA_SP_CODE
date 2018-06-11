package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchReasultDto;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerHubAllocationResponse;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.hub.IHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Path("/hubs")
@Component
@Api(tags = { "/hubs : All About hubs Configuration" })
public class HubsResource extends BaseResource {

    @Autowired
    private IHubService hubService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    @ApiOperation(value = "Returns the HubId for the given Pincode and Make")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerHubAllocationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getHubByPincodeAndMake(@QueryParam("pincode") @ApiParam(value = "Pincode", required = true) String pincode,
            @QueryParam("make") @ApiParam(value = "make", required = false) String make) throws BusinessServiceException {

        ResponseDto<HubAllocationDto> response = new ResponseDto<>();
        try {
            HubAllocationDto hubAllocationRequestDto = new HubAllocationDto();
            hubAllocationRequestDto.setPincode(pincode);
            hubAllocationRequestDto.setMake(make);

            response.setData(hubService.getHubByPincodeAndMake(hubAllocationRequestDto));

            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Modifies the service hub allocation master")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerHubAllocationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response modifyServiceHubAllocation(HubAllocationRequestDto hubAllocationRequest) throws BusinessServiceException {

        ResponseDto<Object> response = new ResponseDto<Object>();
        try {
            hubService.editHubAllocation(hubAllocationRequest);

            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns Search result by city,state,pincode and hub")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerHubAllocationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response search(@QueryParam("pincodes") @ApiParam(value = "Pincodes") String pincodes, @QueryParam("states") @ApiParam(value = "states", required = false) String states,
            @QueryParam("cities") @ApiParam(value = "cities", required = false) String cities, @QueryParam("hubIds") @ApiParam(value = "hubIds", required = false) String hubIds,
            @QueryParam("page") Integer page, @QueryParam("size") Integer size) throws BusinessServiceException {

        ResponseDto<List<HubAllocationSearchReasultDto>> response = new ResponseDto<List<HubAllocationSearchReasultDto>>();
        try {
            HubAllocationSearchRequestDto hubAllocationSearchRequestDto = new HubAllocationSearchRequestDto();
            if (!StringUtils.isEmpty(hubIds)) {
                hubAllocationSearchRequestDto.setHubIds(Arrays.asList(hubIds.split(",")));
            }
            if (!StringUtils.isEmpty(states)) {
                hubAllocationSearchRequestDto.setStates(Arrays.asList(states.split(",")));
            }
            if (!StringUtils.isEmpty(cities)) {
                hubAllocationSearchRequestDto.setCities(Arrays.asList(cities.split(",")));
            }
            if (!StringUtils.isEmpty(pincodes)) {
                hubAllocationSearchRequestDto.setPincodes(Arrays.asList(pincodes.split(",")));
            }

            PageRequest pageRequest = null;
            if (page != null && page > 0 && size != null && size > 0) {
                page = page - 1;
                pageRequest = new PageRequest(page, size);
            }
            response = hubService.getHubAllocation(hubAllocationSearchRequestDto, pageRequest);

            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
