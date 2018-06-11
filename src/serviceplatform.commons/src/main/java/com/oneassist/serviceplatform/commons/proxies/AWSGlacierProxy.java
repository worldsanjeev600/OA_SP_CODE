package com.oneassist.serviceplatform.commons.proxies;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.amazonaws.services.glacier.model.DescribeJobRequest;
import com.amazonaws.services.glacier.model.DescribeJobResult;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;

@Service("awsGlacierProxy")
public class AWSGlacierProxy {

    private final Logger logger = Logger.getLogger(AWSGlacierProxy.class);

    @Autowired
    AmazonGlacierClient awsGlacierClient;

    @Value("${AWS_GLACIER_VAULTNAME}")
    private String AWS_GLACIER_VAULTNAME;

    private final String ARCHIVE_RETRIEVAL_JOB = "archive-retrieval";

    public String uploadDocument(String sourceFilePath) throws Exception {
        File uploadFile = new File(sourceFilePath);
        
        String checksum = TreeHashGenerator.calculateTreeHash(uploadFile);
        long contentLength = uploadFile.length();
        String archiveID = null;
        
        byte[] buffer = FileUtils.readFileToByteArray(uploadFile);
        
        try (InputStream instream = new ByteArrayInputStream(buffer)){
            
            UploadArchiveRequest request = new UploadArchiveRequest()
            											.withBody(instream)
            											.withChecksum(checksum)
            											.withContentLength(contentLength)
            											.withVaultName(AWS_GLACIER_VAULTNAME);

            UploadArchiveResult result = null;

            result = awsGlacierClient.uploadArchive(request);

            if (!result.getChecksum().equals(checksum)) {
                throw new AmazonServiceException("Checksum mismatch. Client calculated : " + checksum + " but AWS computed : " + result.getChecksum());
            }

            if (!result.getArchiveId().isEmpty() && result.getArchiveId() != null) {
                archiveID = result.getArchiveId();
            }

        } finally {
            uploadFile.delete();
        }
        
        return archiveID;
    }

    public void deleteArchive(String archiveId) {
        awsGlacierClient.deleteArchive(new DeleteArchiveRequest().withVaultName(AWS_GLACIER_VAULTNAME).withArchiveId(archiveId));
    }

    public String initiateDownloadRequest(String archiveId) {

        InitiateJobResult response = null;

        JobParameters jobParameters = new JobParameters().withType(ARCHIVE_RETRIEVAL_JOB).withArchiveId(archiveId);

        InitiateJobRequest request = new InitiateJobRequest().withVaultName(AWS_GLACIER_VAULTNAME).withJobParameters(jobParameters);

        response = awsGlacierClient.initiateJob(request);
        return response.getJobId();

    }

    public String getJobStatus(String awsJobId) {

        DescribeJobRequest describeJobRequest = new DescribeJobRequest().withJobId(awsJobId).withVaultName(AWS_GLACIER_VAULTNAME);
        DescribeJobResult describeJobResult = awsGlacierClient.describeJob(describeJobRequest);
        
        return describeJobResult.getStatusCode();
    }

    public File downloadJob(String awsJobId, String targetFilePath) throws IOException {

        File downloadFile = null;
        GetJobOutputRequest getJobOutputRequest = new GetJobOutputRequest().withVaultName(AWS_GLACIER_VAULTNAME).withJobId(awsJobId);
        GetJobOutputResult getJobOutputResult = awsGlacierClient.getJobOutput(getJobOutputRequest);

        if (getJobOutputResult != null && getJobOutputResult.getBody() != null) {
        	downloadFile = new File(targetFilePath);
        	
            try (InputStream inputStream = new BufferedInputStream(getJobOutputResult.getBody());
            		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
                
                byte[] buffer = new byte[1024 * 1024];

                int bytesRead = 0;

                do {
                    bytesRead = inputStream.read(buffer);
                    
                    if (bytesRead <= 0) {
                        break;
                    }

                    outputStream.write(buffer, 0, bytesRead);
                } while (bytesRead > 0);
            }
        }

        return downloadFile;
    }
}