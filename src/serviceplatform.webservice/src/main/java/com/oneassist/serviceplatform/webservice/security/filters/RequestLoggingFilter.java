package com.oneassist.serviceplatform.webservice.security.filters;

/**
 * @author surender.jain
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.oneassist.serviceplatform.commons.utils.AsyncService;

//import com.oneassist.oasys.common.OASYSPropReader;
//import com.oneassist.oasys.exceptionframework.exceptions.OasysException;
//import com.oneassist.oasys.restwebservice.helper.CustomHttpServletRequest;
//import com.oneassist.oasys.restwebservice.jaxrs.FilterCallbackController;

public class RequestLoggingFilter extends SpringBeanAutowiringSupport implements Filter {

	private AsyncService asyncService = new AsyncService();

	private static final Logger log = Logger.getLogger(RequestLoggingFilter.class);
	
    @Value("${REQUEST_FILTER_ENABLED}")
    private boolean isRequestFilterEnabled;
    
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		try {
			if (isRequestFilterEnabled
				&& request != null
				&& request instanceof HttpServletRequest) {

				HttpServletRequest httpRequest = (HttpServletRequest) request;
				String path = httpRequest.getRequestURI();
				String hostName = httpRequest.getLocalName();
				request = new CustomHttpServletRequest(httpRequest);
				String requestBody = ((CustomHttpServletRequest) request).getRequestBody();
				asyncService.callService(new FilterCallbackController(),"storeRequestBody", path, hostName, requestBody);
			} 
		} catch (Exception e) {
			log.error("Exception in RequestLoggingFilter ", e);
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}