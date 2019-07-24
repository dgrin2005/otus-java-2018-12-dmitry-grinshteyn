package ru.otus.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.exception.MongoODMException;
import ru.otus.executor.Executor;
import ru.otus.executor.MongoExecutor;
import ru.otus.utilities.DBUtilites;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.utilities.DBUtilites.getDBName;
import static ru.otus.utilities.DBUtilites.getDdl;
import static ru.otus.utilities.DBUtilites.getMongoDbConnection;

public class DataSetDAOImpl implements DataSetDAO {

    private final static Logger logger = Logger.getLogger(DataSetDAOImpl.class.getName());

    private final Executor mongoExecutor;
    private final MongoClient client;
    private final MongoDatabase connection;
    private final String databaseName;
    private final DBUtilites.Ddl ddl;

    public DataSetDAOImpl() throws MongoODMException {
        client = getMongoDbConnection();
        logger.log(Level.INFO, "Connected to MongoDB");
        databaseName = getDBName();
        this.ddl = getDdl();
        if (ddl == DBUtilites.Ddl.CREATE || ddl == DBUtilites.Ddl.CREATEDROP) {
            findAndDropDB(client, databaseName);
        }
        this.connection = client.getDatabase(databaseName);
        this.mongoExecutor = new MongoExecutor(this.connection);
        logger.log(Level.INFO, "Get connection to database " + databaseName);
    }

    @Override
    public <T> void create(T t) throws MongoODMException {
        mongoExecutor.save(t);
    }

    @Override
    public <T> T getById(ObjectId id, Class<T> t) throws MongoODMException {
        return mongoExecutor.load(id, t);
    }

    @Override
    public <T> T getByName(String name, Class<T> t) throws MongoODMException {
        return mongoExecutor.loadByName(name, t);
    }

    @Override
    public <T> List<T> getAll(Class<T> t) throws MongoODMException {
        return mongoExecutor.loadAll(t);
    }

    public <T> void deleteById(ObjectId id, Class<T> t) throws MongoODMException {
        mongoExecutor.deleteById(t, id);
    }

    @Override
    public <T> void deleteByName(String name, Class<T> t) throws MongoODMException {
        mongoExecutor.deleteByName(t, name);
    }

    @Override
    public <T> void delete(List<T> t) throws MongoODMException {
        mongoExecutor.deleteList(t);
    }

    @Override
    public <T> void deleteAll(Class<T> t) throws MongoODMException {
        mongoExecutor.deleteAll(t);
    }

    @Override
    public <T> void update(T t) throws MongoODMException {
        mongoExecutor.update(t);
    }

    @Override
    public <T, V> List<T> equal(Class<T> t, String field, V value) throws MongoODMException {
        return mongoExecutor.loadWhenEqual(t, field, value);
    }

    @Override
    public <T, V> List<T> notEqual(Class<T> t, String field, V value) throws MongoODMException {
        return mongoExecutor.loadWhenNotEqual(t, field, value);
    }

    @Override
    public <T, V extends Comparable> List<T> lessThan(Class<T> t, String field, V value) throws MongoODMException {
        return mongoExecutor.loadWhenLessThan(t, field, value);
    }

    @Override
    public <T, V extends Comparable> List<T> greaterThan(Class<T> t, String field, V value) throws MongoODMException {
        return mongoExecutor.loadWhenGreaterThan(t, field, value);
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
