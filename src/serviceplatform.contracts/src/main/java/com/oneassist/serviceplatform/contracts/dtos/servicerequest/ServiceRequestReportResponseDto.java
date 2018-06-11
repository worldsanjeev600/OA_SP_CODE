package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.InputStream;

public class ServiceRequestReportResponseDto {
	
	private InputStream inputStream;

	private String serviceRequestId;

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public String getServiceRequestId() {
		return serviceRequestId;
	}
	
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	
	@Override
	public String toString() {
		return "ServiceRequestReportResponseDto [inputStream=" + inputStream + ", serviceRequestId=" + serviceRequestId
				+ "]";
	}	
}