package com.oneassist.serviceplatform.commons.filegenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.enums.ServiceRequestReport;

@Component
public class ServiceRequestReportFactory {
	
	@Autowired
	private JobsheetFileGenerator jobsheetFileGenerator;
	
	@Autowired
	private ServiceRequestWHCClaimFormFileGenerator serviceRequestWHCClaimFormFileGenerator;
	
	public BaseFileGenerator getServiceRequestReportHandler(ServiceRequestReport serviceRequestReport) {	
		if (serviceRequestReport.equals(ServiceRequestReport.JOBSHEET)) {
			return jobsheetFileGenerator;
		}else if (serviceRequestReport.equals(ServiceRequestReport.TAXINVOICE)) {
			return null;
		}else if (serviceRequestReport.equals(ServiceRequestReport.WHCCLAIMFORM)) {
			return serviceRequestWHCClaimFormFileGenerator;
		}else{
			return null;
		}
	}
}
