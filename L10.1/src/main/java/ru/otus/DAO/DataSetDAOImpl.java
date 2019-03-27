package ru.otus.DAO;

import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.Executor;

import java.sql.Connection;
import java.util.List;

public class DataSetDAOImpl implements DataSetDAO {

    private Connection connection;

    public DataSetDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        return Executor.save(connection, t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        return Executor.load(connection, id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) {
        return Executor.loadAll(connection, t);
    }

    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        Executor.deleteById(connection, t, id);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        Executor.deleteAll(connection, t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        return Executor.update(connection, t);
    }
}
