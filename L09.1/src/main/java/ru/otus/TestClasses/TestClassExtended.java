package ru.otus.TestClasses;

import java.util.Map;

public class TestClassExtended extends TestClass {

    private int x;
    private int z;


    public TestClassExtended(int a, String b, InnerTestClass itc, Map<String, InnerTestClass> innerTestClassDoubleMap,
                             int x, int z) {
        super(a, b, itc, innerTestClassDoubleMap);
        this.x = x;
        this.z = z;
    }
}
