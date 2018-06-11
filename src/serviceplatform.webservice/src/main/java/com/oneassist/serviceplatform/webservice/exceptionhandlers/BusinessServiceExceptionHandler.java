package com.oneassist.serviceplatform.webservice.exceptionhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Provider
public final class BusinessServiceExceptionHandler implements ExceptionMapper<BusinessServiceException> {

    private final Logger logger = Logger.getLogger(BusinessServiceExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(final BusinessServiceException exception) {
        Status responseStatus = Status.OK;
        ResponseDto<String> entityResponse = new ResponseDto<>();

        entityResponse.setInvalidData(exception.getInvalidData());
        entityResponse.setStatus(ResponseConstant.FAILED);
        entityResponse.setStatusCode(String.valueOf(exception.getStatusCode()));

        if (exception.getStatusCode() > 0) {
            entityResponse.setStatusCode(String.valueOf(exception.getStatusCode()));

            entityResponse.setMessage(messageSource.getMessage(String.valueOf(exception.getStatusCode()), exception.getStatusParamValues(), null));
        } else {
            entityResponse.setMessage(exception.getMessage());
        }

        logger.error("BusinessServiceException handled", exception);

        if (exception.contains(InputValidationException.class)) {
            responseStatus = Status.OK;
        }

        return Response.status(responseStatus).entity(entityResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
