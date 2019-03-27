package ru.otus.Exception;

public class MyOrmException extends Exception {

    public MyOrmException(Exception e) {
        super(e);
    }
}
