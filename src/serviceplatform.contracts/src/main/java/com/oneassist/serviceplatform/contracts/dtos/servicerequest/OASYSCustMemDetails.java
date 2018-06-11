package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.List;

public class OASYSCustMemDetails {

	private Long custId;
	private List<OASYSMembershipDetails> memberships;

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public List<OASYSMembershipDetails> getMemberships() {
		return memberships;
	}

	public void setMemberships(List<OASYSMembershipDetails> memberships) {
		this.memberships = memberships;
	}

	@Override
	public String toString() {
		return "OASYSCustMemDetails [custId=" + custId + ", memberships=" + memberships + "]";
	}
	
	

}
