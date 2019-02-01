package ru.otus;

import static ru.otus.TestFramework.start;

public class MainClass {

    public static void main(String[] args) {
       try {
            System.out.println("Class \"ru.otus.test.Test1\"");
            start("ru.otus.test.Test1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
