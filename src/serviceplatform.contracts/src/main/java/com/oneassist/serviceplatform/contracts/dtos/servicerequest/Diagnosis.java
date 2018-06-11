package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class Diagnosis {
	private String diagnosisId;
	private String diagnosisDescription;
	private String cost;
	private String isInsuranceCovered;
	
	public String getDiagnosisId() {
		return diagnosisId;
	}
	
	public void setDiagnosisId(String diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	
	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}
	
	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
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
		return "Diagnosis [diagnosisId=" + diagnosisId + ", diagnosisDescription=" + diagnosisDescription + ", cost="
				+ cost + ", isInsuranceCovered=" + isInsuranceCovered + "]";
	}
	
		
}