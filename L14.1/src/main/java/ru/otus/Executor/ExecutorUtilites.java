package ru.otus.Executor;

import ru.otus.Annotation.MyOrmTable;
import ru.otus.Annotation.MyOrmTransient;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutorUtilites {

    public static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = Arrays.stream(objectClass.getDeclaredFields())
                .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                .collect(Collectors.toList());
        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

    private static <T> T create(ResultSet resultSet, Class<T> t, Connection connection) throws SQLException, IllegalAccessException, InstantiationException, MyOrmException {
        List<Field> fields = getAllFields(t);
        T dataSet = t.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(OneToOne.class)) {
                long id = (long) resultSet.getObject(field.getName()+"_id");
                String queryString = getLoadQuery(field.getType());
                if (!queryString.isEmpty()) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
                        preparedStatement.setLong(1, id);
                        ResultSet resultSet1 = preparedStatement.executeQuery();
                        field.set(dataSet, extract(resultSet1, field.getType(), connection));
                    } catch (SQLException | IllegalAccessException | InstantiationException e) {
                        throw new MyOrmException(e.getMessage(), e);
                    }
                }
            } else {
                field.set(dataSet, resultSet.getObject(field.getName()));
            }
        }
        return dataSet;
    }

    static <T> T extract(ResultSet resultSet, Class<T> t, Connection connection) throws SQLException, InstantiationException, IllegalAccessException, MyOrmException {
        if (!resultSet.next()) {
            return null;
        }
        return create(resultSet, t, connection);
    }

    static <T> List<T> extractAll(ResultSet resultSet, Class<T> t, Connection connection) throws SQLException, InstantiationException, IllegalAccessException, MyOrmException {
        final List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(create(resultSet, t, connection));
        }
        return result;
    }

    static <T> String getSaveQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            String queryStringPart1 = fields.stream()
                    .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                    .map(x -> x.getName() + (x.isAnnotationPresent(OneToOne.class)?"_id":""))
                    .collect(Collectors.joining(", ", "INSERT INTO " + getTable(t) + " (", ")"));
            String queryStringPart2 = fields.stream()
                    .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                    .map(x -> "?")
                    .collect(Collectors.joining(", ", " VALUES (", ")"));
            return queryStringPart1 + queryStringPart2;
        }
        return "";
    }

    static <T> String getLoadQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream()
                    .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                    .map(x -> x.getName() + (x.isAnnotationPresent(OneToOne.class)?"_id":""))
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + getTable(t) + " WHERE id = ?"));
        }
        return "";
    }

    static <T> String getLoadAllQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream()
                    .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                    .map(x -> x.getName() + (x.isAnnotationPresent(OneToOne.class)?"_id":""))
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + getTable(t)));
        }
        return "";
    }

    static <T> String getUpdateQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream()
                    .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                    .map(x -> x.getName()  + (x.isAnnotationPresent(OneToOne.class)?"_id":"") + " = ?")
                    .collect(Collectors.joining(", ", "UPDATE " + getTable(t) + " SET ", " WHERE id = ?"));
        }
        return "";
    }

    static <T> String getDeleteByIdQuery(Class<T> t) {
        return "DELETE FROM " + getTable(t) + " WHERE id = ?";
    }

    static <T> String getDeleteAllQuery(Class<T> t) {
        return "DELETE FROM " + getTable(t);
    }

    static <T> void fillQueryParameters(PreparedStatement preparedStatement, List<Field> fields, T t) throws SQLException, IllegalAccessException {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            preparedStatement.setObject(i + 1, fields.get(i).isAnnotationPresent(OneToOne.class)
                    ?((DataSet)fields.get(i).get(t)).getId():fields.get(i).get(t));
        }
    }

    public static String getTable(Class t) {
        if (t.isAnnotationPresent(MyOrmTable.class)) {
            return ((MyOrmTable)t.getAnnotation(MyOrmTable.class)).value();
        } else {
            return t.getSimpleName();
        }
    }

}
