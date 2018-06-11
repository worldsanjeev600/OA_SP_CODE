
package com.oneassist.serviceplatform.commons.specifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.oneassist.serviceplatform.commons.entities.ServiceAddressEntity;

public class ServiceAddressSpecifications {

	public static Specification<ServiceAddressEntity> filterServiceAddressSRSearch(final List<Long> addressIds) {

		return new Specification<ServiceAddressEntity>() {

			@Override
			public Predicate toPredicate(Root<ServiceAddressEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> addrIDpredicates = new ArrayList<>();

				List<List<Long>> subAddressLists = choppedLists(addressIds, 1000);
				Iterator<List<Long>> itr = subAddressLists.iterator();
				
				while(itr.hasNext()){
					addrIDpredicates.add(root.get("serviceAddressId").in(itr.next()));
				}
				
				return orTogether(addrIDpredicates, builder);

			}
			
			private List<List<Long>> choppedLists(List<Long> addressIDList, final int length) {
				List<List<Long>> addressLists = new ArrayList<>();
				int size = addressIDList.size();
				for(int i=0;i<size;i+=length){
					addressLists.add(new ArrayList<Long>(addressIDList.subList(i, Math.min(size, i+length))));
				}
				return addressLists;
			}

			private Predicate orTogether(List<Predicate> predicates, CriteriaBuilder cb) {

				return cb.or(predicates.toArray(new Predicate[0]));
			}
		};
	}
}
