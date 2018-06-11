package com.oneassist.serviceplatform.commons.custom;

import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;

@Transactional
public interface ServiceGetRequestCustomRepository {

	ServiceRequestEntity findAllByCriteria(ServiceRequestSearchDto criteria); 
}