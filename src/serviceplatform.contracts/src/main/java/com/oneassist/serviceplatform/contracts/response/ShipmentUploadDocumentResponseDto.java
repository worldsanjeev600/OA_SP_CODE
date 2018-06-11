package com.oneassist.serviceplatform.contracts.response;

public class ShipmentUploadDocumentResponseDto {
	
	private ShipmentUploadDocumentDto data;

	public ShipmentUploadDocumentDto getData() {
		return data;
	}

	public void setData(ShipmentUploadDocumentDto data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ShipmentUploadDocumentResponseDto [data=" + data + "]";
	}
	
}
