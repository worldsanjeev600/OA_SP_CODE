package com.oneassist.serviceplatform.commons.enums;

public enum ServiceReqeustEntityDocumentName {

	ASSET("asset");
	
	private final String serviceRequestEntityDocumentName;

	ServiceReqeustEntityDocumentName(String serviceRequestEntityDocumentName) {

		this.serviceRequestEntityDocumentName = serviceRequestEntityDocumentName;
	}

	public String getServiceRequestEntityDocumentName() {

		return this.serviceRequestEntityDocumentName;
	}

	public static ServiceReqeustEntityDocumentName getServiceRequestEntityDocumentName(String serviceRequestEntityDocumentName) {
		for (ServiceReqeustEntityDocumentName enumRequestDocument : ServiceReqeustEntityDocumentName.values()) {
			if (enumRequestDocument.getServiceRequestEntityDocumentName().equalsIgnoreCase(serviceRequestEntityDocumentName)) {
				return enumRequestDocument;
			}
		}
		return null;
	}
}