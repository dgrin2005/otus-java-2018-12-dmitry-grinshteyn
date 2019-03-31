package ru.otus.Executor;

import org.hibernate.Filter;
import org.hibernate.Session;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.sql.*;
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

    /*public static <T extends DataSet> T save(Connection connection, T t) throws MyOrmException {
        String queryString = getSaveQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
                List<Field> fields = getAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long newId = resultSet.getLong(1);
                    t.setId(newId);
                    return t;
                }
            } catch (SQLException | IllegalAccessException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
        return null;
    }*/

    public static <T extends DataSet> T save(Connection connection, T t) throws MyOrmException {
        String queryString = getSaveQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
                List<Field> fields = getAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    long newId = resultSet.getLong(1);
                    t.setId(newId);
                    return t;
                }
            } catch (SQLException | IllegalAccessException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static <T extends DataSet> T load(Connection connection, long id, Class<T> t) throws MyOrmException {
        String queryString = getLoadQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return extract(resultSet, t, connection);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static <T extends DataSet> List<T> loadAll(Connection connection, Class<T> t) throws MyOrmException {
        String queryString = getLoadAllQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                ResultSet resultSet = preparedStatement.executeQuery();
                return extractAll(resultSet, t, connection);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends DataSet> T update(Connection connection, T t) throws MyOrmException {
        String queryString = getUpdateQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                List<Field> fields = getAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.setLong(fields.size() + 1, t.getId());
                preparedStatement.executeUpdate();
                return (T)load(connection, t.getId(), t.getClass());
            } catch (SQLException | IllegalAccessException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
        return null;
    }

    public static <T extends DataSet> void deleteById(Connection connection, Class<T> t, long id) throws MyOrmException {
        String queryString = getDeleteByIdQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
    }

    public static <T extends DataSet> void deleteAll(Connection connection, Class<T> t) throws MyOrmException {
        String queryString = getDeleteAllQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new MyOrmException(e.getMessage(), e);
            }
        }
    }
}
