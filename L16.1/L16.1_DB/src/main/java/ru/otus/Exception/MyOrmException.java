package ru.otus.exception;

public class MyOrmException extends Exception {

    public MyOrmException(String message, Exception e) {
        super(message, e);
    }
}
