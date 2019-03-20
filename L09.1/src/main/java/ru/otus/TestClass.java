package ru.otus;

import java.util.Map;

public class TestClass {

    private int a;
    private String b;
    private InnerTestClass itc;
    private Map<String, InnerTestClass> innerTestClassDoubleMap;

    public TestClass(int a, String b, InnerTestClass itc, Map<String, InnerTestClass> innerTestClassDoubleMap) {
        this.a = a;
        this.b = b;
        this.itc = itc;
        this.innerTestClassDoubleMap = innerTestClassDoubleMap;
    }
}
