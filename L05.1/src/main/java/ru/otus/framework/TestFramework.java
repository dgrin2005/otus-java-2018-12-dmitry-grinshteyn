package ru.otus.framework;

import ru.otus.framework.annotation.After;
import ru.otus.framework.annotation.Before;
import ru.otus.framework.annotation.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFramework {

    public static void start(String className) {
        try {
            start(Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(Class cl) {
        Method methodBefore = getBeforeMethod(cl);
        Method methodAfter = getAfterMethod(cl);
        List<Method> methodList = findMethods(cl, Test.class);
        for (Method method : methodList) {
            startTestMethod(cl, method, methodBefore, methodAfter);
        }
    }

    private static void startTestMethod(Class cl, Method method, Method methodBefore, Method methodAfter) {
        try {
            Object obj = cl.newInstance();
            try {
                if (methodBefore != null) {
                    methodBefore.invoke(obj);
                }
                method.invoke(obj);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (methodAfter != null) {
                    methodAfter.invoke(obj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Method getBeforeMethod(Class cl) {
        List<Method> methodList = findMethods(cl, Before.class);
        if (methodList.size() > 1) {
            error("Too many @Before");
            throw new RuntimeException("Too many @Before");
        } else {
            if (methodList.size() == 1) {
                return methodList.get(0);
            }
            else {
                return null;
            }
        }
    }

    private static Method getAfterMethod(Class cl) {
        List<Method> methodList = findMethods(cl, After.class);
        if (methodList.size() > 1) {
            error("Too many @After");
            throw new RuntimeException("Too many @After");
        } else {
            if (methodList.size() == 1) {
                return methodList.get(0);
            }
            else {
                return null;
            }
        }
    }

    private static List<Method> findMethods(Class cl, Class find) {
        Method[] methods = cl.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method m : methods) {
            if (m.getAnnotation(find) != null) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    public static void error (String str) {
        System.out.println("--- " + str);
    }

    public static void success (String str) {
        System.out.println("+++ " + str);
    }

}
