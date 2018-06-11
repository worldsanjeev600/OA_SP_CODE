
package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestAssetEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.asset.ServiceRequestAssetRequestDto;


public class ServiceRequestAssetSpecifications {
	public static Specification<ServiceRequestAssetEntity> filterServiceRequests(final ServiceRequestAssetRequestDto serviceRequestAssetRequestDto) {
		return new Specification<ServiceRequestAssetEntity>() {
			@Override
			public Predicate toPredicate(Root<ServiceRequestAssetEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<>();
				if (serviceRequestAssetRequestDto.getServiceRequestId() != null) {
					predicates.add(builder.equal(root.get("serviceRequestId"), serviceRequestAssetRequestDto.getServiceRequestId()));
				}
				if (serviceRequestAssetRequestDto.getAssetId() != null) {
					predicates.add(builder.equal(root.get("assetId"), serviceRequestAssetRequestDto.getAssetId()));
				}	
				predicates.add(builder.equal(root.get("status"), Constants.ACTIVE));
				//query.orderBy(builder.desc(root.get("serviceRequestId")));
				return andTogether(predicates, builder);
			}
			private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
	}
}
