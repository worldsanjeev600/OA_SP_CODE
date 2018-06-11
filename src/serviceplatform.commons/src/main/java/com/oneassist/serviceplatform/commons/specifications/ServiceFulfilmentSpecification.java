package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceFulfilmentEntity;
import com.oneassist.serviceplatform.contracts.dtos.allocationmaster.ServicePartnerAllocationSearchRequest;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ServiceFulfilmentSpecification {

    public static Specification<PincodeServiceFulfilmentEntity> filterServiceHubMaster(final ServicePartnerAllocationSearchRequest searchRequestDto) {

        return new Specification<PincodeServiceFulfilmentEntity>() {

            @Override
            public Predicate toPredicate(Root<PincodeServiceFulfilmentEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                List<Predicate> predicates = new ArrayList<>();
                List<Predicate> orPredicates = new ArrayList<>();

                if (searchRequestDto.getPincodes() != null) {
                    List<List<String>> pincodeLists = ListUtils.partition(searchRequestDto.getPincodes(), 1000);
                    if (pincodeLists != null) {
                        for (List<String> pincodeList : pincodeLists) {
                            orPredicates.add(builder.or(root.get("pincode").in(pincodeList)));
                        }
                    }
                }
                if (!StringUtils.isEmpty(searchRequestDto.getProductCode())) {
                    predicates.add(builder.equal(root.get("subCategoryCode"), searchRequestDto.getProductCode()));
                }
                if (searchRequestDto.getServiceTypeId() != null) {
                    predicates.add(builder.equal(root.get("serviceRequestTypeId"), searchRequestDto.getServiceTypeId()));
                }
                if (searchRequestDto.getPartnerCode() != null) {
                    predicates.add(builder.equal(root.get("partnerCode"), searchRequestDto.getPartnerCode()));
                }
                predicates.add(builder.equal(root.get("status"), Constants.ACTIVE));

                Predicate andPredicate = andTogether(predicates, builder);

                if (searchRequestDto.getPincodes() != null) {
                    Predicate orPredicate = orTogether(orPredicates, builder);
                    return builder.and(andPredicate, orPredicate);
                } else {
                    return andPredicate;
                }
            }

            private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {

                return cb.and(predicates.toArray(new Predicate[0]));
            }

            private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {

                return cb.or(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
