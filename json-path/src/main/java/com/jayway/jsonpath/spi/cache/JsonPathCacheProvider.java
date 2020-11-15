package com.jayway.jsonpath.spi.cache;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

import static com.jayway.jsonpath.internal.Utils.notNull;

public class JsonPathCacheProvider {
    private static Cache<String, JsonPath> cache;

    public static void setCache(Cache<String, JsonPath> cache){
        notNull(cache, "Cache may not be null");
        synchronized (JsonPathCacheProvider.class){
            if(JsonPathCacheProvider.cache != null){
                throw new JsonPathException("Cache provider must be configured before cache is accessed.");
            } else {
                JsonPathCacheProvider.cache = cache;
            }
        }
    }

    public static Cache<String, JsonPath> getCache() {
        if(JsonPathCacheProvider.cache == null){
            synchronized (JsonPathCacheProvider.class){
                if(JsonPathCacheProvider.cache == null){
                    JsonPathCacheProvider.cache = getDefaultCache();
                }
            }
        }
        return JsonPathCacheProvider.cache;
    }


    private static Cache<String, JsonPath> getDefaultCache(){
        return new LRUCache<>(400);
        //return new NOOPCache();
    }
}
