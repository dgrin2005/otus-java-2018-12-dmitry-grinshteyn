package ru.otus.dao;

import org.bson.types.ObjectId;
import ru.otus.exception.MyOrmException;

import java.util.List;

public interface DataSetDAO extends AutoCloseable {

    <T> void create(T t) throws MyOrmException;

    <T> T getById(ObjectId id, Class<T> t) throws MyOrmException;

    <T> T getByName(String name, Class<T> t) throws MyOrmException;

    <T> List<T> getAll(Class<T> t) throws MyOrmException;

    <T> void deleteById(ObjectId id, Class<T> t) throws MyOrmException;

    <T> void deleteByName(String name, Class<T> t) throws MyOrmException;

    <T> void delete(List<T> t) throws MyOrmException;

    <T> void deleteAll(Class<T> t) throws MyOrmException;

    <T> void update(T t) throws MyOrmException;

    <T, V> List<T> equal(Class<T> t, String field, V value) throws MyOrmException;

    <T, V> List<T> notEqual(Class<T> t, String field, V value) throws MyOrmException;

    <T, V extends Comparable> List<T> lessThan(Class<T> t, String field, V value) throws MyOrmException;

    <T, V extends Comparable> List<T> greaterThan(Class<T> t, String field, V value) throws MyOrmException;

}
