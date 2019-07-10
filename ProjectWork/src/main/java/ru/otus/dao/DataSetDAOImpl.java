package ru.otus.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.exception.MyOrmException;
import ru.otus.executor.Executor;

import java.util.List;

public class DataSetDAOImpl implements DataSetDAO {

    private MongoDatabase connection;

    public DataSetDAOImpl(MongoClient client, String databaseName) {
        this.connection = client.getDatabase(databaseName);
    }

    @Override
    public <T> T create(T t) throws MyOrmException {
        return Executor.save(connection, t);
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> t) throws MyOrmException {
        return Executor.load(connection, id, t);
    }

    @Override
    public <T> T getByName(String name, Class<T> t) throws MyOrmException {
        return Executor.loadByName(connection, name, t);
    }

    @Override
    public <T> List<T> getAll(Class<T> t) throws MyOrmException {
        return Executor.loadAll(connection, t);
    }

    public <T> void deleteById(ObjectId id, Class<T> t) throws MyOrmException {
        Executor.deleteById(connection, t, id);
    }

    @Override
    public <T> void deleteByName(String name, Class<T> t) throws MyOrmException {
        Executor.deleteByName(connection, t, name);
    }

    @Override
    public <T> void deleteAll(Class<T> t) throws MyOrmException {
        Executor.deleteAll(connection, t);
    }

    @Override
    public <T> T update(T t) throws MyOrmException {
        return Executor.update(connection, t);
    }
}
