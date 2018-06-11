package com.oneassist.serviceplatform.commons.cache.base;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public abstract class InMemoryCache<T> implements InitializingBean, ICache<T> {

	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	protected Map<String, T> cache = null;

	protected abstract T getFromDB(String key);

	protected abstract Map<String, T> getAllFromDB();

	private int cacheRefreshTime;

	@Override
	public void afterPropertiesSet() throws Exception {
		scheduleRefreshCache();
	}

	private void scheduleRefreshCache() {
		refreshCache();
		doSchedule();
	}

	private void doSchedule() {
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				scheduleRefreshCache();

			}
		};
		
		timer.schedule(task, cacheRefreshTime);
	}

	public void refreshCache() {
		Map<String, T> newCache = null;
		newCache = getAllFromDB();
		if (newCache != null) {
			cache = newCache;
		}
	}
	
	public Map<String, T> getAll() {
		Map<String, T> newCache = (Map<String, T>) cache;
	
		if(newCache==null)
		{
			newCache = getAllFromDB();
			cache = newCache;
		}
		
		return newCache;
	}

	public T get(String key) {

		if (cache == null) {
			return null;
		}
		
		T result = cache.get(key);
		
		if (result == null) {
			result = getFromDB(key);
			if (result != null) {
				cache.put(key, result);
			}
		}
	
		return result;
	}
	
	public int getCacheRefreshTime() {
		return cacheRefreshTime;
	}

	public void setCacheRefreshTime(int cacheRefreshTime) {
		this.cacheRefreshTime = cacheRefreshTime;
	}
}
