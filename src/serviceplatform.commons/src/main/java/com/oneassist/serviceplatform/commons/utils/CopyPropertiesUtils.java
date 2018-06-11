package com.oneassist.serviceplatform.commons.utils;

import java.util.HashMap;
import java.util.Map;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ClaimSettlement;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Completed;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DeliveryDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PickupDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Visit;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class CopyPropertiesUtils {

    public static WorkflowData copyNotNullWorkflowData(WorkflowData source, WorkflowData destination) {
        if (destination == null) {
            destination = new WorkflowData();
        }
        if (source.getClaimSettlement() != null) {
            ClaimSettlement srcClaimSettlement = source.getClaimSettlement();
            ClaimSettlement claimSettlement = destination.getClaimSettlement() != null ? destination.getClaimSettlement() : new ClaimSettlement();
            if (isNotEmpty(srcClaimSettlement.getClaimAmount())) {
                claimSettlement.setClaimAmount(srcClaimSettlement.getClaimAmount());
            }
            if (isNotEmpty(srcClaimSettlement.getIcPaymentConfirmation())) {
                claimSettlement.setIcPaymentConfirmation(srcClaimSettlement.getIcPaymentConfirmation());
            }
            if (isNotEmpty(srcClaimSettlement.getRemarks())) {
                claimSettlement.setRemarks(srcClaimSettlement.getRemarks());
            }
            if (isNotEmpty(srcClaimSettlement.getSettlementDate())) {
                claimSettlement.setSettlementDate(srcClaimSettlement.getSettlementDate());
            }
            if (isNotEmpty(srcClaimSettlement.getStatus())) {
                claimSettlement.setStatus(srcClaimSettlement.getStatus());
            }
            if (isNotEmpty(srcClaimSettlement.getStatusCode())) {
                claimSettlement.setStatusCode(srcClaimSettlement.getStatusCode());
            }
            if(isNotEmpty(srcClaimSettlement.getDescription())){
            	claimSettlement.setDescription(srcClaimSettlement.getDescription());
            }
            destination.setClaimSettlement(claimSettlement);
        }
        if (source.getCompleted() != null) {
            Completed srcCompleted = source.getCompleted();
            Completed completed = destination.getCompleted() != null ? destination.getCompleted() : new Completed();
            if (isNotEmpty(srcCompleted.getDescription())) {
                completed.setDescription(srcCompleted.getDescription());
            }
            if (isNotEmpty(srcCompleted.getRefundAmount())) {
                completed.setRefundAmount(srcCompleted.getRefundAmount());
            }
            if (isNotEmpty(srcCompleted.getRemarks())) {
                completed.setRemarks(srcCompleted.getRemarks());
            }
            if (isNotEmpty(srcCompleted.getServiceCancelReason())) {
                completed.setServiceCancelReason(srcCompleted.getServiceCancelReason());
            }
            if (isNotEmpty(srcCompleted.getStatus())) {
                completed.setStatus(srcCompleted.getStatus());
            }
            if (isNotEmpty(srcCompleted.getStatusCode())) {
                completed.setStatusCode(srcCompleted.getStatusCode());
            }
            if (isNotEmpty(srcCompleted.getDescription())) {
                completed.setDescription(srcCompleted.getDescription());
            }
            destination.setCompleted(completed);
        }
        if (source.getDelivery() != null) {
            DeliveryDetail srcDeliveryDetail = source.getDelivery();
            DeliveryDetail deliveryDetail = destination.getDelivery() != null ? destination.getDelivery() : new DeliveryDetail();
            if (isNotEmpty(srcDeliveryDetail.getStatus())) {
                deliveryDetail.setStatus(srcDeliveryDetail.getStatus());
            }
            if (isNotEmpty(srcDeliveryDetail.getStatusCode())) {
                deliveryDetail.setStatusCode(srcDeliveryDetail.getStatusCode());
            }
            if (isNotEmpty(srcDeliveryDetail.getActDelvDate())) {
                deliveryDetail.setActDelvDate(srcDeliveryDetail.getActDelvDate());
            }
            if (isNotEmpty(srcDeliveryDetail.getDelvDueDate())) {
                deliveryDetail.setDelvDueDate(srcDeliveryDetail.getDelvDueDate());
            }
            if (isNotEmpty(srcDeliveryDetail.getDescription())) {
                deliveryDetail.setDescription(srcDeliveryDetail.getDescription());
            }
            
            destination.setDelivery(deliveryDetail);
        }
        if (source.getDocumentUpload() != null) {
            DocumentUpload srcDocumentUpload = source.getDocumentUpload();
            DocumentUpload documentUpload = destination.getDocumentUpload() != null ? destination.getDocumentUpload() : new DocumentUpload();
            if (isNotEmpty(srcDocumentUpload.getStatus())) {
                documentUpload.setStatus(srcDocumentUpload.getStatus());
            }
            if (isNotEmpty(srcDocumentUpload.getStatusCode())) {
                documentUpload.setStatusCode(srcDocumentUpload.getStatusCode());
            }
            if (isNotEmpty(srcDocumentUpload.getDateOfIncident())) {
                documentUpload.setDateOfIncident(srcDocumentUpload.getDateOfIncident());
            }
            if (isNotEmpty(srcDocumentUpload.getDescription())) {
                documentUpload.setDescription(srcDocumentUpload.getDescription());
            }
            if (isNotEmpty(srcDocumentUpload.getPlaceOfIncident())) {
                documentUpload.setPlaceOfIncident(srcDocumentUpload.getPlaceOfIncident());
            }
            if (isNotEmpty(srcDocumentUpload.getDescription())) {
                documentUpload.setDescription(srcDocumentUpload.getDescription());
            }
            destination.setDocumentUpload(documentUpload);
        }
        if (source.getIcDoc() != null) {
            ICDoc srcIcDoc = source.getIcDoc();
            ICDoc icDoc = destination.getIcDoc() != null ? destination.getIcDoc() : new ICDoc();
            if (isNotEmpty(srcIcDoc.getStatus())) {
                icDoc.setStatus(srcIcDoc.getStatus());
            }
            if (isNotEmpty(srcIcDoc.getStatusCode())) {
                icDoc.setStatusCode(srcIcDoc.getStatusCode());
            }
            if (isNotEmpty(srcIcDoc.getDocSentToIC())) {
                icDoc.setDocSentToIC(srcIcDoc.getDocSentToIC());
            }
            if (isNotEmpty(srcIcDoc.getAllDocSentIc())) {
                icDoc.setAllDocSentIc(srcIcDoc.getAllDocSentIc());
            }
            if (isNotEmpty(srcIcDoc.getDocSentToICDate())) {
                icDoc.setDocSentToICDate(srcIcDoc.getDocSentToICDate());
            }
            if (isNotEmpty(srcIcDoc.getEstimatedInvoiceVerificationStatus())) {
                icDoc.setEstimatedInvoiceVerificationStatus(srcIcDoc.getEstimatedInvoiceVerificationStatus());
            }
            if (isNotEmpty(srcIcDoc.getIntimated())) {
                icDoc.setIntimated(srcIcDoc.getIntimated());
            }
            destination.setIcDoc(icDoc);
        }
        if (source.getInsuranceDecision() != null) {
            InsuranceDecision srcInsuranceDecision = source.getInsuranceDecision();
            InsuranceDecision insuranceDecision = destination.getInsuranceDecision() != null ? destination.getInsuranceDecision() : new InsuranceDecision();
            if (isNotEmpty(srcInsuranceDecision.getStatus())) {
                insuranceDecision.setStatus(srcInsuranceDecision.getStatus());
            }
            if (isNotEmpty(srcInsuranceDecision.getStatusCode())) {
                insuranceDecision.setStatusCode(srcInsuranceDecision.getStatusCode());
            }
            if (isNotEmpty(srcInsuranceDecision.getDeliveryICApproved())) {
                insuranceDecision.setDeliveryICApproved(srcInsuranceDecision.getDeliveryICApproved());
            }
            if (isNotEmpty(srcInsuranceDecision.getDeliveryICBerApproved())) {
                insuranceDecision.setDeliveryICBerApproved(srcInsuranceDecision.getDeliveryICBerApproved());
            }
            if (isNotEmpty(srcInsuranceDecision.getDeliveryICRejected())) {
                insuranceDecision.setDeliveryICRejected(srcInsuranceDecision.getDeliveryICRejected());
            }
            if (isNotEmpty(srcInsuranceDecision.getDeliveryToAscStatus())) {
                insuranceDecision.setDeliveryToAscStatus(srcInsuranceDecision.getDeliveryToAscStatus());
            }
            if (isNotEmpty(srcInsuranceDecision.getDescription())) {
                insuranceDecision.setDescription(srcInsuranceDecision.getDescription());
            }
            if (isNotEmpty(srcInsuranceDecision.getExcessAmtApproved())) {
                insuranceDecision.setExcessAmtApproved(srcInsuranceDecision.getExcessAmtApproved());
            }
            if (isNotEmpty(srcInsuranceDecision.getExcessAmtReceived())) {
                insuranceDecision.setExcessAmtReceived(srcInsuranceDecision.getExcessAmtReceived());
            }
            if (isNotEmpty(srcInsuranceDecision.getIcDecision())) {
                insuranceDecision.setIcDecision(srcInsuranceDecision.getIcDecision());
            }
            if (isNotEmpty(srcInsuranceDecision.getIcDepriciation())) {
                insuranceDecision.setIcDepriciation(srcInsuranceDecision.getIcDepriciation());
            }
            if (isNotEmpty(srcInsuranceDecision.getIcEstimateAmt())) {
                insuranceDecision.setIcEstimateAmt(srcInsuranceDecision.getIcEstimateAmt());
            }
            if (isNotEmpty(srcInsuranceDecision.getIcUnderInsur())) {
                insuranceDecision.setIcUnderInsur(srcInsuranceDecision.getIcUnderInsur());
            }
            if (isNotEmpty(srcInsuranceDecision.getPaymentAmt())) {
                insuranceDecision.setPaymentAmt(srcInsuranceDecision.getPaymentAmt());
            }
            if (isNotEmpty(srcInsuranceDecision.getPaymentDate())) {
                insuranceDecision.setPaymentDate(srcInsuranceDecision.getPaymentDate());
            }
            if (isNotEmpty(srcInsuranceDecision.getReinstallPrem())) {
                insuranceDecision.setReinstallPrem(srcInsuranceDecision.getReinstallPrem());
            }
            if (isNotEmpty(srcInsuranceDecision.getSalvageAmt())) {
                insuranceDecision.setSalvageAmt(srcInsuranceDecision.getSalvageAmt());
            }
            if (isNotEmpty(srcInsuranceDecision.getDescription())) {
                insuranceDecision.setDescription(srcInsuranceDecision.getDescription());
            }
            destination.setInsuranceDecision(insuranceDecision);
        }
        if (source.getPickup() != null) {
            PickupDetail srcPickupDetail = source.getPickup();
            PickupDetail pickupDetail = destination.getPickup() != null ? destination.getPickup() : new PickupDetail();
            if (isNotEmpty(srcPickupDetail.getStatus())) {
                pickupDetail.setStatus(srcPickupDetail.getStatus());
            }
            if (isNotEmpty(srcPickupDetail.getStatusCode())) {
                pickupDetail.setStatusCode(srcPickupDetail.getStatusCode());
            }
            if (isNotEmpty(srcPickupDetail.getAscHub())) {
                pickupDetail.setAscHub(srcPickupDetail.getAscHub());
            }
            if (isNotEmpty(srcPickupDetail.getFulfilmentHubId())) {
                pickupDetail.setFulfilmentHubId(srcPickupDetail.getFulfilmentHubId());
            }
            if (isNotEmpty(srcPickupDetail.getPickupDate())) {
                pickupDetail.setPickupDate(srcPickupDetail.getPickupDate());
            }
            if (isNotEmpty(srcPickupDetail.getPickupQc())) {
                pickupDetail.setPickupQc(srcPickupDetail.getPickupQc());
            }
            if (isNotEmpty(srcPickupDetail.getVerifier())) {
                pickupDetail.setVerifier(srcPickupDetail.getVerifier());
            }
            if (isNotEmpty(srcPickupDetail.getDescription())) {
                pickupDetail.setDescription(srcPickupDetail.getDescription());
            }
            destination.setPickup(pickupDetail);
        }
        if (source.getRepair() != null) {
            Repair srcRepair = source.getRepair();
            Repair repair = destination.getRepair() != null ? destination.getRepair() : new Repair();
            if (isNotEmpty(srcRepair.getStatus())) {
                repair.setStatus(srcRepair.getStatus());
            }
            if (isNotEmpty(srcRepair.getStatusCode())) {
                repair.setStatusCode(srcRepair.getStatusCode());
            }
            if (isNotEmpty(srcRepair.getButtonsStatus())) {
                repair.setButtonsStatus(srcRepair.getButtonsStatus());
            }
            if (isNotEmpty(srcRepair.getCameraStatus())) {
                repair.setCameraStatus(srcRepair.getCameraStatus());
            }
            if (isNotEmpty(srcRepair.getChargePinStatus())) {
                repair.setChargePinStatus(srcRepair.getChargePinStatus());
            }
            if (isNotEmpty(srcRepair.getDispalyStatus())) {
                repair.setDispalyStatus(srcRepair.getDispalyStatus());
            }
            if (isNotEmpty(srcRepair.getHeadPhoneJackStatus())) {
                repair.setHeadPhoneJackStatus(srcRepair.getHeadPhoneJackStatus());
            }
            if (isNotEmpty(srcRepair.getMicrophoneStatus())) {
                repair.setMicrophoneStatus(srcRepair.getMicrophoneStatus());
            }
            if (isNotEmpty(srcRepair.getRepairable())) {
                repair.setRepairable(srcRepair.getRepairable());
            }
            if (isNotEmpty(srcRepair.getRepairDate())) {
                repair.setRepairDate(srcRepair.getRepairDate());
            }
            if (isNotEmpty(srcRepair.getRepairDueDate())) {
                repair.setRepairDueDate(srcRepair.getRepairDueDate());
            }

            if (isNotEmpty(srcRepair.getResolutionCode())) {
                repair.setResolutionCode(srcRepair.getResolutionCode());
            }
            if (isNotEmpty(srcRepair.getServiceEndCode())) {
                repair.setServiceEndCode(srcRepair.getServiceEndCode());
            }
            if (isNotEmpty(srcRepair.getSpeakerStatus())) {
                repair.setSpeakerStatus(srcRepair.getSpeakerStatus());
            }
            if (isNotEmpty(srcRepair.getTouchScreenStatus())) {
                repair.setTouchScreenStatus(srcRepair.getTouchScreenStatus());
            }
            if (isNotEmpty(srcRepair.getVerifier())) {
                repair.setVerifier(srcRepair.getVerifier());
            }
            if (isNotEmpty(srcRepair.getWifiStatus())) {
                repair.setWifiStatus(srcRepair.getWifiStatus());
            }
            if (isNotEmpty(srcRepair.getDescription())) {
                repair.setDescription(srcRepair.getDescription());
            }
            destination.setRepair(repair);
        }
        if (source.getRepairAssessment() != null) {
            RepairAssessment srcRepairAssessment = source.getRepairAssessment();
            RepairAssessment repairAssessment = destination.getRepairAssessment() != null ? destination.getRepairAssessment() : new RepairAssessment();
            if (isNotEmpty(srcRepairAssessment.getStatus())) {
                repairAssessment.setStatus(srcRepairAssessment.getStatus());
            }
            if (isNotEmpty(srcRepairAssessment.getStatusCode())) {
                repairAssessment.setStatusCode(srcRepairAssessment.getStatusCode());
            }
            if (isNotEmpty(srcRepairAssessment.getAccidentalDamage())) {
                repairAssessment.setAccidentalDamage(srcRepairAssessment.getAccidentalDamage());
            }
            if (isNotEmpty(srcRepairAssessment.getCostToCompany())) {
                repairAssessment.setCostToCompany(srcRepairAssessment.getCostToCompany());
            }
            if (isNotEmpty(srcRepairAssessment.getCostToCustomer())) {
                repairAssessment.setCostToCustomer(srcRepairAssessment.getCostToCustomer());
            }
            if (isNotEmpty(srcRepairAssessment.getCustomerDecision())) {
                repairAssessment.setCustomerDecision(srcRepairAssessment.getCustomerDecision());
            }
            if (isNotEmpty(srcRepairAssessment.getDescription())) {
                repairAssessment.setDescription(srcRepairAssessment.getDescription());
            }

            destination.setRepairAssessment(repairAssessment);
        }
        if (source.getVerification() != null) {
            Verification srcVerification = source.getVerification();
            Verification verification = destination.getVerification() != null ? destination.getVerification() : new Verification();
            if (isNotEmpty(srcVerification.getStatus())) {
                verification.setStatus(srcVerification.getStatus());
            }
            if (isNotEmpty(srcVerification.getStatusCode())) {
                verification.setStatusCode(srcVerification.getStatusCode());
            }
            if (isNotEmpty(srcVerification.getDamageLossDateTimeVerRemarks())) {
                verification.setDamageLossDateTimeVerRemarks(srcVerification.getDamageLossDateTimeVerRemarks());
            }

            if (isNotEmpty(srcVerification.getDocumentId())) {
                verification.setDocumentId(srcVerification.getDocumentId());
            }
            if (isNotEmpty(srcVerification.getVerificationNewCases())) {
                verification.setVerificationNewCases(srcVerification.getVerificationNewCases());
            }
            if (isNotEmpty(srcVerification.getRemarks())) {
                verification.setRemarks(srcVerification.getRemarks());
            }
            if (isNotEmpty(srcVerification.getDescription())) {
                verification.setDescription(srcVerification.getDescription());
            }
            destination.setVerification(verification);
        }
        if (source.getVisit() != null) {
            Visit srcVisit = source.getVisit();
            Visit visit = destination.getVisit() != null ? destination.getVisit() : new Visit();
            if (isNotEmpty(srcVisit.getStatus())) {
                visit.setStatus(srcVisit.getStatus());
            }
            if (isNotEmpty(srcVisit.getStatusCode())) {
                visit.setStatusCode(srcVisit.getStatusCode());
            }
            if (isNotEmpty(srcVisit.getServiceAddress())) {
                visit.setServiceAddress(srcVisit.getServiceAddress());
            }
            if (isNotEmpty(srcVisit.getDescription())) {
                visit.setDescription(srcVisit.getDescription());
            }
            destination.setVisit(visit);
        }
        return destination;
    }

    private static boolean isNotEmpty(Object value) {
        boolean isNotEmpty = false;
        if (value instanceof String) {
            if (!StringUtils.isEmpty(value)) {
                isNotEmpty = true;
            }
        } else if (value != null) {
            isNotEmpty = true;
        }
        return isNotEmpty;
    }

    public static Map<String, Object> copyNotNullThirdPartyProperties(Map<String, Object> source, Map<String, Object> destination) {
        if (destination == null) {
            destination = new HashMap<String, Object>();
        }
        if (!CollectionUtils.isEmpty(source)) {
            for (Map.Entry<String, Object> keyValue : source.entrySet()) {
                if (isNotEmpty(keyValue.getValue())) {
                    destination.put(keyValue.getKey(), keyValue.getValue());
                }
            }
        }
        return destination;
    }
}
