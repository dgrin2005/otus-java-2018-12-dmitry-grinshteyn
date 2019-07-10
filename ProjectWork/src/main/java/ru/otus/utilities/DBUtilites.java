package ru.otus.utilities;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ru.otus.exception.MyOrmException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBUtilites {

    private final static String propertiesFileName = "mongodb.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        File file = new File(propertiesFileName);
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MongoClient getMongoDbConnection() throws MyOrmException {

        final String host = properties.getProperty("host");
        final String port = properties.getProperty("port");
        final String username = properties.getProperty("username");
        final String password = properties.getProperty("password");

        if (host == null) {
            throw new MyOrmException("Host not found in file " + propertiesFileName);
        }
        StringBuilder connString = new StringBuilder("mongodb://");
        if (username != null && password != null) {
            connString.append(username).append(":").append(password).append("@");
        }
        connString.append(host);
        if (port != null) {
            connString.append(":").append(port);
        }
        return MongoClients.create(String.valueOf(connString));
    }

    public static String getDBName() throws MyOrmException {
        final String dbname = properties.getProperty("dbname");
        if (dbname == null) {
            throw new MyOrmException("Database name not found in file " + propertiesFileName);
        }
        return dbname;
    }

    public static MongoCollection<org.bson.Document> getCollection(MongoDatabase connection, Class t) throws MyOrmException {
        if (!t.isAnnotationPresent(ru.otus.annotation.Document.class)) {
            throw new MyOrmException("Class " + t + " does not match");
        }
        String collectionName = ((ru.otus.annotation.Document) t.getAnnotation(ru.otus.annotation.Document.class)).value();
        return connection.getCollection(collectionName);
    }

    public static Ddl getDdl() {
        final String ddlName = properties.getProperty("ddl");
        if (ddlName == null) {
            return Ddl.CREATE;
        }
        return Ddl.valueOf(ddlName.toUpperCase());
    }

    public enum Ddl {
        CREATE,
        CREATEDROP,
        UPDATE;
    }
}
