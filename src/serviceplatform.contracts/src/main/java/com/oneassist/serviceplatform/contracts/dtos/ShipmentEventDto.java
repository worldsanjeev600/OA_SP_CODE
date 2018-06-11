package com.oneassist.serviceplatform.contracts.dtos;

import java.io.Serializable;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchResultDto;
import com.oneassist.serviceplatform.externalcontracts.ClaimEventDto;

public class ShipmentEventDto extends ClaimEventDto implements Serializable {

    private static final long serialVersionUID = -197957783519609607L;

    private long shipmentId;

    private ShipmentSearchResultDto shipmentDetails;

    public long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentSearchResultDto getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(ShipmentSearchResultDto shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

	@Override
	public String toString() {
		return "ShipmentEventDto [shipmentId=" + shipmentId + ", shipmentDetails=" + shipmentDetails + "]";
	}
    
}
