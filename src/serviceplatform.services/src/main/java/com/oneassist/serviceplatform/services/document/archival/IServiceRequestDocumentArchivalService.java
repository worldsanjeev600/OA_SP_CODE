package com.oneassist.serviceplatform.services.document.archival;


public interface IServiceRequestDocumentArchivalService {

    public void archiveServiceDocuments();

    void downloadArchiveDocuments(long serviceRequestId);
    
}
