package com.jayway.jsonpath.internal.function.date;

import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.spi.cache.Cache;
import com.jayway.jsonpath.spi.cache.LRUCache;

import java.time.format.DateTimeFormatter;

import static com.jayway.jsonpath.internal.Utils.notNull;

public class DateTimeFormatterCacheProvider {
    private static Cache<String, DateTimeFormatter> cache;

    public static DateTimeFormatter getFormatterFromCache(String pattern) {
        Cache<String, DateTimeFormatter> cache = getCache();
        DateTimeFormatter formatter = cache.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            cache.put(pattern, formatter);
        }
        return formatter;
    }

    public static void setCache(Cache<String, DateTimeFormatter> cache){
        notNull(cache, "Cache may not be null");
        synchronized (DateTimeFormatterCacheProvider.class){
            if(DateTimeFormatterCacheProvider.cache != null){
                throw new JsonPathException("Formatter cache provider must be configured before cache is accessed.");
            } else {
                DateTimeFormatterCacheProvider.cache = cache;
            }
        }
    }

    public static Cache<String, DateTimeFormatter> getCache() {
        if(DateTimeFormatterCacheProvider.cache == null){
            synchronized (DateTimeFormatterCacheProvider.class){
                if(DateTimeFormatterCacheProvider.cache == null){
                    DateTimeFormatterCacheProvider.cache = getDefaultCache();
                }
            }
        }
        return DateTimeFormatterCacheProvider.cache;
    }


    private static Cache<String, DateTimeFormatter> getDefaultCache(){
        return new LRUCache<>(400);
        //return new NOOPCache();
    }
}
