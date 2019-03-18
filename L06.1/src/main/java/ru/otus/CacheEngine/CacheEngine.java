package ru.otus.CacheEngine;

public interface CacheEngine {

    void put(Integer key, Object o);

    Object get(Integer key);

    void remove(Integer key);

    void removeAll();

    int size();

    long getHitsCount();

    long getMissCount();

    int getMaxSize();

}
