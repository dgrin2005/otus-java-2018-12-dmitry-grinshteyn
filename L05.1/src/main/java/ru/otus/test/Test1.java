package ru.otus.test;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class Test1 {

    @Before
    public void doBeforeTest1() {
        System.out.println("Before Test1");
    }

    @Test
    public void doOperation1Test1() {
        System.out.println("Operation 1 Test1");
    }

    @Test
    public void doOperation2Test1() {
        System.out.println("Operation 2 Test1");
    }

    @Test
    public void doOperation3Test1() {
        System.out.println("Operation 3 Test1");
    }

    @After
    public void doAfterTest1() {
        System.out.println("After Test1");
    }
}
