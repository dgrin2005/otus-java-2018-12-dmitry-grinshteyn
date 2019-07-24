package ru.otus.exception;

public class MongoODMException extends Exception {

    public MongoODMException(Exception e) {
        super(e);
    }

    public MongoODMException(String e) {
        super(e);
    }
}
