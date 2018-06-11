package com.oneassist.serviceplatform.webservice.resources;

import com.oneassist.serviceplatform.contracts.response.base.ResponseDto;
import com.oneassist.serviceplatform.services.constant.ResponseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class BaseResource {

    @Autowired
    private MessageSource messageSource;

    public void setSuccessStatus(ResponseDto<? extends Object> response, int statusCode) {
        this.setSuccessStatus(response, statusCode, new Object[] { "" });
    }

    public void setSuccessStatus(ResponseDto<? extends Object> response, int statusCode, Object[] statusMessageValues) {
        response.setStatus(ResponseConstant.SUCCESS);
        response.setStatusCode(String.valueOf(statusCode));
        response.setMessage(messageSource.getMessage(String.valueOf(statusCode), statusMessageValues, null));
    }
}
