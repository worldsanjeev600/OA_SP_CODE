package com.oneassist.serviceplatform.commons.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentSearchResultEntity;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository("customRepository")
public class CustomRepositoryImpl implements CustomRepository {

    private final Logger logger = Logger.getLogger(CustomRepositoryImpl.class);

    private static final String SERVICE_TYPE_QUERY = "SELECT o FROM  ServiceRequestTypeMstEntity o";
    private static final String SHIPMENT_DOC_TYPE_QUERY = "SELECT o FROM DocTypeMstEntity o";
    private static final String SHIPMENT_SEARCH_NATIVE_QUERY = "SELECT o FROM ShipmentSearchResultEntity o";

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ServiceRequestTypeMstEntity> findAllByServiceId() {
        final StringBuilder queryString = new StringBuilder();
        queryString.append(SERVICE_TYPE_QUERY);
        final TypedQuery<ServiceRequestTypeMstEntity> findQuery = entityManager.createQuery(queryString.toString(), ServiceRequestTypeMstEntity.class);
        List<ServiceRequestTypeMstEntity> serviceRequestTypeMstEntities = findQuery.getResultList();
        logger.info("In ShipmentSearchRepositoryImpl: findAllByServiceId ends>>>");

        return serviceRequestTypeMstEntities;
    }

    @Override
    public List<DocTypeMstEntity> findAllByDocType() {
        final StringBuilder queryString = new StringBuilder();
        queryString.append(SHIPMENT_DOC_TYPE_QUERY);
        final TypedQuery<DocTypeMstEntity> findQuery = entityManager.createQuery(queryString.toString(), DocTypeMstEntity.class);
        List<DocTypeMstEntity> docTypeMstEntities = findQuery.getResultList();
        logger.info("In ShipmentSearchRepositoryImpl: findAllByDocType ends>>>");
        return docTypeMstEntities;
    }

    @Override
    public List<ShipmentSearchResultEntity> findAllByNativeQuery(ShipmentSearchRequestDto criteria) {
        logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria start>>>");
        final List<String> andConditions = new ArrayList<String>();
        final Map<String, Object> bindParameters = new HashMap<String, Object>();
        bindParameters.clear();
        andConditions.clear();

        if (criteria.getShipmentId() != null && criteria.getShipmentId() != 0) {
            andConditions.add("o.shipmentId = :shipmentId");
            bindParameters.put("shipmentId", criteria.getShipmentId());
        }

        if (criteria.getServiceId() != null && criteria.getServiceId() != 0) {
            andConditions.add("o.serviceRequestTypeId = :serviceId");
            bindParameters.put("serviceId", criteria.getServiceId());
        }

        if (criteria.getLogisticPartnerRefTrackingNumber() != null && !criteria.getLogisticPartnerRefTrackingNumber().isEmpty()) {
            andConditions.add("o.logisticPartnerRefTrackingNo = :logisticPartnerRefTrackingNumber");
            bindParameters.put("logisticPartnerRefTrackingNumber", criteria.getLogisticPartnerRefTrackingNumber());
        }

        if (criteria.getTrackingNo() != null && !criteria.getTrackingNo().isEmpty()) {
            andConditions.add("o.primaryTrackingNumber = :trackingNumber");
            bindParameters.put("trackingNumber", criteria.getTrackingNo());
        }

        if (criteria.getLogisticPartnerCode() != null && !criteria.getLogisticPartnerCode().isEmpty()) {
            andConditions.add("o.logisticPartnerCode = :logisticPartnerCode");
            bindParameters.put("logisticPartnerCode", criteria.getLogisticPartnerCode());
        }

        if (criteria.getFromDate() != null) {
            java.sql.Date fromDate = new java.sql.Date(criteria.getFromDate().getTime());
            andConditions.add("o.createdOn >= :fromDate");
            bindParameters.put("fromDate", fromDate);
        }

        if (criteria.getToDate() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(criteria.getToDate());
            c.add(Calendar.DATE, 1);
            Date addedDate = c.getTime();

            java.sql.Date toDate = new java.sql.Date(addedDate.getTime());
            andConditions.add("o.createdOn <= :createdDateTo");
            bindParameters.put("createdDateTo", toDate);

        }

        if (criteria.getStatus() != null && !Strings.isNullOrEmpty(criteria.getStatus())) {
            andConditions.add("o.shipmentStatus = :status");
            bindParameters.put("status", criteria.getStatus());
        }

        if (criteria.getHubId() != null && !criteria.getHubId().isEmpty()) {
            andConditions.add("o.hubId = :hubId");
            bindParameters.put("hubId", criteria.getHubId());
        }

        if (criteria.getStage() != null && !criteria.getStage().isEmpty()) {
            andConditions.add("o.currentStage = :currentStage");
            bindParameters.put("currentStage", criteria.getStage());
        }

        if (andConditions.isEmpty()) {
            return Collections.emptyList();
        }

        final StringBuilder queryString = new StringBuilder();
        queryString.append(SHIPMENT_SEARCH_NATIVE_QUERY);
        Iterator<String> andConditionsIt = andConditions.iterator();

        if (andConditionsIt.hasNext()) {
            queryString.append(" WHERE ").append(andConditionsIt.next());
        }
        while (andConditionsIt.hasNext()) {
            queryString.append(" AND ").append(andConditionsIt.next());
        }

        final TypedQuery<ShipmentSearchResultEntity> findQuery = entityManager.createQuery(queryString.toString(), ShipmentSearchResultEntity.class);

        // Bind parameters.
        for (Map.Entry<String, Object> bindParameter : bindParameters.entrySet()) {
            findQuery.setParameter(bindParameter.getKey(), bindParameter.getValue());
        }

        queryString.append("ORDER BY o.shipmentId");
        logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria: queryString- " + findQuery);
        List<ShipmentSearchResultEntity> shipmentEntities = null;
        shipmentEntities = findQuery.getResultList();
        logger.info("In ShipmentSearchRepositoryImpl: findAllByCriteria ends>>>");
        return shipmentEntities;
    }

    @Override
    public List<Object[]> findServiceRequestCountPerStatus(Specification<ServiceRequestEntity> spec) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root = query.from(ServiceRequestEntity.class);
        query.multiselect(root.get("status"), builder.count(root));
        query.groupBy(root.get("status"));
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Object[]> findServiceRequestCountPerWorkflowStage(Specification<ServiceRequestEntity> spec) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root root = query.from(ServiceRequestEntity.class);
        query.multiselect(root.get("workflowStage"), builder.count(root));
        query.groupBy(root.get("workflowStage"));
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return entityManager.createQuery(query).getResultList();
    }
}
