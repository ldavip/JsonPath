package com.jayway.jsonpath.spi.cache;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;

public interface Cache<K, V> {

	/**
     * Get the Cached value
     * @param key cache key to lookup the value
     * @return value cached
     */
	 V get(K key);
	
	/**
     * Add JsonPath to the cache
     * @param key cache key to store the JsonPath
     * @param value JsonPath to be cached
     * @throws InvalidJsonException
     */
	 void put(K key, V value);
}
