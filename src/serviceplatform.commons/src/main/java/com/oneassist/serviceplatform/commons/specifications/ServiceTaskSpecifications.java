
package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.oneassist.serviceplatform.commons.constants.Constants;
import com.oneassist.serviceplatform.commons.entities.ServiceTaskEntity;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.request.*;

public class ServiceTaskSpecifications {

	public static Specification<ServiceTaskEntity> calculateCostSpecification(final AssigneeRepairCostRequestDto assigneeRepairCostRequestDto) {

		return new Specification<ServiceTaskEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceTaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> andPredicates = new ArrayList<>();
				
				/*List<String> serviceTaskIds = assigneeRepairCostRequestDto.getRepairAssessmentIds();
				
				if(serviceTaskIds != null && !serviceTaskIds.isEmpty()){
					andPredicates.add(root.get("serviceTaskId").in(serviceTaskIds));
				}*/
				
				andPredicates.add(builder.equal(root.get("status"), Constants.ACTIVE));
				
				Predicate andPredicateBuild = builder.and(andPredicates.toArray(new Predicate[0]));
				
				return andPredicateBuild;
			}

		};
	}

}
