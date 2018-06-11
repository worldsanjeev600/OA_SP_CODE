package com.oneassist.serviceplatform.commons.cache.base;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("cacheFactory")
public class CacheFactory {

    private Map<String, ICache> cache = new HashMap<String, ICache>();

    public ICache get(String cacheName) {
        return cache.get(cacheName);
    }

    public Map<String, ICache> getCache() {
        return cache;
    }

    public void setCache(Map<String, ICache> cache) {
        this.cache = cache;
    }

    public void clearCache(String cacheName) {
        cache.remove(cacheName);
    }

    public void clearCache() {
        cache.clear();
    }

}
