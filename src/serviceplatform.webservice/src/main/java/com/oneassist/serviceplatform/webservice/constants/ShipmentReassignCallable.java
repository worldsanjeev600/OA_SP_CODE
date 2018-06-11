package com.oneassist.serviceplatform.webservice.constants;

import java.util.concurrent.Callable;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ReassignDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.shipment.IShipmentService;
import com.oneassist.serviceplatform.webservice.resources.ShipmentResource;
import org.apache.log4j.Logger;

public class ShipmentReassignCallable implements Callable<ResponseDto<ReassignDto>> {

    private final Logger logger = Logger.getLogger(ShipmentResource.class);

    private IShipmentService logisticService;

    private ShipmentReassignRequestDto obj;

    private ResponseDto<ReassignDto> response = new ResponseDto<>();

    public void setILogistic(IShipmentService logisticService) {
        this.logisticService = logisticService;
    }

    public void setList(ShipmentReassignRequestDto l) {
        this.obj = l;
    }

    @Override
    public ResponseDto<ReassignDto> call() throws Exception {

        try {
            response = logisticService.reassignShipment(obj.getShipmentId(), obj);
            logger.info(">>> LogisticResource reassign Shipment API >>-----Name Of Thread----" + Thread.currentThread().getName());

        } catch (Exception e) {
            logger.error(" LogisticResource reassign Shipment API ERROR :", e);
        }

        return response;
    }
}
