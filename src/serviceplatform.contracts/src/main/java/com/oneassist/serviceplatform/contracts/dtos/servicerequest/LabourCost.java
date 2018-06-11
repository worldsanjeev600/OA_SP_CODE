package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class LabourCost {
	private String labourChargeId;
	private String cost;
	private String isInsuranceCovered;
	public String getLabourChargeId() {
		return labourChargeId;
	}
	public void setLabourChargeId(String labourChargeId) {
		this.labourChargeId = labourChargeId;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getIsInsuranceCovered() {
		return isInsuranceCovered;
	}
	public void setIsInsuranceCovered(String isInsuranceCovered) {
		this.isInsuranceCovered = isInsuranceCovered;
	}
	@Override
	public String toString() {
		return "LabourCost [labourChargeId=" + labourChargeId + ", cost=" + cost + ", isInsuranceCovered="
				+ isInsuranceCovered + "]";
	}
	
}