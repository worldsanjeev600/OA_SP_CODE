package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

public class OASYSCustMemDetailsResponse {

	private String status;
	private OASYSCustMemDetails data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OASYSCustMemDetails getData() {
		return data;
	}

	public void setData(OASYSCustMemDetails data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OASYSCustMemDetailsResponse [status=" + status + ", data=" + data + "]";
	}
	
	

}
