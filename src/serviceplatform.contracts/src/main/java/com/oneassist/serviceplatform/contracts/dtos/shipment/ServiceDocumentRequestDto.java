package com.oneassist.serviceplatform.contracts.dtos.shipment;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

public class ServiceDocumentRequestDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceRequestId;
	private String fileName;
	private File file;
	private InputStream			fileInputStream;
	private String fileContentType;
	private byte[] documentByteArray;
	private Long docTypeId;
	
	public byte[] getDocumentByteArray() {
		return documentByteArray;
	}
	public void setDocumentByteArray(byte[] documentByteArray) {
		this.documentByteArray = documentByteArray;
	}
	public String getServiceRequestId() {
		return serviceRequestId;
	}
	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public Long getDocTypeId() {
		return docTypeId;
	}
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}
	@Override
	public String toString() {
		return "ServiceDocumentRequestDto [serviceRequestId=" + serviceRequestId + ", fileName=" + fileName + ", file="
				+ file + ", fileInputStream=" + fileInputStream + ", fileContentType=" + fileContentType
				+ ", documentByteArray=" + Arrays.toString(documentByteArray) + ", docTypeId=" + docTypeId + "]";
	}
	
}
