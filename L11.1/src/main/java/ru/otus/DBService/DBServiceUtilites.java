package ru.otus.DBService;

import ru.otus.Annotation.MyOrmTransient;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.Executor.ExecutorUtilites;

import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.Executor.ExecutorUtilites.getTable;

class DBServiceUtilites {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String REMOVE_TABLE = "DROP TABLE IF EXISTS ";

    static void createTable(DBServiceMyOrmImpl dbService, Class<? extends DataSet> t) throws MyOrmException {
        try (final Statement statement = dbService.getConnection().createStatement()) {
            List<Field> fields = ExecutorUtilites.getAllFields(t);
            if (fields.size() > 0) {
                String createQuery = fields.stream()
                        .filter(x -> !x.isAnnotationPresent(MyOrmTransient.class))
                        .map(x -> x.getName() + (x.isAnnotationPresent(OneToOne.class)?"_id":"") + " " + getFieldType(x))
                        .collect(Collectors.joining(",\n ", CREATE_TABLE +
                                dbService.getDatabase() + "." + getTable(t) + " (\n", "\n);"));
                statement.executeUpdate(createQuery);
            }
        } catch (SQLException e) {
            throw new MyOrmException(e.getMessage(), e);
        }
    }

    static void removeTable(DBServiceMyOrmImpl dbService, Class<? extends DataSet> t) throws MyOrmException {
        try (final Statement statement = dbService.getConnection().createStatement()) {
            statement.executeUpdate(REMOVE_TABLE + dbService.getDatabase() + "." + getTable(t) + ";");
        } catch (SQLException e) {
            throw new MyOrmException(e.getMessage(), e);
        }
    }

    private static String getFieldType(Field field) {
        String type = "";
        if (field.getType() == long.class || field.getType() == Long.class) {
            type = "BIGINT(20)";
        }
        if (field.getType() == String.class) {
            type = "VARCHAR(255)";
        }
        if (field.getType() == int.class || field.getType() == Integer.class) {
            type = "INT(11)";
        }
        if (field.getType().getSuperclass() == DataSet.class) {
            type = "BIGINT(20)";
        }
        if (field.getName().equals("id")) {
            type = type + " PRIMARY KEY NOT NULL AUTO_INCREMENT";
        }
        return type;
    }
}
