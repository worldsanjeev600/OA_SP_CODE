package com.oneassist.serviceplatform.webservice.security.filters;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

//import com.oneassist.oasys.mongodb.CollectionsStorageDao;
//import com.oneassist.oasys.util.DateUtil;

public class FilterCallbackController extends SpringBeanAutowiringSupport {

	private Logger log = Logger.getLogger(this.getClass().getName());

//	@Autowired
//	public CollectionsStorageDao collectionStorageDao;

	private String collectionName = "fkcollection";

	public void storeRequestBody(String path,String hostName, String requestBody) {
			
		StringBuffer requestBuffer = new StringBuffer();
		requestBuffer.append("{\"createdDateTime\" : \" " + new Date() + "\",");
		requestBuffer.append("\"URL\" : \"" + path + "\",");
		requestBuffer.append("\"hostName\" : \"" + hostName + "\",");
		requestBuffer.append("\"payload\" : " + requestBody + "}");
		
		//collectionStorageDao.insertCollection(requestBuffer.toString(), collectionName);
		
		log.info(requestBuffer.toString());
	}
}