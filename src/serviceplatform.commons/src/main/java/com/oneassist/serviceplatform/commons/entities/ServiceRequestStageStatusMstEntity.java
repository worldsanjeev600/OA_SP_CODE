
package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author alok.singh
 */
@Entity
@Table(name = "OA_SERV_REQ_STAGE_STATUS_MST")
public class ServiceRequestStageStatusMstEntity extends BaseAuditEntity {

	private static final long serialVersionUID = -1486576349069944612L;

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERV_REQ_STG_STATUS_MST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "ID")
	private Long	id;

	@Column(name = "SERVICE_REQUEST_TYPE")
	private String	serviceRequestType;
	
	@Column(name = "STAGE_CODE")
	private String	stageCode;
    
    @Column(name = "STAGE_STATUS_CODE")
	private String	stageStatusCode;
    
    @Column(name = "SERVICE_REQUEST_STATUS")
	private String	serviceRequestStatus;
    
    @Column(name = "STAGE_STATUS_DISPLAY_NAME")
	private String	stageStatusDisplayName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getServiceRequestType() {
		return serviceRequestType;
	}

	public void setServiceRequestType(String serviceRequestType) {
		this.serviceRequestType = serviceRequestType;
	}

	public String getStageCode() {
		return stageCode;
	}

	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}

	public String getStageStatusCode() {
		return stageStatusCode;
	}

	public void setStageStatusCode(String stageStatusCode) {
		this.stageStatusCode = stageStatusCode;
	}

	public String getServiceRequestStatus() {
		return serviceRequestStatus;
	}

	public void setServiceRequestStatus(String serviceRequestStatus) {
		this.serviceRequestStatus = serviceRequestStatus;
	}

	public String getStageStatusDisplayName() {
		return stageStatusDisplayName;
	}

	public void setStageStatusDisplayName(String stageStatusDisplayName) {
		this.stageStatusDisplayName = stageStatusDisplayName;
	}

	@Override
	public String toString() {
		return "ServiceRequestStageStatusMstEntity [id=" + id + ", serviceRequestType=" + serviceRequestType
				+ ", stageCode=" + stageCode + ", stageStatusCode=" + stageStatusCode + ", serviceRequestStatus="
				+ serviceRequestStatus + ", stageStatusDisplayName=" + stageStatusDisplayName + "]";
	}
	
}