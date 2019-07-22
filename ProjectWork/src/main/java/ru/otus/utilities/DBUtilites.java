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

    private final static String PROPERTIES_FILENAME = "mongodb.properties";
    private final static String PROPERTIE_NAME_URI = "uri";
    private final static String PROPERTIE_NAME_DATABASE = "dbname";
    private final static String PROPERTIE_NAME_DDL = "ddl";
    private static Properties properties;

    static {
        properties = new Properties();
        File file = new File(PROPERTIES_FILENAME);
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MongoClient getMongoDbConnection() throws MyOrmException {

        final String uri = properties.getProperty(PROPERTIE_NAME_URI);
        if (uri == null) {
            throw new MyOrmException("URI not found in file " + PROPERTIES_FILENAME);
        }
        return MongoClients.create(uri);
    }

    public static String getDBName() throws MyOrmException {
        final String dbname = properties.getProperty(PROPERTIE_NAME_DATABASE);
        if (dbname == null) {
            throw new MyOrmException("Database name not found in file " + PROPERTIES_FILENAME);
        }
        return dbname;
    }

    public static String getCollectionName(Class t) throws MyOrmException{
        if (!t.isAnnotationPresent(ru.otus.annotation.Document.class)) {
            throw new MyOrmException("Class " + t + " does not match");
        }
        return ((ru.otus.annotation.Document) t.getAnnotation(ru.otus.annotation.Document.class)).value();
    }

    public static MongoCollection<org.bson.Document> getCollection(MongoDatabase connection, Class t)
            throws MyOrmException {
        String collectionName = getCollectionName(t);
        return connection.getCollection(collectionName);
    }

    public static Ddl getDdl() {
        final String ddlName = properties.getProperty(PROPERTIE_NAME_DDL);
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
