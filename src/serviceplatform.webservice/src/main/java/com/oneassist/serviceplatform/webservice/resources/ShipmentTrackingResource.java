package com.oneassist.serviceplatform.webservice.resources;

import java.util.HashMap;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.oneassist.serviceplatform.commons.enums.LogisticResponseCodes;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentTrackingResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.shipment.IShipmentTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/shipments/tracking")
@Component
@SuppressWarnings("rawtypes")
public class ShipmentTrackingResource extends BaseResource {

    @Autowired
    private IShipmentTrackingService shipmentTrackingService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shipmentStatusCallback(HashMap trackingRequest, @QueryParam("partnerCode") String partnerCode) throws RestResourceException {
        Object response = new Object();

        try {

            response = shipmentTrackingService.trackShipment(trackingRequest, partnerCode);

        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{shipmentId}")
    public Response trackingHistory(@PathParam("shipmentId") Long shipmentId) throws RestResourceException {

        ResponseDto<List<ShipmentTrackingResponseDto>> response = new ResponseDto<List<ShipmentTrackingResponseDto>>();

        try {

            response = shipmentTrackingService.getShipmentTrackingHistory(shipmentId);

            this.setSuccessStatus(response, LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_SUCCESS.getErrorCode());
        } catch (Exception ex) {
            throw new RestResourceException(LogisticResponseCodes.SHIPMENT_TRACKINGHISTORY_FAILED.getErrorCode(), ex);
        }

        return Response.status(Status.OK).entity(response).build();
    }
}
