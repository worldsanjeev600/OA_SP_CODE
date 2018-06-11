
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
@Table(name = "OA_SERV_REQ_STAGE_MST")
public class ServiceRequestStageMstEntity extends BaseAuditEntity {

	private static final long serialVersionUID = -1486576349069944615L;

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERV_REQ_STAGE_MST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "ID")
	private Long id;

	@Column(name = "SERVICE_REQUEST_TYPE")
	private String serviceRequestTypeId;

	@Column(name = "STAGE_CODE")
	private String stageCode;

	@Column(name = "STAGE_NAME")
	private String stageName;

	@Column(name="STAGE_OBJECT_NAME")
	private String stageObjectName;

	public String getStageObjectName() {
		return stageObjectName;
	}

	public void setStageObjectName(String stageObjectName) {
		this.stageObjectName = stageObjectName;
	}

	@Column(name = "STAGE_ORDER")
	private Long stageOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	public void setServiceRequestTypeId(String serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	public String getStageCode() {
		return stageCode;
	}

	public void setStageCode(String stageCode) {
		this.stageCode = stageCode;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Long getStageOrder() {
		return stageOrder;
	}

	public void setStageOrder(Long stageOrder) {
		this.stageOrder = stageOrder;
	}
}