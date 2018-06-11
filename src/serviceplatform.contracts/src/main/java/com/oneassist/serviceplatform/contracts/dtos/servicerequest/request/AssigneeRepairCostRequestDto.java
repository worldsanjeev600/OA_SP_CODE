package com.oneassist.serviceplatform.contracts.dtos.servicerequest.request;


public class AssigneeRepairCostRequestDto {

	private Double invoiceValue;
	private String modifiedBy;

	public Double getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(Double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String toString() {
		return "AssigneeRepairCostRequestDto [invoiceValue=" + invoiceValue
				+ ", modifiedBy=" + modifiedBy + "]";
	}

	
}