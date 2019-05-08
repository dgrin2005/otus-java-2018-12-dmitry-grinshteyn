package ru.otus.DBService;

import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOMyOrmImpl;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.MessageSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DBServiceMyOrmImpl implements DBService, AutoCloseable {

    private final Connection connection;
    private final String database;
    private final List<Class> annotatedClasses;

    public DBServiceMyOrmImpl(String database, Class<? extends DataSet>... classes) throws MyOrmException {
        this.database = database;
        final String connString = "jdbc:mysql://" +
                "localhost:" +
                "3306/" +
                database + "?" +
                "user=root&" +
                "password=123A321&" +
                "useSSL=false&" +
                "allowPublicKeyRetrieval=true&" +
                "useLegacyDatetimeCode=false&" +
                "serverTimezone=UTC";
        try {
            this.connection = DriverManager.getConnection(connString);
        } catch (SQLException e) {
            throw new MyOrmException(e.getMessage(), e);
        }
        this.annotatedClasses = Arrays.asList(classes);
        for(Class t : annotatedClasses) {
            DBServiceUtilites.createTable(this, t);
        }
    }

    Connection getConnection() {
        return connection;
    }

    String getDatabase() {
        return database;
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        return dao.create(t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        return dao.getById(id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        return dao.getAll(t);
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        dao.deleteById(id, t);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        dao.deleteAll(t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        return dao.update(t);
    }

    @Override
    public void close() throws Exception {
        for(Class t : annotatedClasses) {
            DBServiceUtilites.removeTable(this, t);
        }
    }

    @Override
    public <T extends DataSet> long count(Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOMyOrmImpl(connection);
        return dao.getAll(t).size();
    }

    @Override
    public Address getAddress() {
        return null;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return null;
    }

}
