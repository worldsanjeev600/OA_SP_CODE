package com.oneassist.serviceplatform.commons.cache.base;

import java.util.Map;

public interface ICache <T> {
	
	public T get(String key);	
	
	public Map<String, T> getAll();
	
	public void refreshCache();	
}
