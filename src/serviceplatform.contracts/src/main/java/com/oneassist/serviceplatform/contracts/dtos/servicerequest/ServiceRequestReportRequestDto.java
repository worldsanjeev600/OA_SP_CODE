package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Map;

public class ServiceRequestReportRequestDto {
	
	
	private String fileName;
	private Long serviceRequestId;
	private String reportType;
	private String templateFilePath;
	private Map<String, Object> data;
	
	
	public String getTemplateFilePath() {
		return templateFilePath;
	}
	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getServiceRequestId() {
		return serviceRequestId;
	}
	public void setServiceRequestId(Long serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	@Override
	public String toString() {
		return "ServiceRequestReportRequestDto [fileName=" + fileName + ", serviceRequestId=" + serviceRequestId
				+ ", reportType=" + reportType + ", templateFilePath=" + templateFilePath + ", data=" + data + "]";
	}
	
}
