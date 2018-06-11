package com.oneassist.serviceplatform.contracts.dtos.shipment;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

/**
 * @author divya
 */
public class ServiceDocumentDto extends BaseAuditDto {

    private static final long serialVersionUID = -5633021932443163824L;

    private String documentId;

    private Long serviceRequestId;

    private String storageRefId;

    private Long documentTypeId;

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

	@Override
	public String toString() {
		return "ServiceDocumentDto [documentId=" + documentId + ", serviceRequestId=" + serviceRequestId
				+ ", storageRefId=" + storageRefId + ", documentTypeId=" + documentTypeId + ", documentName="
				+ documentName + "]";
	}

}
