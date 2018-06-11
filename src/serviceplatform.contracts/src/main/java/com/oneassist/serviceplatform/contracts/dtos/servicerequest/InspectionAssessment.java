
package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectionAssessment {

	/**
	 * 
	 */
	private List<Inspection>	assets;

	private Double				inspectionCharge;

	public List<Inspection> getAssets() {

		return assets;
	}

	public void setAssets(List<Inspection> assets) {

		this.assets = assets;
	}

	public Double getInspectionCharge() {

		return inspectionCharge;
	}

	public void setInspectionCharge(Double inspectionCharge) {

		this.inspectionCharge = inspectionCharge;
	}

	@Override
	public String toString() {
		return "InspectionAssessment [assets=" + assets + ", inspectionCharge=" + inspectionCharge + "]";
	}
	
}