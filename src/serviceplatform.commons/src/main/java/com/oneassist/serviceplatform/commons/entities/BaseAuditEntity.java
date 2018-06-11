package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public abstract class BaseAuditEntity implements Serializable {

	@ApiModelProperty(value = "The current status (A-Active, X-Cancelled, P-Pending)")
	@Column(name = "STATUS")
	private String status;

	//@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "CREATED_ON", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "IST")
	@Column(name = "MODIFIED_ON")
	@LastModifiedDate
	private Date modifiedOn;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@PreUpdate
	public void setLastUpdate() {
		this.modifiedOn = new Date();
	}
	
	@PrePersist
	public void setCreatedDate(){
		this.createdOn = new Date();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
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
