package com.oneassist.serviceplatform.services.shipment;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.NoSuchMessageException;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentTrackingResponseDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

@SuppressWarnings("rawtypes")
public interface IShipmentTrackingService {

    public Object trackShipment(HashMap trackRequest, String partnerCode) throws Exception;

    public ResponseDto<List<ShipmentTrackingResponseDto>> getShipmentTrackingHistory(Long shipmentId) throws NoSuchMessageException, BusinessServiceException;
}
