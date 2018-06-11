package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author alok.singh
 */
@Entity
@Table(name = "OA_SERV_REQ_ENTITY_DOC_DTL")
public class ServiceRequestEntityDocumentEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -1486576349069344641L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERV_REQ_ASSET_DOC_DTL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ENTITY_DOC_ID")
    private Long entityDocumentId;
    
    @Column(name = "ENTITY_ID")
    private String  entityId;
    
    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "DOC_ID")
    private String documentId;

	public Long getEntityDocumentId() {
		return entityDocumentId;
	}

	public void setEntityDocumentId(Long entityDocumentId) {
		this.entityDocumentId = entityDocumentId;
	}

	
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

}