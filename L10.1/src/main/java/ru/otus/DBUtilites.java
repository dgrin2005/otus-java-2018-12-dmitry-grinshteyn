package ru.otus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtilites {

    public static String getTable() {
        return "otus.hw10";
    }

    public static Connection getMySQLConnection() throws SQLException {
        final String connString = "jdbc:mysql://" +
                "localhost:" +
                "3306/" +
                "otus?" +
                "user=root&" +
                "password=123A321&" +
                "useSSL=false&" +
                "useLegacyDatetimeCode=false&" +
                "serverTimezone=UTC";
        return DriverManager.getConnection(connString);
    }
}
