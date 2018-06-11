package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.util.Date;

public class CMSDocumentDetail {

    private Long ocddClaimId;
    private String ocddDocKey;
    private String ocddDocValue;
    private String ocddDocRemarks;
    private String ocddDocStatus;
    private String ocddDocMongoId;
    private Date ocddDocCreatedDate;
    private String ocddDocCreatedBy;
    private Date modifiedDate;
    private String ocddDocModifiedBy;
    private String isDelete;
    private String docMandatory;

    public Long getOcddClaimId() {
        return ocddClaimId;
    }

    public void setOcddClaimId(Long ocddClaimId) {
        this.ocddClaimId = ocddClaimId;
    }

    public String getOcddDocKey() {
        return ocddDocKey;
    }

    public void setOcddDocKey(String ocddDocKey) {
        this.ocddDocKey = ocddDocKey;
    }

    public String getOcddDocValue() {
        return ocddDocValue;
    }

    public void setOcddDocValue(String ocddDocValue) {
        this.ocddDocValue = ocddDocValue;
    }

    public String getOcddDocRemarks() {
        return ocddDocRemarks;
    }

    public void setOcddDocRemarks(String ocddDocRemarks) {
        this.ocddDocRemarks = ocddDocRemarks;
    }

    public String getOcddDocStatus() {
        return ocddDocStatus;
    }

    public void setOcddDocStatus(String ocddDocStatus) {
        this.ocddDocStatus = ocddDocStatus;
    }

    public String getOcddDocMongoId() {
        return ocddDocMongoId;
    }

    public void setOcddDocMongoId(String ocddDocMongoId) {
        this.ocddDocMongoId = ocddDocMongoId;
    }

    public Date getOcddDocCreatedDate() {
        return ocddDocCreatedDate;
    }

    public void setOcddDocCreatedDate(Date ocddDocCreatedDate) {
        this.ocddDocCreatedDate = ocddDocCreatedDate;
    }

    public String getOcddDocCreatedBy() {
        return ocddDocCreatedBy;
    }

    public void setOcddDocCreatedBy(String ocddDocCreatedBy) {
        this.ocddDocCreatedBy = ocddDocCreatedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOcddDocModifiedBy() {
        return ocddDocModifiedBy;
    }

    public void setOcddDocModifiedBy(String ocddDocModifiedBy) {
        this.ocddDocModifiedBy = ocddDocModifiedBy;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getDocMandatory() {
        return docMandatory;
    }

    public void setDocMandatory(String docMandatory) {
        this.docMandatory = docMandatory;
    }

    @Override
    public String toString() {
        return "CMSDocumentDetail [ocddClaimId=" + ocddClaimId + ", ocddDocKey=" + ocddDocKey + ", ocddDocValue=" + ocddDocValue + ", ocddDocRemarks=" + ocddDocRemarks + ", ocddDocStatus="
                + ocddDocStatus + ", ocddDocMongoId=" + ocddDocMongoId + ", ocddDocCreatedDate=" + ocddDocCreatedDate + ", ocddDocCreatedBy=" + ocddDocCreatedBy + ", modifiedDate=" + modifiedDate
                + ", ocddDocModifiedBy=" + ocddDocModifiedBy + ", isDelete=" + isDelete + ", docMandatory=" + docMandatory + "]";
    }

}
