package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity class for OA_DOC_TYPE_CONFIG_DTL table
 * 
 * @author sanjeev.gupta
 *
 */

@Entity
@Table(name = "OA_DOC_TYPE_CONFIG_DTL")
public class DocTypeConfigDetailEntity extends BaseAuditEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_DOC_TYPE_CONFIG_DTL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	private Long id;

	@Column(name = "DOC_TYPE_ID")
	private Long docTypeId;

	@Column(name = "SERVICE_REQUEST_TYPE_ID")
	private String serviceRequestTypeId;

	@Column(name = "IS_MANDATORY")
	private String isMandatory;

	@Column(name = "REFERENCE_CODE")
	private String referenceCode;

	@Column(name = "INSURANCE_PARTNER_CODE")
	private Long insurancePartnerCode;

	@ManyToOne
	@JoinColumn(name = "DOC_TYPE_ID", referencedColumnName = "DOC_TYPE_ID", insertable = false, updatable = false)
	private DocTypeMstEntity docTypeMstEntity;

	/**
	 * @return the docTypeMstEntity
	 */
	public DocTypeMstEntity getDocTypeMstEntity() {
		return docTypeMstEntity;
	}

	/**
	 * @param docTypeMstEntity
	 *            the docTypeMstEntity to set
	 */
	public void setDocTypeMstEntity(DocTypeMstEntity docTypeMstEntity) {
		this.docTypeMstEntity = docTypeMstEntity;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the docTypeId
	 */
	public Long getDocTypeId() {
		return docTypeId;
	}

	/**
	 * @param docTypeId
	 *            the docTypeId to set
	 */
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	/**
	 * @return the serviceRequestTypeId
	 */
	public String getServiceRequestTypeId() {
		return serviceRequestTypeId;
	}

	/**
	 * @param serviceRequestTypeId
	 *            the serviceRequestTypeId to set
	 */
	public void setServiceRequestTypeId(String serviceRequestTypeId) {
		this.serviceRequestTypeId = serviceRequestTypeId;
	}

	/**
	 * @return the isMandatory
	 */
	public String getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory
	 *            the isMandatory to set
	 */
	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the referenceCode
	 */
	public String getReferenceCode() {
		return referenceCode;
	}

	/**
	 * @param referenceCode
	 *            the referenceCode to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * @return the insurancePartnerCode
	 */
	public Long getInsurancePartnerCode() {
		return insurancePartnerCode;
	}

	/**
	 * @param insurancePartnerCode
	 *            the insurancePartnerCode to set
	 */
	public void setInsurancePartnerCode(Long insurancePartnerCode) {
		this.insurancePartnerCode = insurancePartnerCode;
	}

	public DocTypeConfigDetailEntity() {
	}

	@Override
	public String toString() {
		return "DocTypeConfigDetailEntity [id=" + id + ", docTypeId=" + docTypeId + ", serviceRequestTypeId="
				+ serviceRequestTypeId + ", isMandatory=" + isMandatory + ", referenceCode=" + referenceCode
				+ ", insurancePartnerCode=" + insurancePartnerCode + ", docTypeMstEntity=" + docTypeMstEntity + "]";
	}

}
