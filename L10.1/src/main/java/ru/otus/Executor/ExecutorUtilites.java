package ru.otus.Executor;

import ru.otus.DBUtilites;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ExecutorUtilites {

    static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

    private static <T> T create(ResultSet resultSet, Class<T> t) throws SQLException, IllegalAccessException, InstantiationException {
        List<Field> fields = getAllFields(t);
        T dataSet = t.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(dataSet, resultSet.getObject(field.getName()));
        }
        return dataSet;
    }

    static <T> T extract(ResultSet resultSet, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException {
        if (!resultSet.next()) {
            return null;
        }
        return create(resultSet, t);
    }

    static <T> List<T> extractAll(ResultSet resultSet, Class<T> t) throws SQLException, InstantiationException, IllegalAccessException {
        final List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(create(resultSet, t));
        }
        return result;
    }

    static <T> String getSaveQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            String queryStringPart1 = fields.stream().map(Field::getName)
                    .collect(Collectors.joining(", ", "INSERT INTO " + DBUtilites.getTable() + " (", ")"));
            String queryStringPart2 = fields.stream().map(x -> "?")
                    .collect(Collectors.joining(", ", " VALUES (", ")"));
            return queryStringPart1 + queryStringPart2;
        }
        return "";
    }

    static <T> String getLoadQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(Field::getName)
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + DBUtilites.getTable() + " WHERE id = ?"));
        }
        return "";
    }

    static <T> String getLoadAllQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(Field::getName)
                    .collect(Collectors.joining(", ", "SELECT ", " FROM " + DBUtilites.getTable()));
        }
        return "";
    }

    static <T> String getUpdateQuery(Class<T> t) {
        List<Field> fields = getAllFields(t);
        if (fields.size() > 0) {
            return fields.stream().map(x -> x.getName() + " = ?")
                    .collect(Collectors.joining(", ", "UPDATE " + DBUtilites.getTable() + " SET ", " WHERE id = ?"));
        }
        return "";
    }

    static String getDeleteByIdQuery() {
        return "DELETE FROM " + DBUtilites.getTable() + " WHERE id = ?";
    }

    static String getDeleteAllQuery() {
        return "DELETE FROM " + DBUtilites.getTable();
    }

    static <T> void fillQueryParameters(PreparedStatement preparedStatement, List<Field> fields, T t) throws SQLException {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            try {
                preparedStatement.setObject(i + 1, fields.get(i).get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
