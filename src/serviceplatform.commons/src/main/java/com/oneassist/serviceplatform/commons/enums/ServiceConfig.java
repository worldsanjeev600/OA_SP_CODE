package com.oneassist.serviceplatform.commons.enums;

public enum ServiceConfig {
	SR_STATUS("SR_STATUS"), 
	STATUS("STATUS"),
	WORKFLOW_STAGE("WORKFLOW_STAGE"),
	CMS_STAGE("CMS_STAGE"),
	CMS_STAGE_STATUS("CMS_STAGE_STATUS"),
	PARTNER_STAGE("PARTNER_STAGE"),
	PARTNER_STAGE_STATUS("PARTNER_STAGE_STATUS"),
	CUSTOMER_RESCHEDULE_VISIT("CUSTOMER_RESCHEDULE_VISIT"),
	CUSTOMER_NOT_AVAILABLE("CUSTOMER_NOT_AVAILABLE"), 
	DOCUMENT_CONFIG("DOCUMENT_CONFIG");

	private final String serviceConfig;

	ServiceConfig(String serviceConfig) {

		this.serviceConfig = serviceConfig;
	}

	public String getServiceConfig() {

		return this.serviceConfig;
	}

	public static ServiceConfig getServiceConfig(String serviceConfig) {
		for (ServiceConfig enumstatus : ServiceConfig.values()) {
			if (enumstatus.getServiceConfig().equalsIgnoreCase(serviceConfig)) {
				return enumstatus;
			}
		}

		return null;
	}
}
