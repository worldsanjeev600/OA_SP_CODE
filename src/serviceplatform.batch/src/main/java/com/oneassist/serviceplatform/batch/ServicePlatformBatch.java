package com.oneassist.serviceplatform.batch;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author priya.prakash
 *         <p>
 *         Main class to start the service platform batch
 *         </p>
 */
public class ServicePlatformBatch {

	private static final Logger logger = Logger.getLogger(ServicePlatformBatch.class);

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		logger.info("Starting serviceplatform batch ::");

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-batch.xml");

		logger.info("Serviceplatform batch started::");
	}
}
