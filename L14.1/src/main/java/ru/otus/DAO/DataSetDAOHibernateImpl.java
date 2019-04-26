package ru.otus.DAO;

import org.hibernate.Session;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.ExecutorHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class DataSetDAOHibernateImpl implements DataSetDAO {

    private Session session;
    private CriteriaBuilder builder;

    public DataSetDAOHibernateImpl(Session session) {
        this.session = session;
        this.builder = session.getCriteriaBuilder();
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        return ExecutorHibernate.save(session, t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        return ExecutorHibernate.load(session, builder, id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        return ExecutorHibernate.loadAll(session, builder, t);
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        ExecutorHibernate.deleteById(session, builder, t, id);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        ExecutorHibernate.deleteAll(session, builder, t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        return ExecutorHibernate.update(session, t);
    }
}
