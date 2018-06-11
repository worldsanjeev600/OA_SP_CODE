package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class ServiceRequestDocumentDto {

	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private File file;
	private InputStream	fileInputStream;
	private String fileContentType;
	private String docKey;
	private byte[] documentByteArray;
	private Long docTypeId;
	
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
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public String getFileContentType() {
		return fileContentType;
	}
	
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	public String getDocKey() {
		return docKey;
	}
	
	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}
	
	public byte[] getDocumentByteArray() {
		return documentByteArray;
	}
	
	public void setDocumentByteArray(byte[] documentByteArray) {
		this.documentByteArray = documentByteArray;
	}
	
	public Long getDocTypeId() {
		return docTypeId;
	}
	
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}
	
	@Override
	public String toString() {
		return "ServiceDocumentDto [fileName=" + fileName 
				+ ", file=" + file
				+ ", fileInputStream=" + fileInputStream 
				+ ", fileContentType=" + fileContentType 
				+ ", docKey=" + docKey
				+ ", documentByteArray=" + Arrays.toString(documentByteArray)
				+ ", docTypeId=" + docTypeId + "]";
	}
}
