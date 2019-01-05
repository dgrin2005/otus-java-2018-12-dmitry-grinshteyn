package ru.otus;

public class MainClass {
    public static void main(String[] args) {
        String string = "New string";
        AdditionalClass ac = new AdditionalClass();
        ac.createCharSet(string);
        ac.createMultiset(string);
    }
}
