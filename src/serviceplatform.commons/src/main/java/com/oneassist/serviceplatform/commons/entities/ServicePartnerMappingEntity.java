package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OA_SERVICE_PARTNER_MAPPING")
public class ServicePartnerMappingEntity extends BaseAuditEntity {

	private static final long serialVersionUID = 224391873939900731L;

	@Id
	@Column(name = "SERVICE_REQUEST_TYPE_ID")
	private Long serviceRequestTypeId;

	@Id
	@Column(name = "PARTNER_CODE")
	private Long partnerCode;

	public Long getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	public void setServiceRequestTypeId(Long serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	public Long getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(Long partnerCode) {
		this.partnerCode = partnerCode;
	}
}