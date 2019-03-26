package ru.otus.DAO;

import ru.otus.DataSet.DataSet;

import java.sql.Connection;
import java.util.List;

public interface DataSetDAO {

    <T extends DataSet> void create(T t);

    <T extends DataSet> T getById(long id, Class<T> t);

    <T extends DataSet> List<T> getAll(Class<T> t);

    void deleteById(long id);

    void deleteAll();

    <T extends DataSet> void update(T t);
}
