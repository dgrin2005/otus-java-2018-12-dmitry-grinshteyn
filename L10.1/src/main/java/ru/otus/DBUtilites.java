package ru.otus;

import ru.otus.Annotation.TableName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtilites {

    public static String getTable(Class t) {
        TableName a = (TableName) t.getAnnotation(TableName.class);
        if (a != null) {
            return a.value();
        } else {
            return t.getSimpleName();
        }
    }

    public static Connection getMySQLConnection() throws SQLException {
        final String connString = "jdbc:mysql://" +
                "localhost:" +
                "3306/" +
                "otus?" +
                "user=root&" +
                "password=123A321&" +
                "useSSL=false&" +
                "allowPublicKeyRetrieval=true&" +
                "useLegacyDatetimeCode=false&" +
                "serverTimezone=UTC";
        return DriverManager.getConnection(connString);
    }
}
