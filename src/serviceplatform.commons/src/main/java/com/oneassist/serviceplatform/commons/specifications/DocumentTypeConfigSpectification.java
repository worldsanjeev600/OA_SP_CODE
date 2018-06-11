package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.oneassist.serviceplatform.commons.entities.DocTypeConfigDetailEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.DocumentTypeConfigDto;


public class DocumentTypeConfigSpectification {

	public static Specification<DocTypeConfigDetailEntity> filterServiceRequests(
			final DocumentTypeConfigDto documentTypeConfigDto) {
		return new Specification<DocTypeConfigDetailEntity>() {

			@Override
			public Predicate toPredicate(Root<DocTypeConfigDetailEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<>();

				if (documentTypeConfigDto.getServiceRequestTypeId() != null) {
					predicates.add(builder.equal(root.get("serviceRequestTypeId"),
							documentTypeConfigDto.getServiceRequestTypeId()));
				}

				if (documentTypeConfigDto.getReferenceCode() != null) {
					predicates.add(builder.equal(root.get("referenceCode"), documentTypeConfigDto.getReferenceCode()));
				}

				if (documentTypeConfigDto.getInsurancePartnerCode() != null) {
					predicates.add(builder.equal(root.get("insurancePartnerCode"),
							documentTypeConfigDto.getInsurancePartnerCode()));
				}

				return andTogether(predicates, builder);
			}

			private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
	}

}
