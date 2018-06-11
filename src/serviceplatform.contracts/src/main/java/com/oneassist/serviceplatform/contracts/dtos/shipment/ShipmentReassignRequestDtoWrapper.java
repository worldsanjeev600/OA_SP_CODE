package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.util.List;

public class ShipmentReassignRequestDtoWrapper {
	private List<ShipmentReassignRequestDto> shipmentReassignReq;

	
	
    public List<ShipmentReassignRequestDto> getShipmentReassignReq() {
		return shipmentReassignReq;
	}

	public void setShipmentReassignReq(List<ShipmentReassignRequestDto> shipmentReassignReq) {
		this.shipmentReassignReq = shipmentReassignReq;
	}

	@Override
	public String toString() {
		return "ShipmentReassignRequestDtoWrapper [shipmentReassignReq=" + shipmentReassignReq + "]";
	}

	
}
