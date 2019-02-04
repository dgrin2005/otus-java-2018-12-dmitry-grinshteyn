package ru.otus.test;

import ru.otus.framework.annotation.After;
import ru.otus.framework.annotation.Before;
import ru.otus.framework.annotation.Test;

import static ru.otus.framework.Assertion.assertEquals;
import static ru.otus.framework.Assertion.assertTrue;

public class TestClass {

    @Before
    public void doBeforeTest() {
        System.out.println("Before Test");
    }

    @Test
    public void doOperation1Test() {
        System.out.println("Operation 1 Test");
        assertTrue(1 > 0);
    }

    @Test
    public void doOperation2Test() {
        System.out.println("Operation 2 Test");
        assertEquals(2 + 2, 4);
    }

    @Test
    public void doOperation3Test() {
        System.out.println("Operation 3 Test");
        assertEquals("Hello " + "world!", "Hello world!");
    }

    @Test
    public void doOperationExceptionTest() {
        System.out.println("Operation Exception Test");
        throw new RuntimeException("Exception at Operation Exception Test");
    }

    @Test
    public void doOperationFailTest() {
        System.out.println("Operation Fail Test");
        assertEquals(2 + 2, 5);
    }

    @After
    public void doAfterTest() {
        System.out.println("After Test");
    }
}
