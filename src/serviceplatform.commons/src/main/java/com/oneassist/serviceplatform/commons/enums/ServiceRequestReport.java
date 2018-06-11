package com.oneassist.serviceplatform.commons.enums;

public enum ServiceRequestReport {

	JOBSHEET("JOBSHEET"), 
	TAXINVOICE("TAXINVOICE"),
	WHCCLAIMFORM("WHCCLAIMFORM");
	
	private final String serviceRequestReport;

	ServiceRequestReport(String serviceRequestReport) {

		this.serviceRequestReport = serviceRequestReport;
	}

	public String getServiceRequestReport() {

		return this.serviceRequestReport;
	}

	public static ServiceRequestReport getServiceRequestReport(String serviceRequestReport) {
		for (ServiceRequestReport enumRequestReport : ServiceRequestReport.values()) {
			if (enumRequestReport.getServiceRequestReport().equalsIgnoreCase(serviceRequestReport)) {
				return enumRequestReport;
			}
		}
		
		return null;
	}
}