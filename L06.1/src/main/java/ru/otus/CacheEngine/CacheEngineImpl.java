package ru.otus.CacheEngine;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private Map<K, SoftReference<V>> cache;
    private long hitsCount;
    private long missCount;
    private int maxSize;

    public CacheEngineImpl(int maxSize) throws CacheException {
        cache = new LinkedHashMap<>();
        hitsCount = 0L;
        missCount = 0L;
        if (maxSize > 0) {
            this.maxSize = maxSize;
        } else {
            throw new CacheException("Wrong cache max size");
        }
    }

    @Override
    public void put(K key, V v) {
        softReferenceEviction();
        maxSizeEviction();
        cache.put(key, new SoftReference<>(v));
    }

    @Override
    public Object get(K key) {
        if (cache.containsKey(key)) {
            SoftReference<V> entry = cache.get(key);
            if (entry.get() != null) {
                hitsCount++;
                return entry.get();
            } else {
                remove(key);
            }
        }
        missCount++;
        return null;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void removeAll() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public long getHitsCount() {
        return hitsCount;
    }

    @Override
    public long getMissCount() {
        return missCount;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    private void maxSizeEviction() {
        if (size() == maxSize) {
            K firstKey = cache.keySet().iterator().next();
            remove(firstKey);
        }
    }

    private void softReferenceEviction() {
        if (size() == maxSize) {
            cache.entrySet().removeIf(e -> e.getValue().get() == null);
        }
    }

}
