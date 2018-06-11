package com.oneassist.serviceplatform.commons.entities;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author satish
 */

@Entity
@Table(name = "OA_SERVICE_TASK_MST")
public class ServiceTaskEntity extends BaseAuditEntity {

    private static final long serialVersionUID = -1486576349069944611L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_OA_SERVICE_TASK_MST", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "SERVICE_TASK_ID")
    private Long serviceTaskId;

    @ApiModelProperty(value = "Service task name {Screen broken, Replace TV Panel, Replace TV Stand, Pickup }")
    @Column(name = "TASK_NAME")
    private String taskName;

    @ApiModelProperty(value = "Service task description")
    @Column(name = "TASK_DESCRIPTION")
    private String taskDescription;

    @ApiModelProperty(value = "Service task type(Issues,Diagonosis,sparepart)")
    @Column(name = "TASK_TYPE")
    private String taskType;

    @ApiModelProperty(value = "Reference code")
    @Column(name = "REFERENCE_CODE")
    private String referenceCode;

    @Column(name = "PRODUCT_VARIANT_ID")
    @ApiModelProperty(value = "Product variant id (Product technology)")
    private Long productVariantId;

    @Column(name = "COST")
    @ApiModelProperty(value = "cost of service task")
    private Double cost;

    @ApiModelProperty(value = "service task is covered under insurance or not")
    @Column(name = "IS_INSURANCE_COVERED")
    private String isInsuranceCovered;

    public Long getServiceTaskId() {

        return serviceTaskId;
    }

    public void setServiceTaskId(Long serviceTaskId) {

        this.serviceTaskId = serviceTaskId;
    }

    public String getTaskName() {

        return taskName;
    }

    public void setTaskName(String taskName) {

        this.taskName = taskName;
    }

    public String getTaskDescription() {

        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {

        this.taskDescription = taskDescription;
    }

    public String getTaskType() {

        return taskType;
    }

    public void setTaskType(String taskType) {

        this.taskType = taskType;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Double getCost() {

        return cost;
    }

    public void setCost(Double cost) {

        this.cost = cost;
    }

    public String getIsInsuranceCovered() {

        return isInsuranceCovered;
    }

    public void setIsInsuranceCovered(String isInsuranceCovered) {

        this.isInsuranceCovered = isInsuranceCovered;
    }

    public Long getProductVariantId() {

        return productVariantId;
    }

    public void setProductVariantId(Long productVariantId) {

        this.productVariantId = productVariantId;
    }
}
