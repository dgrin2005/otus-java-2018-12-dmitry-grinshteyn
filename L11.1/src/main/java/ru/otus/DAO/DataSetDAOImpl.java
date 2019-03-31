package ru.otus.DAO;

import org.hibernate.Session;
import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DBService.DBServiceImpl;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.Executor;

import javax.persistence.criteria.*;
import java.sql.Connection;
import java.util.List;

public class DataSetDAOImpl implements DataSetDAO {

    private DBService dbService;
    private Session session;
    private CriteriaBuilder builder;
    private Connection connection;

    public DataSetDAOImpl(DBService dbService, Connection connection) {
        this.connection = connection;
        this.dbService = dbService;
    }

    public DataSetDAOImpl(DBService dbService, Session session) {
        this.session = session;
        this.builder = session.getCriteriaBuilder();
        this.dbService = dbService;
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            return Executor.save(session, t);
        } else {
            if (dbService instanceof DBServiceImpl) {
                return Executor.save(connection, t);
            } else {
                return null;
            }
        }
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            return Executor.load(session, builder, id, t);
        } else {
            if (dbService instanceof DBServiceImpl) {
                return Executor.load(connection, id, t);
            } else {
                return null;
            }
        }
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            return Executor.loadAll(session, builder, t);
        } else {
            if (dbService instanceof DBServiceImpl) {
                return Executor.loadAll(connection, t);
            } else {
                return null;
            }
        }
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            Executor.deleteById(session, builder, t, id);
        } else {
            if (dbService instanceof DBServiceImpl) {
                Executor.deleteById(connection, t, id);
            }
        }
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            Executor.deleteAll(session, builder, t);
        } else {
            if (dbService instanceof DBServiceImpl) {
                Executor.deleteAll(connection, t);
            }
        }
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        if (dbService instanceof DBServiceHibernateImpl) {
            return Executor.update(session, t);
        } else {
            if (dbService instanceof DBServiceImpl) {
                return Executor.update(connection, t);
            } else {
                return null;
            }
        }
    }
}
