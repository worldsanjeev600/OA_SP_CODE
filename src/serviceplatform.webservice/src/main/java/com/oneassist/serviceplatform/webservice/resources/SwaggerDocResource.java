package com.oneassist.serviceplatform.webservice.resources;

import javax.ws.rs.Path;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(info = @Info(title="Service Platform",description="This Reference Document gives an overview of all the API's present in Service Platform, the Request URL's of the corresponding resources and their Request and Responses",version="1.0.0"))
@Path("/SwaggerDocConfig")
public interface SwaggerDocResource {

}
