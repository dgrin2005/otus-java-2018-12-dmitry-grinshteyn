package ru.otus.Executor;

import ru.otus.DataSet.DataSet;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import static ru.otus.Executor.ExecutorUtilites.*;

public class Executor {

    public static <T extends DataSet> void save(Connection connection, T t) {
        String queryString = getSaveQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                List<Field> fields = getAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T extends DataSet> T load(Connection connection, long id, Class<T> t) {
        String queryString = getLoadQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                return extract(resultSet, t);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends DataSet> List<T> loadAll(Connection connection, Class<T> t) {
        String queryString = getLoadAllQuery(t);
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                ResultSet resultSet = preparedStatement.executeQuery();
                return extractAll(resultSet, t);
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends DataSet> void update(Connection connection, T t) {
        String queryString = getUpdateQuery(t.getClass());
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                List<Field> fields = getAllFields(t.getClass());
                fillQueryParameters(preparedStatement, fields, t);
                preparedStatement.setLong(fields.size() + 1, t.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteById(Connection connection, long id) {
        String queryString = getDeleteByIdQuery();
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteAll(Connection connection) {
        String queryString = getDeleteAllQuery();
        if (!queryString.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
