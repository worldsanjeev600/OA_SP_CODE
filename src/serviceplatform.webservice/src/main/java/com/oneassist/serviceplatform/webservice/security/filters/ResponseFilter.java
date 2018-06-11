package com.oneassist.serviceplatform.webservice.security.filters;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Return header value in response
 * 
 * @author sanjeev.gupta
 */
@Provider
public class ResponseFilter implements ContainerResponseFilter {

	@Autowired
	private ServletContext context;
	
	private Attributes manifestAttributes;
	
	@PostConstruct
	public void initialize() throws IOException {
	    InputStream resourceAsStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
	    Manifest mf = new Manifest();
	    mf.read(resourceAsStream);
	    
	    manifestAttributes = mf.getMainAttributes();
	}
	
    /**
     * make response context to add in response header
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

    	responseContext.getHeaders().add("X-Build-Number", manifestAttributes.getValue("Build-Number"));
    	responseContext.getHeaders().add("X-Build-Date", manifestAttributes.getValue("Build-Date"));
        responseContext.getHeaders().add("X-Powered-By", "OneAssist");
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    }
}
