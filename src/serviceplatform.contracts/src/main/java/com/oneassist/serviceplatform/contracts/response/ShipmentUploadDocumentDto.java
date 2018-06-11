package com.oneassist.serviceplatform.contracts.response;

import java.io.Serializable;

public class ShipmentUploadDocumentDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mongoRefId;
	private String shipmentId;
	
	public String getShipmentId() {
		return shipmentId;
	}
	
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	public String getMongoRefId() {
		return mongoRefId;
	}
	
	public void setMongoRefId(String mongoRefId) {
		this.mongoRefId = mongoRefId;
	}

	@Override
	public String toString() {
		return "ShipmentUploadDocumentDto [mongoRefId=" + mongoRefId + ", shipmentId=" + shipmentId + "]";
	}
	
}
