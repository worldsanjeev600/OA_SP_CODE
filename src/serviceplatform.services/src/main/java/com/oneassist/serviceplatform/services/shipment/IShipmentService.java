package com.oneassist.serviceplatform.services.shipment;

import java.io.IOException;
import java.util.List;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ReassignDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentDetailsDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentReassignRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchViewResultDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentStatusDto;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentUpdateRequestDto;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;

public interface IShipmentService {

    public ResponseDto<ShipmentRequestDto> createShipment(ShipmentRequestDto shipmentRequest) throws IOException;

    public ResponseDto<ReassignDto> reassignShipment(Long shipmentId, ShipmentReassignRequestDto shipmentReassignRequestDto);

    public ResponseDto<ShipmentUpdateRequestDto> updateShipment(Long shipmentId, String fieldtoupdate, ShipmentUpdateRequestDto updateRequestDto) throws Exception;

    public ResponseDto<List<ShipmentStatusDto>> addBulkShipmentStatus(List<ShipmentStatusDto> shipmentStatusDtos);

    public ResponseDto<List<ShipmentSearchViewResultDto>> searchShipmentInfoByNativeQuery(ShipmentSearchRequestDto shipmentSearchRequestDto);

    public List<ShipmentDetailsDto> getShipmentsByServiceRequestId(Long serviceRequestId);

    public ShipmentRequestDto getByShipmentId(Long shipmentId);
}
