package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.PincodeServicabilityResponse;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerPincodeServicabilityResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerPincodesResponse;
import com.oneassist.serviceplatform.services.pincode.IPincodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/pincodes")
@Component
@Api(tags = { "/pincodes : All About Pincodes Configuration" })
public class PincodesResource extends BaseResource {

    @Autowired
    private IPincodeService pincodeService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pincode}")
    @ApiOperation(value = "Provides a convenience to check whether a service centre exists for the given Pincode")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerPincodeServicabilityResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response pincodeServicabilty(@PathParam("pincode") @ApiParam(value = "pincode", required = true) String pincode,
            @QueryParam("serviceRequestType") @ApiParam(value = "serviceRequestType", required = false) String serviceRequestType,
            @QueryParam("make") @ApiParam(value = "make", required = false) String make) throws RestResourceException {

        ResponseDto<PincodeServicabilityResponse> response = new ResponseDto<PincodeServicabilityResponse>();

        try {
            PincodeServicabilityResponse data = pincodeService.checkPincodeServicabilty(pincode, serviceRequestType);
            response.setData(data);

            if (data.getIsPincodeServicable() == Constants.YES_FLAG) {
                this.setSuccessStatus(response, ServiceRequestResponseCodes.PINCODE_SERVICABLE.getErrorCode());
            } else {
                this.setSuccessStatus(response, ServiceRequestResponseCodes.PINCODE_NOT_SERVICABLE.getErrorCode());
            }
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.PINCODE_SEARCH_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns the list of pincodes for the given service request type")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerPincodesResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getPincodesByRequestType(@QueryParam("serviceRequestType") @ApiParam(value = "serviceRequestType", required = true) String serviceRequestType) throws RestResourceException {

        ResponseDto<List<String>> response = new ResponseDto<>();

        try {

            response.setData(pincodeService.getPincodesByRequestType(serviceRequestType));

            this.setSuccessStatus(response, ServiceRequestResponseCodes.PINCODE_SEARCH_SUCCESS.getErrorCode(), new Object[] { serviceRequestType });
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.PINCODE_SEARCH_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
