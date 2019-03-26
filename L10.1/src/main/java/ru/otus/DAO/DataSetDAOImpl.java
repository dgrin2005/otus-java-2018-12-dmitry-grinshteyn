package ru.otus.DAO;

import ru.otus.DataSet.DataSet;
import ru.otus.Executor.Executor;

import java.sql.Connection;
import java.util.List;

public class DataSetDAOImpl implements DataSetDAO {

    private Connection connection;

    public DataSetDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T extends DataSet> void create(T t) {
        Executor.save(connection, t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) {
        return Executor.load(connection, id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) {
        return Executor.loadAll(connection, t);
    }

    public void deleteById(long id) {
        Executor.deleteById(connection, id);
    }

    @Override
    public void deleteAll() {
        Executor.deleteAll(connection);
    }

    @Override
    public <T extends DataSet> void update(T t) {
        Executor.update(connection, t);
    }
}
