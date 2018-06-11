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
@Table(name = "OA_PINCODE_SERV_FULFILMENT_MST")
public class PincodeServiceFulfilmentEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -7734664984192284760L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_PINCODE_SERV_FULFIL_MST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "FULFILMENT_ID")
    private Long fulfilmentId;

    @Column(name = "PINCODE")
    private String pincode;

    @Column(name = "SERVICE_REQUEST_TYPE_ID")
    private Long serviceRequestTypeId;

    @Column(name = "PARTNER_PRIORITY")
    private Integer partnerPriority;

    @Column(name = "PARTNER_CODE")
    private Long partnerCode;

    @Column(name = "SERVICE_TAT")
    private Integer serviceTat;

    @Column(name = "SUBCATEGORY_CODE")
    private String subCategoryCode;

    @Column(name = "PARTNER_BU_CODE")
    private String partnerBUCode;

    @Transient
    private Long recordId;

    public Long getFulfilmentId() {

        return fulfilmentId;
    }

    public void setFulfilmentId(Long fulfilmentId) {

        this.fulfilmentId = fulfilmentId;
    }

    public String getPincode() {

        return pincode;
    }

    public void setPincode(String pincode) {

        this.pincode = pincode;
    }

    public Integer getPartnerPriority() {

        return partnerPriority;
    }

    public void setPartnerPriority(Integer partnerPriority) {

        this.partnerPriority = partnerPriority;
    }

    public Long getPartnerCode() {

        return partnerCode;
    }

    public void setPartnerCode(Long partnerCode) {

        this.partnerCode = partnerCode;
    }

    public Integer getServiceTat() {

        return serviceTat;
    }

    public void setServiceTat(Integer serviceTat) {

        this.serviceTat = serviceTat;
    }

    public String getSubCategoryCode() {

        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {

        this.subCategoryCode = subCategoryCode;
    }

    public Long getRecordId() {

        return recordId;
    }

    public void setRecordId(Long recordId) {

        this.recordId = recordId;
    }

    public static long getSerialversionuid() {

        return serialVersionUID;
    }

    public Long getServiceRequestTypeId() {

        return serviceRequestTypeId;
    }

    public void setServiceRequestTypeId(Long serviceRequestTypeId) {

        this.serviceRequestTypeId = serviceRequestTypeId;
    }

    public String getPartnerBUCode() {

        return partnerBUCode;
    }

    public void setPartnerBUCode(String partnerBUCode) {

        this.partnerBUCode = partnerBUCode;
    }
}
