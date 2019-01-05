package ru.otus;

import org.apache.commons.lang3.CharSet;

public class MainClass {
    public static void main(String[] args) {
        String string = "New string";
        AdditionalClass ac = new AdditionalClass();
        ac.createCharSet(string);
        ac.createMultiset(string);
    }
}
