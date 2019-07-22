package ru.otus.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.exception.MyOrmException;
import ru.otus.executor.Executor;
import ru.otus.utilities.DBUtilites;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.utilities.DBUtilites.getDBName;
import static ru.otus.utilities.DBUtilites.getDdl;
import static ru.otus.utilities.DBUtilites.getMongoDbConnection;

public class DataSetDAOImpl implements DataSetDAO {

    private final static Logger logger = Logger.getLogger(DataSetDAOImpl.class.getName());

    private MongoClient client;
    private MongoDatabase connection;
    private String databaseName;
    private DBUtilites.Ddl ddl;

    public DataSetDAOImpl() throws MyOrmException {
        client = getMongoDbConnection();
        logger.log(Level.INFO, "Connected to MongoDB");
        databaseName = getDBName();
        this.ddl = getDdl();
        if (ddl == DBUtilites.Ddl.CREATE || ddl == DBUtilites.Ddl.CREATEDROP) {
            findAndDropDB(client, databaseName);
        }
        this.connection = client.getDatabase(databaseName);
        logger.log(Level.INFO, "Get connection to database " + databaseName);
    }

    @Override
    public <T> void create(T t) throws MyOrmException {
        Executor.save(connection, t);
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
    public <T> void delete(List<T> t) throws MyOrmException {
        Executor.deleteList(connection, t);
    }

    @Override
    public <T> void deleteAll(Class<T> t) throws MyOrmException {
        Executor.deleteAll(connection, t);
    }

    @Override
    public <T> void update(T t) throws MyOrmException {
        Executor.update(connection, t);
    }

    @Override
    public <T, V> List<T> equal(Class<T> t, String field, V value) throws MyOrmException {
        return Executor.loadWhenEqual(connection, t, field, value);
    }

    @Override
    public <T, V> List<T> notEqual(Class<T> t, String field, V value) throws MyOrmException {
        return Executor.loadWhenNotEqual(connection, t, field, value);
    }

    @Override
    public <T, V extends Comparable> List<T> lessThan(Class<T> t, String field, V value) throws MyOrmException {
        return Executor.loadWhenLessThan(connection, t, field, value);
    }

    @Override
    public <T, V extends Comparable> List<T> greaterThan(Class<T> t, String field, V value) throws MyOrmException {
        return Executor.loadWhenGreaterThan(connection, t, field, value);
    }

    private void dropDB() {
        connection.drop();
        logger.log(Level.INFO, "Database " + databaseName + " dropped");
    }

    private void findAndDropDB(MongoClient client, String databaseName) {
        for (String s : client.listDatabaseNames()) {
            if (s.equals(databaseName)) {
                client.getDatabase(databaseName).drop();
                logger.log(Level.INFO, "Database " + databaseName + " dropped");
            }
        }
    }

    @Override
    public void close() {
        if (ddl == DBUtilites.Ddl.CREATEDROP) {
            dropDB();
        }
        client.close();
        logger.log(Level.INFO, "Disconnected from MongoDB");
    }

}
