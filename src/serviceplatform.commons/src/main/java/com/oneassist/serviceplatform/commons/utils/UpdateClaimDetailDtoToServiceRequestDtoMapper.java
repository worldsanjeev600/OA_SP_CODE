package com.oneassist.serviceplatform.commons.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestStatus;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DeliveryDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentUpload;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ICDoc;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.InsuranceDecision;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PickupDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Repair;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.RepairAssessment;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.UpdateClaimDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Verification;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;
import org.apache.log4j.Logger;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.util.StringUtils;

public class UpdateClaimDetailDtoToServiceRequestDtoMapper extends PropertyMap<UpdateClaimDetailDto, ServiceRequestDto> {

    private static final Logger logger = Logger.getLogger(UpdateClaimDetailDtoToServiceRequestDtoMapper.class);

    private static String ADVICE_ID_PARAM_NAME = "paymentReqId";

    @Override
    protected void configure() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        map().setServicePartnerCode(source.getPartnerCode());
        map().setExternalSRReferenceId(source.getOcdClaimIdTmp());
        using(getRefPrimaryTrackingNo).map(source, destination.getRefPrimaryTrackingNo());
        map().setCloudStorageArchiveId(source.getCloudStorageArchiveId());
        map().setCloudStorageJobStatus(source.getCloudStorageJobStatus());
        map().setServiceRequestType(source.getClaimType());
        using(getWorkflowProcessId).map(source, destination.getWorkflowProcessId());

        using(getAssignee).map(source, destination.getAssignee());
        map().setCustomerId(source.getOcdCustId());

        using(getReferenceNo).map(source, destination.getReferenceNo());
        map().setModifiedBy(source.getModifiedBy());
        map().setWorkflowStage(source.getStageName());
        map().setStatus(source.getOcdClaimStatus());

        using(getRemarks).map(source, destination.getRemarks());
        using(getAdviceId).map(source, destination.getAdviceId());
        map().getServiceRequestAddressDetails().setAddressLine1(source.getCommAddr());
        map().getServiceRequestAddressDetails().setAddressLine2(source.getAddressLine2());
        map().getServiceRequestAddressDetails().setDistrict(source.getCity());
        map().getServiceRequestAddressDetails().setPincode(source.getCommPincode());
        map().getServiceRequestAddressDetails().setEmail(source.getEmailid());
        map().getServiceRequestAddressDetails().setCountryCode(source.getCountryCode());
        map().getServiceRequestAddressDetails().setLandmark(source.getLandmark());
        map().getServiceRequestAddressDetails().setMobileNo(source.getMobile1());

