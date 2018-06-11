package com.oneassist.serviceplatform.commons.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Divya
 */
@Entity
@Table(name = "OA_DOC_TYPE_MST")
public class DocTypeMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DOC_TYPE_ID")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_DOC_TYPE_MST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	private Long docTypeId;

	@Column(name = "DOC_NAME")
	private String docName;

	@Column(name = "DOC_KEY")
	private String docKey;

	@Column(name = "IS_MANDATORY")
	private String isMandatory;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "STATUS")
	private String status;

	public String getDocKey() {
		return docKey;
	}

	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DocTypeMstEntity [docTypeId=" + docTypeId + ", docName=" + docName + ", isMandatory=" + isMandatory
				+ ", description=" + description + ", status=" + status + "]";
	}

}