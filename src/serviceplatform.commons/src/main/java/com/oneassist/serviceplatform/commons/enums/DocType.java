package com.oneassist.serviceplatform.commons.enums;

public enum DocType {

	SHIPMENTPROOFOFDELIVERY("SHIPMENT_PROOF_OF_DELIVERY"), 
	SHIPMENTlABEL("SHIPMENT_LABEL");
	
	private final String docType;

	DocType(String docType) {

		this.docType = docType;
	}

	public String getDocType() {

		return this.docType;
	}

	public static DocType getDocType(String docType) {
		for (DocType enumstatus : DocType.values()) {
			if (enumstatus.getDocType().equalsIgnoreCase(docType)) {
				return enumstatus;
			}
		}
		
		return null;
	}
}