        using(getWorkFlowdata).map(source, destination.getWorkflowData());
        using(getThirdPartyProperties).map(source, destination.getThirdPartyProperties());
        using(getAssets).map(source, destination.getAssets());
    }

    Converter<UpdateClaimDetailDto, WorkflowData> getWorkFlowdata = new AbstractConverter<UpdateClaimDetailDto, WorkflowData>() {

        @Override
        protected WorkflowData convert(UpdateClaimDetailDto updateClaimDetailDto) {

            {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();

                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    objectMapper.setSerializationInclusion(Include.NON_NULL);
                    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
                    WorkflowData workflowData = new WorkflowData();
                    Repair repair = null;
                    try {
                        repair = objectMapper.readValue(objectMapper.writeValueAsString(updateClaimDetailDto.getClaimRepairQcDetails()), Repair.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (repair == null) {
                        repair = new Repair();
                    }
                    repair.setRepairDate(getFormattedDate(updateClaimDetailDto.getOcdActRepairDate()));
                    repair.setRepairDueDate(getFormattedDate(updateClaimDetailDto.getOcdRepairDueDate()));
                    workflowData.setRepair(repair);

                    PickupDetail pickup = null;
                    try {
                        pickup = objectMapper.readValue(objectMapper.writeValueAsString(updateClaimDetailDto.getClaimPickupQcDetails()), PickupDetail.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pickup == null) {
                        pickup = new PickupDetail();
                    }
                    pickup.setFulfilmentHubId(updateClaimDetailDto.getOcdFulfilmentHubId());
                    pickup.setPickupDate(getFormattedDate(updateClaimDetailDto.getOcdPickupDate()));
                    workflowData.setPickup(pickup);

                    DocumentUpload documentUpload = new DocumentUpload();
                    documentUpload.setDeviceBreakdownDetail(updateClaimDetailDto.getClaimDeviceBreakDownDetails());
                    documentUpload.setMobileDamageDetails(updateClaimDetailDto.getClaimMobileDamageDetails());
                    documentUpload.setMobileLossDetails(updateClaimDetailDto.getClaimMobileLossDetails());
                    if (Constants.CMS_COMPLETE_STATUS_CODE.equalsIgnoreCase(updateClaimDetailDto.getOcddDocUploadStatus())) {
                        documentUpload.setStatusCode(ServiceRequestStatus.COMPLETED.getStatus());
                        documentUpload.setStatus(ServiceRequestStatus.COMPLETED.getValue());
                    }
                    documentUpload.setDateOfIncident(getFormattedDate(updateClaimDetailDto.getOcdDamageLossDateTime()));

                    workflowData.setDocumentUpload(documentUpload);

                    ICDoc icDoc = new ICDoc();
                    icDoc.setDocSentToICDate(getFormattedDate(updateClaimDetailDto.getHardCopyDocSentToICDate()));
                    icDoc.setIntimated(updateClaimDetailDto.getIcIntimationFlag());
                    icDoc.setAllDocSentIc(updateClaimDetailDto.getOcdAllDocSentIc());

                    workflowData.setIcDoc(icDoc);

                    InsuranceDecision insuranceDecision = new InsuranceDecision();
                    insuranceDecision.setDeliveryICApproved(updateClaimDetailDto.getDeliveryICApproved());
                    insuranceDecision.setDeliveryICBerApproved(updateClaimDetailDto.getDeliveryICBerApproved());
                    insuranceDecision.setDeliveryICRejected(updateClaimDetailDto.getDeliveryICRejected());
                    insuranceDecision.setDeliveryToAscStatus(updateClaimDetailDto.getDeliveryToAscStatus());
                    insuranceDecision.setIcUnderInsur(updateClaimDetailDto.getOcdIcUnderInsur());
                    insuranceDecision.setSalvageAmt(updateClaimDetailDto.getOcdIcSalvageAmt());
                    insuranceDecision.setReinstallPrem(updateClaimDetailDto.getOcdIcReinstallPrem());
                    insuranceDecision.setPaymentDate(getFormattedDate(updateClaimDetailDto.getOcdIcPaymentDate()));
                    insuranceDecision.setExcessAmtApproved(updateClaimDetailDto.getOcdExcessAmtApproved());
                    insuranceDecision.setExcessAmtReceived(updateClaimDetailDto.getOcdExcessAmtReceived());
                    insuranceDecision.setPaymentAmt(updateClaimDetailDto.getOcdIcPaymentAmt());
                    insuranceDecision.setIcDecision(updateClaimDetailDto.getOcdIcDecision());
                    insuranceDecision.setIcDepriciation(updateClaimDetailDto.getOcdIcDepriciation());
                    insuranceDecision.setIcEstimateAmt(updateClaimDetailDto.getOcdIcEstimateAmount());

                    insuranceDecision.setIcExcessAmt(updateClaimDetailDto.getOcdIcExcessAmt());

                    workflowData.setInsuranceDecision(insuranceDecision);

                    RepairAssessment repairAssessment = new RepairAssessment();
                    if (updateClaimDetailDto.getOcdAmtToCust() != null) {
                        repairAssessment.setCostToCustomer(updateClaimDetailDto.getOcdAmtToCust().toString());
                    }
                    repairAssessment.setEndTime(getFormattedDate(updateClaimDetailDto.getEstimationCompletedDate()));
                    if (updateClaimDetailDto.getAmtPaidByOneassistToASC() != null) {
                        repairAssessment.setCostToCompany(updateClaimDetailDto.getAmtPaidByOneassistToASC().toString());
                    }

                    repairAssessment.setEstimateAmt(updateClaimDetailDto.getOcdEstimateAmt());
                    repairAssessment.setStatus(updateClaimDetailDto.getEstimationStatus());

                    workflowData.setRepairAssessment(repairAssessment);

                    DeliveryDetail delivery = new DeliveryDetail();
                    delivery.setActDelvDate(getFormattedDate(updateClaimDetailDto.getOcdActDelvDate()));
                    delivery.setDelvDueDate(getFormattedDate(updateClaimDetailDto.getOcdDelvDueDate()));

                    workflowData.setDelivery(delivery);

                    Verification verification = new Verification();
                    verification.setDamageLossDateTimeVerRemarks(updateClaimDetailDto.getOcdDamageLossDateTimeVerRemarks());
                    verification.setDamageLossDateTimeVerStat(updateClaimDetailDto.getOcdDamageLossDateTimeVerStat());
                    verification.setVerificationNewCases(updateClaimDetailDto.getVerificationNewCases());
                    verification.setStatus(updateClaimDetailDto.getOcdVerDocAllStatus());
                    verification.setDamageLossDateTimeVerRemarks(updateClaimDetailDto.getOcdDamageLossDateTimeVerRemarks());
                    workflowData.setVerification(verification);
                    return workflowData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }
    };

    Converter<UpdateClaimDetailDto, Map<String, Object>> getThirdPartyProperties = new AbstractConverter<UpdateClaimDetailDto, Map<String, Object>>() {

        @Override
        protected Map<String, Object> convert(UpdateClaimDetailDto updateClaimDetailDto) {

            {
                Map<String, Object> thirdpartyProperties = new HashMap<String, Object>();
                thirdpartyProperties.put(Constants.CLAIM_COUNT_PARAM_NAME, updateClaimDetailDto.getClaimCount());
                thirdpartyProperties.put(Constants.MODEL_VERSION_PARAM_NAME, updateClaimDetailDto.getModelVersion());
                thirdpartyProperties.put(Constants.MU_PARAM_NAME, updateClaimDetailDto.getMu());
                thirdpartyProperties.put(Constants.ASSET_INVOICE_PARAM_NAME, updateClaimDetailDto.getOcdAssetInvoiceNo());
                thirdpartyProperties.put(Constants.MARKET_VALUE_PARAM_NAME, updateClaimDetailDto.getOcdIcMarketValue());
                thirdpartyProperties.put(Constants.COURTESY_REQUIRED_PARAM_NAME, updateClaimDetailDto.getCourtesyRequired());
                thirdpartyProperties.put(Constants.DEVICE_WARRANTY_PARAM_NAME, updateClaimDetailDto.getDeviceWarranty());
                thirdpartyProperties.put(Constants.DOCS_NEVER_UPLOADED_PARAM_NAME, updateClaimDetailDto.getDocsNeverUploaded());

                thirdpartyProperties.put(Constants.DOCS_RECEIVED_VIA_EMAIL, updateClaimDetailDto.getDocsRecievedViaEmail());

                thirdpartyProperties.put(Constants.EXPECTED_DELIVERY_DATE_FORM, updateClaimDetailDto.getExpectedDeliveryDateForm());

                thirdpartyProperties.put(Constants.HUB_ID_PARAM_NAME, updateClaimDetailDto.getOcdHubId());
                thirdpartyProperties.put(Constants.PARTNER_BU_CODE_PARAM_NAME, updateClaimDetailDto.getPartnerBUCode());

                thirdpartyProperties.put(Constants.PICKUP_FROM_ASC_STATUS_PARAM_NAME, updateClaimDetailDto.getPickupFromAscStatus());

                thirdpartyProperties.put(Constants.PLAN_CODE_PARAM_NAME, updateClaimDetailDto.getPlanCode());

                thirdpartyProperties.put(Constants.REQUIREMENT_TRIGGERED_PARAM_NAME, updateClaimDetailDto.getRequirementTriggered());

                thirdpartyProperties.put(Constants.SERVICE_ID_PARAM_NAME, updateClaimDetailDto.getServiceId());
                thirdpartyProperties.put(Constants.SIGMA_PARAM_NAME, updateClaimDetailDto.getSigma());

                thirdpartyProperties.put(Constants.SUM_ASSURED_PARAM_NAME, updateClaimDetailDto.getSumAssured());

                thirdpartyProperties.put(Constants.TAT_VALUE_PARAM_NAME, updateClaimDetailDto.getTatValue());
                thirdpartyProperties.put(Constants.TAT_PARAM_NAME, updateClaimDetailDto.getTat());
                try {
                    thirdpartyProperties.put(Constants.PARTNER_ATTRIBUTE_DETAILS_PARAM_NAME, new ObjectMapper().writeValueAsString(updateClaimDetailDto.getClaimPartnerAttributesdetails()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                thirdpartyProperties.put(Constants.START_DATE_PARAM_NAME, updateClaimDetailDto.getStartDate());
                return thirdpartyProperties;
            }
        }
    };

    Converter<UpdateClaimDetailDto, List<ServiceRequestAssetRequestDto>> getAssets = new AbstractConverter<UpdateClaimDetailDto, List<ServiceRequestAssetRequestDto>>() {

        @Override
        protected List<ServiceRequestAssetRequestDto> convert(UpdateClaimDetailDto updateClaimDetailDto) {

            {
                List<ServiceRequestAssetRequestDto> assets = new ArrayList<ServiceRequestAssetRequestDto>();
                ServiceRequestAssetRequestDto asset = new ServiceRequestAssetRequestDto();
                asset.setSerialNo(updateClaimDetailDto.getImei());
                asset.setModelNo(updateClaimDetailDto.getMobModel());
                asset.setMake(updateClaimDetailDto.getMobMake());
                asset.setProductCode(updateClaimDetailDto.getProdCode());
                assets.add(asset);
                return assets;
            }
        }
    };
    Converter<UpdateClaimDetailDto, Long> getAssignee = new AbstractConverter<UpdateClaimDetailDto, Long>() {

        @Override
        protected Long convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                if (!StringUtils.isEmpty(updateClaimDetailDto.getAssignee())) {
                    try {
                        return Long.parseLong(updateClaimDetailDto.getAssignee());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }
    };
    Converter<UpdateClaimDetailDto, String> getReferenceNo = new AbstractConverter<UpdateClaimDetailDto, String>() {

        @Override
        protected String convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                if (updateClaimDetailDto.getMemId() != null) {

                    return updateClaimDetailDto.getMemId().toString();
                }
                return null;
            }
        }
    };
    Converter<UpdateClaimDetailDto, String> getRefPrimaryTrackingNo = new AbstractConverter<UpdateClaimDetailDto, String>() {

        @Override
        protected String convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                if (!StringUtils.isEmpty(updateClaimDetailDto.getOcdClaimId())) {
                    return updateClaimDetailDto.getOcdClaimId().toString();
                }
                return null;
            }
        }
    };

    private String getFormattedDate(String date) {
        try {
            if (date != null) {
                date = DateUtils.toLongFormattedString(new Date(Long.parseLong(date)));
            }
        } catch (Exception e) {
            logger.error("Exception while parsing date:::" + date);
        }
        return date;
    }

    Converter<UpdateClaimDetailDto, String> getAdviceId = new AbstractConverter<UpdateClaimDetailDto, String>() {

        @Override
        protected String convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                if (updateClaimDetailDto.getOcdClaimAssetAttributes() != null && updateClaimDetailDto.getOcdClaimAssetAttributes().get(ADVICE_ID_PARAM_NAME) != null) {
                    return updateClaimDetailDto.getOcdClaimAssetAttributes().get(ADVICE_ID_PARAM_NAME).toString();
                }
                return null;
            }
        }
    };

    Converter<UpdateClaimDetailDto, String> getWorkflowProcessId = new AbstractConverter<UpdateClaimDetailDto, String>() {

        @Override
        protected String convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                if (updateClaimDetailDto.getOcdActivitiProcId() != null) {
                    return updateClaimDetailDto.getOcdActivitiProcId().toString();
                }
                return null;
            }
        }
    };

    Converter<UpdateClaimDetailDto, String> getRemarks = new AbstractConverter<UpdateClaimDetailDto, String>() {

        @Override
        protected String convert(UpdateClaimDetailDto updateClaimDetailDto) {
            {
                return updateClaimDetailDto.getCloseClaimReasonCode() != null ? updateClaimDetailDto.getCloseClaimReasonCode() : updateClaimDetailDto.getIcRejectReason();
            }
        }
    };
}
