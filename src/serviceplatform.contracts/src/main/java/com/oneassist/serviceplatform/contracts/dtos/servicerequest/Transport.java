package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class Transport {
	private String transportTaskId;
	private String cost;
	private String isInsuranceCovered;
	public String getTransportTaskId() {
		return transportTaskId;
	}
	public void setTransportTaskId(String transportTaskId) {
		this.transportTaskId = transportTaskId;
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
		return "Transport [transportTaskId=" + transportTaskId + ", cost=" + cost + ", isInsuranceCovered="
				+ isInsuranceCovered + "]";
	}
}