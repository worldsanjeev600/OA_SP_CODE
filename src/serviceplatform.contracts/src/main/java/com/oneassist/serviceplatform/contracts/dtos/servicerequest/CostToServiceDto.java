package com.oneassist.serviceplatform.contracts.dtos.servicerequest;



public class CostToServiceDto {

	private double costToCustomer;
	private double costToCompany;
	private boolean estimateRequestApprovedStatus;
	
	public double getCostToCustomer() {
	
		return costToCustomer;
	}
	
	public void setCostToCustomer(double costToCustomer) {
	
		this.costToCustomer = costToCustomer;
	}
	
	public double getCostToCompany() {
	
		return costToCompany;
	}
	
	public void setCostToCompany(double costToCompany) {
	
		this.costToCompany = costToCompany;
	}

	
	public boolean isEstimateRequestApprovedStatus() {
	
		return estimateRequestApprovedStatus;
	}

	
	public void setEstimateRequestApprovedStatus(boolean estimateRequestApprovedStatus) {
	
		this.estimateRequestApprovedStatus = estimateRequestApprovedStatus;
	}

	@Override
	public String toString() {
		return "CostToServiceDto [costToCustomer=" + costToCustomer + ", costToCompany=" + costToCompany
				+ ", estimateRequestApprovedStatus=" + estimateRequestApprovedStatus + "]";
	}
	
}
