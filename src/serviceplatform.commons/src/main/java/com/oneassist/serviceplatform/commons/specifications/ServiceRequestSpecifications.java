
package com.oneassist.serviceplatform.commons.specifications;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.base.Strings;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.FeedbackStatusEnum;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestSortEnum;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestSearchDto;


public class ServiceRequestSpecifications {

	public static Specification<ServiceRequestEntity> filterServiceRequests(final ServiceRequestSearchDto serviceRequestDto) {

		return new Specification<ServiceRequestEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceRequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<>();
				
				if (serviceRequestDto.getServiceRequestId() != null) {
					predicates.add(builder.equal(root.get("serviceRequestId"), serviceRequestDto.getServiceRequestId()));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestDto.getRefPrimaryTrackingNo())) {
					predicates.add(builder.equal(root.get("refPrimaryTrackingNo"), serviceRequestDto.getRefPrimaryTrackingNo()));
				}				
				
				if (serviceRequestDto.getAssignee() != null) {
					predicates.add(builder.equal(root.get("assignee"), serviceRequestDto.getAssignee()));
				}
				
				if (serviceRequestDto.getServicePartnerCode() != null) {
					predicates.add(builder.equal(root.get("servicePartnerCode"), serviceRequestDto.getServicePartnerCode()));
				}
				
