package com.oneassist.serviceplatform.contracts.response;

import java.io.Serializable;

public class ShipmentDownloadDocumentResponseDto implements Serializable {
	
	private ShipmentDownloadDocumentDto data;

	public ShipmentDownloadDocumentDto getData() {
		return data;
	}

	public void setData(ShipmentDownloadDocumentDto data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ShipmentDownloadDocumentResponseDto [data=" + data + "]";
	}
	
}