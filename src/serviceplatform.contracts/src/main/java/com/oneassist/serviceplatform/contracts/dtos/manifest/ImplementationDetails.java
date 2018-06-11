package com.oneassist.serviceplatform.contracts.dtos.manifest;

public class ImplementationDetails {
	
	private String implementationTitle;

	private String implementationVersion;
	
	private String implementationVendorId;	
	
	private String currentSystemDateTime;
	
	public String getCurrentSystemDateTime() {
		return currentSystemDateTime;
	}

	public void setCurrentSystemDateTime(String currentSystemDateTime) {
		this.currentSystemDateTime = currentSystemDateTime;
	}

	public String getImplementationTitle() {
		return implementationTitle;
	}

	public void setImplementationTitle(String implementationTitle) {
		this.implementationTitle = implementationTitle;
	}

	public String getImplementationVersion() {
		return implementationVersion;
	}

	public void setImplementationVersion(String implementationVersion) {
		this.implementationVersion = implementationVersion;
	}

	public String getImplementationVendorId() {
		return implementationVendorId;
	}

	public void setImplementationVendorId(String implementationVendorId) {
		this.implementationVendorId = implementationVendorId;
	}
}
