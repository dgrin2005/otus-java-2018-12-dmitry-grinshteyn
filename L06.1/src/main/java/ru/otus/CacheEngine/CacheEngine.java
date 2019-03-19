package ru.otus.CacheEngine;

public interface CacheEngine<K, V> {

    void put(K key, V v);

    Object get(K key);

    void remove(K key);

    void removeAll();

    int size();

    long getHitsCount();

    long getMissCount();

    int getMaxSize();

}
