package com.oneassist.serviceplatform.commons.utils;

import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CMSPickupQcDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CMSRepairQcDetail;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.UpdateClaimDetailDto;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.util.StringUtils;

public class ServiceRequestDtoToUpdateClaimDetailDtoMapper extends PropertyMap<ServiceRequestDto, UpdateClaimDetailDto> {

    @Override
    protected void configure() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        map().setPartnerCode(source.getServicePartnerCode());
        map().setOcdClaimIdTmp(source.getExternalSRReferenceId());

        using(getOcdClaimId).map(source, destination.getOcdClaimId());

        map().setCloudStorageArchiveId(source.getCloudStorageArchiveId());
        map().setCloudStorageJobStatus(source.getCloudStorageJobStatus());
        using(getAssignee).map(source, destination.getAssignee());
        map().setOcdCustId(source.getCustomerId());
        map().setCustId(source.getCustomerId());

        using(getMemId).map(source, destination.getMemId());

        map().setModifiedBy(source.getModifiedBy());
        map().setStageName(source.getWorkflowStage());
        map().setOcdClaimStatus(source.getStatus());
        map().setStatus(source.getStatus());
        map().setCloseClaimReasonCode(source.getRemarks());
        map().setAddressLine1(source.getServiceRequestAddressDetails().getAddressLine1());
        map().setAddressLine2(source.getServiceRequestAddressDetails().getAddressLine2());

        map().setCity(source.getServiceRequestAddressDetails().getDistrict());
        map().setCommPincode(source.getServiceRequestAddressDetails().getPincode());
        map().setPincode(source.getServiceRequestAddressDetails().getPincode());
        map().setEmailid(source.getServiceRequestAddressDetails().getEmail());
        map().setCountryCode(source.getServiceRequestAddressDetails().getCountryCode());
        map().setLandmark(source.getServiceRequestAddressDetails().getLandmark());
        map().setMobile1(source.getServiceRequestAddressDetails().getMobileNo());

        map().setOcdActRepairDate(source.getWorkflowData().getRepair().getRepairDate());
        using(getClaimRepairQcDetails).map(source, destination.getClaimRepairQcDetails());
        map().setOcdRepairDueDate(source.getWorkflowData().getRepair().getRepairDueDate());
        map().setOcdFulfilmentHubId(source.getWorkflowData().getPickup().getFulfilmentHubId());
        using(getClaimPickupQcDetails).map(source, destination.getClaimPickupQcDetails());
        map().setOcdPickupDate(source.getWorkflowData().getPickup().getPickupDate());
        map().setClaimDeviceBreakDownDetails(source.getWorkflowData().getDocumentUpload().getDeviceBreakdownDetail());
        map().setClaimMobileDamageDetails(source.getWorkflowData().getDocumentUpload().getMobileDamageDetails());
        map().setClaimMobileLossDetails(source.getWorkflowData().getDocumentUpload().getMobileLossDetails());
        map().setOcddDocUploadStatus(source.getWorkflowData().getDocumentUpload().getStatus());
        map().setOcdDamageLossDateTime(source.getWorkflowData().getDocumentUpload().getDateOfIncident());
        map().setOcdIncidentDescription(source.getRequestDescription());
        map().setHardCopyDocSentToICDate(source.getWorkflowData().getIcDoc().getDocSentToICDate());
        map().setIcIntimationFlag(source.getWorkflowData().getIcDoc().getIntimated());
        map().setOcdAllDocSentIc(source.getWorkflowData().getIcDoc().getAllDocSentIc());

        map().setDeliveryICApproved(source.getWorkflowData().getInsuranceDecision().getDeliveryICApproved());
        map().setDeliveryICBerApproved(source.getWorkflowData().getInsuranceDecision().getDeliveryICBerApproved());
        map().setDeliveryICRejected(source.getWorkflowData().getInsuranceDecision().getDeliveryICRejected());
        map().setDeliveryToAscStatus(source.getWorkflowData().getInsuranceDecision().getDeliveryToAscStatus());
        map().setOcdIcUnderInsur(source.getWorkflowData().getInsuranceDecision().getIcUnderInsur());
        map().setOcdIcSalvageAmt(source.getWorkflowData().getInsuranceDecision().getSalvageAmt());
        map().setOcdIcReinstallPrem(source.getWorkflowData().getInsuranceDecision().getReinstallPrem());
        map().setOcdIcPaymentDate(source.getWorkflowData().getInsuranceDecision().getPaymentDate());
        map().setOcdExcessAmtApproved(source.getWorkflowData().getInsuranceDecision().getExcessAmtApproved());
        map().setOcdExcessAmtReceived(source.getWorkflowData().getInsuranceDecision().getExcessAmtReceived());
        map().setOcdIcPaymentAmt(source.getWorkflowData().getInsuranceDecision().getPaymentAmt());
        map().setOcdIcDecision(source.getWorkflowData().getInsuranceDecision().getIcDecision());
        map().setOcdIcDepriciation(source.getWorkflowData().getInsuranceDecision().getIcDepriciation());
        map().setOcdIcEstimateAmount(source.getWorkflowData().getInsuranceDecision().getIcEstimateAmt());
        map().setOcdIcExcessAmt(source.getWorkflowData().getInsuranceDecision().getIcExcessAmt());

        using(getOcdAmtToCust).map(source, destination.getOcdAmtToCust());
        using(getAmtPaidByOneassistToASC).map(source, destination.getAmtPaidByOneassistToASC());

