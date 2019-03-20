package ru.otus;

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
        if (objectClass.isArray()) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (Object[]) object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        if (Arrays.stream(objectClass.getInterfaces()).anyMatch(i -> i.equals(List.class))) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (List)object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        if (Arrays.stream(objectClass.getInterfaces()).anyMatch(i -> i.equals(Set.class))) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (Set)object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        if (objectClass == PriorityQueue.class) {
            JSONArray jsonArray = new JSONArray();
            for (Object o : (Queue)object) {
                jsonArray.add(writeToJSON(o));
            }
            return jsonArray;
        }
        if (Arrays.stream(objectClass.getInterfaces()).anyMatch(i -> i.equals(Map.class))) {
            for (Map.Entry<Object, Object> entry: ((Map<Object, Object>)object).entrySet()) {
                jsonObject.put(entry.getKey(), writeToJSON(entry.getValue()));
            }
            return jsonObject;
        }
        Field[] fs = objectClass.getDeclaredFields();
        for (Field field : fs) {
            field.setAccessible(true);
            jsonObject.put(field.getName(), writeToJSON(field.get(object)));
        }
        return jsonObject;
    }

}
