package com.oneassist.serviceplatform.contracts.response;

import java.io.Serializable;

public class DownloadDocumentResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DownloadDocumentDto data;

	public DownloadDocumentDto getData() {
		return data;
	}

	public void setData(DownloadDocumentDto data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DownloadDocumentResponseDto [data=" + data + "]";
	}	
	
}