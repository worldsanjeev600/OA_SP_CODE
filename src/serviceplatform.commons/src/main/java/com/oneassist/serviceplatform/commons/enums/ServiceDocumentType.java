package com.oneassist.serviceplatform.commons.enums;

public enum ServiceDocumentType {

	POST_REPAIR_IMAGE("POST_REPAIR_IMAGE"), 
	ESTIMATE_INVOICE("ESTIMATE_INVOICE"),
	PRE_REPAIR_IMAGE("PRE_REPAIR_IMAGE"), 
	DEVICE_UNIT_LABEL("DEVICE_UNIT_LABEL"),
	DEVICE_FRONT_IMAGE("DEVICE_FRONT_IMAGE"), 
	DAMAGED_SPARE_PART("DAMAGED_SPARE_PART"),
	SHIPMENT_LABEL("SHIPMENT_LABEL"),
	SHIPMENT_PROOF_OF_DELIVERY("SHIPMENT_PROOF_OF_DELIVERY");
	
	private final String serviceRequestDocument;

	ServiceDocumentType(String serviceRequestDocument) {

		this.serviceRequestDocument = serviceRequestDocument;
	}

	public String getServiceDocumentType() {

		return this.serviceRequestDocument;
	}

	public static ServiceDocumentType getServiceDocumentType(String serviceRequestDocument) {
		for (ServiceDocumentType enumRequestDocument : ServiceDocumentType.values()) {
			if (enumRequestDocument.getServiceDocumentType().equalsIgnoreCase(serviceRequestDocument)) {
				return enumRequestDocument;
			}
		}
		return null;
	}
}