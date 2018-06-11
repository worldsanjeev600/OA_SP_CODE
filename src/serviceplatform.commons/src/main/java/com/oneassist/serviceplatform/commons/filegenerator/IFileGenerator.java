package com.oneassist.serviceplatform.commons.filegenerator;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;

public interface IFileGenerator {
	
	public ServiceRequestReportResponseDto generateFile(ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception;
}