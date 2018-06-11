package com.oneassist.serviceplatform.commons.enums;

public enum Recipient {

	CUSTOMER("Customer"), 
	SERVICEPARTNER("ServiceCenter"),
	TECHNICIAN("Technician");
	
	private final String recipientType;

	Recipient(String recipientType) {

		this.recipientType = recipientType;
	}

	public String getRecipientType() {

		return this.recipientType;
	}

	public static Recipient getRecipient(String recipientType) {
		for (Recipient recipient : Recipient.values()) {
			if (recipient.getRecipientType().equalsIgnoreCase(recipientType)) {
				return recipient;
			}
		}
		
		return null;
	}
}