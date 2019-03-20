package ru.otus.CacheEngine;

import java.lang.ref.SoftReference;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private Map<K, SoftReference<CacheEngineValue<V>>> cache;
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
        eviction();
        cache.put(key, new SoftReference<>(new CacheEngineValue<>(v)));
    }

    @Override
    public Object get(K key) {
        if (cache.containsKey(key)) {
            SoftReference<CacheEngineValue<V>> entry = cache.get(key);
            if (entry.get() != null) {
                hitsCount++;
                entry.get().setAccess();
                return entry.get().getValue();
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

    private void eviction() {
        if (size() == maxSize) {
            cache.entrySet().removeIf(e -> e.getValue().get() == null);
        }
        if (size() == maxSize) {
            cache.entrySet().stream()
                    .min(Comparator.comparingLong(e -> Objects.isNull(e.getValue())
                            ? 0 : e.getValue().get().getLastAccessTime())).ifPresent(e -> cache.remove(e.getKey()));
        }
    }

}
