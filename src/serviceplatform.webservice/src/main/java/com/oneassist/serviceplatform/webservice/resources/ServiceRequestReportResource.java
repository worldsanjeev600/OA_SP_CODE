package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DashboardDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardInfoDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.DashBoardRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.servicerequest.SwaggerServiceConfigDetailResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentDashboardResponse;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/servicerequests/reports")
@Component
@Api(tags = { "/reports : All About Getting Service Request Reports" })
public class ServiceRequestReportResource extends BaseResource {

    @Autowired
    private IServiceRequestReportService serviceRequestReportService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/dashboard")
    @ApiOperation(value = "Provides count of Service Requests for each Workflow Stages")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerServiceConfigDetailResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getDashboardCountDetails() throws RestResourceException {

        ResponseDto<DashboardDto> response = new ResponseDto<>();

        try {
            response.setData(serviceRequestReportService.getDashboardCountDetails());

            this.setSuccessStatus(response, ServiceRequestResponseCodes.CMS_DASHBOARD_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(ServiceRequestResponseCodes.CMS_DASHBOARD_FAILURE.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Path("/shipments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides the Dashboard Data of shipments")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentDashboardResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getDashboardInfo(@QueryParam("status") @ApiParam(value = "Shipment Status", allowableValues = "P,C,U,R,X", required = true) String shipmentStatus,
            @QueryParam("toDate") @ApiParam(value = "shipment Date", required = true, example = "01-Jan-2017") String shipmentModifiedDate) throws RestResourceException {

        DashBoardRequestDto dashBoardRequestDto = new DashBoardRequestDto();
        dashBoardRequestDto.setShipmentStatus(shipmentStatus);
        dashBoardRequestDto.setShipmentModifiedDate(shipmentModifiedDate);

        ResponseDto<DashBoardInfoDto> response = new ResponseDto<DashBoardInfoDto>();

        try {
            response.setData(serviceRequestReportService.getDashBoardInfo(dashBoardRequestDto));

            this.setSuccessStatus(response, LogisticResponseCodes.DASHDOARD_DATA_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.DASHDOARD_DATA_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
