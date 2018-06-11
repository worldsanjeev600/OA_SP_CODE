package com.oneassist.serviceplatform.contracts.response;

public class DownloadDocumentDto {

    private String inputStream;

    private String fileName;

    public String getInputStream() {
        return inputStream;
    }

    public void setInputStream(String inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	@Override
	public String toString() {
		return "DownloadDocumentDto [inputStream=" + inputStream + ", fileName=" + fileName + "]";
	}
    
}
