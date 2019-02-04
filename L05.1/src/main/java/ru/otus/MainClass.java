package ru.otus;

import static ru.otus.framework.TestFramework.start;

public class MainClass {

    public static void main(String[] args) {
        System.out.println("Class \"ru.otus.test.TestClass\"");
        System.out.println();
        start("ru.otus.test.TestClass", "doOperation1Test");
        System.out.println();
        start("ru.otus.test.TestClass", "doOperation2Test");
        System.out.println();
        start("ru.otus.test.TestClass", "doOperationExceptionTest");
        System.out.println();
        start("ru.otus.test.TestClass", "doOperationFailTest");
        System.out.println();
        start("ru.otus.test.TestClass", "doOperation3Test");
        System.out.println();
    }

}
