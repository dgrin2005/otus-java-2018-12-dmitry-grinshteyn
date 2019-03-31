package ru.otus.DAO;

import org.hibernate.Session;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.Executor;

import javax.persistence.criteria.*;
import java.util.List;

public class DataSetDAOImpl implements DataSetDAO {

    private Session session;
    private CriteriaBuilder builder;

    public DataSetDAOImpl(Session session) {
        this.session = session;
        this.builder = session.getCriteriaBuilder();
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        return Executor.save(session, t);
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        return Executor.load(session, builder, id, t);
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) {
        return Executor.loadAll(session, builder, t);
    }


    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        Executor.deleteById(session, builder, t, id);
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        Executor.deleteAll(session, builder, t);
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        return Executor.update(session, t);
    }
}
