package com.jayway.jsonpath.spi.cache;

public class NOOPCache<K, V> implements Cache<K, V> {

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {
    }
}
