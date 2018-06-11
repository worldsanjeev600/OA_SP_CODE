package com.oneassist.serviceplatform.webservice.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Attributes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.contracts.dtos.manifest.ImplementationDetails;
import com.oneassist.serviceplatform.services.commons.ManifestService;

import io.swagger.annotations.Api;

@Path("/manifest")
@Component
@Api(tags = { "/manifest : Provides manifest information about this web application" })
public class ManifestResource {

	@Autowired
	ManifestService manifestService;

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getManifestAttributes() throws FileNotFoundException, IOException{
		Attributes manifestAttributes = manifestService.getManifestAttributes();

		return Response.status(Response.Status.OK)
				.entity(manifestAttributes)
				.build();
	}

	@Path("/implementation-details")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getVersion() throws FileNotFoundException, IOException{
		ImplementationDetails implementationVersion = manifestService.getImplementationVersion();

		return Response.status(Response.Status.OK)
				.entity(implementationVersion)
				.build();
	}
}