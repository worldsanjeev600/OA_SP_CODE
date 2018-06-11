package com.oneassist.serviceplatform.webservice.exceptionhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;

@Provider
public final class ExceptionHandler implements ExceptionMapper<Exception> {

	private final Logger logger = Logger.getLogger(ExceptionHandler.class);
	
	@Override
	public Response toResponse(final Exception exception) {
		ResponseDto<String> entityResponse = new ResponseDto<>();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
		
        entityResponse.setStatus(ResponseConstant.FAILED);
        entityResponse.setMessage(exception.getMessage());
        
        logger.error("Exception handled", exception);
    	
		return Response.status(responseStatus)
				   .entity(entityResponse)
				   .type(MediaType.APPLICATION_JSON)
				   .build();
	}
}