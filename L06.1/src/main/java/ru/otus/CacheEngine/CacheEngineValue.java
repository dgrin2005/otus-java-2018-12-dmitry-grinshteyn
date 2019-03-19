package ru.otus.CacheEngine;

import java.lang.ref.SoftReference;

public class CacheEngineValue<V> {
    private SoftReference<V> value;
    private long lastAccessTime;

    public CacheEngineValue(V value) {
        this.value = new SoftReference<>(value);
        this.lastAccessTime = System.currentTimeMillis();
    }

    public SoftReference<V> getValue() {
        return value;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccess() {
        this.lastAccessTime = System.currentTimeMillis();
    }

}
