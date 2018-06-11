package com.oneassist.serviceplatform.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.CreateClaimDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import org.modelmapper.PropertyMap;

public class ServiceReqeustDtoToCreateClaimDtoMapper extends PropertyMap<ServiceRequestDto, CreateClaimDto> {

    @Override
    protected void configure() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);

        map().setCustomerId(source.getCustomerId());
        map().setMemId(source.getReferenceNo());
        map().setInitiatingSystem(source.getInitiatingSystem());
        map().setCreatedBy(source.getCreatedBy());
        map().setClaimType(source.getServiceRequestType());
        map().getClaimAddressDetails().setAddressId(source.getServiceRequestAddressDetails().getAddressId());
        map().getClaimAddressDetails().setAddressType(source.getServiceRequestAddressDetails().getAddressType());
        map().getClaimAddressDetails().setAddressLine1(source.getServiceRequestAddressDetails().getAddressLine1());
        map().getClaimAddressDetails().setAddressLine2(source.getServiceRequestAddressDetails().getAddressLine2());
        map().getClaimAddressDetails().setPincode(source.getServiceRequestAddressDetails().getPincode());
        map().getClaimAddressDetails().setState(source.getServiceRequestAddressDetails().getState());
        map().getClaimAddressDetails().setCity(source.getServiceRequestAddressDetails().getCity());
        map().setPlaceOfIncident(source.getWorkflowData().getDocumentUpload().getPlaceOfIncident());
        map().setIncidentDescription(source.getRequestDescription());
        map().setDateOfIncident(source.getWorkflowData().getDocumentUpload().getDateOfIncident());

        map().setClaimMobileLossDetails(source.getWorkflowData().getDocumentUpload().getMobileLossDetails());

        map().setClaimMobileDamageDetails(source.getWorkflowData().getDocumentUpload().getMobileDamageDetails());

        map().setClaimDeviceBreakDownDetails(source.getWorkflowData().getDocumentUpload().getDeviceBreakdownDetail());

    }
}
