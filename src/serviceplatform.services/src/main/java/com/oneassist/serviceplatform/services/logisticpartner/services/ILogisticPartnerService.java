package com.oneassist.serviceplatform.services.logisticpartner.services;

import java.util.HashMap;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;

@SuppressWarnings("rawtypes")
public interface ILogisticPartnerService {

    public Object createShipment(ShipmentRequestDto shipmentRequest) throws Exception;

    public Object trackShipment(HashMap trackingPayload) throws Exception;

    public Object cancelShipment(String logistincPartnerTrackingNumber) throws Exception;

}
