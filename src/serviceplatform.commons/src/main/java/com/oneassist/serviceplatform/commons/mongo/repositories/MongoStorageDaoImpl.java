package com.oneassist.serviceplatform.commons.mongo.repositories;

import java.io.InputStream;
import java.util.List;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings("deprecation")
@Repository("mongoStorageDao")
public class MongoStorageDaoImpl implements IMongoStorageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public void insertCollection(String param, String collectionName) {
        DBObject dbObject = (DBObject) JSON.parse(param);
        mongoTemplate.save(dbObject, collectionName);
    }

    @Override
    public String insertDocument(InputStream fileInputStream, String fileName, String contentType, DBObject metadata) {
        return gridFsTemplate.store(fileInputStream, fileName, contentType, metadata).getId().toString();
    }

    @Override
    public List<GridFSDBFile> getDocuments(String... ids) {
        ObjectId[] objarray = new ObjectId[ids.length];
        for (int i = 0; i < ids.length; i++) {
            objarray[i] = new ObjectId(ids[i]);
        }
        Query query = new Query(Criteria.where("_id").in(objarray));
        return this.gridFsTemplate.find(query);
    }

    @Override
    public void deleteDocument(String documentId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(documentId))));
    }
}
