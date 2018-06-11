package com.oneassist.serviceplatform.commons.utils;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.EditClaimDetailDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

public class ServiceRequestDtoToEditClaimDetailDtoMapper extends PropertyMap<ServiceRequestDto, EditClaimDetailDto> {

    @Override
    protected void configure() {
        map().setOcdClaimIdTmp(source.getExternalSRReferenceId());

        using(getOcdClaimId).map(source, destination.getOcdClaimId());

        map().setOcdDamageLossDateTime(source.getWorkflowData().getDocumentUpload().getDateOfIncident());
        map().setOcdIncidentDescription(source.getRequestDescription());
    }

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

}
