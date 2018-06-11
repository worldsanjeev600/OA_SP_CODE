package com.oneassist.serviceplatform.webservice.security.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {

	protected ByteArrayOutputStream cachedBytes;
	protected String encoding;
	protected String requestBody;
 
	public CustomHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);

		encoding = (request.getCharacterEncoding() == null) ? "UTF-8" : request.getCharacterEncoding();
		requestBody = IOUtils.toString(this.getInputStream(), encoding);
		
		if (StringUtils.isEmpty(requestBody)) {
			requestBody = null;
		}
	}
  
  
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (cachedBytes == null) {
			cachedBytes = new ByteArrayOutputStream();
			IOUtils.copy(super.getInputStream(), cachedBytes);
		}
		
		return new CachedServletInputStream();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * Inner class that reads from stored byte array
	 */
	public class CachedServletInputStream extends ServletInputStream {
		private ByteArrayInputStream input;

		public CachedServletInputStream() {
			input = new ByteArrayInputStream(cachedBytes.toByteArray());
		}
		
		@Override
		public int read() throws IOException {
			return input.read();
		}
		
		@Override
		public int read(byte[] b) throws IOException {
			return input.read(b);
		}
		
		@Override
		public int read(byte[] b, int off, int len) {
			return input.read(b, off, len);
		}
	}
}