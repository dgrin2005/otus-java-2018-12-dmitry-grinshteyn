package ru.otus.executor;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.otus.exception.MyOrmException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static ru.otus.utilities.DBUtilites.getCollection;
import static ru.otus.executor.ExecutorUtilites.*;

public class Executor {

    public static <T> T save(MongoDatabase connection, T t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t.getClass());
        Document document = ExecutorUtilites.createBSONFromObject(t);
        collection.insertOne(document);
        String idFieldName = getIdFieldName(t.getClass());
        if (!idFieldName.isEmpty()) {
            try {
                List<Field> fields = getAllFields(t.getClass());
                for (Field field : fields) {
                    if (field.getName().equals(idFieldName)) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        field.set(t, document.get(MONGODB_ID));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new MyOrmException("No access to field " + idFieldName + "in class " + t.getClass());
            }
        }
        return t;
    }

    public static <T> T load(MongoDatabase connection, ObjectId id, Class<T> t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents = collection.find(eq(MONGODB_ID, id));
        Document doc = documents.first();
        if (doc != null) {
            return ExecutorUtilites.createObjectFromBSON(doc, t);
        } else {
            return null;
        }
    }

    public static <T> T loadByName(MongoDatabase connection, String name, Class<T> t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents = collection.find(eq("name", name));
        Document doc = documents.first();
        if (doc != null) {
            return ExecutorUtilites.createObjectFromBSON(doc, t);
        } else {
            return null;
        }
    }

    public static <T> List<T> loadAll(MongoDatabase connection, Class<T> t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents =  collection.find(eq(MONGODB_CLASSNAME, t.getName()));
        List<T> list = new ArrayList<>();
        for (Document doc : documents) {
            list.add(ExecutorUtilites.createObjectFromBSON(doc, t));
        }
        return list;
    }

    public static <T> T update(MongoDatabase connection, T t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t.getClass());
        Document document = ExecutorUtilites.createBSONFromObject(t);
        String idFieldName = getIdFieldName(t.getClass());
        if (!idFieldName.isEmpty()) {
            try {
                List<Field> fields = getAllFields(t.getClass());
                for (Field field : fields) {
                    if (field.getName().equals(idFieldName)) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        collection.replaceOne(eq(MONGODB_ID, field.get(t)), document);
                        return (T)load(connection, (ObjectId) field.get(t), t.getClass());
                    }
                }
            } catch (IllegalAccessException e) {
                throw new MyOrmException("No access to field " + idFieldName + " in class " + t.getClass());
            }
        }
        throw new MyOrmException("Can not update object " + t + " - there is no id in class " + t.getClass());
    }

    public static <T> void deleteById(MongoDatabase connection, Class<T> t, ObjectId id) throws MyOrmException {
        try {
            MongoCollection<Document> collection = getCollection(connection, t);
            T dataSet = t.newInstance();
            String idFieldName = getIdFieldName(t);
            if (!idFieldName.isEmpty()) {
                Field field = t.getField(idFieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(dataSet, id);
            }
            Document document = ExecutorUtilites.createBSONFromObject(dataSet);
            collection.deleteOne(document);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MyOrmException(e);
        } catch (NoSuchFieldException e) {
            throw new MyOrmException("No id in class " + t);
        }
    }

    public static <T> void deleteByName(MongoDatabase connection, Class<T> t, String name) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents = collection.find(eq("name", name));
        for (Document doc : documents) {
            collection.deleteOne(doc);
        }
    }

    public static <T> void deleteAll(MongoDatabase connection, Class<T> t) throws MyOrmException {
        MongoCollection<Document> collection = getCollection(connection, t);
        collection.deleteMany(new Document());
    }

}
