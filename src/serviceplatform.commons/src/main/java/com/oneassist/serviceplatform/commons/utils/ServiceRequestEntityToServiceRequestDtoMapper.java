package com.oneassist.serviceplatform.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.Pendency;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestFeedbackDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.WorkflowData;

@Component
public class ServiceRequestEntityToServiceRequestDtoMapper
		extends PropertyMap<ServiceRequestEntity, ServiceRequestDto> {
	

	protected void configure() {

			using(getFeedback).map(source, destination.getServiceRequestFeedback());
			using(getWorkFlowdata).map(source, destination.getWorkflowData());
			using(getThirdPartyProperties).map(source, destination.getThirdPartyProperties());
			using(getPendency).map(source,destination.getPendency());

	};

	Converter<ServiceRequestEntity, WorkflowData> getWorkFlowdata = new AbstractConverter<ServiceRequestEntity, WorkflowData>() {
		protected WorkflowData convert(ServiceRequestEntity entity) {

			if (StringUtils.isNotBlank(entity.getWorkflowData())) {
				try {
					
					Gson gson  = new Gson(); 
					WorkflowData data = gson.fromJson(entity.getWorkflowData(), WorkflowData.class);
					return data;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	};

	Converter<ServiceRequestEntity, ServiceRequestFeedbackDto> getFeedback = new AbstractConverter<ServiceRequestEntity, ServiceRequestFeedbackDto>() {
		protected ServiceRequestFeedbackDto convert(ServiceRequestEntity entity) {
			if (StringUtils.isNotBlank(entity.getFeedbackCode()) || StringUtils.isNotBlank(entity.getFeedbackComments())
					|| StringUtils.isNoneBlank(entity.getFeedbackRating())) {
				ServiceRequestFeedbackDto feedbackDto = new ServiceRequestFeedbackDto();
				feedbackDto.setFeedbackComments(entity.getFeedbackComments());
				feedbackDto.setFeedbackRating(entity.getFeedbackRating());
				if (StringUtils.isNotBlank(entity.getFeedbackCode())) {
					feedbackDto.setFeedbackCode(entity.getFeedbackCode());
				}

				return feedbackDto;
			}
			return null;
		}
	};

	Converter<ServiceRequestEntity, Map<String, Object>> getThirdPartyProperties = new AbstractConverter<ServiceRequestEntity, Map<String, Object>>() {
		protected Map<String, Object> convert(ServiceRequestEntity servcieRequestEntity) {
			if (StringUtils.isNotBlank(servcieRequestEntity.getThirdPartyProperties())) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> thirdPartyPropertyMap = mapper.readValue(servcieRequestEntity.getThirdPartyProperties(),
							new TypeReference<HashMap<String, Object>>() {
							});
					return thirdPartyPropertyMap;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;

		}

	};
	
	Converter<ServiceRequestEntity, Map<String, Pendency>> getPendency = new AbstractConverter<ServiceRequestEntity, Map<String, Pendency>>() {
		protected Map<String, Pendency> convert(ServiceRequestEntity servcieRequestEntity) {
			if (StringUtils.isNotBlank(servcieRequestEntity.getPendency())) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Pendency> pendencyMap = mapper.readValue(servcieRequestEntity.getPendency(),
							new TypeReference<HashMap<String, Pendency>>() {
							});
					return pendencyMap;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;

		}

	};
}
