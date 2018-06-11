package com.oneassist.serviceplatform.commons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestDocumentTypeMasterRepository;

@Service
public class ServiceRequestDocumentTypeMasterImpl implements IServiceRequestDocumentTypeMasterService {

    @Autowired
    private ServiceRequestDocumentTypeMasterRepository serviceRequestDocumentTypeMasterRepository;

    @Override
    public List<DocTypeMstEntity> getAllServiceTaskConfig() {

        List<DocTypeMstEntity> serviceDocumentEntities = serviceRequestDocumentTypeMasterRepository.findByStatus(Constants.ACTIVE);

        return serviceDocumentEntities;
    }

	@Override
	public DocTypeMstEntity getDocumentTypeByDocumentTypeId(String key) {
		// TODO Auto-generated method stub
		DocTypeMstEntity serviceDocumentTypeEntity = serviceRequestDocumentTypeMasterRepository.findBydocTypeIdAndStatus(key, Constants.ACTIVE);
		return serviceDocumentTypeEntity;
	}
	
}
