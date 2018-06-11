package com.oneassist.serviceplatform.batch.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oneassist.serviceplatform.batch.enums.ServiceNotificationListenerEvent;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestEventCode;
import com.oneassist.serviceplatform.commons.enums.ServiceRequestUpdateAction;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.DateFormatDeserializer;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.PaymentDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestDto;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestService;

/**
 * @author alok.singh
 *         <p>
 *         Service platform notification listener for service SP_ADVICE_DATA_NOTICIATION_QUEUE_SIT2
 *         </p>
 */
@Component
public class ServiceNotificationListener implements Runnable {

    private static final Logger logger = Logger.getLogger(ServiceNotificationListener.class);

    @Autowired
    protected AmazonSQSClient sqsClient;

    private ObjectMapper objectMapper;

    @Value("${AWS_SR_NOTIFICATION_MESSAGE_QUEUE_NAME}")
    private String queueName;

    private String queueUrl;
    @Autowired
    private ThreadPoolTaskExecutor listenerThreadPoolTaskExecutor;

    @Autowired
    private IServiceRequestService serviceRequestService;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @PostConstruct
    public void startListening() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateFormatDeserializer());
        objectMapper.registerModule(module);
        GetQueueUrlRequest urlRequest = new GetQueueUrlRequest();
        urlRequest.setQueueName(queueName);
        queueUrl = sqsClient.getQueueUrl(urlRequest).getQueueUrl();
        ServiceNotificationListener listener = this;
        listenerThreadPoolTaskExecutor.execute(listener);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        while (true) {
            try {
                ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest(queueUrl);
                ReceiveMessageResult receiveMsgResult = sqsClient.receiveMessage(receiveRequest);
                if (receiveMsgResult != null && !CollectionUtils.isEmpty(receiveMsgResult.getMessages())) {
                    for (Message message : receiveMsgResult.getMessages()) {
                        try {
                        	//logger.error("message:: "+message);
                            HashMap messageObject = objectMapper.readValue(message.getBody(), HashMap.class);
                            //logger.error("messageObject:: "+messageObject);
                            Object eventMsgString = messageObject.get("Message");
                            if(eventMsgString != null){
	                            HashMap eventMsg = objectMapper.readValue(eventMsgString.toString(), HashMap.class);
	                            String eventName = (String) eventMsg.get(EVENT_NAME_PARAM_NAME);
	                            logger.error("Event Name:: "+eventName);
	                            if (!StringUtils.isEmpty(eventName) && ServiceNotificationListenerEvent.ADVICE_UPDATE.getEventType().equalsIgnoreCase(eventName)) {
	                                processEvent(eventMsg, eventName);
	                            } else {
	                                logger.error("ServiceNotificationListener >>> Event name is empty in message OR Invalid event name ::" + message.getBody());
	                            }
                            }else{
                            	logger.error("Message content is null coming from SNS Queue.");
                            }
                            sqsClient.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.error("Inside ServiceNotificationListener onMessage", ex);
                            throw new RuntimeException(ex);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Exceptin in ServiceNotificationListener:: ", e);
            }

        }
    }

    @SuppressWarnings({ "rawtypes" })
    private void processEvent(HashMap eventMsg, String eventName) throws NumberFormatException, Exception {
        ServiceNotificationListenerEvent event = ServiceNotificationListenerEvent.valueOf(eventName);
        logger.error("Processing Message ::" + eventMsg);
        if (event != null) {
            switch (event) {
                case ADVICE_UPDATE:
                	String adviceLinkedString = (String)eventMsg.get(MESSAGE_PAYLOAD);
                	HashMap adviceLinkedMap = objectMapper.readValue(adviceLinkedString, HashMap.class);
                	List listData = (List)adviceLinkedMap.get(ADVICE_LIST);
                	if(!CollectionUtils.isEmpty(listData)){
                		PaymentDto adviceDto = objectMapper.readValue(objectMapper.writeValueAsString(listData.get(0)), PaymentDto.class);
                		if(adviceDto != null){
                			String adviceId = adviceDto.getAdviceId();
                			ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findByAdviceId(adviceId);
                			if(serviceRequestEntity == null){
                				logger.error("Service Platform does not contain this Advice Id:: "+ adviceId);
                			}else{
                				System.out.println("Advice Id:: "+adviceId);
                				ServiceRequestDto serviceRequestDto = new ServiceRequestDto();
                				serviceRequestDto.setServiceRequestId(serviceRequestEntity.getServiceRequestId());
                				serviceRequestDto.setModifiedBy("BATCH");
                				if (adviceDto.getAdviceAmount().compareTo(adviceDto.getTransactionAmount()) <= 0) {
                					serviceRequestService.performServiceAction(ServiceRequestUpdateAction.UPDATE_SERVICE_REQUEST_ON_EVENT, serviceRequestDto, ServiceRequestEventCode.DEDUCTIBLE_PAYMENT_DONE);
                					logger.error("Advice Processed Succesfully :: " +adviceId);
                				}else{
                					logger.error("Advice amount mismatch; so cannot process:: " +adviceId);
                				}
                			}
                		}
                		else{
                    		logger.error("Error while getting adviceDto from adviceList:: "+ listData + "\n"+adviceDto);
                    	}
                	}else{
                		logger.error("Advice List is empty:: "+ listData);
                	}
                    break;
                default:
                    logger.error("ServiceNotificationListener >>> No event found with name ::" + eventName);
                    break;
            }
        } else {
            logger.error("Invalid event " + eventName + "is received");
        }
    }
    
    private static String EVENT_NAME_PARAM_NAME = "eventName";
    private static final String ADVICE_LIST = "adviceList";
    private static final String MESSAGE_PAYLOAD = "messagePayload";
}
