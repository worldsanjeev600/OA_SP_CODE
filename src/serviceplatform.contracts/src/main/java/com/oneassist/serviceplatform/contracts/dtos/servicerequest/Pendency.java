package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Pendency {

	@JsonInclude(Include.NON_NULL)
	private List<DocumentRejection> documents;
	
	@JsonInclude(Include.NON_NULL)
	private RejectionStatus incidenceDate;
	
	@JsonInclude(Include.NON_NULL)
	private RejectionStatus incidenceDescription;

	public List<DocumentRejection> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentRejection> documents) {
		this.documents = documents;
	}

	public RejectionStatus getIncidenceDate() {
		return incidenceDate;
	}

	public void setIncidenceDate(RejectionStatus incidenceDate) {
		this.incidenceDate = incidenceDate;
	}

	public RejectionStatus getIncidenceDescription() {
		return incidenceDescription;
	}

	public void setIncidenceDescription(RejectionStatus incidenceDescription) {
		this.incidenceDescription = incidenceDescription;
	}

}
