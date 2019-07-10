package ru.otus.exception;

public class MyOrmException extends Exception {

    public MyOrmException(Exception e) {
        super(e);
    }

    public MyOrmException(String e) {
        super(e);
    }
}
