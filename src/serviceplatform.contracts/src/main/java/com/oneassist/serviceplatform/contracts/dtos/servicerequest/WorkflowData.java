package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class WorkflowData implements Serializable {

	private static final long serialVersionUID = 1L;

	private DocumentUpload documentUpload;
	private Verification verification;
	private Visit visit;
	private ICDoc icDoc;
	private RepairAssessment repairAssessment;
	private SoftApproval softApproval;
	private InsuranceDecision insuranceDecision;
	private Repair repair;
	private Completed completed;
	private ClaimSettlement claimSettlement;
	private PartnerStageStatus partnerStageStatus;
	private InspectionAssessment inspectionAssessment;
	private PickupDetail pickup;
	private DeliveryDetail delivery;

	public DocumentUpload getDocumentUpload() {
		return documentUpload;
	}

	public void setDocumentUpload(DocumentUpload documentUpload) {
		this.documentUpload = documentUpload;
	}

	public Verification getVerification() {
		return verification;
	}

	public void setVerification(Verification verification) {
		this.verification = verification;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public ICDoc getIcDoc() {
		return icDoc;
	}

	public void setIcDoc(ICDoc icDoc) {
		this.icDoc = icDoc;
	}

	public RepairAssessment getRepairAssessment() {
		return repairAssessment;
	}

	public void setRepairAssessment(RepairAssessment repairAssessment) {
		this.repairAssessment = repairAssessment;
	}

	public SoftApproval getSoftApproval() {
		return softApproval;
	}

	public void setSoftApproval(SoftApproval softApproval) {
		this.softApproval = softApproval;
	}

	public InsuranceDecision getInsuranceDecision() {
		return insuranceDecision;
	}

	public void setInsuranceDecision(InsuranceDecision insuranceDecision) {
		this.insuranceDecision = insuranceDecision;
	}

	public Repair getRepair() {
		return repair;
	}

	public void setRepair(Repair repair) {
		this.repair = repair;
	}

	public Completed getCompleted() {
		return completed;
	}

	public void setCompleted(Completed completed) {
		this.completed = completed;
	}

	public ClaimSettlement getClaimSettlement() {
		return claimSettlement;
	}

	public void setClaimSettlement(ClaimSettlement claimSettlement) {
		this.claimSettlement = claimSettlement;
	}

	public PartnerStageStatus getPartnerStageStatus() {
		return partnerStageStatus;
	}

	public void setPartnerStageStatus(PartnerStageStatus partnerStageStatus) {
		this.partnerStageStatus = partnerStageStatus;
	}

	public InspectionAssessment getInspectionAssessment() {
		return inspectionAssessment;
	}

	public void setInspectionAssessment(InspectionAssessment inspectionAssessment) {
		this.inspectionAssessment = inspectionAssessment;
	}

	public PickupDetail getPickup() {
		return pickup;
	}

	public void setPickup(PickupDetail pickup) {
		this.pickup = pickup;
	}

	public DeliveryDetail getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryDetail delivery) {
		this.delivery = delivery;
	}

	@Override
	public String toString() {
		return "WorkflowData [documentUpload=" + documentUpload + ", verification=" + verification + ", visit=" + visit
				+ ", icDoc=" + icDoc + ", repairAssessment=" + repairAssessment + ", softApproval=" + softApproval
				+ ", insuranceDecision=" + insuranceDecision + ", repair=" + repair + ", completed=" + completed
				+ ", claimSettlement=" + claimSettlement + ", partnerStageStatus=" + partnerStageStatus
				+ ", inspectionAssessment=" + inspectionAssessment + ", pickup=" + pickup + ", delivery=" + delivery
				+ "]";
	}

}