				if (serviceRequestDto.getServicePartnerBuCode() != null) {
					predicates.add(builder.equal(root.get("servicePartnerBuCode"), serviceRequestDto.getServicePartnerBuCode()));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestDto.getWorkflowStage())) {
					predicates.add(builder.equal(root.get("workflowStage"), serviceRequestDto.getWorkflowStage()));
				}
				
				if (serviceRequestDto.getServiceRequestType() != null) {
					//predicates.add(builder.equal(root.get("serviceRequestTypeId"), serviceRequestDto.getServiceRequestTypeId()));
					List<String> list = Arrays.asList(serviceRequestDto.getServiceRequestType().split(",")) ;
					predicates.add(root.get("serviceRequestTypeId").in(list));
				}
				
				if (serviceRequestDto.getWorkFlowAlert() != null) {
                    List<String> workFlowAlerts = Arrays.asList(serviceRequestDto.getWorkFlowAlert().split(",")) ;
                    predicates.add(root.get("workflowAlert").in(workFlowAlerts));
                }
				
				if(serviceRequestDto.getReferenceNumbers() != null){
					List<String> refNoslist = Arrays.asList(serviceRequestDto.getReferenceNumbers().split(","));
					predicates.add(root.get("referenceNo").in(refNoslist));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestDto.getStatus())) {
					
					List<String> list = Arrays.asList(serviceRequestDto.getStatus().split(",")) ;
					predicates.add(root.get("status").in(list));
				}			
				
				if(FeedbackStatusEnum.A.getFeedbackStatusEnum().equalsIgnoreCase(serviceRequestDto.getFeedbackStatus())){
					predicates.add(root.get("feedbackRating").isNotNull());
				}
				
				else if(FeedbackStatusEnum.NA.getFeedbackStatusEnum().equals(serviceRequestDto.getFeedbackStatus())){
					predicates.add(root.get("feedbackRating").isNull());
				}
				
				if(StringUtils.isNotBlank(serviceRequestDto.getWorkFlowStageStatus())){
					predicates.add(builder.equal(root.get("workflowStageStatus"), serviceRequestDto.getWorkFlowStageStatus()));
				}
				
				if(serviceRequestDto.getInitiatingSystem()!=null){
					predicates.add(builder.equal(root.get("initiatingSystem"), serviceRequestDto.getInitiatingSystem()));
				}
				
				if (serviceRequestDto.getFromDate() != null && serviceRequestDto.getToDate() !=null ) {
					
					if(serviceRequestDto.getFromTime() != null && serviceRequestDto.getToTime()!=null){
						String[] toTimes=serviceRequestDto.getToTime().split(":");
						String[] fromTimes=serviceRequestDto.getFromTime().split(":");
						java.util.Date fromDt  = DateUtils.setHours(serviceRequestDto.getFromDate(), Integer.parseInt(fromTimes[0]));
                        fromDt = DateUtils.setMinutes(fromDt, Integer.parseInt(fromTimes[1]));
                        fromDt = DateUtils.setSeconds(fromDt,0);
                        
                        java.util.Date toDt = DateUtils.setHours(serviceRequestDto.getToDate(), Integer.parseInt(toTimes[0]));
                        toDt = DateUtils.setMinutes(toDt, Integer.parseInt(toTimes[1]));
                        toDt = DateUtils.setSeconds(toDt, 59);
                        
    					predicates.add(builder.greaterThanOrEqualTo(root.<Date>get("createdOn"), fromDt));					
    					predicates.add(builder.lessThanOrEqualTo(root.<Date>get("createdOn"), toDt));

					}else{
					predicates.add(builder.greaterThanOrEqualTo(builder.function("TRUNC", Date.class, root.get("createdOn")), serviceRequestDto.getFromDate()));					
					predicates.add(builder.lessThanOrEqualTo(builder.function("TRUNC", Date.class, root.get("createdOn")), serviceRequestDto.getToDate()));
					}
				}
				
				if (StringUtils.isNotBlank(serviceRequestDto.getSortBy())
						&& StringUtils.isNotBlank(serviceRequestDto.getSortOrder())
						&& ServiceRequestSortEnum.getServiceRequestSortBy(serviceRequestDto.getSortBy()) != null) {
					if (serviceRequestDto.getSortOrder().equalsIgnoreCase(Constants.DESC)) {
						query.orderBy(builder.desc(root.get(serviceRequestDto.getSortBy())));
					} else if (serviceRequestDto.getSortOrder().equalsIgnoreCase(Constants.ASC)) {
						query.orderBy(builder.asc(root.get(serviceRequestDto.getSortBy())));
					}
				}

				query.orderBy(builder.desc(root.get("serviceRequestId")));
				
				return andTogether(predicates, builder);
			}

			private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
	}
	
	public static Specification<ServiceRequestEntity> countServiceRequestsByStatus(final ServiceRequestSearchDto serviceRequestSearchDto) {

		return new Specification<ServiceRequestEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceRequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = new ArrayList<>();
				
				if (serviceRequestSearchDto.getServiceRequestId() != null) {
					predicates.add(builder.equal(root.get("serviceRequestId"), serviceRequestSearchDto.getServiceRequestId()));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestSearchDto.getRefPrimaryTrackingNo())) {
					predicates.add(builder.equal(root.get("refPrimaryTrackingNo"), serviceRequestSearchDto.getRefPrimaryTrackingNo()));
				}				
				
				if (serviceRequestSearchDto.getAssignee() != null) {
					predicates.add(builder.equal(root.get("assignee"), serviceRequestSearchDto.getAssignee()));
				}
				
				if (serviceRequestSearchDto.getServicePartnerCode() != null) {
					predicates.add(builder.equal(root.get("servicePartnerCode"), serviceRequestSearchDto.getServicePartnerCode()));
				}
				
				if (serviceRequestSearchDto.getServicePartnerBuCode() != null) {
					predicates.add(builder.equal(root.get("servicePartnerBuCode"), serviceRequestSearchDto.getServicePartnerBuCode()));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestSearchDto.getWorkflowStage())) {
					predicates.add(builder.equal(root.get("workflowStage"), serviceRequestSearchDto.getWorkflowStage()));
				}
				
				if (serviceRequestSearchDto.getServiceRequestType() != null) {
					//predicates.add(builder.equal(root.get("serviceRequestTypeId"), serviceRequestSearchDto.getServiceRequestType()));
					List<String> list = Arrays.asList(serviceRequestSearchDto.getServiceRequestType().split(",")) ;
					predicates.add(root.get("serviceRequestTypeId").in(list));
				}
				
				if (!Strings.isNullOrEmpty(serviceRequestSearchDto.getStatus())) {
					
					List<String> list = Arrays.asList(serviceRequestSearchDto.getStatus().split(",")) ;
					predicates.add(root.get("status").in(list));
				}				
				
				if (serviceRequestSearchDto.getFromDate() != null && serviceRequestSearchDto.getToDate() !=null ) {					
					predicates.add(builder.greaterThanOrEqualTo(builder.function("TRUNC", Date.class, root.get("createdOn")), serviceRequestSearchDto.getFromDate()));
					predicates.add(builder.lessThanOrEqualTo(builder.function("TRUNC", Date.class, root.get("createdOn")), serviceRequestSearchDto.getToDate()));
				}

				return andTogether(predicates, builder);
 
			}

			private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
	}
}
