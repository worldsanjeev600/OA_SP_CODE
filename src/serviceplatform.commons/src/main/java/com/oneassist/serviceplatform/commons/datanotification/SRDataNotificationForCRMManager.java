package com.oneassist.serviceplatform.commons.datanotification;

import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventName;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.ServiceRequestHelper;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SRDataNotificationForCRMManager extends DataNotificationForCRMManager<ServiceRequestDto> {

    private static final Logger logger = LoggerFactory.getLogger(SRDataNotificationForCRMManager.class);

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceRequestHelper serviceRequestHelper;

    @Override
    public void notify(DataNotificationEventType eventTye, ServiceRequestDto notificationEntity, Object... decisionCriteriaParams) {
        try {
            if (DataNotificationEventType.NEW.equals(eventTye)) {
                super.sendNotification(notificationEntity, DataNotificationEventName.SERVICE_REQUEST_UPDATE, eventTye, notificationEntity.getInitiatingSystem());
            } else {
                if (decisionCriteriaParams != null && decisionCriteriaParams.length > 0) {
                    ServiceRequestUpdateAction updateAction = (ServiceRequestUpdateAction) decisionCriteriaParams[0];
                    if (updateAction != null) {
                        switch (updateAction) {
                            case ASSIGN:
                            case RESCHEDULE_SERVICE:
                            case UPDATE_SERVICE_REQUEST_ON_EVENT:
                            case CANCEL_SERVICE:
                                if (notificationEntity != null) {
                                    sendNotification(notificationEntity, DataNotificationEventName.SERVICE_REQUEST_UPDATE, eventTye, notificationEntity.getInitiatingSystem());
                                } else if (decisionCriteriaParams.length > 1) {
                                    Long serviceRequestId = (Long) decisionCriteriaParams[1];
                                    ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestId);
                                    if (serviceRequestEntity != null) {
                                        ServiceRequestDto requestDto = serviceRequestHelper.convertObject(serviceRequestEntity, ServiceRequestDto.class);
                                        requestDto.setServiceRequestType(serviceRequestHelper.getServiceReqeustType(serviceRequestEntity.getServiceRequestTypeId()));
                                        sendNotification(requestDto, DataNotificationEventName.SERVICE_REQUEST_UPDATE, eventTye, requestDto.getInitiatingSystem());
                                    }
                                } else {
                                    logger.error("Not publishing the data as request entity is null");
                                }
                                break;
                            default:
                                logger.error("Not publishing data for event ::" + eventTye);
                                break;
                        }
                    }
                } else {
                    sendNotification(notificationEntity, DataNotificationEventName.SERVICE_REQUEST_UPDATE, eventTye, notificationEntity.getInitiatingSystem());
                }
            }
        } catch (Exception e) {
            logger.error("Exception while publishing data to queue", e);
        }
    }
}
