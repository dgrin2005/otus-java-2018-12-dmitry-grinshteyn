package ru.otus.executor;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.otus.exception.MongoODMException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static ru.otus.utilities.DBUtilites.getCollection;
import static ru.otus.executor.ExecutorUtilites.*;
import static ru.otus.utilities.DBUtilites.getCollectionName;

public class MongoExecutor implements Executor{

    private final static Logger logger = Logger.getLogger(MongoExecutor.class.getName());
    private final MongoDatabase connection;

    public MongoExecutor(MongoDatabase connection) {
        this.connection = connection;
    }

    @Override
    public <T> void save(T t) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t.getClass());
        Map<String, Field> fieldMap = getFieldMap(t.getClass());
        Document document = ExecutorUtilites.createBSONFromObject(t);
        collection.insertOne(document);
        String idFieldName = getIdFieldName(t.getClass());
        if (!idFieldName.isEmpty()) {
            try {
                Field field = fieldMap.get(idFieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(t, document.get(MONGODB_ID));
            } catch (IllegalAccessException e) {
                throw new MongoODMException("No access to field " + idFieldName + "in class " + t.getClass());
            }
        }
        logger.log(Level.INFO, "Added document " + t + " in collection " + getCollectionName(t.getClass()));
    }

    @Override
    public <T> T load(ObjectId id, Class<T> t) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents = collection.find(eq(MONGODB_ID, id));
        Document doc = documents.first();
        if (doc != null) {
            logger.log(Level.INFO, "Loaded by id " + id + " document " + t + " in collection " +
                    getCollectionName(t));
            return ExecutorUtilites.createObjectFromBSON(doc, t);
        } else {
            logger.log(Level.WARNING, "Not found any document by id " + id + " in collection " +
                    getCollectionName(t));
            return null;
        }
    }

    @Override
    public <T> T loadByName(String name, Class<T> t) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
        String fieldName = correspondentFieldMap.get("name");
        if (!fieldName.isEmpty()) {
            FindIterable<Document> documents = collection.find(eq(fieldName, name));
            Document doc = documents.first();
            if (doc != null) {
                logger.log(Level.INFO, "Loaded by name " + name + " document " + t + " in collection " +
                        getCollectionName(t));
                return ExecutorUtilites.createObjectFromBSON(doc, t);
            } else {
                logger.log(Level.WARNING, "Not found any document by name " + name + " in collection " +
                        getCollectionName(t));
                return null;
            }
        } else {
            logger.log(Level.WARNING, "Not found field 'name' in collection " + getCollectionName(t));
            return null;
        }
    }

    @Override
    public <T> List<T> loadAll(Class<T> t) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents =  collection.find(eq(MONGODB_CLASSNAME, t.getName()));
        List<T> list = new ArrayList<>();
        for (Document doc : documents) {
            list.add(ExecutorUtilites.createObjectFromBSON(doc, t));
        }
        logger.log(Level.INFO, "Loaded all documents from collection " + getCollectionName(t));
        return list;
    }

    @Override
    public <T> void update(T t) throws MongoODMException {
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
                        logger.log(Level.INFO, "Updated document " + t + " at collection " +
                                getCollectionName(t.getClass()));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new MongoODMException("No access to field " + idFieldName + " in class " + t.getClass());
            }
        } else {
            throw new MongoODMException("Can not update object " + t + " - there is no id in class " + t.getClass());
        }
    }

    @Override
    public <T> void deleteById(Class<T> t, ObjectId id) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        FindIterable<Document> documents = collection.find(eq(MONGODB_ID, id));
        for (Document doc : documents) {
            collection.deleteOne(doc);
            logger.log(Level.INFO, "Document with id " + id + " deleted from collection " + getCollectionName(t));
        }
    }

    @Override
    public <T> void deleteByName(Class<T> t, String name) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
        String fieldName = correspondentFieldMap.get("name");
        if (!fieldName.isEmpty()) {
            FindIterable<Document> documents = collection.find(eq(fieldName, name));
            for (Document doc : documents) {
                collection.deleteOne(doc);
                logger.log(Level.INFO, "Document with name " + name + " deleted from collection " +
                        getCollectionName(t));
            }
        } else {
            logger.log(Level.WARNING, "Not found field 'name' in collection " + getCollectionName(t));
        }
    }

    @Override
    public <T> void deleteList(List<T> t) throws MongoODMException {
        if (t.size() > 0) {
            Class tClass = t.get(0).getClass();
            Map<String, Field> fieldMap = getFieldMap(tClass);
            String idFieldName = getIdFieldName(tClass);
            if (!idFieldName.isEmpty()) {
                Field field = fieldMap.get(idFieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                for (T element : t) {
                    try {
                        deleteById(tClass, (ObjectId) field.get(element));
                    } catch (IllegalAccessException e) {
                        throw new MongoODMException(e);
                    }
                }
            }
        }
    }

    @Override
    public <T> void deleteAll(Class<T> t) throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        collection.deleteMany(new Document());
        logger.log(Level.INFO, "Deleted all documents from collection " + getCollectionName(t));
    }

    @Override
    public <T, V> List<T> loadWhenEqual(Class<T> t, String fieldName, V value)
            throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, Field> fieldMap = getFieldMap(t);
        FindIterable<Document> documents =  collection.find(eq(MONGODB_CLASSNAME, t.getName()));
        List<T> list = new ArrayList<>();
        for (Document doc : documents) {
            try {
                T dataSet = createObjectFromBSON(doc, t);
                Field field = fieldMap.get(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (field.get(dataSet).equals(value)) {
                    list.add(dataSet);
                }
            } catch (IllegalAccessException e) {
                throw new MongoODMException(e);
            }
        }
        logger.log(Level.INFO, "Loaded documents when " + fieldName + " equal " + value + " from collection " +
                getCollectionName(t));
        return list;
    }

    @Override
    public <T, V> List<T> loadWhenNotEqual(Class<T> t, String fieldName, V value)
            throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, Field> fieldMap = getFieldMap(t);
        FindIterable<Document> documents =  collection.find(eq(MONGODB_CLASSNAME, t.getName()));
        List<T> list = new ArrayList<>();
        for (Document doc : documents) {
            try {
                T dataSet = createObjectFromBSON(doc, t);
                Field field = fieldMap.get(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (!field.get(dataSet).equals(value)) {
                    list.add(dataSet);
                }
            } catch (IllegalAccessException e) {
                throw new MongoODMException(e);
            }
        }
        logger.log(Level.INFO, "Loaded documents when " + fieldName + " not equal " + value + " from collection " +
                getCollectionName(t));
        return list;
    }

    @Override
    public <T, V extends Comparable> List<T> loadWhenGreaterThan(Class<T> t, String fieldName, V value)
            throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
        String fieldNameCompare = correspondentFieldMap.get(fieldName);
        if (!fieldNameCompare.isEmpty()) {
            FindIterable<Document> documents = collection.find(eq(MONGODB_CLASSNAME, t.getName()));
            List<T> list = new ArrayList<>();
            for (Document doc : documents) {
                if (doc.containsKey(fieldNameCompare)) {
                    if (value.compareTo(doc.get(fieldNameCompare)) < 0) {
                        list.add(ExecutorUtilites.createObjectFromBSON(doc, t));
                    }
                }
            }
            logger.log(Level.INFO, "Loaded documents when " + fieldName + " equal " + value + " from collection " +
                    getCollectionName(t));
            return list;
        } else {
            logger.log(Level.WARNING, "Not found field '" + fieldName + "' in collection " + getCollectionName(t));
            return new ArrayList<>();
        }
    }

    @Override
    public <T, V extends Comparable> List<T> loadWhenLessThan(Class<T> t, String fieldName, V value)
            throws MongoODMException {
        MongoCollection<Document> collection = getCollection(connection, t);
        Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
        String fieldNameCompare = correspondentFieldMap.get(fieldName);
        if (!fieldNameCompare.isEmpty()) {
            FindIterable<Document> documents = collection.find(eq(MONGODB_CLASSNAME, t.getName()));
            List<T> list = new ArrayList<>();
            for (Document doc : documents) {
                if (doc.containsKey(fieldNameCompare)) {
                    if (value.compareTo(doc.get(fieldNameCompare)) > 0) {
                        list.add(ExecutorUtilites.createObjectFromBSON(doc, t));
                    }
                }
            }
            logger.log(Level.INFO, "Loaded documents when " + fieldName + " equal " + value + " from collection " +
                    getCollectionName(t));
            return list;
        } else {
            logger.log(Level.WARNING, "Not found field '" + fieldName + "' in collection " + getCollectionName(t));
            return new ArrayList<>();
        }
    }

}
