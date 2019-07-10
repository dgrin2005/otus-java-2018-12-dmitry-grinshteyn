package ru.otus.dao;

import org.bson.types.ObjectId;
import ru.otus.exception.MyOrmException;

import java.util.List;

public interface DataSetDAO extends AutoCloseable {

    <T> T create(T t) throws MyOrmException;

    <T> T getById(ObjectId id, Class<T> t) throws MyOrmException;

    <T> T getByName(String name, Class<T> t) throws MyOrmException;

    <T> List<T> getAll(Class<T> t) throws MyOrmException;

    <T> void deleteById(ObjectId id, Class<T> t) throws MyOrmException;

    <T> void deleteByName(String name, Class<T> t) throws MyOrmException;

    <T> void deleteAll(Class<T> t) throws MyOrmException;

    <T> T update(T t) throws MyOrmException;
}
