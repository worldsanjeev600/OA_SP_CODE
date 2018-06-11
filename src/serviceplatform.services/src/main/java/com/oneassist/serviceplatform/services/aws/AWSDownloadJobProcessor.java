package com.oneassist.serviceplatform.services.aws;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.oneassist.serviceplatform.commons.enums.AWSJobStatus;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.proxies.AWSGlacierProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceDocumentRepository;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;

@Service
public class AWSDownloadJobProcessor {

    private final Logger logger = Logger.getLogger(AWSDownloadJobProcessor.class);

    @Value("${CLOUD_STORAGE_DOWNLOAD_THREAD_SLEEP_TIME}")
    private String CLOUD_STORAGE_DOWNLOAD_THREAD_SLEEP_TIME;

    @Value("${AWS_GLACIER_DOWNLOAD_FILEPATH}")
    private String AWS_GLACIER_DOWNLOAD_FILEPATH;

    @Autowired
    @Qualifier("mongoStorageDao")
    private IMongoStorageDao mongoStorageDao;

    @Autowired
    private AWSGlacierProxy awsGlacierProxy;

    @Autowired
    ServiceDocumentRepository serviceDocumentRepository;

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    public void processDownloadArchiveJob(String awsJobId, long serviceRequestid) throws Exception {

        Boolean jobCompleted = false;
        String jobStatus = null;
        AWSJobStatus awsJobStatus = null;

        try {

            while (!jobCompleted) {

                jobStatus = awsGlacierProxy.getJobStatus(awsJobId);

                awsJobStatus = AWSJobStatus.getAWSJobStatus(jobStatus.toUpperCase());

                switch (awsJobStatus) {
                    case CLOUD_STORAGE_SUCCESS:
                        jobCompleted = true;
                        break;

                    case CLOUD_STORAGE_INPROGRESS:
                        Thread.sleep(Long.parseLong(CLOUD_STORAGE_DOWNLOAD_THREAD_SLEEP_TIME));
                        break;

                    case CLOUD_STORAGE_FAILURE:
                        jobCompleted = true;
                        serviceRequestRepository.updateCloudJobStatus(AWSJobStatus.CLOUD_STORAGE_FAILURE.getJobStatus(),new Date(),serviceRequestid);
                        break;
                }
            }

            if (jobCompleted && AWSJobStatus.CLOUD_STORAGE_SUCCESS.getJobStatus().equalsIgnoreCase(jobStatus)) {
                String targetFilePath = AWS_GLACIER_DOWNLOAD_FILEPATH + serviceRequestid + ".zip";

                File downloadedFile = awsGlacierProxy.downloadJob(awsJobId, targetFilePath);

                if (downloadedFile != null) {
                    if (uploadArchiveToMongo(targetFilePath)) {
                        resetArchivalId(serviceRequestid);
                        serviceRequestRepository.updateCloudJobStatus(AWSJobStatus.CLOUD_STORAGE_SUCCESS.getJobStatus(),new Date(),serviceRequestid);
                    }
                }
            }

        } catch (Exception exp) {
            logger.error("Error Processing Download Job: " + awsJobId + " for Service Request ID: " + serviceRequestid + exp);
        }
    }

    private void resetArchivalId(long serviceRequestId) {
        serviceRequestRepository.updateAwsArchiveId(null, new Date(), serviceRequestId);
    }

    private boolean uploadArchiveToMongo(String zipFilePath) throws IOException {

        ZipFile zipfile = new ZipFile(zipFilePath);
        final Enumeration<? extends ZipEntry> entries = zipfile.entries();
        DBObject metadata = new BasicDBObject();

        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = entries.nextElement();
            String fileName = zipEntry.getName().split("#")[0];
            String oldMongoId = zipEntry.getName().split("#")[1];
            String contentType = zipEntry.getName().substring(zipEntry.getName().lastIndexOf(".") + 1, zipEntry.getName().lastIndexOf("#"));
            String newMongoId = mongoStorageDao.insertDocument(zipfile.getInputStream(zipEntry), fileName, contentType, metadata);
            int updateCount = serviceDocumentRepository.updateMongoStorageReferenceId(oldMongoId, newMongoId);

        }

        return true;
    }

}
