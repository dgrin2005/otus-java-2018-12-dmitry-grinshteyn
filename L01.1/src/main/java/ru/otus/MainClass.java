package ru.otus;

import org.apache.commons.lang3.CharSet;

public class MainClass {
    public static void main(String... args) {
        String string = "New string";
        CharSet charSet = CharSet.getInstance(string);
        System.out.println("String '" + string + "' contains set of chars:" + charSet);
    }
}
