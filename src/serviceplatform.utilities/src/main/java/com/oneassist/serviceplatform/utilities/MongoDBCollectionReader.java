package com.oneassist.serviceplatform.utilities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBCollectionReader {

	public static void main(String args[]){
		List<ServerAddress> servers = new ArrayList<ServerAddress>();
		ServerAddress server = new ServerAddress("192.168.40.120", 27017);
		
		servers.add(server);
	 	
		MongoCredential credential = MongoCredential.createCredential("oneassist", "cmsdocuments", "oneassist123".toCharArray());
		List<MongoCredential> credentials = new ArrayList<MongoCredential >();
		credentials.add(credential);
		MongoClient client = new MongoClient(servers,credentials);
		SimpleMongoDbFactory factory = new SimpleMongoDbFactory(client, "cmsdocuments");
		MongoTemplate template = new MongoTemplate(factory);
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("srNo", 2939947);
		
		DB db = client.getDB("cmsdocuments");

		/**** Get collection / table from 'testdb' ****/
		// if collection doesn't exists, MongoDB will create it for you
		DBCollection table = db.getCollection("ThirdpartyPayloadCollection");

		/**** Insert ****/
		// create a document to store key and value

		/**** Find and display ****/
		DBCursor cursor = table.find(searchQuery);

		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}
}
