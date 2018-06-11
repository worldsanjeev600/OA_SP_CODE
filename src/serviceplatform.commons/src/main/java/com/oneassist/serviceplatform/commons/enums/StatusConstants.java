package com.oneassist.serviceplatform.commons.enums;

/**
 * 
 */
public enum StatusConstants {

	ACTIVE(1), 
	DEACTIVE(0);

	private final int	status;

	private StatusConstants(int status) {

		this.status = status;
	}

	public int getStatus() {

		return this.status;
	}
}