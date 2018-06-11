package com.oneassist.serviceplatform.commons.datanotification;

import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventName;
import com.oneassist.serviceplatform.commons.enums.DataNotificationEventType;
import com.oneassist.serviceplatform.contracts.dtos.DataNotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * @author priya.prakash
 *         <p>
 *         Provides utility to publish the notification to the queue
 *         </p>
 */
public abstract class DataNotificationForCRMManager<T> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataNotificationForCRMManager.class);

    public abstract void notify(DataNotificationEventType eventTye, T notificationEntity, Object... decisionCriteriaParams);

    @Autowired
    protected AmazonSQSClient sqsClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${SP_DATA_NOTIFICATION_QUEUE_NAME_CRM}")
    private String queueName;

    private String queueUrl;

    private static int MAX_RETRY_COUNT = 3;

    private String notificationPayload;

    @PostConstruct
    public void initializeQueueUrl() {
        GetQueueUrlRequest urlRequest = new GetQueueUrlRequest();
        urlRequest.setQueueName(queueName);
        queueUrl = sqsClient.getQueueUrl(urlRequest).getQueueUrl();
    }

    /**
     * <p>
     * Publishes notification to the queue
     * </p>
     * 
     * @param messagePayload
     * @param eventName
     * @param eventType
     * @param async
     * @param initiatingSystem
     * @throws JsonProcessingException
     */
    protected boolean sendNotification(Object messagePayload, DataNotificationEventName eventName, DataNotificationEventType eventType, Long initiatingSystem) {
        logger.info("Recieved data notification for event::" + eventName + " :: event type::" + eventType + ":: " + messagePayload);
        boolean status = false;
        try {
            notificationPayload = getDataNotificationPayload(messagePayload, eventName, eventType, initiatingSystem);
            new Thread(this).start();
        } catch (Exception e) {
            logger.error("Exception while sending nofication data ::" + messagePayload);
        }
        return status;
    }

    private boolean sendNotification(String messagePayload) {
        boolean status = false;
        int retryCount = 0;
        while (retryCount < MAX_RETRY_COUNT) {
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setMessageBody(messagePayload);
            sendMessageRequest.setQueueUrl(queueUrl);
            SendMessageResult sqs = sqsClient.sendMessage(sendMessageRequest);
            if (!StringUtils.isEmpty(sqs.getMessageId())) {
                status = true;
                break;
            }
            retryCount++;
        }
        if (status) {
            logger.info("Message : " + messagePayload + " has been succesfully published to queue");
        } else {
            logger.error("Publishing Message : " + messagePayload + " has been failed");
        }
        return status;
    }

    /**
     * <p>
     * Populates the notification Data payload
     * </p>
     * 
     * @param messagePayload
     * @param eventName
     * @param eventType
     * @return
     * @throws JsonProcessingException
     */
    private String getDataNotificationPayload(Object messagePayload, DataNotificationEventName eventName, DataNotificationEventType eventType, Long initiatingSystem) throws JsonProcessingException {
        String payload = null;
        DataNotificationDto notificationPayload = new DataNotificationDto();
        notificationPayload.setEventName(eventName.getEventName());
        notificationPayload.setEventType(eventType.getEventType());
        notificationPayload.setEventPublishedDateTime(new Date());
        notificationPayload.setInitiatingSystem(initiatingSystem);
        notificationPayload.setEventMessageId(UUID.randomUUID().toString());
        notificationPayload.setMessagePayload(messagePayload);
        payload = objectMapper.writeValueAsString(notificationPayload);
        return payload;
    }

    @Override
    public void run() {
        sendNotification(notificationPayload);
    }
}
