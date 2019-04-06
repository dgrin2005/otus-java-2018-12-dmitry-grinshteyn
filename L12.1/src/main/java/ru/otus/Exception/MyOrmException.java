package ru.otus.Exception;

public class MyOrmException extends Exception {

    public MyOrmException(String message, Exception e) {
        super(message, e);
    }
}
