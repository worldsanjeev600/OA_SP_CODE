package com.oneassist.serviceplatform.contracts.dtos.servicerequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class ImageStorageReference {

    private String imageName;

    private String fileName;

    private String documentId;

    private String imageMongoRefId;

    private Long documentTypeId;

    public String getImageMongoRefId() {
        return imageMongoRefId;
    }

    public void setImageMongoRefId(String imageMongoRefId) {
        this.imageMongoRefId = imageMongoRefId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    @Override
    public String toString() {
        return "ImageStorageReference [imageName=" + imageName + ", fileName=" + fileName + ", documentId=" + documentId + ", imageMongoRefId=" + imageMongoRefId + ", documentTypeId="
                + documentTypeId + "]";
    }

}
