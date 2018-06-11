package com.oneassist.serviceplatform.batch.schedulers;

import com.oneassist.serviceplatform.services.document.archival.IServiceRequestDocumentArchivalService;
import com.oneassist.serviceplatform.services.logisticpartner.services.ECOMService;
import com.oneassist.serviceplatform.services.logisticpartner.services.FedexService;
import com.oneassist.serviceplatform.services.servicerequest.IServiceRequestBreachService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ServicePlatformScheduler {

    private static final Logger logger = Logger.getLogger(ServicePlatformScheduler.class);

    @Autowired
    private ECOMService ecomService;

    @Autowired
    private FedexService fedexService;

    @Autowired
    private IServiceRequestBreachService serviceRequestBreachService;

    @Autowired
    private IServiceRequestDocumentArchivalService serviceRequestArchivalService;

    @Scheduled(cron = "0 0 0/2 1/1 * ?")
    public void trackShipment() {
        // RVW: What is the use of it?
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void slaBreach() {
        try {
            serviceRequestBreachService.processSLABreach();
        } catch (Exception e) {
            logger.error("Exception while sla breach processing", e);
        }
    }

    @Scheduled(cron = "0 0 01 * * ?")
    public void documentArchiveBatch() {
        try {

            // Runs at 1AM noon every day

            serviceRequestArchivalService.archiveServiceDocuments();
        } catch (Exception e) {
            logger.error("Exception while Processing Document Upload Batch", e);
        }
    }
}
