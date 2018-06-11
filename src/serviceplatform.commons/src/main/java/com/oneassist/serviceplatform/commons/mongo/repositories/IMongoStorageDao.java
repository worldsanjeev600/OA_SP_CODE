package com.oneassist.serviceplatform.commons.mongo.repositories;

import java.io.InputStream;
import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

public interface IMongoStorageDao {

    public void insertCollection(String param, String collectionName);

    public String insertDocument(InputStream fileInputStream, String fileName, String contentType, DBObject metadata);

    public List<GridFSDBFile> getDocuments(String... ids);

    public void deleteDocument(String documentId);
}
