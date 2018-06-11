package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.oneassist.serviceplatform.commons.entities.PincodeServiceEntity;
import com.oneassist.serviceplatform.contracts.dtos.hub.HubAllocationSearchRequestDto;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

public class ServiceHubMasterSpecifications {

    public static Specification<PincodeServiceEntity> filterServiceHubMaster(final HubAllocationSearchRequestDto searchRequestDto) {

        return new Specification<PincodeServiceEntity>() {

            @Override
            public Predicate toPredicate(Root<PincodeServiceEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

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
                if (!CollectionUtils.isEmpty(searchRequestDto.getHubIds())) {
                    predicates.add(root.get("hubId").in(searchRequestDto.getHubIds()));
                }

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
