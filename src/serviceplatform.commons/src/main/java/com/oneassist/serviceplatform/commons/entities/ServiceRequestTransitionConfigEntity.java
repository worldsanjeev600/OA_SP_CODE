
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
@Table(name = "OA_SERV_REQ_TRANSITION_CONFIG")
public class ServiceRequestTransitionConfigEntity extends BaseAuditEntity {

	private static final long serialVersionUID = -1486576349069944613L;

	@Id
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERV_REQ_TRANS_CONFIG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@Column(name = "ID")
	private Long	id;

	@Column(name = "SERVICE_REQUEST_TYPE")
	private String	serviceRequestType;
	
	@Column(name = "TRANSITION_FROM_STAGE")
	private String	transitionFromStage;
    
    @Column(name = "EVENT_NAME")
	private String	eventName;
    
    @Column(name = "TRANSITION_TO_STAGE")
	private String	transitionToStage;
    
    @Column(name = "TRANSITION_TO_STAGE_STATUS")
	private String	transitionToStageStatus;

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
	
	public String getTransitionFromStage() {
		return transitionFromStage;
	}

	public void setTransitionFromStage(String transitionFromStage) {
		this.transitionFromStage = transitionFromStage;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getTransitionToStage() {
		return transitionToStage;
	}

	public void setTransitionToStage(String transitionToStage) {
		this.transitionToStage = transitionToStage;
	}

	public String getTransitionToStageStatus() {
		return transitionToStageStatus;
	}

	public void setTransitionToStageStatus(String transitionToStageStatus) {
		this.transitionToStageStatus = transitionToStageStatus;
	}

	@Override
	public String toString() {
		return "ServiceRequestTransitionConfigEntity [id=" + id + ", serviceRequestType=" + serviceRequestType
				+ ", transitionFromStage=" + transitionFromStage + ", eventName=" + eventName + ", transitionToStage="
				+ transitionToStage + ", transitionToStageStatus=" + transitionToStageStatus + "]";
	}
}