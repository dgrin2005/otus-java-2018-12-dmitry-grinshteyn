package ru.otus.MessageSystem;

import ru.otus.Exception.MyOrmException;

import java.io.IOException;

public abstract class Message {
    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(Addressee addressee) throws MyOrmException, IOException;
}
