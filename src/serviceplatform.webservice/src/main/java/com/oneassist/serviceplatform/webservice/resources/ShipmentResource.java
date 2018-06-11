package com.oneassist.serviceplatform.webservice.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.ws.rs.Consumes;
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
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.commons.utils.DateUtils;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ReassignDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignFailDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignRequestDtoWrapper;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchViewResultDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentStatusDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.BaseResponse;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentBulkStatusUpdateResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentCreationResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentReassignResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentSearchResponse;
import com.oneassist.serviceplatform.contracts.response.swagger.shipment.SwaggerShipmentUpdationResponse;
import com.oneassist.serviceplatform.services.shipment.IShipmentService;
import com.oneassist.serviceplatform.webservice.constants.ShipmentReassignCallable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LogisticResource Resource This class will manage all the shipment related web services
 * 
 * @author satish.kumar
 */
@Path("/shipments")
@Component
@Api(tags = { "/shipments : All About Managing Shipment Information" })
public class ShipmentResource extends BaseResource {

    private final Logger logger = Logger.getLogger(ShipmentResource.class);

    @Autowired
    private IShipmentService shipmentService;

    /**
     * 
     * @param shipmentRequest
     * @return
     * @throws RestResourceException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides support for creating a Shipment")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentCreationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response createShipment(ShipmentRequestDto shipmentRequest) throws RestResourceException {
        ResponseDto<ShipmentRequestDto> response = new ResponseDto<ShipmentRequestDto>();

        try {

            response = shipmentService.createShipment(shipmentRequest);
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.RAISE_SHIPMENT_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method is used to update shipment
     * 
     * @param shipmentId
     * @param updateRequestDto
     * @return
     * @throws RestResourceException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{shipmentId}/{fieldtoupdate}")
    @ApiOperation(value = "Provides support for updating a shipment")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentUpdationResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response updateShipment(@PathParam("shipmentId") Long shipmentId, @PathParam("fieldtoupdate") String fieldtoupdate, ShipmentUpdateRequestDto updateRequestDto) throws RestResourceException {

        ResponseDto<ShipmentUpdateRequestDto> response = null;

        try {

            response = shipmentService.updateShipment(shipmentId, fieldtoupdate, updateRequestDto);

        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.UPDATE_SHIPMENT_REQUEST_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    /**
     * This method will reassign shipment request
     * 
     * @param shipmentRequest
     * @return
     * @throws RestResourceException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reassign")
    @ApiOperation(value = "Provides support for ReAssigning a Shipment to a Logistic Partner")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentReassignResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response reassignShipment(ShipmentReassignRequestDtoWrapper wrapper) throws RestResourceException {
        ResponseDto<ReassignDto> response = new ResponseDto<ReassignDto>();
        ReassignDto reassignDto = new ReassignDto();
        List<ShipmentReassignFailDto> sendList = new ArrayList<ShipmentReassignFailDto>();
        List<Future<ResponseDto<ReassignDto>>> futurelst = new ArrayList<Future<ResponseDto<ReassignDto>>>();
        ExecutorService executors = Executors.newFixedThreadPool(20);

        try {

            HashSet<ShipmentReassignCallable> tasks = new HashSet<ShipmentReassignCallable>();

            for (int i = 0; i < wrapper.getShipmentReassignReq().size(); i++) {

                ShipmentReassignCallable callable = new ShipmentReassignCallable();
                callable.setList(wrapper.getShipmentReassignReq().get(i));
                callable.setILogistic(shipmentService);
                tasks.add(callable);
            }

            futurelst = executors.invokeAll(tasks);
            logger.info(">>> LogisticResource reassign Shipment API >> response err list size" + futurelst.size());

            if (futurelst != null) {
                for (Future<ResponseDto<ReassignDto>> future : futurelst) {
                    logger.info(">>> LogisticResource reassign Shipment API >> Thread Task Is done" + future.isDone());

                    if (future.get().getData().getRecordFailed() != 0) {
                        sendList.addAll(future.get().getData().getFailedList());
                    }
                }
            }

            if (sendList.size() > 0) {
                reassignDto.setFailedList(sendList);
                reassignDto.setRecordFailed(sendList.size());
                response.setData(reassignDto);
                response.setMessage("Shipment reassigned unsucessfully");
                response.setStatus("success");
            } else {
                response.setStatus("success");
                response.setMessage("Shipment reassigned sucessfully");
                reassignDto.setFailedList(new ArrayList<ShipmentReassignFailDto>());
                response.setData(reassignDto);
            }
        } catch (Exception ex) {

            throw new RestResourceException(LogisticResponseCodes.REASSIGN_SHIPMENT_REQUEST_FAILED.getErrorCode(), ex);
        } finally {
            executors.shutdown();
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateBulkShipmentStatus")
    @ApiOperation(value = "Provides a convenience for Updating the Stages of various shipments in bulk")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentBulkStatusUpdateResponse.class, message = "Success"),
            @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response updateBulkShipmentStatus(List<ShipmentStatusDto> shipmentStatusDtos) {

        ResponseDto<List<ShipmentStatusDto>> response = shipmentService.addBulkShipmentStatus(shipmentStatusDtos);

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Provides a convenience of filtering shipments based on a criteria")
    @ApiResponses({ @ApiResponse(code = 200, response = SwaggerShipmentSearchResponse.class, message = "Success"), @ApiResponse(code = 500, response = BaseResponse.class, message = "Failure") })
    public Response getShipmentSearchRecords(@QueryParam("shipmentId") Long shipmentId, @QueryParam("logisticPartnerRefTrackingNumber") String logisticPartnerRefTrackingNumber,
            @QueryParam("serviceId") Long serviceId, @QueryParam("status") String status, @QueryParam("trackingNo") String trackingNo, @QueryParam("logisticPartnerCode") String logisticPartnerCode,
            @QueryParam("hubId") String hubId, @QueryParam("stage") String stage, @QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate) throws RestResourceException {

        ResponseDto<List<ShipmentSearchViewResultDto>> response = new ResponseDto<>();

        try {
            ShipmentSearchRequestDto shipmentSearch = new ShipmentSearchRequestDto();
            shipmentSearch.setShipmentId(shipmentId);
            shipmentSearch.setLogisticPartnerRefTrackingNumber(logisticPartnerRefTrackingNumber);
            shipmentSearch.setServiceId(serviceId);
            shipmentSearch.setStatus(status);
            shipmentSearch.setTrackingNo(trackingNo);
            shipmentSearch.setLogisticPartnerCode(logisticPartnerCode);
            shipmentSearch.setHubId(hubId);
            shipmentSearch.setStage(stage);

            if (fromDate != null) {
                shipmentSearch.setFromDate(DateUtils.fromShortFormattedString(fromDate));
            }

            if (toDate != null) {
                shipmentSearch.setToDate(DateUtils.fromShortFormattedString(toDate));
            }

            response = shipmentService.searchShipmentInfoByNativeQuery(shipmentSearch);
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.SHIPMENT_SEARCH_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
