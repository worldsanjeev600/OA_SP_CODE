package com.oneassist.serviceplatform.commons.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OA_SERVICE_DOC_DTL")
public class ServiceDocumentEntity extends BaseAuditEntity {

    private static final long serialVersionUID = 4210929750396993666L;

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "DOC_ID")
    private String documentId;

    @Column(name = "SERVICE_REQUEST_ID")
    private Long serviceRequestId;

    @Column(name = "STORAGE_REF_ID")
    private String storageRefId;

    @Column(name = "DOC_TYPE_ID")
    private Long documentTypeId;

    @Column(name = "FILE_NAME")
    private String documentName;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getStorageRefId() {
        return storageRefId;
    }

    public void setStorageRefId(String storageRefId) {
        this.storageRefId = storageRefId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
