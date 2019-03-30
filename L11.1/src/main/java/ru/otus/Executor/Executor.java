package ru.otus.Executor;

import org.hibernate.Session;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.List;

import static ru.otus.Executor.ExecutorUtilites.*;

public class Executor {

    public static <T extends DataSet> T save(Session session, T t) throws MyOrmException {
        session.save(t);
        return t;
    }

    public static <T extends DataSet> T load(Session session, CriteriaBuilder builder, long id, Class<T> t) throws MyOrmException {
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

    public static <T extends DataSet> T update(Session session, CriteriaBuilder builder, T t) throws MyOrmException {
        Class aClass = t.getClass();
        CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(aClass);
        Root from = criteria.from(aClass);
        List<Field> fields = getAllFields(t.getClass());
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                criteria.set(field.getName(), field.get(t));
            } catch (IllegalAccessException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
        criteria.where(builder.equal(from.get("id"), t.getId()));
        session.createQuery(criteria).executeUpdate();
        return (T) load(session, builder, t.getId(), (Class<DataSet>) aClass);
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
