package ru.otus.dao;

import org.bson.types.ObjectId;
import ru.otus.exception.MongoODMException;

import java.util.List;

public interface DataSetDAO extends AutoCloseable {

    <T> void create(T t) throws MongoODMException;

    <T> T getById(ObjectId id, Class<T> t) throws MongoODMException;

    <T> T getByName(String name, Class<T> t) throws MongoODMException;

    <T> List<T> getAll(Class<T> t) throws MongoODMException;

    <T> void deleteById(ObjectId id, Class<T> t) throws MongoODMException;

    <T> void deleteByName(String name, Class<T> t) throws MongoODMException;

    <T> void delete(List<T> t) throws MongoODMException;

    <T> void deleteAll(Class<T> t) throws MongoODMException;

    <T> void update(T t) throws MongoODMException;

    <T, V> List<T> equal(Class<T> t, String field, V value) throws MongoODMException;

    <T, V> List<T> notEqual(Class<T> t, String field, V value) throws MongoODMException;

    <T, V extends Comparable> List<T> lessThan(Class<T> t, String field, V value) throws MongoODMException;

    <T, V extends Comparable> List<T> greaterThan(Class<T> t, String field, V value) throws MongoODMException;

}
