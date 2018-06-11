package com.oneassist.serviceplatform.commons.enums;

public enum AWSJobStatus {

    CLOUD_STORAGE_SUCCESS("SUCCEEDED"), 
    CLOUD_STORAGE_INPROGRESS("INPROGRESS"), 
    CLOUD_STORAGE_FAILURE("FAILED");
	
	private final String jobStatus;

	AWSJobStatus(String jobStatus) {

		this.jobStatus = jobStatus;
	}

    public String getJobStatus() {
        return jobStatus;
    }

    public static AWSJobStatus getAWSJobStatus(String jobStatus) {
		for (AWSJobStatus awsJobStatus : AWSJobStatus.values()) {
			if (awsJobStatus.getJobStatus().equals(jobStatus)) {
				return awsJobStatus;
			}
		}
		return null;
	}
}