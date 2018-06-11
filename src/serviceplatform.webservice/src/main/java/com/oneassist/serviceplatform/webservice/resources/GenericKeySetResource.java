package com.oneassist.serviceplatform.webservice.resources;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.generickeyset.GenericKeySetRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceReqValidateAuthCodeResponse;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import com.oneassist.serviceplatform.services.generickeyset.IGenericKeySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generic KeySet Resource
 * 
 * @author surender.jain
 */
@Path("/generickeysets")
@Component
public class GenericKeySetResource extends BaseResource {

    @Autowired
    private IGenericKeySetService genericKeySetService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{keySetName}")
    @ApiOperation(value = "Provides system configuration based on parameters")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceReqValidateAuthCodeResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getGenericKeySetData(@PathParam("keySetName") @ApiParam(value = "key set name", required = true) String keySetName) throws BusinessServiceException {

        ResponseDto<List<GenericKeySetDetailDto>> entityResponse = new ResponseDto<>();
        try {
            GenericKeySetRequestDto genericKeySetRequestDto = new GenericKeySetRequestDto();

            genericKeySetRequestDto.setKeySetName(keySetName);

            entityResponse.setData(genericKeySetService.getGenericKeySetDetail(genericKeySetRequestDto));

            entityResponse.setStatus(ResponseConstant.SUCCESS);
            entityResponse.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(entityResponse).build();
    }
}
