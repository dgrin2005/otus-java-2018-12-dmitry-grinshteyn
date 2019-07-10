package ru.otus.executor;

import org.bson.Document;
import ru.otus.dataset.DataSet;
import ru.otus.exception.MyOrmException;
import ru.otus.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class ExecutorUtilites {

    final static String MONGODB_ID = "_id";
    final static String MONGODB_CLASSNAME = "#classname";

    static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

    static Document createBSONFromObject(Object t) throws MyOrmException {
        Class tClass = t.getClass();
        if (!tClass.isAnnotationPresent(ru.otus.annotation.Document.class)) {
            throw new MyOrmException("Class " + tClass + " for object " + t + " does not match");
        }
        try {
            Document document = new Document();
            String idFieldName = getIdFieldName(tClass);
            List<Field> fields = getAllFields(tClass);
            for (Field field: fields) {
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                if (field.get(t).getClass().getSuperclass() == DataSet.class) {
                    document.append(field.getName(), createBSONFromObject(field.get(t)));
                } else {
                    if (isImplementedInterface(field.get(t).getClass(), Collection.class)) {
                        List<Object> list = new ArrayList<>();
                        for (Object obj : (Collection)field.get(t)) {
                            if (isPrimitive(obj.getClass())) {
                                list.add(obj);
                            } else {
                                list.add(createBSONFromObject(obj));
                            }
                        }
                        document.append(field.getName(), list);
                    } else {
                        document.append(field.getName(), field.get(t));
                    }
                }
            }
            document.append(MONGODB_CLASSNAME, tClass.getName());

            if (!idFieldName.isEmpty()) {
                document.append(MONGODB_ID, document.get(idFieldName));
                document.remove(idFieldName);
            }
            return document;
        } catch (IllegalAccessException e) {
            throw new MyOrmException(e);
        }
    }

    static <T> T createObjectFromBSON(Document document, Class<?> t) throws MyOrmException {
        if (!t.isAnnotationPresent(ru.otus.annotation.Document.class)) {
            throw new MyOrmException("Class " + t + " for document " + document + " does not match");
        }
        try {
            if (document.get(MONGODB_CLASSNAME).equals(t.getName())) {
                List<Field> fields = getAllFields(t);
                T dataSet = (T) t.newInstance();
                String idFieldName = getIdFieldName(t);
                for (Field field : fields) {
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    String fieldName = field.getName();
                    if (!idFieldName.isEmpty()) {
                        if (fieldName.equals(idFieldName)) {
                            field.set(dataSet, document.get(MONGODB_ID));
                            return dataSet;
                        }
                    }
                    if (document.get(fieldName) instanceof Document) {
                        field.set(dataSet, createObjectFromBSON((Document)document.get(fieldName), field.getType()));
                    } else {
                        if (document.get(fieldName) != null) {
                            if (isImplementedInterface(document.get(field.getName()).getClass(), Collection.class)) {
                                List<Object> list = new ArrayList<>();
                                for (Object obj : (Collection) document.get(fieldName)) {
                                    try {
                                        if (isPrimitive(obj.getClass())) {
                                            list.add(obj);
                                        } else {
                                            list.add(createObjectFromBSON((Document) obj, Class.forName(
                                                    String.valueOf(((Document) obj).get(MONGODB_CLASSNAME)))));
                                        }
                                    } catch (ClassNotFoundException e) {
                                        throw new MyOrmException(e);
                                    }
                                }
                                field.set(dataSet, list);
                            } else {
                                field.set(dataSet, document.get(fieldName));
                            }
                        } else {
                            field.set(dataSet, document.get(fieldName));
                        }
                    }
                }
                return dataSet;
            } else {
                throw  new MyOrmException("Class does not match");
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MyOrmException(e);
        }
    }

    private static boolean isImplementedInterface(Class<?> objectClass, Class<?> interfaceClass) {
        if (Arrays.stream(objectClass.getInterfaces()).noneMatch(i -> i.equals(interfaceClass))) {
            if (objectClass.getSuperclass() != null) {
                return isImplementedInterface(objectClass.getSuperclass(), interfaceClass);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private static boolean isPrimitive(Class t) {
        return t.isPrimitive() || t == Integer.class || t == Long.class ||
                t == Boolean.class || t == Byte.class ||
                t == Short.class || t == Character.class ||
                t == Float.class || t == Double.class || t == String.class;
    }

    static String getIdFieldName(Class t) throws MyOrmException {
        List<Field> fields = getAllFields(t);
        String idFieldName = "";
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (idFieldName.isEmpty()) {
                    idFieldName = field.getName();
                } else {
                    throw new MyOrmException("Too much id in class " + t);
                }
            }
        }
        return idFieldName;
    }

}
