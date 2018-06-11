package com.oneassist.serviceplatform.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestFeedbackResponseDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.response.ServiceResponseDto;

@Component
public class ServiceRequestDtoToServiceResponseDtoMapper extends PropertyMap<ServiceRequestDto, ServiceResponseDto> {

	protected void configure() {

		using(getFeedback).map(source, destination.getServiceRequestFeedback());

};
	
	Converter<ServiceRequestDto, ServiceRequestFeedbackResponseDto> getFeedback = new AbstractConverter<ServiceRequestDto, ServiceRequestFeedbackResponseDto>() {
		protected ServiceRequestFeedbackResponseDto convert(ServiceRequestDto serviceRequestDto) {
			if (serviceRequestDto.getServiceRequestFeedback() != null && (StringUtils
					.isNotBlank(serviceRequestDto.getServiceRequestFeedback().getFeedbackCode())
					|| StringUtils.isNotBlank(serviceRequestDto.getServiceRequestFeedback().getFeedbackComments())
					|| StringUtils.isNoneBlank(serviceRequestDto.getServiceRequestFeedback().getFeedbackRating()))) {
				ServiceRequestFeedbackResponseDto feedbackResponseDto = new ServiceRequestFeedbackResponseDto();
				feedbackResponseDto
						.setFeedbackComments(serviceRequestDto.getServiceRequestFeedback().getFeedbackComments());
				feedbackResponseDto
						.setFeedbackRating(serviceRequestDto.getServiceRequestFeedback().getFeedbackRating());
				if (StringUtils.isNotBlank(serviceRequestDto.getServiceRequestFeedback().getFeedbackCode())) {
					feedbackResponseDto.setFeedbackCode(com.oneassist.serviceplatform.commons.utils.StringUtils
							.getListFromString(serviceRequestDto.getServiceRequestFeedback().getFeedbackCode()));
				}

				return feedbackResponseDto;
			}
			return null;
		}
};
}
