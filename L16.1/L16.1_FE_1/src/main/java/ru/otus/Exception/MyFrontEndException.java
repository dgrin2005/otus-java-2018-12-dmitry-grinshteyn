package ru.otus.Exception;

public class MyFrontEndException extends Exception {

    public MyFrontEndException(String message, Exception e) {
        super(message, e);
    }
}
