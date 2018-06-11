package com.oneassist.serviceplatform.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.oneassist.serviceplatform.commons.exception.BusinessServiceException;

@Component
public class AsyncService implements Runnable{

	private  Object[] args;
	
	private Method method;
	
	private Object target;
	
	private String targetClass;
	
	private String targetMethod;
	
	private static final Logger log = Logger.getLogger(AsyncService.class);
	
	/**
	 * This method calls the service method in a new  thread
	 * 
	 * @param service The target service to be invoked. Must be the instance of the object with all the required dependencies supplied.
	 * @param methodName The method name to be invoked. This implementation does not support invoking overloaded methods.
	 * @param pArgs The arguments to be supplied to the target service method
	 * @throws BusinessServiceException 
	 */
	public void callService(Object service,String methodName, Object... pArgs) throws BusinessServiceException{
		
		if(service == null){
			log.error("Target object cannot be null.");
			throw new BusinessServiceException("Target Service cannot be null");
		}
		
		if(StringUtils.isBlank(methodName)){
			log.error("Method name cannot be blank.");
			throw new BusinessServiceException("Method name cannot be blank.");
		}
		
		targetMethod = methodName;
		targetClass = service.getClass().getName();
		
		for(Method it:service.getClass().getMethods()){
			if(it.getName().equalsIgnoreCase(targetMethod)){
				method = it;
				args =pArgs;
				target =service; 
				break;						
			}
		}
		
		if(method == null){
			log.error(String.format("Method[%1$s] not found in Class:%2$s",targetClass,targetMethod));
			throw new BusinessServiceException(String.format("Method[%1$s] not found in Class:%2$s",targetClass,targetMethod));
		}
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			method.invoke(target,args);
		} catch (IllegalArgumentException e) {
			log.error(String.format("target method[ Class:%1$s |Method:%2$s] invoked with illegal arguments",targetClass,targetMethod),e);
		} catch (IllegalAccessException e) {
			log.error(String.format("target method[ Class:%1$s |Method:%2$s] is security restricted",targetClass,targetMethod),e);
		} catch (InvocationTargetException e) {
			log.error("target method generated exception",e);
		}
	}
}
