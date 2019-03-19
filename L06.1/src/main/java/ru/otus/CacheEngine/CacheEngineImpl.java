package ru.otus.CacheEngine;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private Map<K, CacheEngineValue<V>> cache;
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
        cache.put(key, new CacheEngineValue<>(v));
    }

    @Override
    public Object get(K key) {
        if (cache.containsKey(key)) {
            CacheEngineValue<V> entry = cache.get(key);
            if (entry.getValue().get() != null) {
                hitsCount++;
                entry.setAccess();
                return entry.getValue().get();
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
            K firstKey = getFirstAccessKey();
            if (firstKey != null) {
                remove(firstKey);
            }
        }
    }

    private void softReferenceEviction() {
        if (size() == maxSize) {
            cache.entrySet().removeIf(e -> e.getValue().getValue().get() == null);
        }
    }

    private K getFirstAccessKey() {
        K key = null;
        long firstAccessTime = 0;
        long currentAccessTime = 0;
        for (Map.Entry<K, CacheEngineValue<V>> entry : cache.entrySet()) {
            if (key == null) {
                key = entry.getKey();
                firstAccessTime = entry.getValue().getLastAccessTime();
            } else {
                currentAccessTime = entry.getValue().getLastAccessTime();
                if (currentAccessTime < firstAccessTime) {
                    key = entry.getKey();
                    firstAccessTime = currentAccessTime;
                }
            }
        }
        return key;
    }

}
