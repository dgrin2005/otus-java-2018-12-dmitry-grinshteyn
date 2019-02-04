package ru.otus.framework;

import static ru.otus.framework.TestFramework.error;
import static ru.otus.framework.TestFramework.success;

public class Assertion {

    public static void assertTrue(boolean condition) {
        if (condition) {
            success("Assertion is true");
        } else {
            error("Assertion error");
        }

    }

    public static void assertFalse(boolean condition) {
        if (!condition) {
            success("Assertion is true");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(int a, int b) {
        if (a == b) {
            success("Assertion is true");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(double a, double b, double delta) {
        if (Math.abs(a - b) <= delta) {
            success("Assertion is true");
        } else {
            error("Assertion error");
        }

    }

    public static void assertEquals(String a, String  b) {
        if (a.equals(b)) {
            success("Assertion is true");
        } else {
            error("Assertion error");
        }

    }

}
