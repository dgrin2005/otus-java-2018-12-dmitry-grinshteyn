package ru.otus.DBService;

import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOImpl;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBServiceImpl implements DBService, AutoCloseable {

    private Connection connection;

    public DBServiceImpl() throws SQLException {
        final String connString = "jdbc:mysql://" +
                "localhost:" +
                "3306/" +
                "otus?" +
                "user=root&" +
                "password=123A321&" +
                "useSSL=false&" +
                "allowPublicKeyRetrieval=true&" +
                "useLegacyDatetimeCode=false&" +
                "serverTimezone=UTC";
        this.connection = DriverManager.getConnection(connString);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        return dao.create(t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        return dao.getById(id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        return dao.getAll(t);
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        dao.deleteById(id, t);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        dao.deleteAll(t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        DataSetDAO dao = new DataSetDAOImpl(this, connection);
        return dao.update(t);
    }

    @Override
    public void close() throws Exception {

    }
}
