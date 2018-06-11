package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.io.Serializable;
import java.util.Arrays;

public class DocumentDownloadRequestDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String[] documentIds;
	
	public String[] getDocumentIds() {
		return documentIds;
	}
	
	public void setDocumentIds(String[] documentIds) {
		this.documentIds = documentIds;
	}

	@Override
	public String toString() {
		return "DocumentDownloadRequestDto [documentIds=" + Arrays.toString(documentIds) + "]";
	}
	
}
