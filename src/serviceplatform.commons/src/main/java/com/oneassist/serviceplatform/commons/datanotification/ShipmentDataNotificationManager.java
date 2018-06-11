package com.oneassist.serviceplatform.commons.datanotification;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestTypeMstEntity;
import com.oneassist.serviceplatform.commons.entities.ShipmentEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventName;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.mastercache.ServiceRequestTypeMasterCache;
import com.oneassist.serviceplatform.commons.repositories.LogisticShipmentRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.shipment.ShipmentRequestDto;

@Component
public class ShipmentDataNotificationManager extends DataNotificationManager<ShipmentRequestDto> implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(ShipmentDataNotificationManager.class);
	
	@Autowired
	private LogisticShipmentRepository logisticShipmentRepository;
	
	@Autowired
	private ServiceRequestHelper serviceRequestHelper;
    
    @Autowired
    private ServiceRequestTypeMasterCache serviceRequestTypeMasterCache;
	
    @Override
    public void notify(DataNotificationEventType eventTye, ShipmentRequestDto notificationEntity, Object... decisionCriteriaParams) {
    	try {
    		if(notificationEntity != null){
    			super.sendNotification(notificationEntity, DataNotificationEventName.SHIPMENT_UPDATE, eventTye, notificationEntity.getServiceRequestDetails() != null ? notificationEntity
                        .getServiceRequestDetails().getInitiatingSystem() : null);
    		}else if(decisionCriteriaParams != null && decisionCriteriaParams.length > 0){
    			Long shipmentId = (Long)decisionCriteriaParams[0];
    			ShipmentEntity shipmentEntity =logisticShipmentRepository.findOne(shipmentId);
    			ShipmentRequestDto shipmentRequestDto = serviceRequestHelper.convertObject(shipmentEntity, ShipmentRequestDto.class);
    			Map<String, ServiceRequestTypeMstEntity> serviceRequestTypeMstEntityMap = serviceRequestTypeMasterCache.getAll();
                ServiceRequestTypeMstEntity serviceRequestTypeMstEntity = null;
                String serviceRequestType = null;
                for (Map.Entry<String, ServiceRequestTypeMstEntity> entry : serviceRequestTypeMstEntityMap.entrySet()) {
                    serviceRequestTypeMstEntity = entry.getValue();
                    if (serviceRequestTypeMstEntity.getServiceRequestTypeId().longValue() == shipmentEntity.getServiceRequestDetails().getServiceRequestTypeId().longValue()) {
                        serviceRequestType = entry.getKey();
                    }
                }
            	shipmentRequestDto.getServiceRequestDetails().setServiceRequestType(serviceRequestType);
            	shipmentRequestDto.getServiceRequestDetails().setServiceRequestTypeName(serviceRequestType);
    			super.sendNotification(shipmentRequestDto, DataNotificationEventName.SHIPMENT_UPDATE, eventTye, shipmentRequestDto.getServiceRequestDetails() != null ? shipmentRequestDto
                        .getServiceRequestDetails().getInitiatingSystem() : null);
    		}
    		
        } catch (Exception e) {
            logger.error("Exception while publishing data to queue", e);
        }
    }
}
