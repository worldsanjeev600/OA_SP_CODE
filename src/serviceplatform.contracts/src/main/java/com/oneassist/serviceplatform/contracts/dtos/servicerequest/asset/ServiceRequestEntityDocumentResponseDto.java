package com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset;

import java.io.Serializable;
import java.util.List;

import com.oneassist.serviceplatform.contracts.dtos.BaseAuditDto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author alok.singh
 */
public class ServiceRequestEntityDocumentResponseDto extends BaseAuditDto implements Serializable {

    private static final long serialVersionUID = -7265551596715963251L;

    @ApiModelProperty(value = "Unique Identifier for Entity Document")
    private Long entityDocumentId;
    
    @ApiModelProperty(value = "Entity Id")
    private String entityId;
    
    @ApiModelProperty(value = "Entity Name")
    private String entityName;

    @ApiModelProperty(value = "Document Id")
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

	@Override
	public String toString() {
		return "ServiceRequestAssetDocumentResponseDto [entityDocumentId=" + entityDocumentId + ", entityId=" + entityId
				+ ", entityName=" + entityName + ", documentId=" + documentId + "]";
	}

    
}
