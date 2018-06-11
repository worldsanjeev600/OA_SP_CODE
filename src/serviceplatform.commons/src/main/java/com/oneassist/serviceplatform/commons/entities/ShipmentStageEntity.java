package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author srikanth
 */
@Entity
@Table(name = "OA_SHIPMENT_STAGE")
public class ShipmentStageEntity implements Serializable {

	private static final long serialVersionUID = -1486576349069944611L;
	
	private static final String SHIPMENT_ID = "SHIPMENT_ID";

	@Id
	@Column(name = SHIPMENT_ID)
	private Long shipmentId;

	@Id
	@Column(name = "STAGE_NAME")
	private String shipmentStage;

	@Column(name = "STAGE_START_TIME")
	private Date startTime;

	@Column(name = "STAGE_END_TIME")
	private Date endTime;

	@Column(name = "STAGE_CREATED_BY")
	private String createdBy;

	@Column(name = "STAGE_MODIFIED_BY")
	private String modifiedBy;

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getShipmentStage() {
		return shipmentStage;
	}

	public void setShipmentStage(String shipmentStage) {
		this.shipmentStage = shipmentStage;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}