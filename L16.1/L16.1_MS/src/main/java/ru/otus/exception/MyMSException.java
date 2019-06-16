package ru.otus.exception;

public class MyMSException extends Exception {

    public MyMSException(String message, Exception e) {
        super(message, e);
    }

    public MyMSException(String message) {
        super(message);
    }
}
