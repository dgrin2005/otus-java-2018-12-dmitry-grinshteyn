package ru.otus.JSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

public class JSONObjectWriter {

    public Object writeToJSON(Object object) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject();

        Class<?> objectClass = object.getClass();

        if (objectClass.isPrimitive() || objectClass == Integer.class || objectClass == Long.class ||
                objectClass == Boolean.class || objectClass == Byte.class ||
                objectClass == Short.class || objectClass == Character.class ||
                objectClass == Float.class || objectClass == Double.class || objectClass == String.class) {
                    return object;
        }
        if (isImplementedInterface(objectClass, Collection.class)) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (Collection)object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        if (isImplementedInterface(objectClass, Map.class)) {
            for (Map.Entry<Object, Object> entry: ((Map<Object, Object>)object).entrySet()) {
                jsonObject.put(entry.getKey(), writeToJSON(entry.getValue()));
            }
            return jsonObject;
        }
        if (objectClass.isArray()) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (Object[]) object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        List<Field> fs = getAllFields(objectClass);
        for (Field field : fs) {
            field.setAccessible(true);
            jsonObject.put(field.getName(), writeToJSON(field.get(object)));
        }
        return jsonObject;
    }

    private List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

    private boolean isImplementedInterface(Class<?> objectClass, Class<?> interfaceClass) {
        if (!Arrays.stream(objectClass.getInterfaces()).anyMatch(i -> i.equals(interfaceClass))) {
            if (objectClass.getSuperclass() != null) {
                return isImplementedInterface(objectClass.getSuperclass(), interfaceClass);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
