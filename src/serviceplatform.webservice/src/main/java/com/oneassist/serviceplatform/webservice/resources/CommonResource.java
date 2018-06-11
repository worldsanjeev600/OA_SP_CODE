package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.MasterData;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceScheduleDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceScheduleResponse;
import com.oneassist.serviceplatform.services.commons.ICommonService;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Common resource
 * 
 * @author divya.hl
 */
@Path("/masterData")
@Component
public class CommonResource extends BaseResource {

    @Autowired
    private ICommonService commonService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMastersData(@QueryParam("types") String types) throws RestResourceException {
        ResponseDto<Map<String, Object>> entityResponse = new ResponseDto<>();
        try {
            if (types != null) {
                List<String> dropDownRequest = Arrays.asList(types.split(","));
                Map<String, Object> masterDataResponse = commonService.getAllData(dropDownRequest);
                entityResponse.setData(masterDataResponse);
                entityResponse.setStatus(ResponseConstant.SUCCESS);
                entityResponse.setMessage(ResponseConstant.SUCCESS);
            }
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_DOCUMENT_DELETE_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(entityResponse).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/availableServiceSlots")
    @ApiOperation(value = "Provides list of service slots available")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceScheduleResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getAvailableScheduleSlotsForInspection(@QueryParam("serviceRequestType") @ApiParam(value = "Service Request Type", required = true) String serviceRequestType,
            @QueryParam("serviceRequestDate") @ApiParam(value = "Service Request Date", required = true) String serviceRequestDate) throws RestResourceException {

        ResponseDto<ServiceScheduleDto> response = new ResponseDto<ServiceScheduleDto>();

        try {

            ServiceScheduleDto serviceScheduleDto = new ServiceScheduleDto();
            serviceScheduleDto.setServiceRequestDate(DateUtils.fromShortFormattedString(serviceRequestDate));
            serviceScheduleDto.setServiceRequestType(serviceRequestType);
            response = commonService.getAvailableScheduleSlotsForInspection(serviceScheduleDto);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_SLOT_SCHEDULE_ERROR.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{masterData}")
    @ApiOperation(value = "Provides list of services")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceScheduleResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response searchMasterData(@PathParam("masterData") @ApiParam(value = "", required = true) String masterDataValue,
            @QueryParam("partnerCode") @ApiParam(value = "Service Partner Codes", required = false) String partnerCodes) throws RestResourceException {

        ResponseDto<List<Map<String, Object>>> response = new ResponseDto<List<Map<String, Object>>>();

        try {
            MasterData masterData = MasterData.getMasterData(masterDataValue);
            response.setData(commonService.filterMasterData(masterData, partnerCodes));
            response.setStatus(ResponseConstant.SUCCESS);
            response.setMessage(ResponseConstant.SUCCESS);
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.SERVICE_SLOT_SCHEDULE_ERROR.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clearCache/{cache}")
    public Response clearCache(@PathParam("cache") String cacheName) throws RestResourceException {

        ResponseDto<Object> response = new ResponseDto<Object>();
        commonService.clearCache(cacheName);
        response.setStatus(ResponseConstant.SUCCESS);
        response.setMessage(ResponseConstant.SUCCESS);

        return Response.status(Status.OK).entity(response).build();
    }
}
