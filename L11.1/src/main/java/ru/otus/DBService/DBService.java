package ru.otus.DBService;

import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import java.util.List;

public interface DBService extends AutoCloseable{

    <T extends DataSet> T create(T t) throws MyOrmException;

    <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException;

    <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException;

    <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException;

    <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException;

    <T extends DataSet> T update(T t) throws MyOrmException;

}
