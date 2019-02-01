package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestFramework {

    public static void start(Class cl) throws Exception {
        Method[] methods = cl.getDeclaredMethods();
        int countBefore = 0;
        int countAfter = 0;
        Method methodBefore = null;
        Method methodAfter = null;
        ArrayList<Method> metodsTest = new ArrayList<>();
        for (Method m : methods) {
            if (m.getAnnotation(Before.class) != null) {
                countBefore++;
                methodBefore = m;
            }
            if (m.getAnnotation(After.class) != null) {
                countAfter++;
                methodAfter = m;
            }
            if (m.getAnnotation(Test.class) != null) {
                metodsTest.add(m);
            }
        }
        if (countBefore > 1) {
            throw new RuntimeException("Too many @Before");
        }
        if (countAfter > 1) {
            throw new RuntimeException("Too many @After");
        }
        Object obj = cl.newInstance();
        if (countBefore == 1) {
            methodBefore.invoke(obj);
        }
        for (Method aMetodsTest : metodsTest) {
            aMetodsTest.invoke(obj);
        }
        if (countAfter == 1) {
            methodAfter.invoke(obj);
        }
    }


    public static void start(String className) throws Exception {
        Class cl = Class.forName(className);
        start(cl);
    }
}
