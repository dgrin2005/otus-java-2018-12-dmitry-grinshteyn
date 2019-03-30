package ru.otus.Executor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ExecutorUtilites {

    static List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(getAllFields(objectClass.getSuperclass()));
        }
        return fields;
    }

}
