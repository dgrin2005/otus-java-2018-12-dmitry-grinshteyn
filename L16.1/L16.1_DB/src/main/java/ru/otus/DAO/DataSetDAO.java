package ru.otus.dao;

import ru.otus.dataset.DataSet;
import ru.otus.exception.MyOrmException;

import java.util.List;

public interface DataSetDAO {

    <T extends DataSet> T create(T t) throws MyOrmException;

    <T extends DataSet> T getById(long id, Class<T> t) throws Exception;

    <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException;

    <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException;

    <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException;

    <T extends DataSet> T update(T t) throws MyOrmException;
}
