package ru.otus.Executor;

import org.hibernate.Session;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExecutorHibernate {

    public static <T extends DataSet> T save(Session session, T t) throws MyOrmException {
        session.save(t);
        return t;
    }

    public static <T extends DataSet> T load(Session session, CriteriaBuilder builder, long id, Class<T> t) throws Exception {
        CriteriaQuery<T> criteria = builder.createQuery(t);
        Root from = criteria.from(t);
        criteria.where(builder.equal(from.get("id"), id));
        return session.createQuery(criteria).getSingleResult();
    }

    public static <T extends DataSet> List<T> loadAll(Session session, CriteriaBuilder builder, Class<T> t) {
        CriteriaQuery<T> criteria = builder.createQuery(t);
        criteria.from(t);
        return session.createQuery(criteria).list();
    }

    public static <T extends DataSet> T update(Session session, T t) throws MyOrmException {
        session.update(t);
        return t;
    }

    public static <T extends DataSet> void deleteById(Session session, CriteriaBuilder builder, Class<T> t, long id) throws MyOrmException {
        CriteriaDelete<T> criteria = builder.createCriteriaDelete(t);
        Root from = criteria.from(t);
        criteria.where(builder.equal(from.get("id"), id));
        session.createQuery(criteria).executeUpdate();
    }

    public static <T extends DataSet> void deleteAll(Session session, CriteriaBuilder builder, Class<T> t) throws MyOrmException {
        CriteriaDelete<T> criteria = builder.createCriteriaDelete(t);
        criteria.from(t);
        session.createQuery(criteria).executeUpdate();
    }
}
