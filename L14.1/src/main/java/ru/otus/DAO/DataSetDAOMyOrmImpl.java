package ru.otus.DAO;

import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.ExecutorMyOrm;

import java.sql.Connection;
import java.util.List;

public class DataSetDAOMyOrmImpl implements DataSetDAO {

    private Connection connection;

    public DataSetDAOMyOrmImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        return ExecutorMyOrm.save(connection, t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        return ExecutorMyOrm.load(connection, id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        return ExecutorMyOrm.loadAll(connection, t);
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        ExecutorMyOrm.deleteById(connection, t, id);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        ExecutorMyOrm.deleteAll(connection, t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        return ExecutorMyOrm.update(connection, t);
    }
}
