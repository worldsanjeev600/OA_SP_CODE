package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "OA_PINCODE_SERVICE_MST")
public class PincodeServiceEntity extends BaseAuditEntity {
	
	private static final long serialVersionUID = 4210929750396993666L;

	@Id
	@Column (name = "PINCODE_SERVICE_ID")
	@SequenceGenerator(name="SEQ_GEN", sequenceName="SEQ_OA_PINCODE_SERVICE_MST", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
	private Long pincodeServiceId;
	
	@Column(name = "PINCODE")
	private String pincode;
	
	@Column(name = "HUB_ID")
	private Long hubId;
	
	@Column(name = "HANDSET_COURTESY_APPLICABLE")
	private String isCourtesyApplicable;
	
	@Column(name = "PINCODE_CATEGORY")
	private String pincodeCategory;
	
	@Transient
	private Long recordId;
	
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	
	public Long getPincodeServiceId() {
		return pincodeServiceId;
	}

	public void setPincodeServiceId(Long pincodeServiceId) {
		this.pincodeServiceId = pincodeServiceId;
	}
	
	public String getPincode() {
		return pincode;
	}
	
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	
	public String getIsCourtesyApplicable() {
		return isCourtesyApplicable;
	}
	
	public void setIsCourtesyApplicable(String isCourtesyApplicable) {
		this.isCourtesyApplicable = isCourtesyApplicable;
	}
	
	public Long getHubId() {
		return hubId;
	}
	
	public void setHubId(Long hubId) {
		this.hubId = hubId;
	}
	public String getPincodeCategory() {
		return pincodeCategory;
	}
	public void setPincodeCategory(String pincodeCategory) {
		this.pincodeCategory = pincodeCategory;
	}
	@Override
	public String toString() {
		return "PincodeServiceEntity [pincodeServiceId=" + pincodeServiceId
				+ ", pincode=" + pincode + ", hubId=" + hubId
				+ ", isCourtesyApplicable=" + isCourtesyApplicable
				+ ", pincodeCategory=" + pincodeCategory + ", recordId="
				+ recordId + "]";
	}
}
