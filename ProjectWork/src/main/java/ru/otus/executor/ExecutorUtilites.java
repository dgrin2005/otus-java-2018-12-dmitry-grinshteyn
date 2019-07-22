package ru.otus.executor;

import org.bson.Document;
import ru.otus.exception.MyOrmException;
import ru.otus.annotation.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
            List<Field> fields = getAllFields(tClass);
            for (Field field: fields) {
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                if (field.get(t).getClass().isAnnotationPresent(ru.otus.annotation.Document.class)) {
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
            replaceColumnNames(document, tClass);
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
                Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
                T dataSet = (T) t.newInstance();
                String idFieldName = getIdFieldName(t);
                for (Field field : fields) {
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    String fieldName = field.getName();
                    String correspondentFieldName = correspondentFieldMap.get(fieldName);


                    if (!idFieldName.isEmpty()) {
                        if (fieldName.equals(idFieldName)) {
                            field.set(dataSet, document.get(MONGODB_ID));
                            return dataSet;
                        }
                    }


                    if (document.get(correspondentFieldName) instanceof Document) {
                        field.set(dataSet, createObjectFromBSON((Document)document.get(correspondentFieldName), field.getType()));
                    } else {
                        if (document.get(correspondentFieldName) != null) {
                            if (isImplementedInterface(document.get(correspondentFieldName).getClass(), Collection.class)) {
                                List<Object> list = new ArrayList<>();
                                for (Object obj : (Collection) document.get(correspondentFieldName)) {
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
                                field.set(dataSet, document.get(correspondentFieldName));
                            }
                        } else {
                            field.set(dataSet, document.get(correspondentFieldName));
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

    static Map<String, Field> getFieldMap(Class t) {
         return getAllFields(t).stream().collect(Collectors.toMap(Field::getName, x -> x));
    }

    static Map<String, String> getCorrespondentFieldMap(Class t) {
        Map<String, String> correspondentFieldMap = new HashMap<>();
        List<Field> fields = getAllFields(t);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                correspondentFieldMap.put( field.getName(), MONGODB_ID);
            } else {
                if (field.isAnnotationPresent(Column.class)) {
                    correspondentFieldMap.put(field.getName(), field.getAnnotation(ru.otus.annotation.Column.class).value());
                } else {
                    correspondentFieldMap.put(field.getName(), field.getName());
                }
            }
        }
        return correspondentFieldMap;
    }

    private static void replaceColumnNames(Document document, Class t) {
        Map<String, String> correspondentFieldMap = getCorrespondentFieldMap(t);
        List<Field> fields = getAllFields(t);
        for (Field field : fields) {
            String fieldName = field.getName();
            String correspondentFieldName = correspondentFieldMap.get(fieldName);
            if (!correspondentFieldName.equals(fieldName)) {
                document.append(correspondentFieldName, document.get(fieldName));
                document.remove(fieldName);
            }
        }

    }


}
