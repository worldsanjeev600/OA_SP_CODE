package com.oneassist.serviceplatform.webservice.security.filters;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import com.oneassist.serviceplatform.contracts.dtos.ErrorInfoDto;
import com.oneassist.serviceplatform.services.authentication.AuthenticationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Basic Authorization
 * 
 * @author sanjeev.gupta
 */
@Provider
@Component
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String UNAUTHORIZED_ACCESS = "User cannot access the resource";

    private final Logger logger = Logger.getLogger(AuthenticationFilter.class);

    @Value("${SPAuthorization}")
    private String SPAuthorization;

    /**
     * filter authorization
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws WebApplicationException, UnsupportedEncodingException {

        boolean authenticationStatus = false;
        String authCredentials = containerRequestContext.getHeaderString(HEADER_AUTHORIZATION);
        boolean skipAuthentication = this.skipAuthentication(containerRequestContext.getUriInfo().getPath());
        boolean shipmentTrackingCallback = containerRequestContext.getUriInfo().getPath().endsWith("shipments/tracking");

        try {
            if (skipAuthentication) {
                authenticationStatus = true;
            } else if (shipmentTrackingCallback) {
                // For shipmentTracking callback URL, Ecom cannot send us the authorization in the header
                // hence sending it as queryString in the URL.
                MultivaluedMap<String, String> queryParams = containerRequestContext.getUriInfo().getQueryParameters();
                if (queryParams.get(HEADER_AUTHORIZATION) != null) {
                    List<String> authHeaders = queryParams.get(HEADER_AUTHORIZATION);
                    if (!CollectionUtils.isEmpty(authHeaders)) {
                        authenticationStatus = authenticationService.authenticate(authHeaders.get(0));
                    }
                } else if (authCredentials != null) {
                    authenticationStatus = authenticationService.authenticate(authCredentials);
                }
            } else {
                authenticationStatus = authenticationService.authenticate(authCredentials);
            }
        } catch (Exception e) {
            logger.error("Exception while authenticatication", e);
        } finally {
            // Will be removed once other system is moved to production with R47
            if (!authenticationStatus) {
                authCredentials = containerRequestContext.getHeaderString("SPAuthorization");
                if (!StringUtils.isEmpty(authCredentials) && !StringUtils.isEmpty(SPAuthorization)) {
                    authenticationStatus = authCredentials.equals(SPAuthorization);
                }
            }
        }

        if (!authenticationStatus) {
            logger.error("Unauthorized access : auth token " + authCredentials + ", For URL :" + containerRequestContext.getUriInfo().getPath());
            ErrorInfoDto errorInfo = new ErrorInfoDto(401, UNAUTHORIZED_ACCESS);
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(errorInfo).build());
        }
    }

    private boolean skipAuthentication(String uriPath) {
        boolean skipAuthentication = uriPath.contains("swagger");

        return skipAuthentication;
    }
}