        map().setEstimationCompletedDate(source.getWorkflowData().getRepairAssessment().getEndTime());
        map().setEstimationStatus(source.getWorkflowData().getRepairAssessment().getStatus());

        map().setOcdActDelvDate(source.getWorkflowData().getDelivery().getActDelvDate());
        map().setOcdDelvDueDate(source.getWorkflowData().getDelivery().getDelvDueDate());

        map().setOcdDamageLossDateTimeVerRemarks(source.getWorkflowData().getVerification().getDamageLossDateTimeVerRemarks());
        map().setOcdDamageLossDateTimeVerStat(source.getWorkflowData().getVerification().getDamageLossDateTimeVerStat());
        map().setVerificationNewCases(source.getWorkflowData().getVerification().getVerificationNewCases());
        map().setOcdVerDocAllStatus(source.getWorkflowData().getVerification().getStatus());

        using(getImei).map(source, destination.getImei());
        using(getMobMake).map(source, destination.getMobMake());
        using(getMobModel).map(source, destination.getMobModel());
        using(getProdCode).map(source, destination.getProdCode());

    }

    Converter<ServiceRequestDto, String> getImei = new AbstractConverter<ServiceRequestDto, String>() {

        @Override
        protected String convert(ServiceRequestDto entity) {

            {
                if (!CollectionUtils.isEmpty(entity.getAssets())) {
                    return entity.getAssets().get(0).getSerialNo();
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, String> getMobMake = new AbstractConverter<ServiceRequestDto, String>() {

        @Override
        protected String convert(ServiceRequestDto entity) {

            {
                if (!CollectionUtils.isEmpty(entity.getAssets())) {
                    return entity.getAssets().get(0).getMake();
                }
                return null;

            }
        }
    };
    Converter<ServiceRequestDto, String> getMobModel = new AbstractConverter<ServiceRequestDto, String>() {

        @Override
        protected String convert(ServiceRequestDto entity) {

            {
                if (!CollectionUtils.isEmpty(entity.getAssets())) {
                    return entity.getAssets().get(0).getModelNo();
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, String> getProdCode = new AbstractConverter<ServiceRequestDto, String>() {

        @Override
        protected String convert(ServiceRequestDto entity) {

            {
                if (!CollectionUtils.isEmpty(entity.getAssets())) {
                    return entity.getAssets().get(0).getProductCode();
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, CMSRepairQcDetail> getClaimRepairQcDetails = new AbstractConverter<ServiceRequestDto, CMSRepairQcDetail>() {

        @Override
        protected CMSRepairQcDetail convert(ServiceRequestDto entity) {

            {
                ObjectMapper objectMapper = new ObjectMapper();

                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                objectMapper.setSerializationInclusion(Include.NON_EMPTY);
                if (entity.getWorkflowData() != null && entity.getWorkflowData().getRepair() != null) {
                    try {
                        return objectMapper.readValue(objectMapper.writeValueAsString(entity.getWorkflowData().getRepair()), CMSRepairQcDetail.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;

            }
        }
    };

    Converter<ServiceRequestDto, CMSPickupQcDetail> getClaimPickupQcDetails = new AbstractConverter<ServiceRequestDto, CMSPickupQcDetail>() {

        @Override
        protected CMSPickupQcDetail convert(ServiceRequestDto entity) {

            {
                ObjectMapper objectMapper = new ObjectMapper();

                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                objectMapper.setSerializationInclusion(Include.NON_EMPTY);
                if (entity.getWorkflowData() != null && entity.getWorkflowData().getPickup() != null) {
                    try {
                        return objectMapper.readValue(objectMapper.writeValueAsString(entity.getWorkflowData().getPickup()), CMSPickupQcDetail.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;

            }
        }
    };
    Converter<ServiceRequestDto, Long> getOcdClaimId = new AbstractConverter<ServiceRequestDto, Long>() {

        @Override
        protected Long convert(ServiceRequestDto entity) {
            {
                if (entity.getRefPrimaryTrackingNo() != null) {
                    return Long.parseLong(entity.getRefPrimaryTrackingNo());
                }
                return null;

            }
        }
    };

    Converter<ServiceRequestDto, String> getAssignee = new AbstractConverter<ServiceRequestDto, String>() {

        @Override
        protected String convert(ServiceRequestDto entity) {
            {
                if (!StringUtils.isEmpty(entity.getAssignee())) {
                    return entity.getAssignee().toString();
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, Long> getMemId = new AbstractConverter<ServiceRequestDto, Long>() {

        @Override
        protected Long convert(ServiceRequestDto entity) {
            {
                if (entity.getReferenceNo() != null) {

                    return Long.parseLong(entity.getReferenceNo().toString());
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, Double> getOcdAmtToCust = new AbstractConverter<ServiceRequestDto, Double>() {

        @Override
        protected Double convert(ServiceRequestDto entity) {
            {
                if (entity.getReferenceNo() != null) {

                    return new Double(entity.getReferenceNo().toString());
                }
                return null;
            }
        }
    };
    Converter<ServiceRequestDto, Double> getAmtPaidByOneassistToASC = new AbstractConverter<ServiceRequestDto, Double>() {

        @Override
        protected Double convert(ServiceRequestDto entity) {
            {
                if (entity.getReferenceNo() != null) {

                    return new Double(entity.getReferenceNo().toString());
                }
                return null;
            }
        }
    };
}
