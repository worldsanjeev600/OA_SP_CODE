package com.oneassist.serviceplatform.commons.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;


@Transactional
public class ServiceGetRequestRepositoryImpl implements ServiceGetRequestCustomRepository{

	private final Logger logger = Logger.getLogger(ServiceGetRequestRepositoryImpl.class);
	
	private static final String SERVICE_REQUEST_GET_QUERY = "SELECT o FROM ServiceRequestEntity o";
	
	@PersistenceContext
    EntityManager entityManager;
	
	@Override
	public ServiceRequestEntity findAllByCriteria(ServiceRequestSearchDto criteria) {
		logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria start>>>");
		final List<String> andConditions = new ArrayList<String>();
        final Map<String, Object> bindParameters = new HashMap<String, Object>();
		
        if (criteria.getServiceRequestId() != null) {
            andConditions.add("o.serviceRequestId = :serviceRequestId");
            bindParameters.put("serviceRequestId", criteria.getServiceRequestId());
        }
        
        if (criteria.getRefPrimaryTrackingNo() != null) {
            andConditions.add("o.refPrimaryTrackingNo = :refPrimaryTrackingNo");
            bindParameters.put("refPrimaryTrackingNo", criteria.getRefPrimaryTrackingNo());
        }
        
        if (criteria.getRefPrimaryTrackingNo() != null && criteria.getRefSecondaryTrackingNo() != null) {
            andConditions.add("o.refSecondaryTrackingNo = :refSecondaryTrackingNo");
            bindParameters.put("refSecondaryTrackingNo", criteria.getRefSecondaryTrackingNo());
        }
       
        if (andConditions.isEmpty()) {
            return (ServiceRequestEntity) Collections.emptyList();
        }
        
        final StringBuilder queryString = new StringBuilder();
        queryString.append(SERVICE_REQUEST_GET_QUERY);
        Iterator<String> andConditionsIt = andConditions.iterator();
        if (andConditionsIt.hasNext()) {
            queryString.append(" WHERE ").append(andConditionsIt.next());
        }
        
        while (andConditionsIt.hasNext()) {
            queryString.append(" AND ").append(andConditionsIt.next());
        }
        
        final TypedQuery<ServiceRequestEntity> findQuery = entityManager.createQuery(
        																	queryString.toString(), 
        																	ServiceRequestEntity.class);
        
        for (Map.Entry<String, Object> bindParameter : bindParameters.entrySet()) {
            findQuery.setParameter(bindParameter.getKey(), bindParameter.getValue());
        }
     
        queryString.append("ORDER BY o.serviceRequestId");
        logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria: queryString- "+findQuery);
        ServiceRequestEntity serviceRequestEntities=null;
        
        try{
        	serviceRequestEntities = findQuery.getSingleResult();
        }catch(Exception ex){
        	logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria: Exception occured: ",ex);
        }
        
        logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria ends>>>");
        return serviceRequestEntities;
	}
}
