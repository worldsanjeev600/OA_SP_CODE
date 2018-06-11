package com.oneassist.serviceplatform.contracts.response;

import java.io.Serializable;


public class ServiceDownloadDocumentResponseDto implements Serializable {
	
	private ServiceDownloadDocumentDto data;

	public ServiceDownloadDocumentDto getData() {
		return data;
	}

	public void setData(ServiceDownloadDocumentDto data) {
		this.data = data;
	}
	
}
