package com.oneassist.serviceplatform.services.document.archival;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.base.Strings;
import com.mongodb.gridfs.GridFSDBFile;
import com.oneassist.serviceplatform.commons.entities.ServiceRequestEntity;
import com.oneassist.serviceplatform.commons.enums.AWSJobStatus;
import com.oneassist.serviceplatform.commons.mongo.repositories.IMongoStorageDao;
import com.oneassist.serviceplatform.commons.proxies.AWSGlacierProxy;
import com.oneassist.serviceplatform.commons.repositories.ServiceRequestRepository;
import com.oneassist.serviceplatform.commons.utils.AsyncService;
import com.oneassist.serviceplatform.services.aws.AWSDownloadJobProcessor;

@Service
public class ServiceRequestDocumentArchivalServiceImpl implements IServiceRequestDocumentArchivalService {

    private final Logger logger = Logger.getLogger(ServiceRequestDocumentArchivalServiceImpl.class);

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    @Qualifier("mongoStorageDao")
    private IMongoStorageDao mongoStorageDao;

    @Autowired
    private AWSGlacierProxy awsGlacierProxy;

    @Value("${GLACIER_ARCHIVE_UPLOAD_ZIPFILEPATH}")
    private String GLACIER_ARCHIVE_UPLOAD_ZIPFILEPATH;

    @Value("${AWS_GLACIER_VAULTNAME}")
    private String AWS_GLACIER_VAULTNAME;

    @Value("${ARCHIVAL_CUTOFF_IN_DAYS}")
    private String ARCHIVAL_CUTOFF_IN_DAYS;

    @Override
    public void archiveServiceDocuments() {

        List<Object[]> archivableSrs = serviceRequestRepository.getArchivableSRs(Integer.valueOf(ARCHIVAL_CUTOFF_IN_DAYS));
        Iterator<Object[]> srIterator = archivableSrs.iterator();

        while (srIterator.hasNext()) {
            String serviceRequestId = null;

            try {
                String sourceFilePath = null;
                Object[] srDetails = srIterator.next();

                serviceRequestId = srDetails[0].toString();

                logger.info("Processing Archival for Service Request ID :: " + serviceRequestId);

                String[] mongoIDs = srDetails[1].toString().split("#");

                List<GridFSDBFile> serviceRequestDocuments = mongoStorageDao.getDocuments(mongoIDs);

                if (serviceRequestDocuments != null && !serviceRequestDocuments.isEmpty()) {
                    sourceFilePath = this.getFileName(GLACIER_ARCHIVE_UPLOAD_ZIPFILEPATH, serviceRequestId);

                    this.populateSRZipFile(sourceFilePath, serviceRequestDocuments);

                    String awsGlacierArchiveID = awsGlacierProxy.uploadDocument(sourceFilePath);

                    logger.info("File uploaded to Glacier for SR ID:: " + serviceRequestId + " with ArchiveID: " + awsGlacierArchiveID);

                    boolean updateStatus = false;

                    if (!Strings.isNullOrEmpty(awsGlacierArchiveID)) {
                        updateStatus = updateAwsArchiveId(awsGlacierArchiveID, serviceRequestId);
                    } else {
                        logger.info("ArchiveID for SR number: " + serviceRequestId + " is NULL");
                    }

                    if (updateStatus) {
                        //deleteServiceDocsInMongo(serviceRequestId, mongoIDs);
                    }
                }
                else{
                    logger.info("No Docs found for SR#: " + serviceRequestId + " in Mongo DB");
                }
                
            } catch (Exception exp) {
                logger.error("Error Processing Archive Upload with SR ID:: " + serviceRequestId, exp);
            }
        }
    }

    @Override
    public void downloadArchiveDocuments(long serviceRequestId) {

        try {
            ServiceRequestEntity serviceRequestEntity = serviceRequestRepository.findOne(serviceRequestId);
            String awsArchiveId = serviceRequestEntity.getCloudStorageArchiveId();

            if (!Strings.isNullOrEmpty(awsArchiveId)
                    && !(AWSJobStatus.CLOUD_STORAGE_INPROGRESS.getJobStatus().equalsIgnoreCase(serviceRequestEntity.getCloudJobStatus()))) {

                String awsJobId = awsGlacierProxy.initiateDownloadRequest(awsArchiveId);

                if (!Strings.isNullOrEmpty(awsJobId)) {
                    AsyncService asyncService = new AsyncService();
                    asyncService.callService(new AWSDownloadJobProcessor(), "processDownloadArchiveJob", awsJobId, serviceRequestId);
                    serviceRequestRepository.updateCloudJobStatus(AWSJobStatus.CLOUD_STORAGE_INPROGRESS.getJobStatus(), new Date(), serviceRequestId);
                }
            }
        } catch (Exception e) {
            logger.error("Error Processing download request for SR#: " + serviceRequestId, e);
        }
    }

    private void populateSRZipFile(String uploadFilePath, List<GridFSDBFile> serviceRequestDocuments) throws Exception {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(uploadFilePath));
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            for (GridFSDBFile serviceRequestDocument : serviceRequestDocuments) {
                String documentName = serviceRequestDocument.getFilename() + "#" + serviceRequestDocument.getId();
                ZipEntry zipFile = new ZipEntry(documentName);

                try (InputStream inputStream = serviceRequestDocument.getInputStream()) {
                    zipOutputStream.putNextEntry(zipFile);

                    for (int bytes = inputStream.read(); bytes != -1; bytes = inputStream.read()) {
                        zipOutputStream.write(bytes);
                    }
                }
            }
        }
    }

    private boolean updateAwsArchiveId(String archiveId, String serviceRequestId) {

        logger.info("Update AWS ArchivalID for SR ID :: " + serviceRequestId);

        boolean status = false;

        int result = serviceRequestRepository.updateAwsArchiveId(archiveId, new Date(), Long.valueOf(serviceRequestId));

        if (result != 1) {
            awsGlacierProxy.deleteArchive(archiveId);
        } else {
            status = true;
        }

        return status;
    }

    private void deleteServiceDocsInMongo(String serviceRequestId, String[] mongoIds) {

        logger.info("Deleting Docs in Mongo for SR ID :: " + serviceRequestId);

        for (String mongoId : mongoIds) {
            mongoStorageDao.deleteDocument(mongoId);
        }
    }

    private String getFileName(String folderPath, String serviceRequestId) {
        return folderPath + serviceRequestId + ".zip";
    }
}
