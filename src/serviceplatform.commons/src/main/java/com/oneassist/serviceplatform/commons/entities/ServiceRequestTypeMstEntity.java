
package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Satish Kumsr
 */
@Entity
@Table(name = "OA_SERVICE_REQUEST_TYPE_MST")
public class ServiceRequestTypeMstEntity extends BaseAuditEntity {

	private static final long serialVersionUID = -1486576349069944611L;

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERVICE_REQST_TYPE_MST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "SERVICE_REQUEST_TYPE_ID")
	//NEED TO CHANGE THIS GENERATOR
	private Long	serviceRequestTypeId;

    @Column(name = "SERVICE_REQUEST_TYPE")
	private String	serviceRequestType;
    
    @Column(name = "SERVICE_REQUEST_TYPE_NAME")
	private String	serviceRequestTypeName;

	public Long getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	public void setServiceRequestTypeId(Long serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	public String getServiceRequestType() {
		return serviceRequestType;
	}

	public void setServiceRequestType(String serviceRequestType) {
		this.serviceRequestType = serviceRequestType;
	}

	public String getServiceRequestTypeName() {
		return serviceRequestTypeName;
	}

	public void setServiceRequestTypeName(String serviceRequestTypeName) {
		this.serviceRequestTypeName = serviceRequestTypeName;
	}
	
}