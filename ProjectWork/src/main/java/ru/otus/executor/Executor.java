package ru.otus.executor;

import org.bson.types.ObjectId;
import ru.otus.exception.MongoODMException;

import java.util.List;

public interface Executor {

    <T> void save(T t) throws MongoODMException;

    <T> T load(ObjectId id, Class<T> t) throws MongoODMException;

    <T> T loadByName(String name, Class<T> t) throws MongoODMException;

    <T> List<T> loadAll(Class<T> t) throws MongoODMException;

    <T> void update(T t) throws MongoODMException;

    <T> void deleteById(Class<T> t, ObjectId id) throws MongoODMException;

    <T> void deleteByName(Class<T> t, String name) throws MongoODMException;

    <T> void deleteList(List<T> t) throws MongoODMException;

    <T> void deleteAll(Class<T> t) throws MongoODMException;

    <T, V> List<T> loadWhenEqual(Class<T> t, String fieldName, V value) throws MongoODMException;

    <T, V> List<T> loadWhenNotEqual(Class<T> t, String fieldName, V value) throws MongoODMException;

    <T, V extends Comparable> List<T> loadWhenGreaterThan(Class<T> t, String fieldName, V value) throws MongoODMException;

    <T, V extends Comparable> List<T> loadWhenLessThan(Class<T> t, String fieldName, V value) throws MongoODMException;

}
