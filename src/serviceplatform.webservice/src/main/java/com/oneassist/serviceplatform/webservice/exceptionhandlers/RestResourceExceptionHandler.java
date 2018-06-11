package com.oneassist.serviceplatform.webservice.exceptionhandlers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;
import com.oneassist.serviceplatform.commons.exception.InputValidationException;
import com.oneassist.serviceplatform.commons.exception.RestResourceException;
import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Provider
public final class RestResourceExceptionHandler implements ExceptionMapper<RestResourceException> {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = Logger.getLogger(RestResourceExceptionHandler.class);

    @Override
    public Response toResponse(final RestResourceException exception) {
        Status responseStatus = Status.INTERNAL_SERVER_ERROR;
        ResponseDto<String> entityResponse = new ResponseDto<>();

        entityResponse.setStatus(ResponseConstant.FAILED);
        entityResponse.setStatusCode(String.valueOf(exception.getStatusCode()));

        // sets the invalid data thrown by business service due to validation error.
        if (exception.getCause() != null && exception.getCause() instanceof BusinessServiceException) {

            BusinessServiceException bse = (BusinessServiceException) exception.getCause();
            entityResponse.setInvalidData(bse.getInvalidData());

            // give more priority to inner BusinessServiceException wrapped inside RestResourceException.
            // to set the message text from resources
            if (bse.getStatusCode() > 0) {
                entityResponse.setMessage(messageSource.getMessage(String.valueOf(bse.getStatusCode()), bse.getStatusParamValues(), null));
            } else {
                entityResponse.setMessage(bse.getMessage());
            }
        } else {
            // sets the message text from resources
            if (exception.getStatusCode() > 0) {
                entityResponse.setMessage(messageSource.getMessage(String.valueOf(exception.getStatusCode()), exception.getStatusParamValues(), null));
            } else {
                entityResponse.setMessage(exception.getMessage());
            }
        }

        logger.error("RestResourceException handled", exception);

        if (exception.contains(InputValidationException.class)) {
            responseStatus = Status.OK;
        }
        responseStatus = Status.OK;
        return Response.status(responseStatus).entity(entityResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
