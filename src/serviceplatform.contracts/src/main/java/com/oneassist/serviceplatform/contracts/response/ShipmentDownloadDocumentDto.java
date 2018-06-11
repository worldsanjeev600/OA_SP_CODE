package com.oneassist.serviceplatform.contracts.response;

import java.io.InputStream;
import java.io.Serializable;


public class ShipmentDownloadDocumentDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputstream;
	public String getInputstream() {
		return inputstream;
	}
	public void setInputstream(String inputstream) {
		this.inputstream = inputstream;
	}
	private InputStream inputStream1;
	public InputStream getInputStream1() {
		return inputStream1;
	}
	public void setInputStream1(InputStream inputStream1) {
		this.inputStream1 = inputStream1;
	}
	@Override
	public String toString() {
		return "ShipmentDownloadDocumentDto [inputstream=" + inputstream + ", inputStream1=" + inputStream1 + "]";
	}
	
}
