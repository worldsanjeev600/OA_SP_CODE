package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

public class ServiceTaskDto extends BaseAuditDto {

	private static final long	serialVersionUID	= -520920822753234495L;

	private Long				serviceTaskId;

	private String				taskName;

	private String				taskDescription;

	private String				taskType;

	private String				referenceCode;

	private String				isInsuranceCovered;

	private Long				productVariantId;

	private Double cost;
	
	public Long getServiceTaskId() {
		return serviceTaskId;
	}
	
	public void setServiceTaskId(Long serviceTaskId) {
		this.serviceTaskId = serviceTaskId;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getIsInsuranceCovered() {
		return isInsuranceCovered;
	}
	
	public void setIsInsuranceCovered(String isInsuranceCovered) {
		this.isInsuranceCovered = isInsuranceCovered;
	}
	
	public Long getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(Long productVariantId) {
		this.productVariantId = productVariantId;
	}
	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "ServiceTaskDto [serviceTaskId=" + serviceTaskId + ", taskName=" + taskName + ", taskDescription="
				+ taskDescription + ", taskType=" + taskType + ", referenceCode=" + referenceCode
				+ ", isInsuranceCovered=" + isInsuranceCovered + ", productVariantId=" + productVariantId + ", cost="
				+ cost + "]";
	}
	
}
