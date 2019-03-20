package ru.otus.CacheEngine;

public class CacheEngineValue<V> {
    private V value;
    private long lastAccessTime;

    public CacheEngineValue(V value) {
        this.value = value;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public V getValue() {
        return value;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccess() {
        this.lastAccessTime = System.currentTimeMillis();
    }

}
