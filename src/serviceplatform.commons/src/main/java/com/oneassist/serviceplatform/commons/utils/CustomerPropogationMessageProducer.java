package com.oneassist.serviceplatform.commons.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomerPropogationMessageProducer {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerPropogationMessageProducer.class);

	@Autowired
	protected AmazonSQSClient		sqsClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${CUSTOMER_PROPAGATION_QUEUE_NAME}")
	private String queueName;
	
	private String queueUrl;
	
	@PostConstruct
	public void initializeQueueUrl(){
		GetQueueUrlRequest urlRequest = new GetQueueUrlRequest();
		urlRequest.setQueueName(queueName);
		queueUrl = sqsClient.getQueueUrl(urlRequest).getQueueUrl();
	}

	public void sendMessages(Object eventPayload) throws Exception {

		try {
			if(eventPayload != null){
				String messagePayload = eventPayload  instanceof String ? eventPayload.toString() : objectMapper.writeValueAsString(eventPayload);
				SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, messagePayload);

				SendMessageResult result = sqsClient.sendMessage(sendMessageRequest);
				if (result.getMessageId() != null) {
					LOG.info("Message " + messagePayload + " has been published to queue::" + queueUrl + " successfully");
				} else {
					LOG.info("Publishing message " + messagePayload + " to queue " + queueUrl + " failed.");
				}
			}

		} catch(Exception e) {
			LOG.error("exception while sending message", e);
		}
	}
}
