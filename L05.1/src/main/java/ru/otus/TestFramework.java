package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFramework {

    public static void start(String className, String methodName) {
        try {
            Class cl = Class.forName(className);
            Method mt = cl.getMethod(methodName);
            if (mt != null && isTestMethod(mt)) {
                start(cl, mt);
            } else {
                error("No test method " + methodName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(Class cl, Method mt) throws Exception {
        Method methodBefore = getBeforeMethod(cl);
        Method methodAfter = getAfterMethod(cl);
        Object obj = cl.newInstance();
        try {
            if (methodBefore != null) {
                methodBefore.invoke(obj);
            }
            mt.invoke(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (methodAfter != null) {
                methodAfter.invoke(obj);
            }
        }
    }

    private static Method getBeforeMethod(Class cl) {
        List<Method> methodList = findMethod(cl, Before.class);
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
        List<Method> methodList = findMethod(cl, After.class);
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

    private static boolean isTestMethod(Method mt) {
        return mt.getAnnotation(Test.class) != null;
    }

    private static List<Method> findMethod(Class cl, Class find) {
        Method[] methods = cl.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>();
        for (Method m : methods) {
            if (m.getAnnotation(find) != null) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    private static void error (String str) {
        System.out.println("&&& " + str);
    }

    private static void success (String str) {
        System.out.println("!!! " + str);
    }

    public static void assertTrue(boolean condition) {
        if (condition) {
            success("Assertion successful");
        } else {
            error("Assertion error");
        }

    }

    public static void assertFalse(boolean condition) {
        if (!condition) {
            success("Assertion successful");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(int a, int b) {
        if (a == b) {
            success("Assertion successful");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(double a, double b, double delta) {
        if (Math.abs(a - b) <= delta) {
            success("Assertion successful");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(String a, String  b) {
        if (a.equals(b)) {
            success("Assertion successful");
        } else {
            error("Assertion error");
        }

    }

}
