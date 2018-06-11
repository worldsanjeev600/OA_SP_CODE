package com.oneassist.serviceplatform.commons.custom;

import java.util.List;
import com.oneassist.serviceplatform.commons.entities.DocTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentSearchResultEntity;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentSearchRequestDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository("customRepository")
public interface CustomRepository {

    public List<ServiceRequestTypeMstEntity> findAllByServiceId();

    public List<DocTypeMstEntity> findAllByDocType();

    public List<ShipmentSearchResultEntity> findAllByNativeQuery(ShipmentSearchRequestDto criteria);

    public List<Object[]> findServiceRequestCountPerStatus(Specification<ServiceRequestEntity> spec);

    public List<Object[]> findServiceRequestCountPerWorkflowStage(Specification<ServiceRequestEntity> spec);
}
