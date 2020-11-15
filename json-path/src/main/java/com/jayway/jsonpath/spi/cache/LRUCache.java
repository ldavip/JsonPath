/*
 * Copyright 2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jayway.jsonpath.spi.cache;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> implements Cache<K, V> {

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<K, V> map = new ConcurrentHashMap<>();
    private final Deque<K> queue = new LinkedList<>();
    private final int limit;

    public LRUCache(int limit) {
        this.limit = limit;
    }

    public void put(K key, V value) {
        V oldValue = map.put(key, value);
        if (oldValue != null) {
            removeThenAddKey(key);
        } else {
            addKey(key);
        }
        if (map.size() > limit) {
            map.remove(removeLast());
        }
    }

    public V get(K key) {
        V jsonPath = map.get(key);
        if(jsonPath != null){
            removeThenAddKey(key);
        }
        return jsonPath;
    }

    private void addKey(K key) {
        lock.lock();
        try {
            queue.addFirst(key);
        } finally {
            lock.unlock();
        }
    }

    private K removeLast() {
        lock.lock();
        try {
            return queue.removeLast();
        } finally {
            lock.unlock();
        }
    }

    private void removeThenAddKey(K key) {
        lock.lock();
        try {
            queue.removeFirstOccurrence(key);
            queue.addFirst(key);
        } finally {
            lock.unlock();
        }

    }

    private void removeFirstOccurrence(K key) {
        lock.lock();
        try {
            queue.removeFirstOccurrence(key);
        } finally {
            lock.unlock();
        }
    }

    public V getSilent(K key) {
        return map.get(key);
    }

    public void remove(K key) {
        removeFirstOccurrence(key);
        map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public String toString() {
        return map.toString();
    }
}